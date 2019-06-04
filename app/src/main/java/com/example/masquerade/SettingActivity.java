package com.example.masquerade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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


    int profileInd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        group = findViewById(R.id.gender);
        txt = findViewById(R.id.input);

        mask1 = findViewById(R.id.imageView6);
        mask2 = findViewById(R.id.imageView14);
        mask3 = findViewById(R.id.imageView15);
        mask4 = findViewById(R.id.imageView16);
        mask4 = findViewById(R.id.imageView17);
        mask5 = findViewById(R.id.imageView18);
        mask6 = findViewById(R.id.imageView19);
        mask7 = findViewById(R.id.imageView20);

        mask1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 1");
                user = User.getInstance();
                user.setProfileInd(1);
            }
        });

        mask2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 2");
                user = User.getInstance();
                user.setProfileInd(2);
            }
        });

        mask3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 3");
                user = User.getInstance();
                user.setProfileInd(3);
            }
        });

        mask4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 4");
                user = User.getInstance();
                user.setProfileInd(4);
            }
        });

        mask5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 5");
                user = User.getInstance();
                user.setProfileInd(5);
            }
        });

        mask6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 6");
                user = User.getInstance();
                user.setProfileInd(6);
            }
        });

        mask7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.w("good" ,"already choose image 7");
                user = User.getInstance();
                user.setProfileInd(7);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        if(!validate_nickname()){ return ;}
        mDatabase.child("Users").child(currentUser.getUid()).child("nickname").setValue(nick_name);
        mDatabase.child("Users").child(currentUser.getUid()).child("gender").setValue(gender);
        startActivity(new Intent(SettingActivity.this, selectTag.class));
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
