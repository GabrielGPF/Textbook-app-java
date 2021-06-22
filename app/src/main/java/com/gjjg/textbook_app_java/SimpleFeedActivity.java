package com.gjjg.textbook_app_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class SimpleFeedActivity extends AppCompatActivity {

    private RecyclerView simpleFeedRecyclerView;
    private ArrayList<String> testData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_feed);

        setTitle(FirebaseUtil.getInstance().getCurrentUserUsername() + " - feed");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        simpleFeedRecyclerView = findViewById(R.id.simpleFeedRecyclerView);
        simpleFeedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SimpleFeedViewAdapter simpleFeedViewAdapter = new SimpleFeedViewAdapter(this, FirebaseUtil.getInstance().getCurrentUserImages());
        simpleFeedRecyclerView.setAdapter(simpleFeedViewAdapter);
        simpleFeedRecyclerView.smoothScrollToPosition(intent.getIntExtra("index", 0));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
//        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivityForResult(myIntent, 0);
        return true;
    }
}