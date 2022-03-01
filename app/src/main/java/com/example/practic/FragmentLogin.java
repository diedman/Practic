package com.example.practic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentLogin extends Fragment {
    TextView TextView_Forgot_Password, TextView_SignUp;
    Button Button_SignIn;

    public FragmentLogin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View thisView = inflater.inflate(R.layout.fragment_login, container, false);
        TextView_Forgot_Password = thisView.findViewById(R.id.textView_Forgot_Password);
        TextView_SignUp = thisView.findViewById(R.id.textView_SignUp);
        Button_SignIn = thisView.findViewById(R.id.button_SignIn);

        Button_SignIn.setOnClickListener(view -> {

        });

        TextView_SignUp.setOnClickListener(view -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true)
                    .replace(R.id.fragmentContainerView, FragmentRegistration.class, null)
                    .commit();
        });

        TextView_Forgot_Password.setOnClickListener(view -> {

        });

        return thisView;
    }
}