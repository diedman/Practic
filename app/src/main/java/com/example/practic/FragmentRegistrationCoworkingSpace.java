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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class FragmentRegistrationCoworkingSpace extends Fragment {
    int hour, minute;
    int chosenSpaceId;
    String chosenDate;
    String chosenStartTime;
    String chosenEndTime;
    String chosenPurpose;
    Button btnNext;
    RadioGroup rgPurposes;
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
        rgPurposes    = thisView.findViewById(R.id.radiogroup_Purpose);
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
            chosenDate      = edtDate.getText().toString();
            chosenStartTime = edtStartTime.getText().toString();
            chosenEndTime   = edtEndTime.getText().toString();

            for (int i = 0; i < rgPurposes.getChildCount(); ++i) {
                View rb = rgPurposes.getChildAt(i);
                if ((rb instanceof RadioButton) && ((RadioButton)rb).isChecked()) {
                    chosenPurpose = ((RadioButton)rb).getText().toString();
                    break;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(thisView.getContext());
            AtomicReference<String> resMessage = null;
            builder.setTitle(getString(R.string.coworking_equipment_ask));

            // TODO: Подавать значения из базы
            List<String> toolsList = DBCommunication.getTools();//{"ноутбук1", "ноутбук2", "ноутбук3", "ноутбук4", "ноутбук5"};
            String[] tools = new String[toolsList.size()];
            toolsList.toArray(tools);
            boolean[] checkedItems = new boolean[tools.length];

            builder.setMultiChoiceItems(tools, checkedItems,
                    (dialogInterface, item, isChecked) -> {});

            builder.setPositiveButton(R.string.coworking_equipment_dialog_yes,
                    (dialogInterface, listener) -> {
                        toolsList.clear();
                        for (int i = 0; i < checkedItems.length; ++i) {
                            if (checkedItems[i]) {
                                toolsList.add(tools[i]);
                            }
                        }

                        String[] checkedTools = new String[toolsList.size()];
                        toolsList.toArray(checkedTools);

                        int res = registerOnCoworking(checkedTools);
                        if (res == 1) {
                            resMessage.set("Регистрация прошла успешно!");
                        } else if (res == 0) {
                            resMessage.set("Вы уже зарегистрированы!");
                        } else {
                            resMessage.set("Соединение с сервером не установлено! Пожалуйста, проверьте подключение к интернету!");
                        }
                    });

            builder.setNegativeButton(R.string.coworking_equipment_dialog_no,
                    (dialogInterface, listener) -> {
                        int res = registerOnCoworking(new String[0]);
                        if (res == 1) {
                            resMessage.set("Регистрация прошла успешно!");
                        } else if (res == 0) {
                            resMessage.set("Вы уже зарегистрированы!");
                        } else {
                            resMessage.set("Соединение с сервером не установлено! Пожалуйста, проверьте подключение к интернету!");
                        }

//                        builder.setTitle(resMessage.get());
//                        dialog = builder.
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
        //TODO: Подавать значения из базы сюда
        ArrayList<CoworkingSpace> spaces = (ArrayList<CoworkingSpace>) DBCommunication.getCoworkingSpaces();//new ArrayList<>();
//        testArray.add(new CoworkingSpace(0,"Coworking1",1,1));
//        testArray.add(new CoworkingSpace(1,"Coworking2",1,1));

        //TODO: Если есть идеи, как лучше подавать строку в адаптер, то поправьте
        ArrayList<String> titles = new ArrayList<>();
        for (CoworkingSpace space : spaces){
            titles.add(space.getTitle());
        }

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, titles);
        menuCoworking.setAdapter(menuAdapter);

        menuCoworking.setOnItemClickListener((adapterView, view, i, l) -> chosenSpaceId = spaces.get(i).getId());
    }

    private int registerOnCoworking(String[] tools) {
        Date startDateTime = null, endDateTime = null;
        try {
             startDateTime = new Date(Objects.requireNonNull(new SimpleDateFormat("hh:mm dd/MM/yyyy")
                    .parse(chosenStartTime + " " + chosenDate)).getTime());
             endDateTime = new Date(Objects.requireNonNull(new SimpleDateFormat("hh:mm dd/MM/yyyy")
                     .parse(chosenEndTime + " " + chosenDate)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String strqr = Utilities.getRandomHexStr(64);
        return DBCommunication.registerOnCoworking(chosenSpaceId, CoworkerData.id, strqr,
                                                   startDateTime, endDateTime, chosenPurpose, tools);
    }
}