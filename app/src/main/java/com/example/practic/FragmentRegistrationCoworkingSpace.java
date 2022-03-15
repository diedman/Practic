package com.example.practic;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FragmentRegistrationCoworkingSpace extends Fragment {
    int hour, minute;
    Button btnNext;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    DatePickerDialog.OnDateSetListener dateSetListener;
    EditText edtDate, edtStartTime, edtEndTime;
    AutoCompleteTextView menuCoworking;

    public static FragmentRegistrationCoworkingSpace newInstance() {
        return new FragmentRegistrationCoworkingSpace();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_registration_coworking_space, container, false);

        initFields(thisView);
        initListeners(thisView);
        setTimeListener(edtStartTime, thisView, getString(R.string.coworking_choose_start_time));
        setTimeListener(edtEndTime, thisView, getString(R.string.coworking_choose_end_time));

        return thisView;
    }

    private void initFields(View thisView) {
        edtDate       = thisView.findViewById(R.id.editText_Date);
        edtStartTime  = thisView.findViewById(R.id.editText_Start_Time);
        edtEndTime    = thisView.findViewById(R.id.editText_End_Time);
        btnNext       = thisView.findViewById(R.id.button_Next);
        menuCoworking = thisView.findViewById(R.id.autoCompleteTextView_Coworking_Registration);
    }

    private void setTimeListener(EditText editText, View thisView, String title) {
        editText.setOnClickListener(view -> {

            timeSetListener = ((timePicker, hour_, minute_) ->
                    editText.setText(String.format(Locale.getDefault(),
                            "%02d:%02d", hour_, minute_)));

            TimePickerDialog timePickerDialog = new TimePickerDialog(thisView.getContext(),
                    timeSetListener, hour, minute, true);
            timePickerDialog.setTitle(title);
            timePickerDialog.show();
        });
    }

    private void initListeners(View thisView){
        Calendar calendar = Calendar.getInstance();
        final int year  = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day   = calendar.get(Calendar.DAY_OF_MONTH);
        edtDate.setOnClickListener(view -> {
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
        //TODO: Подавать значения из базы сюда
        ArrayList<CoworkingSpace> testArray = (ArrayList<CoworkingSpace>) DBCommunication.getCoworkingSpaces();//new ArrayList<>();
//        testArray.add(new CoworkingSpace(0,"Coworking1",1,1));
//        testArray.add(new CoworkingSpace(1,"Coworking2",1,1));

        //TODO: Если есть идеи, как лучше подавать строку в адаптер, то поправьте
        ArrayList<String> titles = new ArrayList<>();
        for (CoworkingSpace space : testArray){
            titles.add(space.getTitle());
        }

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, titles);
        menuCoworking.setAdapter(menuAdapter);
    }
}