package com.example.masquerade;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingActivity extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;
    User user;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    FirebaseUser currentUser = mAuth.getCurrentUser();
    RadioGroup group;
    TextInputLayout txt;
    TextInputEditText editText;
    RadioButton button;
    //todo: check logic
    boolean is_set_button = false;
    String gender;
    String nick_name;

    ImageView mask1;
    ImageView mask2;
    ImageView mask3;
    ImageView mask4;
    ImageView mask5;
    ImageView mask6;
    ImageView mask7;

    RadioButton male;
    RadioButton female;
    RadioButton other;

    String profileInd = "default_pic";

    boolean pick_image = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        group = findViewById(R.id.gender);
        txt = findViewById(R.id.input);
        editText = findViewById(R.id.editText);
        mask1 = findViewById(R.id.imageView6);
        mask2 = findViewById(R.id.imageView14);
        mask3 = findViewById(R.id.imageView15);
        mask4 = findViewById(R.id.imageView16);
        //mask4 = findViewById(R.id.imageView17);
        mask5 = findViewById(R.id.imageView17);
        mask6 = findViewById(R.id.imageView18);
        mask7 = findViewById(R.id.imageView19);


        mask1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 1");
                user = User.getInstance();
                user.setProfileInd("mask1");
                profileInd = "mask1";
                pick_image = true;


            }
        });

        mask2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 2");
                user = User.getInstance();
                user.setProfileInd("mask2");
                profileInd = "mask2";
                pick_image = true;
            }
        });

        mask3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 3");
                user = User.getInstance();
                user.setProfileInd("mask3");
                profileInd = "mask3";
                pick_image = true;
            }
        });

        mask4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 4");
                user = User.getInstance();
                user.setProfileInd("mask4");
                profileInd = "mask4";
                pick_image = true;
            }
        });

        mask5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 5");
                user = User.getInstance();
                user.setProfileInd("mask5");
                profileInd = "mask5";
                pick_image = true;
            }
        });

        mask6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 6");
                user = User.getInstance();
                user.setProfileInd("mask6");
                profileInd = "mask6";
                pick_image = true;
            }
        });

        mask7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 7");
                user = User.getInstance();
                user.setProfileInd("mask7");
                profileInd = "mask7";
                pick_image = true;
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        male = findViewById(R.id.second);
        female = findViewById(R.id.first);
        other = findViewById(R.id.third);


        user = User.getInstance();
        if(user.getNickname() != null)
            editText.setText(user.nickname);

//        Log.w("log info",user.nickname);

        if(user.getGender() != null) {
            if (user.getGender().equals("Male")) {
                group.check(R.id.second);
                male.setChecked(true);
                Log.w("male", "check");
                is_set_button = true;
            } else if (user.getGender().equals("Female")) {
                Log.w("male", "female");
                group.check(R.id.first);
                female.setChecked(true);
                is_set_button = true;
            } else if (user.getGender().equals("Other")) {
                group.check(R.id.third);
                other.setChecked(true);
                Log.w("male", "other");
                is_set_button = true;
            }
        }

        if(user.getProfileInd() != null){
            pick_image = true;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }





    public void checkButton(View v){
        int radioId = group.getCheckedRadioButtonId();
        button = findViewById(radioId);
        gender = button.getText().toString();
        Log.w("checkbutton says",gender);
        is_set_button = true;
        user = User.getInstance();
        user.setGender(gender);
    }

    public boolean validate_nickname(){
        nick_name = txt.getEditText().getText().toString().trim();
        if(nick_name.isEmpty()){
            txt.setError("Fields cannot be empty");
            return false;
        }
        else {
            user = User.getInstance();
            user.setNickname(nick_name);
            txt.setError(null);
            return true;
        }
    }


    public void confirmInput(View v){
        if(!is_set_button){
            Toast.makeText(this,"please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!pick_image){
            Toast.makeText(this,"please select a profile image", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!validate_nickname()){ return ;}
        mDatabase.child("Users").child(currentUser.getUid()).child("nickname").setValue(nick_name);
        mDatabase.child("Users").child(currentUser.getUid()).child("gender").setValue(gender);
        mDatabase.child("Users").child(currentUser.getUid()).child("profileInd").setValue(profileInd);

        startActivity(new Intent(SettingActivity.this, selectTag.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
