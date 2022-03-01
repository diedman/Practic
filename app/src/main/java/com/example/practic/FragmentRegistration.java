package com.example.practic;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentRegistration extends Fragment {
    TextView TextView_Forgot_Password, TextView_SignIn;
    Button Button_SignUp;
    AutoCompleteTextView Menu_Gender, Menu_Marital_Status;
    TextInputLayout OutlinedTextField_Birth_Date;
    EditText EditText_Birth_Date;
    DatePickerDialog.OnDateSetListener dateSetListener;


    public FragmentRegistration() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_registration, container, false);
        TextView_Forgot_Password = thisView.findViewById(R.id.textView_Forgot_Password);
        TextView_SignIn = thisView.findViewById(R.id.textView_SignUp);
        Button_SignUp = thisView.findViewById(R.id.button_SignIn);
        Menu_Gender = thisView.findViewById(R.id.autoCompleteTextView_Gender);
        Menu_Marital_Status = thisView.findViewById(R.id.autoCompleteTextView_Marital_Status);
        OutlinedTextField_Birth_Date = thisView.findViewById(R.id.outlinedTextField_Birth_Date);
        EditText_Birth_Date = thisView.findViewById(R.id.editText_Birth_Date);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        OutlinedTextField_Birth_Date.setEndIconOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(thisView.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
            datePickerDialog.show();
        });

        dateSetListener = (datePicker, year_, month_, day_) -> {
            month_++;
            String date = day_ + "/" + month_ + "/" + year_;
            EditText_Birth_Date.setText(date);
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.genders));
        Menu_Gender.setAdapter(adapter);

        adapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.marital_status));
        Menu_Marital_Status.setAdapter(adapter);

        Button_SignUp.setOnClickListener(view -> {

        });

        TextView_SignIn.setOnClickListener(view -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, FragmentLogin.class, null)
                    .commit();
        });

        TextView_Forgot_Password.setOnClickListener(view -> {

        });

        return thisView;
    }
}