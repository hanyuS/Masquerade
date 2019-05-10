package com.example.masquerade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class signup extends AppCompatActivity {
    TextView signUp_email, signUp_password, signUp_confirmPassword;
    Button signUp_enter;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUp_email = findViewById(R.id.signUp_email);
        signUp_password = findViewById(R.id.signUp_password);
        signUp_confirmPassword =  findViewById(R.id.signUp_rePassword);
        signUp_enter = findViewById(R.id.signUp_enter);
        back = findViewById(R.id.backTo_login);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this,login.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    protected void onPause() {
        overridePendingTransition(0,0);
        super.onPause();
    }

}
