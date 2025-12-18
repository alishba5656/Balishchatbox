package com.example.balishchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {

    TextView loginbut;
    EditText rg_username, rg_email, rg_password, rg_repassword;
    Button rg_signup;
    CircleImageView rg_profileImg;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        loginbut = findViewById(R.id.loginbut);
        rg_username = findViewById(R.id.rgusername);
        rg_email = findViewById(R.id.rgemail);
        rg_password = findViewById(R.id.rgpassword);
        rg_repassword = findViewById(R.id.rgrepassword);
        rg_profileImg = findViewById(R.id.profilerg0);
        rg_signup = findViewById(R.id.signupbutton);

        loginbut.setOnClickListener(v -> {
            startActivity(new Intent(registration.this, login.class));
            finish();
        });

        rg_signup.setOnClickListener(v -> {
            String namee = rg_username.getText().toString().trim();
            String emaill = rg_email.getText().toString().trim();
            String password = rg_password.getText().toString().trim();
            String cPassword = rg_repassword.getText().toString().trim();
            String status = "Hey I'm Using This Application";

            if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cPassword)) {
                Toast.makeText(this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
            } else if (!emaill.matches(emailPattern)) {
                rg_email.setError("Type A Valid Email Here");
            } else if (password.length() < 6) {
                rg_password.setError("Password Must Be 6 Characters Or More");
            } else if (!password.equals(cPassword)) {
                rg_password.setError("The Password Doesn't Match");
            } else {
                auth.createUserWithEmailAndPassword(emaill, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if (imageURI != null) {
                                    storageReference.putFile(imageURI).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                                imageuri = uri.toString();
                                                Users users = new Users(id, namee, emaill, password, imageuri, status);
                                                reference.setValue(users).addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        startActivity(new Intent(registration.this, MainActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(registration.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            });
                                        }
                                    });
                                } else {
                                    // Default image if not selected
                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/av-messenger-dc8f3.appspot.com/o/man.png?alt=media";
                                    Users users = new Users(id, namee, emaill, password, imageuri, status);
                                    reference.setValue(users).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            startActivity(new Intent(registration.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(registration.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        rg_profileImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            imageURI = data.getData();
            rg_profileImg.setImageURI(imageURI);
        }
    }
}
