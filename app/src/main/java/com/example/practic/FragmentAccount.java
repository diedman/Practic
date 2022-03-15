package com.example.practic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentAccount extends Fragment {

    private ExpandableListAdapter ExpAdapter;
    private ExpandableListView PersonalDataList;

    public static FragmentAccount newInstance() {
        return new FragmentAccount();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_account, container, false);
        RecyclerView rvQRCodesCoworking = thisView.findViewById(R.id.recyclerView_QR_Coworking);
        RecyclerView rvQRCodesEvent = thisView.findViewById(R.id.recyclerView_QR_Event);
        Button btnAccountEdit = thisView.findViewById(R.id.button_Account_Edit);

        ArrayList<QRCodeDisplay> QRCodes = new ArrayList<>();
        QRCodes.add( new QRCodeDisplay("Title","Time", "Date",
                "Place", null, null, R.drawable.noavatar));
        QRCodes.add( new QRCodeDisplay("Title","Time", "Date",
                "Place", "Workplace", "Locker", R.drawable.noavatar));

        QRDisplayAdapter adapter = new QRDisplayAdapter(QRCodes);
        rvQRCodesCoworking.setAdapter(adapter);
        rvQRCodesCoworking.setLayoutManager(new LinearLayoutManager(thisView.getContext()));

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
