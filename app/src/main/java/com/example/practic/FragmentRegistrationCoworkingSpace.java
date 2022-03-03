package com.example.practic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentRegistrationCoworkingSpace extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View thisView = inflater.inflate(R.layout.fragment_registration_coworking_space, container, false);
        return thisView;
    }
}