package com.gjjg.textbook_app_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainFeedActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView mainFeedRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        setTitle("Textbook");

        mainFeedRecyclerView = findViewById(R.id.mainFeedRecyclerView);
        mainFeedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseUtil.getPosts(this, mainFeedRecyclerView);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume(){
        super.onResume();
        FirebaseUtil.getPosts(this, mainFeedRecyclerView);
    }

    public void logoutButtonClick(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void addButtonClick(View view) {
        Intent intent = new Intent(this, AsciiConverterActivity.class);
        startActivity(intent);
    }

    public void profileButtonClick(View view) {
        FirebaseUtil.getInstance().setCurrentUser(FirebaseAuth.getInstance().getUid());
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}