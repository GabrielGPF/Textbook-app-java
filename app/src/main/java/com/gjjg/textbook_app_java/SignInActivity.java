package com.gjjg.textbook_app_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setTitle("Textbook - Sign in");

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Intent myIntent = new Intent(getApplicationContext(), MainFeedActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);
        }
    }

    public void signinButtonClick(View view) {
        EditText emailEditText = (EditText)findViewById(R.id.editTextTextEmailAddress0);
        EditText passwordEditText = (EditText)findViewById(R.id.editTextTextPassword0);

        String emailValue = emailEditText.getText().toString();
        String passwordValue = passwordEditText.getText().toString();

        login(emailValue, passwordValue);
    }

    public void signupButtonClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void login(String email, String password){
        boolean emailFilled = false;
        boolean emailValid = false;
        boolean passwordFilled = false;

        if (email.length() > 0){
            emailFilled = true;
        }

        if (Util.isEmailValid(email)){
            emailValid = true;
        }

        if (password.length() > 0){
            passwordFilled = true;
        }

        if (!emailFilled && !passwordFilled){
            Toast.makeText(this, "Fill all fields.", Toast.LENGTH_LONG).show();
        } else if (!emailFilled){
            Toast.makeText(this, "Fill email field.",Toast.LENGTH_LONG).show();
        } else if (!passwordFilled){
            Toast.makeText(this, "Fill password field.",Toast.LENGTH_LONG).show();
        } else if (!emailValid){
            Toast.makeText(this, "Insert a valid email.",Toast.LENGTH_LONG).show();
        } else {
            firebaseSignin(email, password, this);
        }
    }

    private void firebaseSignin(String email, String password, Context context){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Sign in succeeded.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainFeedActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignInActivity.this, "Authentication error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}