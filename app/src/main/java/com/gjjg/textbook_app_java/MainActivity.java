package com.gjjg.textbook_app_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signinButtonClick(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void signupButtonClick(View view) {
    }

    public void gerarAsciiArtButtonClick(View view) {
        Intent intent = new Intent(this, AsciiConverter.class);
        startActivity(intent);
    }
}