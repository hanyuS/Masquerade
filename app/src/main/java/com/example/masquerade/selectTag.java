package com.example.masquerade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class selectTag extends AppCompatActivity implements View.OnClickListener{

    int preSelectedIndex = -1;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;
    User user;
    private FirebaseAuth  mAuth = FirebaseAuth.getInstance();;
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String [] tag_arr = {"sports","movie","music","video","games","digital technology","fashion",
            "animation", "arts","make-up","travel","food","pets","academic"};
    Map<String, Boolean> index_map  = new HashMap<String, Boolean>() {{
        put(tag_arr[0],false);
        put(tag_arr[1],false);
        put(tag_arr[2],false);
        put(tag_arr[3],false);
        put(tag_arr[4],false);
        put(tag_arr[5],false);
        put(tag_arr[6],false);
        put(tag_arr[7],false);
        put(tag_arr[8],false);
        put(tag_arr[9],false);
        put(tag_arr[10],false);
        put(tag_arr[11],false);
        put(tag_arr[12],false);
        put(tag_arr[13],false);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView listView = (ListView) findViewById(R.id.listview);
        findViewById(R.id.confirm_btn).setOnClickListener(this);

        final List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(false,"sports"));
        tags.add(new Tag(false,"movie"));
        tags.add(new Tag(false, "music"));
        tags.add(new Tag(false,"video"));
        tags.add(new Tag(false,"games"));
        tags.add(new Tag(false,"digital technology"));
        tags.add(new Tag(false, "fashion"));
        tags.add(new Tag(false,"animation"));
        tags.add(new Tag(false,"arts"));
        tags.add(new Tag(false,"make-up"));
        tags.add(new Tag(false, "travel"));
        tags.add(new Tag(false,"foods"));
        tags.add(new Tag(false,"pets"));
        tags.add(new Tag(false,"academic"));
        final CustomAdapter adapter = new CustomAdapter(this, tags);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Tag model = tags.get(i);

                if (model.isSelected())
                    model.setSelected(false);

                else
                    model.setSelected(true);

                tags.set(i, model);

                //now update adapter
                adapter.updateRecords(tags);

                Log.w("tags", i + " "+ tag_arr[i]);
                if (index_map.get(tag_arr[i])) {
                    index_map.put(tag_arr[i],false);
                }
                else{
                    index_map.put(tag_arr[i],true);
                }
            }
        });

    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if(i == R.id.confirm_btn){
            user = User.getInstance();
            user.setUser_tag(index_map);
            update_tags();
            startActivity(new Intent(selectTag.this,HomeActivity.class));
            finish();
        }
    }

    public void update_tags(){
        mDatabase.child("Users").child(currentUser.getUid()).child("tags").setValue(index_map);
    }
}