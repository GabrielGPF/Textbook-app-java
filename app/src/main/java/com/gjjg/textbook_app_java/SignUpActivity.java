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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Textbook - Sign up");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void signupButtonClick(View view) {
        EditText usernameEditText = (EditText)findViewById(R.id.editTextUsername);
        EditText emailEditText = (EditText)findViewById(R.id.editTextEmail);
        EditText passwordEditText = (EditText)findViewById(R.id.editTextPassword);
        EditText confirmPasswordEditText = (EditText)findViewById(R.id.editTextConfirmPassword);

        String usernameValue = usernameEditText.getText().toString();
        String emailValue = emailEditText.getText().toString();
        String passwordValue = passwordEditText.getText().toString();
        String confirmPasswordValue = confirmPasswordEditText.getText().toString();

        signup(usernameValue, emailValue, passwordValue, confirmPasswordValue);
    }

    public void signup(String username, String email, String password, String confirmPassword){
        boolean usernameFilled = false;
        boolean emailFilled = false;
        boolean emailValid = false;
        boolean passwordFilled = false;
        boolean confirmPasswordFilled = false;
        boolean confirmPasswordValid = false;

        if (username.length() > 0){
            usernameFilled = true;
        }

        if (email.length() > 0){
            emailFilled = true;
        }

        if (Util.isEmailValid(email)){
            emailValid = true;
        }

        if (password.length() > 0){
            passwordFilled = true;
        }

        if (confirmPassword.length() > 0){
            confirmPasswordFilled = true;
        }

        if (password.equals(confirmPassword)){
            confirmPasswordValid = true;
        }

        if (!usernameFilled || !emailFilled || !passwordFilled || !confirmPasswordFilled){
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_LONG).show();
        } else if (!emailValid){
            Toast.makeText(this, "Insira um email válido.",Toast.LENGTH_LONG).show();
        } else if (!confirmPasswordValid){
            Toast.makeText(this, "Senhas não condizem.",Toast.LENGTH_LONG).show();
        } else {
            getValidUsername(username, email, password);
        }
    }

    private void firebaseSignup(String username, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUpActivity.this, "Conta criada com sucesso.", Toast.LENGTH_SHORT).show();
                            createNewUserDatabase(username, user.getUid());
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Conta já existente.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getValidUsername(String username, String email, String password) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean usernameValid = true;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String docUsername = (String)document.getData().get("username");
                                if(docUsername.equals(username)){
                                    usernameValid = false;
                                }
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                        }

                        if(usernameValid){
                            firebaseSignup(username, email, password);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Username already registered.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createNewUserDatabase(String username, String uid){
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("username", username);
        newUser.put("images", new ArrayList<String>());
        db.collection("users").document(uid).set(newUser);
    }
}