package com.gjjg.textbook_app_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
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
            Toast.makeText(this, "Preencha os campos.", Toast.LENGTH_LONG).show();
        } else if (!emailFilled){
            Toast.makeText(this, "Preencha o campo email.",Toast.LENGTH_LONG).show();
        } else if (!passwordFilled){
            Toast.makeText(this, "Preencha o campo senha.",Toast.LENGTH_LONG).show();
        } else if (!emailValid){
            Toast.makeText(this, "Insira um email válido.",Toast.LENGTH_LONG).show();
        } else {
            firebaseSignin(email, password);
        }
    }

    private void firebaseSignin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInActivity.this, "Logado com sucesso.", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(SignInActivity.this, ListActivity.class);
//                            startActivity(intent);
                        } else {
                            Toast.makeText(SignInActivity.this, "Problema de autenticação.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}