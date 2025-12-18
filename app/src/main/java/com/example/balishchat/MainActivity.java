package com.example.balishchat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView imgLogout, settingsIcon, messageIcon;
    RecyclerView mainUserRecyclerView;
    UserAdpter adapter;
    ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        // Redirect to login if not signed in
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
            return;
        }

        // Find views safely
        try {
            imgLogout = findViewById(R.id.logoutimg);
            settingsIcon = findViewById(R.id.settingBut);
            messageIcon = findViewById(R.id.messageIcon);
            mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        } catch (Exception e) {
            Toast.makeText(this, "Check your XML IDs!", Toast.LENGTH_LONG).show();
            return;
        }

        // RecyclerView setup
        usersArrayList = new ArrayList<>();
        adapter = new UserAdpter(this, usersArrayList);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainUserRecyclerView.setAdapter(adapter);

        // Fetch users from Firebase safely
        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        usersArrayList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot unique : snapshot.getChildren()) {
                                Users users = unique.getValue(Users.class);
                                if (users != null &&
                                        !users.getUserId().equals(auth.getCurrentUser().getUid())) {
                                    usersArrayList.add(users);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
                    }
                });

        // Logout button
        imgLogout.setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialoge_layout);

            Button yes = dialog.findViewById(R.id.yesbnt);
            Button no = dialog.findViewById(R.id.nobnt);

            yes.setOnClickListener(v1 -> {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, login.class));
                finish();
            });

            no.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });

        // Settings icon click
        settingsIcon.setOnClickListener(v -> {
            try {
                startActivity(new Intent(MainActivity.this, setting.class));
            } catch (Exception e) {
                Toast.makeText(this, "Settings page not found.", Toast.LENGTH_SHORT).show();
            }
        });

        // Message icon click (dummy chat)
        messageIcon.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, chatwindo.class);
                intent.putExtra("uid", "TEST_UID");
                intent.putExtra("nameeee", "Test User");
                intent.putExtra("reciverImg", "https://www.w3schools.com/howto/img_avatar.png");
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Failed to open chat.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
