package com.example.masquerade;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    TextView username,gender;
    FirebaseUser fuser;
    DatabaseReference contactReference,reference;
    Intent intent;
    DatabaseReference database;
    String uid, contactId;
    int friendLevel, theirFriendLevel;
    Button addFriend, removeContact;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.profile);
        username = findViewById(R.id.username);
        gender = findViewById(R.id.gender);
        addFriend = findViewById(R.id.btn_addFriend);
        removeContact = findViewById(R.id.btn_removeContact);


        intent = getIntent();
        contactId = intent.getStringExtra("contactid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        uid = fuser.getUid();
        contactReference = FirebaseDatabase.getInstance().getReference("Users").child(contactId);
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        //update();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("test", contactReference.child("nickname").toString());
                Log.d("test", contactReference.toString());
                friendLevel = Integer.parseInt(dataSnapshot.child(uid).child("friendlists").child(contactId).getValue().toString());
                theirFriendLevel = Integer.parseInt(dataSnapshot.child(contactId).child("friendlists").child(uid).getValue().toString());
                if(friendLevel == -3){
                    username.setText("No information available, since you two are no longer contacts.");
                    gender.setText("");
                    addFriend.setEnabled(false);
                    removeContact.setEnabled(false);
                    return;
                }
                if(friendLevel >=-1){
                    addFriend.setEnabled(false);
                }
                if(friendLevel >=0){
                    username.setText("nickname: " + dataSnapshot.child(contactId).child("nickname").getValue().toString());
                }
                else{
                    username.setText("nickname: Anonymous(This information will be visible if you become friends.)");
                }
                if(friendLevel >=10){
                    gender.setText("gender: " + dataSnapshot.child(contactId).child("gender").getValue().toString());
                }
                else if(friendLevel >=0){
                    gender.setText("gender: Invisible (This information will be visible if you talk more)");
                }
                else {
                    gender.setText("gender: Invisible (This information will be visible if you become friends.)");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void update() {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(contactId).child("friendlists");
//        ref.addChildEventListener(new ChildEventListener(){
//
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////                pairedUser = dataSnapshot.child("contactlists").getValue(String.class);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if(dataSnapshot.getKey().toString().equals(uid)){
//                    theirFriendLevel = Integer.parseInt(dataSnapshot.getValue().toString());
//                }
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }

    public void makeFriend(View view) {
        if(theirFriendLevel == -2){
            reference.child(uid).child("friendlists").child(contactId).setValue(-1);
        }
        else if(theirFriendLevel == -1){
            reference.child(uid).child("friendlists").child(contactId).setValue(0);
            reference.child(contactId).child("friendlists").child(uid).setValue(0);
            Toast.makeText(ProfileActivity.this, "You two are friends now!", Toast.LENGTH_LONG).show();
        }
    }

    public void removeContact(View view) {
        reference.child(uid).child("friendlists").child(contactId).setValue(-3);
        reference.child(contactId).child("friendlists").child(uid).setValue(-3);
        removeContact.setEnabled(false);
    }
}