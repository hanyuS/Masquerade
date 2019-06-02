package com.example.masquerade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        group = findViewById(R.id.gender);
        txt = findViewById(R.id.input);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
