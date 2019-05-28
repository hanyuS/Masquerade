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

                        //iterate through each user, ignoring their UID
                        for (Map.Entry<String, Object> entry : users.entrySet()){

                            //Get user map
                            Map singleUser = (Map) entry.getValue();
                            userId.add((String) singleUser.get("Id"));
                            //Get phone field and append to list
                            userTags.add((boolean[]) singleUser.get("tags"));
                        }

                        findPairs(userTags, userId);

                        System.out.println(userTags.toString());
                    }

                    private void findPairs(ArrayList<boolean[]> uTags, ArrayList<String> uId){
                        if(uTags.size()!= uId.size()){
                            System.err.println("Error occured");
                        }
                        //generate a random number
                        int size = uTags.size();
                        int random = (int) (Math.random() * size);
                        String firstUser = "";
                        String secondUser = "";

                        //iterate the array from the random number
                        boolean[][] recorder = new boolean [size][numOfTags];
                        Arrays.fill(recorder, false);
                        //iterate through the half array
                        for(int i=random; i<size; i++){
                            //get one user's tags
                            boolean[] thisTag = uTags.get(i);

                            //iterate through tags fields
                            OUT:
                            for(int j=0; j<thisTag.length; j++){
                                //find one tag
                                if(thisTag[j] == true){
                                    recorder[i][j] = true;
                                    for(int k=0; k<size; k++){
                                        if(recorder[k][j] == true){
                                            firstUser += uId.get(k);
                                            secondUser += uId.get(j);
                                            break OUT;
                                        }
                                    }
                                }
                            }
                            PairUsers(firstUser, secondUser);
                        }
                    }

                    private void PairUsers(String Userone, String Usertwo){
                        System.out.println("Userone");
                        System.out.println("Usertwo");
                        //to be done
                    }
                });

    }

}
