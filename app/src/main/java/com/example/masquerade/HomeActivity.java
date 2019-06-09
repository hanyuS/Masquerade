package com.example.masquerade;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
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


public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    int code = 2;
    Boolean Mask = false;
    String pairedUser="";
    Boolean pairing = false;
    DrawerLayout drawer;
    RecyclerView recyclerView;
    ArrayList<contactItem> list;
    contactAdapter adapter;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waitPair();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        //ListView navigation = findViewById(R.id.nav_list);
        findViewById(R.id.signout).setOnClickListener(this);
        findViewById(R.id.setting_btn).setOnClickListener(this);
        findViewById(R.id.btn_friendlist).setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.contact_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        String curUID = curUser.getUid();
        Log.d("get value from firebase",curUID);
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(curUID).child("contactlists");
        reference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("firebase snapshot",dataSnapshot.getKey());
                list = new ArrayList<contactItem>();
                //list.add(new contactItem("logo_small","Press '+' TO Make Friends","","",false));
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Log.d("some","some");
                    Log.d("the friend name is", dataSnapshot1.getKey());
                    Log.d("the pair tag is", dataSnapshot1.child("tags").getValue(String.class));
                    if(dataSnapshot1.child("avatar").exists() && dataSnapshot1.child("tags").exists() && dataSnapshot1.child("tags").getValue().toString().charAt(0) != ' ') {

                        contactItem item = new contactItem(dataSnapshot1.child("avatar").getValue(String.class), "Common Tags:", dataSnapshot1.child("tags").getValue(String.class), dataSnapshot1.getKey(), false);
                        list.add(item);
                    }
                }
                adapter = new contactAdapter(HomeActivity.this,list);
                recyclerView.setAdapter(adapter);
                Log.d("finish read firebase","finish");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this,"Somethins is wrong", Toast.LENGTH_SHORT).show();
            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open, R.string.drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.
                        onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        FloatingActionButton fab = findViewById(R.id.match);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FloatingActionButton fab = findViewById(R.id.match);
                if (!Mask) {
                    fab.setImageResource(R.drawable.rotate);
                    pair();
                    pairing = true;
                }
                else {
                    fab.setImageResource(R.drawable.ic_add_black_24dp);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    Intent intent = new Intent(HomeActivity.this , MessageActivity.class);
                    intent.putExtra("userid",pairedUser);
                    pairedUser = "";
                    startActivity(intent);
                    Mask = false;
                    pairing = false;
                }
            }
        });

        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }


    @Override
    public void onClick(View v){
        if(v.getId()==R.id.signout){
            Log.d("whatis wroing", "ABCD");
            signout();
        }
        if(v.getId()==R.id.setting_btn){
            if(pairing)
            {
                drawer.closeDrawers();
                Toast.makeText(HomeActivity.this, "You cannot change your settings while searching for contacts", Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(HomeActivity.this,SettingActivity.class));
        }
        if(v.getId()==R.id.btn_friendlist){
            Log.d("click","friednlist");

            startActivity(new Intent(HomeActivity.this,friendActivity.class));
            drawer.closeDrawers();

        }
    }
    public void signout(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1030427491069-3hvkhfksob78t4pjpua9pqhjekk1clef.apps.googleusercontent.com")
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("finish signout","yes");
                        startActivity(new Intent(HomeActivity.this,WelcomeActivity.class));
                        finish();
                    }
                });
    }

    public void pair(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            System.err.println("Error occured, NO USER");
        }
        final String uid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot

                        collectUserTags((Map<String, Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    private void collectUserTags(Map<String,Object> users) {
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.child("Users").child(uid).child("match").setValue("true");
                        ArrayList<boolean []> userTags = new ArrayList<boolean[]>();
                        ArrayList<String> userId = new ArrayList<String>();

                        String [] tag_arr = {"sports","movie","music","video","games","digital technology","fashion",
                                "animation", "arts","make-up","travel","food","pets","academic"};
                        //iterate through each user, ignoring their UID

                        for (Map.Entry<String, Object> entry : users.entrySet()){

                            //Get user map
                            Map singleUser = (Map) entry.getValue();
                            if((!singleUser.get("match").equals("true")) && !uid.equals(singleUser.get("Uid").toString())){
                                Log.d("uid is", uid);
                                Log.d("skip", singleUser.get("Uid").toString());
                                Log.d("skip", singleUser.get("match").toString());
                                continue;
                            }
                            if(singleUser.containsKey("contactlists") && ((Map)singleUser.get("contactlists")).containsKey(uid)){
                                continue;
                            }
                            userId.add((String) singleUser.get("Uid"));
                            //Get phone field and append to list
                            Log.d("pair", singleUser.get("Uid").toString());
                            //Log.d("pair",singleUser.get("tags").toString());
                            Map tagMap = (Map) singleUser.get("tags");
                            boolean [] tagArr = new boolean[14];

                            tagArr[0] = (boolean)tagMap.get(tag_arr[0]);
                            tagArr[1] = (boolean)tagMap.get(tag_arr[1]);
                            tagArr[2] = (boolean)tagMap.get(tag_arr[2]);
                            tagArr[3] = (boolean)tagMap.get(tag_arr[3]);
                            tagArr[4] = (boolean)tagMap.get(tag_arr[4]);
                            tagArr[5] = (boolean)tagMap.get(tag_arr[5]);
                            tagArr[6] = (boolean)tagMap.get(tag_arr[6]);
                            tagArr[7] = (boolean)tagMap.get(tag_arr[7]);
                            tagArr[8] = (boolean)tagMap.get(tag_arr[8]);
                            tagArr[9] = (boolean)tagMap.get(tag_arr[9]);
                            tagArr[10] = (boolean)tagMap.get(tag_arr[10]);
                            tagArr[11] = (boolean)tagMap.get(tag_arr[11]);
                            tagArr[12] = (boolean)tagMap.get(tag_arr[12]);
                            tagArr[13] = (boolean)tagMap.get(tag_arr[13]);

                            userTags.add(tagArr);
                        }
                        Log.d("start pairing", "try pair");
                        findPairs(userTags, userId);

                        //System.out.println(userTags.toString());
                    }

                    private void findPairs(ArrayList<boolean[]> uTags, ArrayList<String> uId){
                        if(uTags.size()!= uId.size()){

                            System.err.println("Error occured");
                        }
                        Log.d("findPairs", "enter find pairs");

                        //generate a random number
                        int size = uTags.size();
                        int randInt = (int) (Math.random() * size);
                        Log.d("random int is ", ""+randInt);
                        //String pairedUser = "";

                        //find this uid's location
                        String sametag = "";
                        String [] tagArr = {"sports","movie","music","video","games","digital technology","fashion",
                                "animation", "arts","make-up","travel","food","pets","academic"};
                        int location = 0;
                        for(int i=0; i<size; i++){
                            Log.d("uid is", uid);
                            Log.d("current get uid is", uId.get(i));
                            if(uid.equals(uId.get(i).toString())){
                                location = i;
                                Log.d("find my own location:", ""+location);
                                break;
                            }
                        }
                        boolean[] thisusertags = uTags.get(location);
                        Log.d("my uid is", uid);
                        Log.d("users to macth", "" + size);

                        //iterate the array from the random number
                        OUT1:

                        for(int i = 0; i < size; i++){


                            //get one user's tags
                            int ii = (i + randInt) % size;
                            if(ii == location){

                                continue;
                            }
                            Log.d("match",uId.get(ii));
                            boolean[] thisTag = uTags.get(ii);
                            //iterate through tags fields
                            for(int j=0; j<thisTag.length; j++){
                                //find one tag
                                if(thisTag[j] && thisusertags[j]){
                                    pairedUser = uId.get(ii);
                                    sametag =  tagArr[j];
                                    Log.d("find user", pairedUser);
                                    break OUT1;
                                }
                            }
                        }

                        if(!pairedUser.equals("")){
                            Log.d("start pair","see results");
                            PairUsers(uid, pairedUser, sametag);
//                            id[0]=pairedUser;
                            Log.d("see paired user", pairedUser);
                            Log.d("finish pair","finish");
                            return ;
                        }

                        //no such user is found
                        //update this user's match field
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.child("Users").child(uid).child("match").setValue("true");
                    }

                    private void PairUsers(final String Userone, final String Usertwo, String sametag){
                        Log.d("debug",Userone);
                        Log.d("debug",Usertwo);
                        //to be done
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                        database.child("Users").child(Userone).child("match").setValue(Usertwo);
                        database.child("Users").child(Usertwo).child("match").setValue(Userone);
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                String user1Name = dataSnapshot2.child("Users").child(Userone).child("nickname").getValue(String.class);
                                String user2Name = dataSnapshot2.child("Users").child(Usertwo).child("nickname").getValue(String.class);
                                String user1Avatar = dataSnapshot2.child("Users").child(Userone).child("profileInd").getValue(String.class);
                                String user2Avatar = dataSnapshot2.child("Users").child(Usertwo).child("profileInd").getValue(String.class);
                                database.child("Users").child(Usertwo).child("contactlists").child(Userone).child("nickname").setValue(user1Name);
                                database.child("Users").child(Userone).child("contactlists").child(Usertwo).child("nickname").setValue(user2Name);
                                database.child("Users").child(Usertwo).child("contactlists").child(Userone).child("avatar").setValue(user1Avatar);
                                database.child("Users").child(Userone).child("contactlists").child(Usertwo).child("avatar").setValue(user2Avatar);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        database.child("Users").child(Userone).child("contactlists").child(Usertwo).child("tags").setValue(" " + sametag);
                        database.child("Users").child(Usertwo).child("contactlists").child(Userone).child("tags").setValue(" " + sametag);
                        database.child("Users").child(Userone).child("contactlists").child(Usertwo).child("isFriend").setValue(false);
                        database.child("Users").child(Usertwo).child("contactlists").child(Userone).child("isFriend").setValue(false);
                        database.child("Users").child(Userone).child("friendlists").child(Usertwo).setValue(-2);
                        database.child("Users").child(Usertwo).child("friendlists").child(Userone).setValue(-2);
                        //     addToContact(Userone, Usertwo);
                    }
                });
        return;
    }

    public void waitPair(){
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        ref.addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                pairedUser = dataSnapshot.child("contactlists").getValue(String.class);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Boolean match = (Boolean) dataSnapshot.child("match").getValue();

                Log.d("see what changes", dataSnapshot.getKey());
                Log.d("see what changes", dataSnapshot.getValue().toString());

                if(dataSnapshot.getKey().equals("match") && !dataSnapshot.getValue().equals("true")){
                    FloatingActionButton fab = findViewById(R.id.match);
                    Mask = true;
                    fab.setImageResource(R.drawable.logo_small);
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                    pairedUser = dataSnapshot.getValue(String.class);
                    Log.d("the paired user is", pairedUser);
                    return;
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
