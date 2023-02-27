package com.example.findmytutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class TutorNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        final Fragment Availability = new Availability();
        final Fragment Chat = new Chat();
        final Fragment Search = new Search();

        Bundle bundle = getIntent().getExtras();

        Availability.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.FragmentedView, Availability).commit();

        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.Availability:
                            default:
                                fragment = Availability;
                                fragment.setArguments(bundle);
                                break;
                            case R.id.Chat:
                                fragment = Chat;
                                fragment.setArguments(bundle);
                                break;
                            case R.id.Search:
                                fragment = Search;
                                fragment.setArguments(bundle);
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.FragmentedView, fragment).commit();
                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.Favourites);
    }

    public void logoutButton(View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(TutorNavigationActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }
}