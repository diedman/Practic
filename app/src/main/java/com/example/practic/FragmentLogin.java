package com.example.practic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FragmentLogin extends Fragment {
    EditText edtEmail, edtPassword;
    TextView tvForgotPassword, tvSignUp;
    Button btnSignIn;

    public static FragmentLogin newInstance() {
        return new FragmentLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_login, container, false);

        initFields(thisView);
        initAdapters(thisView);

        return thisView;
    }

    //Метод инициализации полей
    private void initFields(View thisView) {
        edtEmail         = thisView.findViewById(R.id.editText_Email);
        edtPassword      = thisView.findViewById(R.id.editText_Password);
        tvForgotPassword = thisView.findViewById(R.id.textView_Forgot_Password);
        tvSignUp         = thisView.findViewById(R.id.textView_SignUp);
        btnSignIn        = thisView.findViewById(R.id.button_SingIn);
    }

    //Метод инициализации адаптеров
    private void initAdapters(View thisView) {
        //Обработчик кнопки входа в аккаунт
        btnSignIn.setOnClickListener(view -> {
            String email    = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.equals("") || password.equals("")) {
                Toast.makeText(getContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean authenticationRes = DBCommunication.authenticateCoworker(email, password) == 1;

            if (!authenticationRes) {
                Utilities.showMessageDialog(thisView, "Авторизация не удалась!", "");
                return;
            }

            SharedPreferences appPref = requireContext().getSharedPreferences("app_shared_data",
                                                                             Context.MODE_PRIVATE);
            appPref.edit().putString("email", email).apply();
            appPref.edit().putString("password", password).apply();

            MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
            getParentFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, FragmentLenta.class, null)
                    .commit();
        });

        //Адаптер для перехода на форму регистрации
        tvSignUp.setOnClickListener(view -> getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, FragmentRegistration.class, null)
                .commit());

        tvForgotPassword.setOnClickListener(view -> {

        });
    }

}