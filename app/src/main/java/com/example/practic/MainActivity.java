package com.example.practic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
        //bottomNavigationView.setVisibility(View.GONE);
        if (!hasVisited) {
            String email    = appPref.getString("email", "");
            String password = appPref.getString("password", ":");

            int isDataCorrect = DBCommunication.authenticateCoworker(email, password);

            if (isDataCorrect != 1) {
                loadFragment(FragmentRegistration.newInstance());
            }
        }
        else {
            loadFragment(FragmentLogin.newInstance());
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.page_news:
                            loadFragment(FragmentLenta.newInstance());
                            item.setChecked(true);
                            break;
                        case R.id.page_coworking:
                            loadFragment(FragmentRegistrationCoworkingSpace.newInstance());
                            item.setChecked(true);
                            break;
                        case R.id.page_personalArea:
                            loadFragment(FragmentAccount.newInstance());
                            item.setChecked(true);
                            break;
                    }
                    return false;
                });
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit();
    }
}