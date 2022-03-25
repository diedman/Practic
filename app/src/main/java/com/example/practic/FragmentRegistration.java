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

    //Метод инициализации полей
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
        btnSignUp         = thisView.findViewById(R.id.button_reg_event_SingUp);
    }

    //Метод инициализации адаптеров
    private void initAdapters(View thisView) {
        Calendar calendar = Calendar.getInstance();
        final int year  = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day   = calendar.get(Calendar.DAY_OF_MONTH);

        //Обработчик который вызывает выбор даты
        edtBirthDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(thisView.getContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener, year, month, day);
            datePickerDialog.show();
        });

        //Обработчик меняет дату в текстовом поле
        dateSetListener = (datePicker, year_, month_, day_) -> {
            month_++;
            String date = day_ + "/" + month_ + "/" + year_;
            edtBirthDate.setText(date);
        };

        //Адаптер для поля пола
        ArrayAdapter<String> adapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.genders));
        menuGender.setAdapter(adapter);

        //Адаптер для поля семейного статуса
        adapter = new ArrayAdapter<>(thisView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.marital_status));
        menuMaritalStatus.setAdapter(adapter);

        //Адаптер для кнопки регистрации
        btnSignUp.setOnClickListener(view -> {

            String firstname     = edtFirstname.getText().toString().trim();
            String lastname      = edtLastname.getText().toString().trim();
            String password      = edtPassword.getText().toString().trim();
            Date birthdate;
            try {
                birthdate        = new java.sql.Date(Objects.requireNonNull(new SimpleDateFormat("dd/MM/yyyy")
                        .parse(edtBirthDate.getText().toString())).getTime());
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Неверный формат даты!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return;
            }
            String email         = edtEmail.getText().toString().trim();
            String phoneNum      = edtPhoneNum.getText().toString().trim();
            String sex           = menuGender.getText().toString().trim();
            String maritalStatus = menuMaritalStatus.getText().toString().trim();

            if (firstname.equals("") || lastname.equals("") || password.equals("")
                    || email.equals("") || sex.equals("") || maritalStatus.equals(""))
            {
                Toast.makeText(getContext(), "Не все обязательные поля заполнены!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean registerRes = DBCommunication.registerCoworker(firstname, lastname, null,
                    password, birthdate, email, phoneNum,
                    sex, maritalStatus) == 1;

            if (registerRes) {
                Toast.makeText(getContext(), "Регистрация не удалась!", Toast.LENGTH_LONG).show();
                return;
            }

            SharedPreferences appPref = requireContext().getSharedPreferences("app_shared_data",
                                                                            Context.MODE_PRIVATE);
            appPref.edit().putString("email", email).apply();
            appPref.edit().putString("password", password).apply();

            getParentFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, FragmentLenta.class, null)
                    .commit();
        });

        //Адаптер для перехода на форму входа
        tvSignIn.setOnClickListener(view -> getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, FragmentLogin.class, null)
                .commit());

        tvForgotPassword.setOnClickListener(view -> {

        });
    }

}