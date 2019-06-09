package com.example.masquerade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.masquerade.MessageAdapter;
import com.example.masquerade.Chat;
import com.example.masquerade.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference,myReference;

    ImageButton btn_send, btn_profile;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    Intent intent;
    int friendLevel, theirFriendLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        btn_profile = findViewById(R.id.btn_profile);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if(friendLevel == -3) {
                    Toast.makeText(MessageActivity.this, "You can't send message, since you two are no longer contacts", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg);
                } else{
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                if(friendLevel >= 0) {
                    myReference.child("friendlists").child(userid).setValue(friendLevel + 1);
                }
                text_send.setText("");
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MessageActivity.this,ProfileActivity.class);
                profileIntent.putExtra("contactid",userid);
                startActivity(profileIntent);
            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tagString = dataSnapshot.child("contactlists").child(fuser.getUid()).child("tags").getValue().toString();
                Log.d("listtest", tagString);
                if(tagString.charAt(0) == ' '){
                    myReference.child("contactlists").child(userid).child("tags").setValue(tagString.substring(1));
                }
                else{
                    myReference.child("contactlists").child(userid).child("tags").setValue(tagString);
                }
                Boolean isFriend = dataSnapshot.child("contactlists").child(fuser.getUid()).child("isFriend").getValue(Boolean.class);
                User user = dataSnapshot.getValue(User.class);
                if(isFriend){
                    username.setText(user.nickname);
                }

                else {
                    username.setText("Anonymous");
                }
                if (user.getProfileInd().equals("default_pic")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    profile_image.setImageResource(getResources().getIdentifier(user.getProfileInd(), "drawable", getPackageName()));
                }

                readMessages(fuser.getUid(), userid, user.getProfileInd());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendLevel = Integer.parseInt(dataSnapshot.child("friendlists").child(userid).getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);

    }


    private void readMessages(final String myid, final String userid, final String imageurl){

        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }


                    messageAdapter = new MessageAdapter(MessageActivity.this,mchat, imageurl);

                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
