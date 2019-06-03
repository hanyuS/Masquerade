package com.example.masquerade;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String nickname;
    public String email;
    public int user_age;
    public Map<String,Boolean> user_tag;
    private static User instance;
    public String gender;
    public String Uid;
    private String imageURL;
    public String match = "false";
    //todo: contact list & conversation

    private User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public static User getInstance(){
        if (instance == null){
            instance = new User();
            return instance;
        }
        else{   return instance; }
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public void setUser_tag(Map<String, Boolean> tags){
        this.user_tag = tags;
    }
    public void setGender(String gender){ this.gender = gender;}


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUser_id(String uid) {
        this.Uid = uid;
    }
    public String getUser_id() {
        return Uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setMatch(String match){this.match = match;}
}
