package com.example.practic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Locale;

public class FragmentRegistrationCoworkingSpace extends Fragment {
    int hour, minute;
    Button btnTimeStart, btnTimeEnd, btnNext;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_registration_coworking_space, container, false);

        initFields(thisView);
        setListenerForEquipmentDialog(thisView);
        setTimeButtonListener(btnTimeStart, thisView);
        setTimeButtonListener(btnTimeEnd, thisView);


        return thisView;
    }

    private void initFields(View thisView) {
        btnTimeStart = thisView.findViewById(R.id.button_Time_Start);
        btnTimeEnd   = thisView.findViewById(R.id.button_Time_End);
        btnNext      = thisView.findViewById(R.id.button_Next);
    }

    private void setTimeButtonListener(Button button, View thisView) {
        button.setOnClickListener(view -> {

            onTimeSetListener = ((timePicker, hour_, minute_) ->
                    button.setText(String.format(Locale.getDefault(),
                            "%02d:%02d", hour_, minute_)));

            TimePickerDialog timePickerDialog = new TimePickerDialog(thisView.getContext(),
                    onTimeSetListener, hour, minute, true);
            timePickerDialog.setTitle("");
            timePickerDialog.show();
        });
    }

    private void setListenerForEquipmentDialog(View thisView){
        btnNext.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(thisView.getContext());
            builder.setTitle(getString(R.string.coworking_equipment_ask));

            // TODO: Подавать значения из базы
            String[] testData = {"ноутбук1", "ноутбук2", "ноутбук3", "ноутбук4", "ноутбук5"};
            boolean[] checkedItems = {false, false, false, false, false};

            builder.setMultiChoiceItems(testData, checkedItems,
                    (dialogInterface, item, isChecked) -> {

            });

            builder.setPositiveButton(R.string.coworking_equipment_dialog_yes,
                    (dialogInterface, listener) -> {

            });

            builder.setNegativeButton(R.string.coworking_equipment_dialog_no,
                    (dialogInterface, listener) -> {

            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}