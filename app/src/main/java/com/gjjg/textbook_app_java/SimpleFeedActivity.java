package com.gjjg.textbook_app_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class SimpleFeedActivity extends AppCompatActivity {

    private RecyclerView simpleFeedRecyclerView;
    private ArrayList<String> testData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_feed);

        // Tem que mudar para passar a arte
        testData.add("ugauga");
        testData.add("wlkamdwa");
        testData.add("binga balong");
        testData.add("roberto boetoeor");
        testData.add("penis pinto boquete");
        testData.add("pepepepep");

        simpleFeedRecyclerView = findViewById(R.id.simpleFeedRecyclerView);
        simpleFeedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SimpleFeedViewAdapter simpleFeedViewAdapter = new SimpleFeedViewAdapter(this, testData);
        simpleFeedRecyclerView.setAdapter(simpleFeedViewAdapter);
    }
}