package com.gjjg.textbook_app_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView profileRecyclerView;
    private ArrayList<String> testData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Tem que mudar para passar a arte
        testData.add("ugauga");
        testData.add("wlkamdwa");
        testData.add("binga balong");
        testData.add("roberto boetoeor");
        testData.add("penis pinto boquete");
        testData.add("pepepepep");

        profileRecyclerView = findViewById(R.id.profileRecyclerView);
        profileRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ProfileAdapter profileAdapter = new ProfileAdapter(this, testData);
        profileRecyclerView.setAdapter(profileAdapter);
    }
}