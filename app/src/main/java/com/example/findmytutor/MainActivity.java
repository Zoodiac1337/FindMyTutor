package com.example.findmytutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            String email = currentUser.getEmail();
            Intent myIntent = new Intent();
            if (email.endsWith("@my.ntu.ac.uk"))
                myIntent = new Intent(MainActivity.this, StudentNavigationActivity.class);
            else if (email.endsWith("@ntu.ac.uk"))
                myIntent = new Intent(MainActivity.this, TutorNavigationActivity.class);
            myIntent.putExtra("email", email);

            startActivity(myIntent);
            Toast.makeText(MainActivity.this, email+" logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void signin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Intent myIntent = new Intent();
                    if (email.endsWith("@my.ntu.ac.uk"))
                        myIntent = new Intent(MainActivity.this, StudentNavigationActivity.class);
                    else if (email.endsWith("@ntu.ac.uk"))
                        myIntent = new Intent(MainActivity.this, TutorNavigationActivity.class);
                    myIntent.putExtra("email", email);
                    startActivity(myIntent);
                    finish();
                    Toast.makeText(MainActivity.this, email+" logged in", Toast.LENGTH_SHORT).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignInActivity", "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signinButtonClicked(View view){
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);
        String sEmail = email.getText().toString();
        String sPassword = password.getText().toString();
        signin(sEmail, sPassword);
    }

    public void toSignupButtonClicked(View view) {
        Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(myIntent);
    }

    public void checkUser(String email){

    }
}