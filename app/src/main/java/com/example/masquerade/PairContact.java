package com.example.masquerade;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;



public class PairContact extends AppCompatActivity {
    public static int numOfTags = 12;
    public static class User {
        public int userName;
        public boolean tags[];

        public User(int user, boolean[] tag) {
            userName = user;
            tags = tag;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("tags", "in pairing process");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            System.err.println("Error occured, NO USER");
        }
        final String uid = user.getUid();
//        FirebaseDatabase.getInstance().getReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                boolean[] thisusertags = dataSnapshot.child("tags").getValue(boolean[].class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                throw databaseError.toException();
//            }
//        });

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

                        ArrayList<boolean []> userTags = new ArrayList<boolean[]>();
                        ArrayList<String> userId = new ArrayList<String>();
                        ArrayList<Boolean> match = new ArrayList<Boolean>();

                        //iterate through each user, ignoring their UID
                        for (Map.Entry<String, Object> entry : users.entrySet()){

                            //Get user map
                            Map singleUser = (Map) entry.getValue();
                            match.add((Boolean) singleUser.get("match"));
                            userId.add((String) singleUser.get("Uid"));
                            //Get phone field and append to list
                            Log.d("pair", singleUser.get("Uid").toString());
                            //Log.d("pair",singleUser.get("tags").toString());
                            Map tagMap = (Map) singleUser.get("tags");
                            boolean [] tagArr = new boolean[14];
                            String [] tag_arr = {"sports","movie","music","video","games","digital technology","fashion",
                                    "animation", "arts","make-up","travel","food","pets","academic"};
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

                        findPairs(userTags, userId, match);

                        System.out.println(userTags.toString());
                    }

                    private void findPairs(ArrayList<boolean[]> uTags, ArrayList<String> uId, ArrayList<Boolean>ismatching){
                        if(uTags.size()!= uId.size()){
                            System.err.println("Error occured");
                        }

                        //generate a random number
                        int size = uTags.size();
                        int random = (int) (Math.random() * size);
                        String pairedUser = "";

                        //find this uid's location
                        int location = 0;
                        for(int i=0; i<size; i++){
                            if(uId.get(i) == uid){
                                location = i;
                                break;
                            }
                        }
                        boolean[] thisusertags = uTags.get(location);

                        //iterate the array from the random number
                        OUT1:
                        for(int i=random; i<size; i++){
                            if(i == location){
                                continue;
                            }
                            if(ismatching.get(i) == false){
                                continue;
                            }
                            //TODO: When we have contacts list, check whether that laobi is our contact.
                            //get one user's tags
                            boolean[] thisTag = uTags.get(i);
                            //iterate through tags fields
                            for(int j=0; j<thisTag.length; j++){
                                //find one tag
                                if(thisTag[j] == true){
                                    if(thisusertags[j] == true){
                                        pairedUser = uId.get(i);
                                        break OUT1;
                                    }
                                }
                            }
                        }

                        if(!pairedUser.equals("")){
                            PairUsers(uid, pairedUser);
                            return;
                        }

                        OUT2:
                        for(int i=0; i<random; i++){
                            if(i == location){
                                continue;
                            }
                            if(ismatching.get(i) == false){
                                continue;
                            }
                            //get one user's tags
                            boolean[] thisTag = uTags.get(i);
                            //iterate through tags fields
                            for(int j=0; j<thisTag.length; j++) {
                                //find one tag
                                if (thisTag[j] == true) {
                                    if (thisusertags[j] == true) {
                                        pairedUser = uId.get(i);
                                        break OUT2;
                                    }
                                }
                            }
                        }
                        if(!pairedUser.equals("")){
                            PairUsers(uid, pairedUser);
                            return;
                        }
                        //no such user is found
                        //update this user's match field
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.child("users").child(uid).child("match").setValue(true);
                    }

                    private void PairUsers(String Userone, String Usertwo){
                        Log.d("debug",Userone);
                        Log.d("debug",Usertwo);
                        //to be done
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        database.child("users").child(Usertwo).child("match").setValue(false);
                   //     addToContact(Userone, Usertwo);
                    }
                });
    }

}
