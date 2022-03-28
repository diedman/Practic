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

import com.google.android.material.textview.MaterialTextView;

import java.sql.Timestamp;
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
    int freeSpacesQuantity;
    String chosenDate;
    String chosenStartTime;
    String chosenEndTime;
    String chosenPurpose;
    Button btnNext;
    RadioGroup rgPurposes;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    DatePickerDialog.OnDateSetListener dateSetListener;
    EditText edtDate, edtStartTime, edtEndTime;
    MaterialTextView mtvSpacesInfo, mtvFreeSpacesInfo;
    AutoCompleteTextView menuCoworking;

    enum RegistrationResults {
        OK,
        NO_SPACES,
        NO_CONNECTION
    }

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

    @Override
    public void onResume() {
        super.onResume();

        setFreeSpaces();
    }

    //Метод инициализации полей
    private void initFields(View thisView) {
        edtDate           = thisView.findViewById(R.id.editText_Date);
        edtStartTime      = thisView.findViewById(R.id.editText_Start_Time);
        edtEndTime        = thisView.findViewById(R.id.editText_End_Time);
        mtvSpacesInfo     = thisView.findViewById(R.id.textView_Spaces_Information);
        mtvFreeSpacesInfo = thisView.findViewById(R.id.textView_Free_Spaces_Information);
        btnNext           = thisView.findViewById(R.id.button_Next);
        menuCoworking     = thisView.findViewById(R.id.autoCompleteTextView_Coworking_Registration);
        rgPurposes        = thisView.findViewById(R.id.radiogroup_Purpose);
    }

    //Метод установки обработчика на поле для вывода выбора времени
    private void setTimeListener(EditText editText, View thisView, String title) {
        editText.setOnClickListener(view -> {

            timeSetListener = ((timePicker, hour_, minute_) -> {
                editText.setText(String.format(Locale.getDefault(),
                    "%02d:%02d", hour_, minute_));
                setFreeSpaces();
            });

            TimePickerDialog timePickerDialog = new TimePickerDialog(thisView.getContext(),
                    timeSetListener, hour, minute, true);
            timePickerDialog.setTitle(title);
            timePickerDialog.show();
        });
    }

    //Метод инициализации обработчиков
    private void initListeners(View thisView){
        Calendar calendar = Calendar.getInstance();
        final int year  = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day   = calendar.get(Calendar.DAY_OF_MONTH);
        //Обработчик вызова окна выбора даты
        edtDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(thisView.getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener, year, month, day);
            datePickerDialog.show();
        });

        //Обработчик изменения даты в поле
        dateSetListener = (datePicker, year_, month_, day_) -> {
            month_++;
            String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", day_, month_, year_);
            chosenDate = date;
            edtDate.setText(date);

            setFreeSpaces();
        };

        //Обработчик нажатия на кнопку "Далее"
        btnNext.setOnClickListener(view -> {
            if (freeSpacesQuantity < 1) {
                showResInfoDialog(thisView, RegistrationResults.NO_SPACES);
                return;
            }
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
            AtomicReference<String> resMessage = new AtomicReference<>();
            builder.setTitle(getString(R.string.coworking_equipment_ask));

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
                            showResInfoDialog(thisView, RegistrationResults.OK);
                        } else {
                            showResInfoDialog(thisView, RegistrationResults.NO_CONNECTION);
                        }

                    });

            builder.setNegativeButton(R.string.coworking_equipment_dialog_no,
                    (dialogInterface, listener) -> {
                        int res = registerOnCoworking(new String[0]);
                        if (res == 1) {
                            showResInfoDialog(thisView, RegistrationResults.OK);
                        } else {
                            showResInfoDialog(thisView, RegistrationResults.NO_CONNECTION);
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

        menuCoworking.setOnItemClickListener((adapterView, view, i, l) -> {
            chosenSpaceId = spaces.get(i).getId();

            String spaceQuantity = String.valueOf(DBCommunication.getSpacesQuantity(chosenSpaceId));
            mtvSpacesInfo.setText(spaceQuantity);
            setFreeSpaces();
        });
    }

    private void setFreeSpaces() {
        String coworking = menuCoworking.getText().toString().trim();

        chosenDate      = edtDate.getText().toString().trim();
        chosenStartTime = edtStartTime.getText().toString().trim();
        chosenEndTime   = edtEndTime.getText().toString().trim();

        if (!coworking.isEmpty() && !chosenDate.isEmpty() && !chosenStartTime.isEmpty() && !chosenEndTime.isEmpty()) {
            Timestamp startSessionDateTime = null, endSessionDateTime = null;
            try {
                startSessionDateTime = new Timestamp(Objects.requireNonNull(new SimpleDateFormat("hh:mm dd/MM/yyyy")
                        .parse(chosenStartTime + " " + chosenDate)).getTime());
                endSessionDateTime = new Timestamp(Objects.requireNonNull(new SimpleDateFormat("hh:mm dd/MM/yyyy")
                        .parse(chosenEndTime + " " + chosenDate)).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            freeSpacesQuantity = DBCommunication.getFreeSpacesQuantity(chosenSpaceId, startSessionDateTime, endSessionDateTime);
            mtvFreeSpacesInfo.setText(String.valueOf(freeSpacesQuantity));
        }
    }

    private int registerOnCoworking(String[] tools) {
        Timestamp startDateTime = null, endDateTime = null;
        try {
             startDateTime = new Timestamp(Objects.requireNonNull(new SimpleDateFormat("hh:mm dd/MM/yyyy")
                    .parse(chosenStartTime + " " + chosenDate)).getTime());
             endDateTime = new Timestamp(Objects.requireNonNull(new SimpleDateFormat("hh:mm dd/MM/yyyy")
                     .parse(chosenEndTime + " " + chosenDate)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String strqr = Utilities.getRandomHexStr(64);
        return DBCommunication.registerOnCoworking(chosenSpaceId, CoworkerData.id, strqr,
                                                   startDateTime, endDateTime, chosenPurpose, tools);
    }

    private void showResInfoDialog(View view, RegistrationResults rr) {
        switch (rr) {
            case OK:
                showDialog(view, "Вы зарегистрировались!", "Приходите " + chosenDate + " к " + chosenStartTime + " на коворкинг по адресу " + menuCoworking.getText().toString() + ".");
                break;

            case NO_SPACES:
                showDialog(view, "Свободных мест нет!", "Попробуйте выбрать другую дату или время.");
                break;

            case NO_CONNECTION:
                showDialog(view, "Что-то пошло не так!", "Регистрация не удалась! Проверьте подключение интернету.");
                break;
        }
    }

    private void showDialog(View view, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> {});

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}