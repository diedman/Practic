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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class FragmentRegistration extends Fragment {
    TextView tvForgotPassword, tvSignIn;
    Button btnSignUp;
    AutoCompleteTextView menuGender, menuMaritalStatus;
    TextInputLayout tilBirthDate;
    EditText edtEmail, edtPassword, edtFirstname,
                    edtLastname, edtBirthDate, edtPhoneNum;
    DatePickerDialog.OnDateSetListener dateSetListener;

    public static FragmentRegistration newInstance() {
        return new FragmentRegistration();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_registration, container, false);

        initFields(thisView);
        initAdapters(thisView);

        return thisView;
    }

    private void initFields(View thisView) {
        edtEmail          = thisView.findViewById(R.id.editText_Email);
        edtPassword       = thisView.findViewById(R.id.editText_Password);
        edtFirstname      = thisView.findViewById(R.id.editText_First_Name);
        edtLastname       = thisView.findViewById(R.id.editText_Last_Name);
        tilBirthDate      = thisView.findViewById(R.id.outlinedTextField_Birthday);
        edtBirthDate      = thisView.findViewById(R.id.editText_Birthday);
        edtPhoneNum       = thisView.findViewById(R.id.editText_Phone);
        menuGender        = thisView.findViewById(R.id.autoCompleteTextView_Gender);
        menuMaritalStatus = thisView.findViewById(R.id.autoCompleteTextView_Marital_Status);
        tvForgotPassword  = thisView.findViewById(R.id.textView_Forgot_Password);
        tvSignIn          = thisView.findViewById(R.id.textView_SignIn);
        btnSignUp         = thisView.findViewById(R.id.button_SingUp);
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
            if (!registration()) {
                Toast.makeText(getContext(), "Регистрация не удалась!", Toast.LENGTH_LONG).show();
                return;
            }

            SharedPreferences appPref = requireContext().getSharedPreferences("app_shared_data",
                                                                            Context.MODE_PRIVATE);
            appPref.edit().putBoolean("hasVisited", true).apply();

            getParentFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, FragmentLenta.class, null)
                    .commit();
        });

        tvSignIn.setOnClickListener(view -> getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, FragmentLogin.class, null)
                .commit());

        tvForgotPassword.setOnClickListener(view -> {

        });
    }

    private boolean registration() {
        String firstname     = edtFirstname.getText().toString();
        String lastname      = edtLastname.getText().toString();
        String password      = edtPassword.getText().toString();
        Date birthdate       = null;
        try {
            birthdate        = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy")
                    .parse(edtBirthDate.getText().toString())).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String email         = edtEmail.getText().toString();
        String phoneNum      = edtPhoneNum.getText().toString();
        String sex           = menuGender.getText().toString();
        String maritalStatus = menuMaritalStatus.getText().toString();
        int res = DBCommunication.registerCoworker(firstname, lastname, null,
                                         password, birthdate, email, phoneNum,
                                         sex, maritalStatus);
        return res == 1;
    }
}