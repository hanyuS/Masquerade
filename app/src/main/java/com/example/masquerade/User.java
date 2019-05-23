package com.example.masquerade;
import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String user_id;
    public int user_age;
    public int [] user_tag;
    //todo: contact list & conversation

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User( String email) {
        //this.username = username;
        this.email = email;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public void setUser_tag(int [] tags){
        this.user_tag = tags;
    }
}
