package com.example.practic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_DATA = "app_shared_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences appPref = getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE);

        boolean hasVisited = appPref.getBoolean("hasVisited", false);
        if (!hasVisited) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new FragmentRegistration())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new FragmentLogin())
                    .commit();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}