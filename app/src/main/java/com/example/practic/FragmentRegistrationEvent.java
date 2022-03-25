package com.example.practic;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;

public class FragmentRegistrationEvent extends Fragment {
    private MaterialTextView mtvTitle, mtvDate, mtvPlace, mtvSpeaker;
    private ScrollView svDescription;
    private EventData eventData;

    public FragmentRegistrationEvent(EventData eventData) {
        this.eventData = eventData;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_registration_event, container, false);

        initFields(thisView);
        initAdapters();

        return thisView;
    }

    //
    private void initAdapters() {
        //TODO! Проинициализировать адаптеры
    }

    //Метод инициализации полей
    private void initFields(View thisView) {
        mtvTitle       = thisView.findViewById(R.id.textView_reg_event_name);
        mtvDate        = thisView.findViewById(R.id.textView_reg_event_date);
        mtvPlace       = thisView.findViewById(R.id.textView_reg_event_place);
        svDescription  = thisView.findViewById(R.id.scrollView_reg_event_description);
        mtvSpeaker     = thisView.findViewById(R.id.textView_reg_event_speaker);
    }

    //Создание QR-кода
    private Bitmap registrationOnEvent() {
        String strqr = Utilities.getRandomHexStr(32);
        DBCommunication.registerOnEvent(eventData.getId(), CoworkerData.id, strqr);
        QRGen qr = new QRGen(strqr);
        return qr.getQr();
    }
}