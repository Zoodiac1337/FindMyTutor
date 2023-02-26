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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class StudentNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        final Fragment Favourites = new Favourites();
        final Fragment Chat = new Chat();
        final Fragment Search = new Search();

        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.Favourites:
                                fragment = Favourites;
                                break;
                            case R.id.Chat:
                                fragment = Chat;
                                break;
                            case R.id.Search:
                            default:
                                fragment = Search;
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
        Intent myIntent = new Intent(StudentNavigationActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }
}