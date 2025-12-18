package com.example.balishchat;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatwindo extends AppCompatActivity {

    String reciverImg, reciverUid, reciverName, SenderUID;

    TextView reciverNName;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    public static String senderImg;
    public static String reciverIImg;
    CardView sendbtn;
    EditText textmsg;

    String senderRoom, reciverRoom;
    RecyclerView messageAdapter;
    ArrayList<msgModelclass> messagesArrayList;
    messagesAdpter mmessagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwindo);
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // -------------------------
        // 1️⃣ Safe Intent extras
        // -------------------------
        reciverName = getIntent().getStringExtra("nameeee");
        if (reciverName == null) reciverName = "No Name";

        reciverImg = getIntent().getStringExtra("reciverImg");
        if (reciverImg == null || reciverImg.isEmpty())
            reciverImg = "https://www.w3schools.com/howto/img_avatar.png";

        reciverUid = getIntent().getStringExtra("uid");
        if (reciverUid == null) reciverUid = "TEST_UID";

        // -------------------------
        // 2️⃣ Initialize views
        // -------------------------
        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        reciverNName = findViewById(R.id.recivername);

        messageAdapter = findViewById(R.id.msgadpter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);

        messagesArrayList = new ArrayList<>();
        mmessagesAdapter = new messagesAdpter(chatwindo.this, messagesArrayList);
        messageAdapter.setAdapter(mmessagesAdapter);

        // -------------------------
        // 3️⃣ Set receiver info
        // -------------------------
        reciverNName.setText(reciverName);


        SenderUID = firebaseAuth.getUid();
        if (SenderUID == null) SenderUID = "SENDER_TEST_UID";

        senderRoom = SenderUID + reciverUid;
        reciverRoom = reciverUid + SenderUID;

        // -------------------------
        // 4️⃣ Firebase references
        // -------------------------
        DatabaseReference userRef = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatRef = database.getReference().child("chats").child(senderRoom).child("messages");

        // -------------------------
        // 5️⃣ Load messages safely
        // -------------------------
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    msgModelclass message = dataSnapshot.getValue(msgModelclass.class);
                    if (message != null) messagesArrayList.add(message);
                }
                mmessagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chatwindo.this, "Failed to load messages.", Toast.LENGTH_SHORT).show();
            }
        });

        // -------------------------
        // 6️⃣ Load sender profile safely
        // -------------------------
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("profilepic") && snapshot.child("profilepic").getValue() != null) {
                    senderImg = snapshot.child("profilepic").getValue().toString();
                } else {
                    senderImg = "";
                }
                reciverIImg = reciverImg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // -------------------------
        // 7️⃣ Send button click
        // -------------------------
        sendbtn.setOnClickListener(view -> {
            String messageText = textmsg.getText().toString().trim();
            if (messageText.isEmpty()) {
                Toast.makeText(chatwindo.this, "Enter the message first", Toast.LENGTH_SHORT).show();
                return;
            }
            textmsg.setText("");
            Date date = new Date();
            msgModelclass message = new msgModelclass(messageText, SenderUID, date.getTime());

            // Save to Firebase safely
            try {
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(message)
                        .addOnCompleteListener(task -> database.getReference()
                                .child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .push().setValue(message));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(chatwindo.this, "Failed to send message", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
