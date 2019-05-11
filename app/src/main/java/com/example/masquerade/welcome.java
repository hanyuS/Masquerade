package com.example.masquerade;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.*;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.*;
import android.view.WindowManager;
import android.widget.ImageView;
import android.os.Handler;


import java.security.ProtectionDomain;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.PendingIntent.getActivity;

public class welcome extends AppCompatActivity {
    ImageView logo;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplication().setTheme(R.style.AppTheme);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_welcome);

        logo = findViewById(R.id.logo);

       handler.postDelayed(new Runnable(){
           @Override
           public void run() {
               Intent loginIntent = new Intent(welcome.this,login.class);
               startActivity(loginIntent);
               overridePendingTransition(0, 0);
               finish();
               //overridePendingTransition(0, 0);
           }
       }, 3000);


    }

    @Override
    protected void onPause() {
        overridePendingTransition(0,0);
        super.onPause();
    }
}
