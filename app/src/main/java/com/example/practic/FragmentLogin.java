package com.example.practic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentLogin extends Fragment {
    TextView tvForgotPassword, tvSignUp;
    Button btnSignIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_login, container, false);

        initFields(thisView);
        initAdapters();

        return thisView;
    }

    private void initFields(View thisView) {
        tvForgotPassword = thisView.findViewById(R.id.textView_Forgot_Password);
        tvSignUp = thisView.findViewById(R.id.textView_SignUp);
        btnSignIn = thisView.findViewById(R.id.button_SingIn);
    }

    private void initAdapters() {
        btnSignIn.setOnClickListener(view -> {
            SharedPreferences appPref = requireContext().getSharedPreferences("app_shared_data",
                                                                             Context.MODE_PRIVATE);
            appPref.edit().putBoolean("hasVisited", true).apply();
        });

        tvSignUp.setOnClickListener(view -> getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, FragmentRegistration.class, null)
                .commit());

        tvForgotPassword.setOnClickListener(view -> {

        });
    }
}