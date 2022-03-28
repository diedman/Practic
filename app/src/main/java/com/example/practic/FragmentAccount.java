package com.example.practic;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FragmentAccount extends Fragment {
    private TextView tvCoworkerFullName;
    private ExpandableListAdapter ExpAdapter;
    private ExpandableListView PersonalDataList;

    public static FragmentAccount newInstance() {
        return new FragmentAccount();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_account, container, false);
        tvCoworkerFullName = thisView.findViewById(R.id.textView_account_Full_Name);
        RecyclerView rvQRCodesCoworking = thisView.findViewById(R.id.recyclerView_QR_Coworking);
        RecyclerView rvQRCodesEvent = thisView.findViewById(R.id.recyclerView_QR_Event);
        Button btnAccountEdit = thisView.findViewById(R.id.button_Account_Edit);

        String fullName = CoworkerData.firstname + " " + CoworkerData.lastname;
        tvCoworkerFullName.setText(fullName);

        ArrayList<CoworkingSession> coworkingSessions = (ArrayList<CoworkingSession>) DBCommunication.getCoworkerSessions(CoworkerData.id);
        ArrayList<QRCodeDisplay> QRCodesOnCoworkings = new ArrayList<>();
        for (CoworkingSession session: coworkingSessions) {
            String title = session.getSpace().getTitle();
            String time  = new SimpleDateFormat("hh:mm").format(session.getStartSession());
            String date  = new SimpleDateFormat("dd/MM/yyy").format(session.getStartSession());
            Bitmap qr    = QRGen.genQR(session.getQr());

            QRCodesOnCoworkings.add(new QRCodeDisplay(title, time, date,
                    null, null, null, qr));
        }
//        QRCodes.add( new QRCodeDisplay("Title","Time", "Date",
//                "Place", null, null, R.drawable.noavatar));
//        QRCodes.add( new QRCodeDisplay("Title","Time", "Date",
//                "Place", "Workplace", "Locker", R.drawable.noavatar));

        QRDisplayAdapter adapter = new QRDisplayAdapter(QRCodesOnCoworkings);
        rvQRCodesCoworking.setAdapter(adapter);
        rvQRCodesCoworking.setLayoutManager(new LinearLayoutManager(thisView.getContext()));

        ArrayList<QRCodeDisplay> QRCodesOnEvents = new ArrayList<>();
        ArrayList<CoworkerEvent> coworkerEvents = (ArrayList<CoworkerEvent>)DBCommunication.getEventsOfCoworker(CoworkerData.id);
        for (CoworkerEvent coworkerEvent: coworkerEvents) {
            String title = coworkerEvent.getEvent().getTitle();
            String time  = new SimpleDateFormat("hh:mm").format(coworkerEvent.getEvent().getMeetingDate());
            String date  = new SimpleDateFormat("dd/MM/yyy").format(coworkerEvent.getEvent().getMeetingDate());
            String place = coworkerEvent.getEvent().getSpace().getTitle();
            Bitmap qr    = QRGen.genQR(coworkerEvent.getQr());

            QRCodesOnEvents.add(new QRCodeDisplay(title, time, date, place, null, null, qr));
        }

        adapter = new QRDisplayAdapter(QRCodesOnEvents);
        rvQRCodesEvent.setAdapter(adapter);
        rvQRCodesEvent.setLayoutManager(new LinearLayoutManager(thisView.getContext()));

        btnAccountEdit.setOnClickListener(view -> getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, FragmentAccountEdit.class, null)
                .commit());

        return thisView;
    }
}
