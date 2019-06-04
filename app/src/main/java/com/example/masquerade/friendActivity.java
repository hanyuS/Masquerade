package com.example.masquerade;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class friendActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<contactItem> list;
    contactAdapter adapter;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

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

        recyclerView = (RecyclerView)findViewById(R.id.contact_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<contactItem>();

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        String curUID = curUser.getUid();
        Log.d("get value from firebase",curUID);
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(curUID).child("contactlists");
        reference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("firebase snapshot",dataSnapshot.getKey());
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Log.d("some","some");
                    Log.d("the friend name is", dataSnapshot1.getKey());
                    Log.d("the pair tag is", dataSnapshot1.child("tags").getValue(String.class));
                    contactItem item = new contactItem(R.drawable.logo_small, "tags",dataSnapshot1.child("tags").getValue(String.class),dataSnapshot1.getKey(),true);
                    list.add(item);
                }
                adapter = new contactAdapter(friendActivity.this,list);
                recyclerView.setAdapter(adapter);
                Log.d("finish read firebase","finish");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(friendActivity.this,"Somethins is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
