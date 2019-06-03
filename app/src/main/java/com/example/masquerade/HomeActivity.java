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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waitPair();
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //ListView navigation = findViewById(R.id.nav_list);
        findViewById(R.id.signout).setOnClickListener(this);

        //        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String curUID = user.getUid();
//        database.child("Users").child(curUID).child("contactlists").addValueEventListener(
//                new ValueEventListener(){
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot contact: dataSnapshot.getChildren()) {
//                            //contacts field 在方法外所以得想办法传出去值
//                            //或者不需要每次contactlist更新的话可以尝试建一个dataSnapshot然后直接for each读取
//                            //另外需要在contact list里添加两个field
//                            //比如User1的contactlist里有user2 这个user2里需要两个field
//                            //一个是pairtag，就是他们共享的tag
//                            //另一个是value，是他们之间的那个数字
//                            //paired user我改过了line248-250；287；295；308；315-320
//                            ArrayList<contactItem> contacts = new ArrayList<>();
//                            contacts.add(new contactItem(R.drawable.logo_small, contact.getValue().toString(), contact.child("pairtag").getValue().toString()));
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//        mRecycleView = findViewById(R.id.contact_list);
//        mRecycleView.setHasFixedSize(true);
//        mLayoutManaer = new LinearLayoutManager(this);
//        mAdapter  = new contactAdapter(contacts);
//        mRecycleView.setLayoutManager(mLayoutManaer);
//        mRecycleView.setAdapter(mAdapter);


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
            public void onClick(View v) {

                FloatingActionButton fab = findViewById(R.id.match);
                if (!Mask) {
                    fab.setImageResource(R.drawable.rotate);
                    pair();
                }
                else {
                    fab.setImageResource(R.drawable.ic_add_black_24dp);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    Intent intent = new Intent(HomeActivity.this , MessageActivity.class);
                    intent.putExtra("userid",pairedUser);
                    pairedUser = "";
                    startActivity(intent);
                    Mask = false;
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
            signout();
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

                    private void PairUsers(String Userone, String Usertwo, String sametag){
                        Log.d("debug",Userone);
                        Log.d("debug",Usertwo);
                        //to be done
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.child("Users").child(Userone).child("match").setValue(Usertwo);
                        database.child("Users").child(Usertwo).child("match").setValue(Userone);
                        database.child("Users").child(Userone).child("contactlists").child(Usertwo).setValue(sametag);
                        database.child("Users").child(Usertwo).child("contactlists").child(Userone).setValue(sametag);
                        database.child("Users").child(Userone).child("friendlists").child(Usertwo).setValue(0);
                        database.child("Users").child(Usertwo).child("friendlists").child(Userone).setValue(0);
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
