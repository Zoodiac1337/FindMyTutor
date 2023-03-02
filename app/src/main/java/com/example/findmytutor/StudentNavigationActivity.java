package com.example.findmytutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class StudentNavigationActivity extends AppCompatActivity {
    int screen = 1;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment Favourites = new Favourites();
    final Fragment Chat = new Chat();
    final Fragment Search = new Search();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Bundle bundle = getIntent().getExtras();

        Favourites.setArguments(bundle);
        Search.setArguments(bundle);


        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.Favourites:
                                fragment = Favourites;
                                screen = 1;
                                break;
                            case R.id.Chat:
                                fragment = Chat;
                                screen = 2;
                                break;
                            case R.id.Search:
                            default:
                                fragment = Search;
                                screen = 3;
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.FragmentedView, fragment).commit();
                        return true;
                    }
                });
        // Set default selection
        fragmentManager.beginTransaction().replace(R.id.FragmentedView, Favourites).commit();
        bottomNavigationView.setSelectedItemId(R.id.Favourites);
    }
    public void logoutButton(View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(StudentNavigationActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
            Toast.makeText(this, "Back button pressed", Toast.LENGTH_SHORT).show();
            if (screen == 1) {
                fragmentManager.beginTransaction().detach(Favourites).commit();
                fragmentManager.beginTransaction().attach(Favourites).commit();
            }
            else if (screen == 2) {
            fragmentManager.beginTransaction().detach(Chat).commit();
            fragmentManager.beginTransaction().attach(Chat).commit();
            }
            else if (screen == 3) {
                fragmentManager.beginTransaction().detach(Search).commit();
                fragmentManager.beginTransaction().attach(Search).commit();
            }
    }
}