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

import android.widget.ImageView;

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

import java.util.*;

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
    TextView username,gender, knownTagsText, guide;
    FirebaseUser fuser;
    DatabaseReference contactReference,reference;
    Intent intent;
    DatabaseReference database;
    String uid, contactId;
    int friendLevel, theirFriendLevel;
    Button addFriend, removeContact;
    List<String> tagsKeys;

    ImageView profilePic;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.profile);
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

        username = findViewById(R.id.username);
        gender = findViewById(R.id.gender);
        guide = findViewById(R.id.notification4);
        knownTagsText = findViewById(R.id.knownTags);
        addFriend = findViewById(R.id.btn_addFriend);
        removeContact = findViewById(R.id.btn_removeContact);
        profilePic = findViewById(R.id.profile_pic);
        intent = getIntent();
        contactId = intent.getStringExtra("contactid");
        tagsKeys = new ArrayList<String>();
        tagsKeys.add("sports");
        tagsKeys.add("movie");
        tagsKeys.add("music");
        tagsKeys.add("video");
        tagsKeys.add("games");
        tagsKeys.add("digital technology");
        tagsKeys.add("fashion");
        tagsKeys.add("animation");
        tagsKeys.add("arts");
        tagsKeys.add("make-up");
        tagsKeys.add("travel");
        tagsKeys.add("food");
        tagsKeys.add("pets");
        tagsKeys.add("academic");

        Random r = new Random(contactId.hashCode());
        java.util.Collections.shuffle(tagsKeys, r);
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

                profilePic.setImageResource(getResources().getIdentifier(dataSnapshot.child(contactId).child("profileInd").getValue(String.class), "drawable", getPackageName()));

                friendLevel = Integer.parseInt(dataSnapshot.child(uid).child("friendlists").child(contactId).getValue().toString());
                theirFriendLevel = Integer.parseInt(dataSnapshot.child(contactId).child("friendlists").child(uid).getValue().toString());
                if(friendLevel == -3){
                    username.setText("Anonymous");
                    gender.setText("Gender: unknown");
                    addFriend.setEnabled(false);
                    addFriend.setText("You cannot add this contact as friend.");
                    removeContact.setEnabled(false);
                    guide.setText("You two are no longer contacts.");
                    knownTagsText.setText("");
                   // unknownTagsText.setText("");
                    return;
                }
                if(friendLevel ==-1){
                    addFriend.setEnabled(false);
                    addFriend.setText("Waiting for response :)");
                }
                else if(friendLevel >=0){
                    addFriend.setEnabled(false);
                    addFriend.setText("You have become friends!");
                }
                if(friendLevel >=0){
                    username.setText(dataSnapshot.child(contactId).child("nickname").getValue().toString());
                }
                else{
                    username.setText("Anonymous");
                }
                if(friendLevel >=10) {
                    gender.setText(dataSnapshot.child(contactId).child("gender").getValue().toString());
                }
                else {
                    gender.setText("Gender: unknown");
                }
                if(friendLevel < 0){
                    guide.setText("Add friends to see this contact's nickname!");
                    knownTagsText.setText("");
                   // unknownTagsText.setText("");
                    return;
                }
                else if(friendLevel < 10){
                    guide.setText("Talk more to see this friend's gender!");
                    knownTagsText.setText("");
                   // unknownTagsText.setText("");
                    return;
                }


                int numberOfKnownTags = Math.min(Math.max(0,(friendLevel - 10)/3),14);
                String knownTagsInfo = "";
                String unknownTagsInfo = "";
                for(int i = 0; i < numberOfKnownTags; i++){
                    String currentTags = tagsKeys.get(i);
                    currentTags = currentTags.substring(0,1).toUpperCase() + currentTags.substring(1);
                    knownTagsInfo = knownTagsInfo + currentTags + ": \t ";
                        if(dataSnapshot.child(contactId).child("tags").child(tagsKeys.get(i)).getValue().toString().equals("true") ){
                            knownTagsInfo = knownTagsInfo + "Yes\n";
                        }
                        else{
                            knownTagsInfo = knownTagsInfo + "No\n";
                        }
                }

                if(numberOfKnownTags >= 14){
                    guide.setText("You can see all information of this friend!");
                    unknownTagsInfo = "";
                }
                else {
                    guide.setText("Talk more to see more tags of this user.");
                    unknownTagsInfo = "";
                    for(int i = numberOfKnownTags; i < 14; i++){
                        String currentTags = tagsKeys.get(i);
                        currentTags = currentTags.substring(0,1).toUpperCase() + currentTags.substring(1);
                        knownTagsInfo = knownTagsInfo + currentTags + ": \t ?\n";
                    }
                }
                knownTagsInfo = knownTagsInfo.substring(0, knownTagsInfo.length() - 1);
                knownTagsText.setText(knownTagsInfo);
                //unknownTagsText.setText(unknownTagsInfo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void makeFriend(View view) {
        if(theirFriendLevel == -2){
            reference.child(uid).child("friendlists").child(contactId).setValue(-1);
        }
        else if(theirFriendLevel == -1){
            reference.child(uid).child("friendlists").child(contactId).setValue(0);
            reference.child(contactId).child("friendlists").child(uid).setValue(0);
            reference.child(uid).child("contactlists").child(contactId).child("isFriend").setValue(true);
            reference.child(contactId).child("contactlists").child(uid).child("isFriend").setValue(true);
            Toast.makeText(ProfileActivity.this, "You two are friends now!", Toast.LENGTH_LONG).show();
        }
    }

    public void removeContact(View view) {
        reference.child(uid).child("friendlists").child(contactId).setValue(-3);
        reference.child(contactId).child("friendlists").child(uid).setValue(-3);
        reference.child(uid).child("contactlists").child(contactId).child("isFriend").setValue(false);
        reference.child(contactId).child("contactlists").child(uid).child("isFriend").setValue(false);
        removeContact.setEnabled(false);
    }
}
