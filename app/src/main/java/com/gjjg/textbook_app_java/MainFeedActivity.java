package com.gjjg.textbook_app_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainFeedActivity extends AppCompatActivity {

    private RecyclerView mainFeedRecyclerView;
    private ArrayList<String> testData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        // Tem que mudar para passar a arte, a foto e o nome de quem postou
        testData.add("ugauga");
        testData.add("wlkamdwa");
        testData.add("binga balong");
        testData.add("roberto boetoeor");
        testData.add("penis pinto boquete");
        testData.add("pepepepep");

        mainFeedRecyclerView = findViewById(R.id.mainFeedRecyclerView);
        mainFeedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MainFeedViewAdapter mainFeedViewAdapter = new MainFeedViewAdapter(this, testData);
        mainFeedRecyclerView.setAdapter(mainFeedViewAdapter);
    }
}