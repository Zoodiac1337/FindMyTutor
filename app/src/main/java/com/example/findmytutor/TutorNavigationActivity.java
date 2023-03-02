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

public class TutorNavigationActivity extends AppCompatActivity {
    int screen = 1;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment Favourites = new Favourites();
    final Fragment Availability = new Availability();
    final Fragment Chat = new Chat();
    final Fragment Search = new Search();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Bundle bundle = getIntent().getExtras();

        Favourites.setArguments(bundle);
        Search.setArguments(bundle);
        Availability.setArguments(bundle);


        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.Availability:
                            default:
                                fragment = Availability;
                                screen = 1;
                                break;
                            case R.id.Favourites:
                                fragment = Favourites;
                                screen = 2;
                                break;
                            case R.id.Chat:
                                fragment = Chat;
                                screen = 3;
                                break;
                            case R.id.Search:
                                fragment = Search;
                                screen = 4;
                                break;
                        }
                        fragmentManager.beginTransaction().replace(R.id.FragmentedView, fragment).commit();
                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.Availability);
        fragmentManager.beginTransaction().replace(R.id.FragmentedView, Availability).commit();
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
            if (singleTutorLayout.getVisibility() == View.VISIBLE) {
                singleTutorLayout.setVisibility(View.GONE);
                searchListView.setVisibility(View.VISIBLE);
            }
        }
        else if (screen == 3) {
            fragmentManager.beginTransaction().detach(Chat).commit();
            fragmentManager.beginTransaction().attach(Chat).commit();
        }
        else if (screen == 4) {
            ListView searchListView = (ListView) this.findViewById(R.id.searchList);
            LinearLayout singleTutorLayout = (LinearLayout) this.findViewById(R.id.singleTutor);
            if (singleTutorLayout.getVisibility() == View.VISIBLE) {
                singleTutorLayout.setVisibility(View.GONE);
                searchListView.setVisibility(View.VISIBLE);
            }
        }
    }
}