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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signup(String email, String password, String type, String sEmail, Map<String, Object> user){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    db.collection(type).document(sEmail).set(user);
                    Log.d("RegisterActivity", "createUserWithEmail:success");
                    Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signupButtonClicked(View view){
        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);
        EditText password2 = findViewById(R.id.editTextTextPassword2);
        EditText firstName = findViewById(R.id.FirstName);
        EditText lastName = findViewById(R.id.LastName);

        Map<String, Object> user = new HashMap<>();
        List <String> favourites = new ArrayList<>();
        user.put("firstName", firstName.getText().toString());
        user.put("lastName", lastName.getText().toString());
        user.put("favourites", favourites);
        user.put("avatarVersion", 1);
        user.put("availability", "");
        user.put("department", "");
        user.put("description", "");
        user.put("location", "");
        user.put("time", new Date());
        user.put("title", "");
        user.put("campus","");
        String type = "";
        String sEmail = email.getText().toString();
        String sPassword = password.getText().toString();
        String sPassword2 = password2.getText().toString();
        if (sEmail.endsWith("@ntu.ac.uk")) type ="Tutor";
        else if (sEmail.endsWith("@my.ntu.ac.uk")) type ="Student";
        else {
            Toast.makeText(RegisterActivity.this, "Please, use university email to create an account.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sPassword.equals(sPassword2)){
            signup(sEmail, sPassword, type, sEmail, user);
        }
        else Toast.makeText(RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
    }

    public void toLoginButtonClicked(View view) {
        Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(myIntent);
    }
}