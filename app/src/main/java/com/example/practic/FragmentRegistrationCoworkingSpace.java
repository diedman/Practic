package com.example.practic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

public class FragmentRegistrationCoworkingSpace extends Fragment {
    int hour, minute;
    Button btnTimeStart, btnTimeEnd, btnNext;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TextInputLayout tilDate;
    EditText edtDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_registration_coworking_space, container, false);

        initFields(thisView);
        initListeners(thisView);
        setTimeButtonListener(btnTimeStart, thisView);
        setTimeButtonListener(btnTimeEnd, thisView);


        return thisView;
    }

    private void initFields(View thisView) {
        btnTimeStart = thisView.findViewById(R.id.button_Time_Start);
        btnTimeEnd   = thisView.findViewById(R.id.button_Time_End);
        tilDate      = thisView.findViewById(R.id.outlinedTextField_Date);
        edtDate      = thisView.findViewById(R.id.editText_Date);
        btnNext      = thisView.findViewById(R.id.button_Next);
    }

    private void setTimeButtonListener(Button button, View thisView) {
        button.setOnClickListener(view -> {

            timeSetListener = ((timePicker, hour_, minute_) ->
                    button.setText(String.format(Locale.getDefault(),
                            "%02d:%02d", hour_, minute_)));

            TimePickerDialog timePickerDialog = new TimePickerDialog(thisView.getContext(),
                    timeSetListener, hour, minute, true);
            timePickerDialog.setTitle("");
            timePickerDialog.show();
        });
    }

    private void initListeners(View thisView){
        Calendar calendar = Calendar.getInstance();
        final int year  = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day   = calendar.get(Calendar.DAY_OF_MONTH);
        tilDate.setEndIconOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(thisView.getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener, year, month, day);
            datePickerDialog.show();
        });

        dateSetListener = (datePicker, year_, month_, day_) -> {
            month_++;
            String date = day_ + "/" + month_ + "/" + year_;
            edtDate.setText(date);
        };

        btnNext.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(thisView.getContext());
            builder.setTitle(getString(R.string.coworking_equipment_ask));

            String[] tools = (String[]) DBCommunication.getTools().toArray();
            //String[] testData = {"ноутбук1", "ноутбук2", "ноутбук3", "ноутбук4", "ноутбук5"};
            boolean[] checkedItems = {false, false, false, false, false};

            builder.setMultiChoiceItems(tools, checkedItems,
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