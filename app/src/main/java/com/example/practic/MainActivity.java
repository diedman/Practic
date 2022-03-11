package com.example.practic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_DATA = "app_shared_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences appPref = getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE);


        boolean hasVisited = appPref.getBoolean("hasVisited", false);

        if (!hasVisited) loadFragment(FragmentRegistration.newInstance());
        else loadFragment(FragmentLogin.newInstance());

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.page_news:
                            loadFragment(FragmentLenta.newInstance());
                            break;
                        case R.id.page_coworking:
                            loadFragment(FragmentRegistrationCoworkingSpace.newInstance());
                            bottomNavigationView.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.page_personalArea:
                            loadFragment(FragmentAccount.newInstance());
                            break;
                    }
                    return false;
                });
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView, fragment);
        ft.commit();
    }
}