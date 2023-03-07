package com.example.findmytutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class StudentNavigationActivity extends AppCompatActivity {
    int screen = 1;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment Favourites = new Favourites();
    final Fragment MazeMap = new MazeMap();
    final Fragment Search = new Search();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Bundle bundle = getIntent().getExtras();

        Favourites.setArguments(bundle);
        Search.setArguments(bundle);

        fragmentManager.beginTransaction().add(R.id.FragmentedView, MazeMap, "MazeMap").add(R.id.FragmentedView, Favourites).add(R.id.FragmentedView, Search).commit();

        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Favourites:
                                fragmentManager.beginTransaction().hide(MazeMap).detach(Search).attach(Favourites).commit();
                                screen = 1;
                                break;
                            case R.id.MazeMap:
                                fragmentManager.beginTransaction().detach(Favourites).detach(Search).show(MazeMap).commit();
                                screen = 2;
                                break;
                            case R.id.Search:
                            default:
                                fragmentManager.beginTransaction().detach(Favourites).hide(MazeMap).attach(Search).commit();
                                screen = 3;
                                break;
                        }

                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.Favourites);
    }
    public void backButton(View view){
        this.onBackPressed();
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
//            Toast.makeText(this, "Back button pressed", Toast.LENGTH_SHORT).show();
            if (screen == 1) {
                ListView searchListView = (ListView) this.findViewById(R.id.favouritesList);
                LinearLayout singleTutorLayout = (LinearLayout) this.findViewById(R.id.singleTutor);
                if (singleTutorLayout.getVisibility() == View.VISIBLE) {
                    singleTutorLayout.setVisibility(View.GONE);
                    searchListView.setVisibility(View.VISIBLE);
                }
            }
            else if (screen == 2) {
//            fragmentManager.beginTransaction().detach(MazeMap).commit();
//            fragmentManager.beginTransaction().attach(MazeMap).commit();
            }
            else if (screen == 3) {
                ListView searchListView = (ListView) this.findViewById(R.id.searchList);
                LinearLayout singleTutorLayout = (LinearLayout) this.findViewById(R.id.singleTutor);
                if (singleTutorLayout.getVisibility() == View.VISIBLE) {
                    singleTutorLayout.setVisibility(View.GONE);
                    searchListView.setVisibility(View.VISIBLE);
                }
            }
    }
    public void switchToMazeMap(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.MazeMap);
    }
}