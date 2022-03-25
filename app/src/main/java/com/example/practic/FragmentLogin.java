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
        initAdapters();

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
    private void initAdapters() {
        //Обработчик кнопки входа в аккаунт
        btnSignIn.setOnClickListener(view -> {
            if (!authenticate()) {
                Toast.makeText(getContext(), "Авторизация не удалась!", Toast.LENGTH_LONG).show();
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

        //Адаптер для перехода на форму регистрации
        tvSignUp.setOnClickListener(view -> getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, FragmentRegistration.class, null)
                .commit());

        tvForgotPassword.setOnClickListener(view -> {

        });
    }

    private boolean authenticate() {
        String email    = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        int res = DBCommunication.authenticateCoworker(email, password);
        return res == 1;
    }
}