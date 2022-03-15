package com.example.practic;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class FragmentAccountEdit extends Fragment {
    DatePickerDialog.OnDateSetListener dateSetListener;
    AutoCompleteTextView menuGender, menuMaritalStatus;
    EditText edtEmail, edtPassword, edtFirstname,
            edtLastname, edtBirthDate, edtPhoneNum;
    Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_account_edit, container, false);
        initFields(thisView);
        initAdapters(thisView);
        return thisView;
    }

    private void initFields (View thisView) {
        edtEmail          = thisView.findViewById(R.id.editText_Email_Edit);
        edtPassword       = thisView.findViewById(R.id.editText_Password_Edit);
        edtFirstname      = thisView.findViewById(R.id.editText_First_Name_Edit);
        edtLastname       = thisView.findViewById(R.id.editText_Last_Name_Edit);
        edtBirthDate      = thisView.findViewById(R.id.editText_Birthday_Edit);
        edtPhoneNum       = thisView.findViewById(R.id.editText_Phone_Edit);
        menuGender        = thisView.findViewById(R.id.autoCompleteTextView_Gender_Edit);
        menuMaritalStatus = thisView.findViewById(R.id.autoCompleteTextView_Marital_Status_Edit);
        btnSave           = thisView.findViewById(R.id.button_Save);
    }

    private void initAdapters(View thisView) {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtBirthDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(thisView.getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener, year, month, day);
            datePickerDialog.show();
        });

        dateSetListener = (datePicker, year_, month_, day_) -> {
            month_++;
            String date = day_ + "/" + month_ + "/" + year_;
            edtBirthDate.setText(date);
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.genders));
        menuGender.setAdapter(adapter);

        adapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.marital_status));
        menuMaritalStatus.setAdapter(adapter);


        btnSave.setOnClickListener(view -> {
            //Обработчик кнопки "Сохранить"
        });
    }
}