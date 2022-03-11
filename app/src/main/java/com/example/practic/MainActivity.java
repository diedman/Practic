package com.example.practic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        SharedPreferences appPref = getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE);

        boolean hasVisited = appPref.getBoolean("hasVisited", false);
        if (!hasVisited) {
            //bottomNavigationView.setVisibility(View.GONE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new FragmentRegistration())
                    .commit();

        } else {
            //bottomNavigationView.setVisibility(View.GONE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new FragmentLogin())
                    .commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.page_news:
                            loadFragment(new FragmentLenta());
                            item.setChecked(true);
                            break;
                        case R.id.page_coworking:
                            loadFragment(new FragmentRegistrationCoworkingSpace());
                            item.setChecked(true);
                            break;
                        case R.id.page_personalArea:
                            loadFragment(new FragmentAccount());
                            item.setChecked(true);
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