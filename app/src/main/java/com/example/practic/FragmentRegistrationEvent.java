package com.example.practic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;

public class FragmentRegistrationEvent extends Fragment {
    private MaterialTextView mtvTitle, mtvDate, mtvPlace, mtvSpeaker;
    //private ScrollView svDescription;
    private TextView tvDescription;
    private Button btnSignUp;
    private EventData eventData;

    public FragmentRegistrationEvent(EventData eventData) {
        this.eventData = eventData;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_registration_event, container, false);

        initFields(thisView);
        initAdapters(thisView);

        return thisView;
    }

    private void initFields(View thisView) {
        mtvTitle       = thisView.findViewById(R.id.textView_reg_event_name);
        mtvTitle.setText(eventData.getTitle());

        mtvDate        = thisView.findViewById(R.id.textView_reg_event_date);
        mtvDate.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(eventData.getMeetingDate()));

        mtvPlace       = thisView.findViewById(R.id.textView_reg_event_place);
        mtvPlace.setText(eventData.getSpace().getTitle());

        tvDescription  = thisView.findViewById(R.id.textView_Description_Text);
        tvDescription.setText(eventData.getDescription());

        mtvSpeaker     = thisView.findViewById(R.id.textView_reg_event_speaker);
        mtvSpeaker.setText(eventData.getSpeaker());
        
        btnSignUp      = thisView.findViewById(R.id.button_reg_event_SingUp);
    }

    private void initAdapters(View thisView) {
        btnSignUp.setOnClickListener(v -> {
            int regRes = registrationOnEvent();
            if (regRes == 1) {
                Utilities.showMessageDialog(thisView, "Вы зарегистрировались на мероприятие!", "");
            } else if (regRes == 0) {
                Utilities.showMessageDialog(thisView, "Вы уже зарегистрированы на это мероприятие!", "");
            } else {
                Utilities.showMessageDialog(thisView, "Соединение с сервером не установлено! Пожалуйста, проверьте подключение к интернету!", "");
            }
        });
    }

    private int registrationOnEvent() {
        String strqr = Utilities.getRandomHexStr(64);
        return DBCommunication.registerOnEvent(eventData.getId(), CoworkerData.id, strqr);
    }
}