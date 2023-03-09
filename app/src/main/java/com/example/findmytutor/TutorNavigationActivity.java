package com.example.findmytutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class TutorNavigationActivity extends AppCompatActivity {
    int screen = 1;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment Favourites = new Favourites();
    final Fragment Availability = new Availability();
    final Fragment MazeMap = new MazeMap();
    final Fragment Search = new Search();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        TextView titleTextView = findViewById(R.id.TitleTextView);
        Bundle bundle = getIntent().getExtras();

        Favourites.setArguments(bundle);
        Search.setArguments(bundle);
        Availability.setArguments(bundle);

        fragmentManager.beginTransaction().add(R.id.FragmentedView, Availability).add(R.id.FragmentedView, MazeMap, "MazeMap").add(R.id.FragmentedView, Favourites).add(R.id.FragmentedView, Search).commit();



        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Availability:
                            default:
                                fragmentManager.beginTransaction().hide(MazeMap).detach(Search).detach(Favourites).attach(Availability).commit();
                                screen = 1;
                                break;
                            case R.id.Favourites:
                                fragmentManager.beginTransaction().hide(MazeMap).detach(Search).detach(Availability).attach(Favourites).commit();
                                screen = 2;
                                break;
                            case R.id.MazeMap:
                                titleTextView.setText("NTU Maze Map");
                                fragmentManager.beginTransaction().show(MazeMap).detach(Search).detach(Favourites).detach(Availability).commit();
                                screen = 3;
                                break;
                            case R.id.Search:
                                fragmentManager.beginTransaction().hide(MazeMap).detach(Availability).detach(Favourites).attach(Search).commit();
                                screen = 4;
                                break;
                        }
                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.Availability);
    }

    public void backButton(View view){
        this.onBackPressed();
    }

    public void logoutButton(View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(TutorNavigationActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }
    @Override
    public void onBackPressed()
    {
//        Toast.makeText(this, "Back button pressed", Toast.LENGTH_SHORT).show();
        if (screen == 1) {
            fragmentManager.beginTransaction().detach(Availability).commit();
            fragmentManager.beginTransaction().attach(Availability).commit();
        }
        if (screen == 2) {
            ListView searchListView = (ListView) this.findViewById(R.id.favouritesList);
            LinearLayout singleTutorLayout = (LinearLayout) this.findViewById(R.id.singleTutor);
            SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) this.findViewById(R.id.swiperefresh);
            if (singleTutorLayout.getVisibility() == View.VISIBLE) {
                singleTutorLayout.setVisibility(View.GONE);
                searchListView.setVisibility(View.VISIBLE);
                pullToRefresh.setVisibility(View.VISIBLE);
            }
            else {
                fragmentManager.beginTransaction().detach(Favourites).commit();
                fragmentManager.beginTransaction().attach(Favourites).commit();
            }
        }

        else if (screen == 4) {
            ListView searchListView = (ListView) this.findViewById(R.id.searchList);
            LinearLayout singleTutorLayout = (LinearLayout) this.findViewById(R.id.singleTutor);
            SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) this.findViewById(R.id.swiperefresh);
            if (singleTutorLayout.getVisibility() == View.VISIBLE) {
                singleTutorLayout.setVisibility(View.GONE);
                searchListView.setVisibility(View.VISIBLE);
                pullToRefresh.setVisibility(View.VISIBLE);
            }
            else {
                fragmentManager.beginTransaction().detach(Search).commit();
                fragmentManager.beginTransaction().attach(Search).commit();
            }
        }
    }
    public void switchToMazeMap(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.MazeMap);
    }
}