package com.example.masquerade;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity {
    int code = 2;
    String pairedUser="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //ListView navigation = findViewById(R.id.nav_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        FloatingActionButton fab = findViewById(R.id.match);
        fab.setOnClickListener(new View.OnClickListener() {
            int times = 0;
            public void onClick(View v) {

                FloatingActionButton fab = findViewById(R.id.match);
                if (times == 0) {
                    fab.setImageResource(R.drawable.rotate);
                    times = 1;
                    Intent intent = new Intent(HomeActivity.this, PairContact.class);
                    startActivityForResult(intent, code);
                }
                else if (times == 1){
                    fab.setImageResource(R.drawable.logo_small);
                    times = 2;
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                }
                else {
                    fab.setImageResource(R.drawable.ic_add_black_24dp);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    times = 0;
                }

            }
        });

        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && data != null) {
            pairedUser = data.getStringExtra("MESSAGE");
        }
        Log.d("see results in Home", pairedUser);
        if(pairedUser.equals("")){
            searchResult(false);
        }
        else{
            searchResult(true);
        }

    }
    public void searchResult(Boolean result){
        if(result){
            FloatingActionButton fab = findViewById(R.id.match);
            fab.setImageResource(R.drawable.logo_small);
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
        }
        else{
            Intent intent = new Intent(HomeActivity.this, PairContact.class);
            startActivityForResult(intent, code);
        }
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d("tags", "jumping into pairContact");
//        startActivity(new Intent(HomeActivity.this, PairContact.class));
//    }
}
