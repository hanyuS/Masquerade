package com.example.masquerade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login extends AppCompatActivity {
    TextView login_email, login_password;
    Button login, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_loginButton);
        signUp = findViewById(R.id.login_signUpButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,signup.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0,0);
        super.onPause();
    }

}
