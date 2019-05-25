package com.example.masquerade;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class selectTag extends AppCompatActivity {

    int preSelectedIndex = -1;

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


        final List<Tag> tags = new ArrayList<>();
        tags.add(new Tag(false,"dummy"));
        tags.add(new Tag(false,"Basketball"));
        tags.add(new Tag(false, "Dance"));
        tags.add(new Tag(false,"Marvel"));
        tags.add(new Tag(false,"DC"));
        tags.add(new Tag(false,"Basketball"));
        tags.add(new Tag(false, "Dance"));
        tags.add(new Tag(false,"Marvel"));
        tags.add(new Tag(false,"DC"));
        tags.add(new Tag(false,"Basketball"));
        tags.add(new Tag(false, "Dance"));
        tags.add(new Tag(false,"Marvel"));
        tags.add(new Tag(false,"DC"));
        tags.add(new Tag(false,"Basketball"));
        tags.add(new Tag(false, "Dance"));
        tags.add(new Tag(false,"Marvel"));
        tags.add(new Tag(false,"DC"));
        tags.add(new Tag(false,"Basketball"));
        tags.add(new Tag(false, "Dance"));
        tags.add(new Tag(false,"Marvel"));
        tags.add(new Tag(false,"DC"));
        tags.add(new Tag(false,"Basketball"));
        tags.add(new Tag(false, "Dance"));
        tags.add(new Tag(false,"Marvel"));
        tags.add(new Tag(false,"DC"));
        tags.add(new Tag(false,"Basketball"));
        tags.add(new Tag(false, "Dance"));
        tags.add(new Tag(false,"Marvel"));
        tags.add(new Tag(false,"DC"));
        tags.add(new Tag(false,"Basketball"));
        tags.add(new Tag(false, "Dance"));
        tags.add(new Tag(false,"Marvel"));
        tags.add(new Tag(false,"DC"));
        tags.add(new Tag(false,"Basketball"));
        tags.add(new Tag(false, "Dance"));
        tags.add(new Tag(false,"Marvel"));
        tags.add(new Tag(false,"DC"));
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
            }
        });

    }
}