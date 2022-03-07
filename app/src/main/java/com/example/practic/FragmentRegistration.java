package com.example.practic;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class FragmentRegistration extends Fragment {
    TextView tvForgotPassword, tvSignIn;
    Button btnSignUp;
    AutoCompleteTextView menuGender, menuMaritalStatus;
    TextInputLayout tilBirthDate;
    EditText edtBirthDate;
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_registration, container, false);

        initFields(thisView);
        initAdapters(thisView);

        return thisView;
    }

    private void initFields(View thisView) {
        tvForgotPassword  = thisView.findViewById(R.id.textView_Forgot_Password);
        tvSignIn          = thisView.findViewById(R.id.textView_SignIn);
        btnSignUp         = thisView.findViewById(R.id.button_Time_Start);
        menuGender        = thisView.findViewById(R.id.autoCompleteTextView_Gender);
        menuMaritalStatus = thisView.findViewById(R.id.autoCompleteTextView_Marital_Status);
        tilBirthDate      = thisView.findViewById(R.id.outlinedTextField_Date);
        edtBirthDate      = thisView.findViewById(R.id.editText_Date);
    }

    private void initAdapters(View thisView) {
        Calendar calendar = Calendar.getInstance();
        final int year  = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day   = calendar.get(Calendar.DAY_OF_MONTH);
        tilBirthDate.setEndIconOnClickListener(view -> {
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

        btnSignUp.setOnClickListener(view -> {
            SharedPreferences appPref = requireContext().getSharedPreferences("app_shared_data",
                                                                            Context.MODE_PRIVATE);
            appPref.edit().putBoolean("hasVisited", true).apply();
        });

        tvSignIn.setOnClickListener(view -> getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, FragmentLogin.class, null)
                .commit());

        tvForgotPassword.setOnClickListener(view -> {

        });
    }
}