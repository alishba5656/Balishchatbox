package com.example.balishchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    EditText email, password;
    Button button;
    TextView logsignup;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextLogEmail);
        password = findViewById(R.id.editTextLogPassword);
        button = findViewById(R.id.logbutton);
        logsignup = findViewById(R.id.logsignup);

        // If already logged in, go to MainActivity
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        }

        button.setOnClickListener(v -> {
            String Email = email.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (TextUtils.isEmpty(Email)) {
                email.setError("Enter Email");
            } else if (TextUtils.isEmpty(pass)) {
                password.setError("Enter Password");
            } else if (!Email.matches(emailPattern)) {
                email.setError("Enter valid Email");
            } else if (pass.length() < 6) {
                password.setError("Password > 6 chars");
            } else {
                // Try login
                auth.signInWithEmailAndPassword(Email, pass)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Login successful → go to MainActivity
                                startActivity(new Intent(login.this, MainActivity.class));
                                finish();
                            } else {
                                // Login failed → redirect to signup
                                Toast.makeText(login.this, "User not found. Please Sign Up first.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(login.this, registration.class));
                            }
                        });
            }
        });

        // Signup text click → go to registration
        logsignup.setOnClickListener(v -> {
            startActivity(new Intent(login.this, registration.class));
        });
    }
}
