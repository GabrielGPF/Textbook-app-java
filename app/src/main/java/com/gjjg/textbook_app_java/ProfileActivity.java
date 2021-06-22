package com.gjjg.textbook_app_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private enum EditButtonMode {
        AbleEditText,
        SaveUsername
    }

    private RecyclerView profileRecyclerView;
    private EditText nameEditText;
    private ImageButton editButton;
    private boolean myProfile = true;
    private boolean editableUsername = false;
    private EditButtonMode editButtonMode = EditButtonMode.AbleEditText;
    private String initialUsername;

    private int editIcon = 17301566;
    private int uploadIcon = 17301589;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editButton = findViewById(R.id.imageButton);
        nameEditText = findViewById(R.id.editTextTextPersonName);
        profileRecyclerView = findViewById(R.id.profileRecyclerView);
        profileRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        ProfileAdapter profileAdapter = new ProfileAdapter(this, new ArrayList<>());
        profileRecyclerView.setAdapter(profileAdapter);
        FirebaseUtil.getUserProfileData(ProfileActivity.this, profileRecyclerView, nameEditText, FirebaseUtil.getInstance().getCurrentUser());

        //Set editable username
        changeUsernameEditTextActive();
        if(!FirebaseUtil.getInstance().getCurrentUser().equals(FirebaseAuth.getInstance().getUid())){
            nameEditText.setBackgroundResource(android.R.color.transparent);
            myProfile = false;
            editButton.setColorFilter(ContextCompat.getColor(this, R.color.browser_actions_bg_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void editButtonClick(View view) {
        switch(editButtonMode){
            case AbleEditText:
                initialUsername = nameEditText.getText().toString();
                if(myProfile){
                    editButton.setImageDrawable(getResources().getDrawable(uploadIcon));
                    editButtonMode = EditButtonMode.SaveUsername;
                    changeUsernameEditTextActive();
                } else {
                    Toast.makeText(this, "You cannot edit other person's username!", Toast.LENGTH_SHORT).show();
                }
                break;
            case SaveUsername:
                editButton.setImageDrawable(getResources().getDrawable(editIcon));
                editButtonMode = EditButtonMode.AbleEditText;
                getValidUsername(nameEditText.getText().toString());
                ///Check if username is available
                changeUsernameEditTextActive();
                break;
        }
    }
    private void getValidUsername(String newUsername) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean usernameValid = true;
                        Map<String, Object> myAccount = new HashMap<>();
                        String myAccountId = "";

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String docUsername = (String)document.getData().get("username");
                                if(initialUsername.equals(docUsername)){
                                    myAccount = document.getData();
                                    myAccount.put("username", newUsername);
                                    myAccountId = document.getId();
                                }
                                if(docUsername.equals(newUsername)){
                                    usernameValid = false;
                                }
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this, "Error getting documents.", Toast.LENGTH_SHORT).show();
                            nameEditText.setText(initialUsername);
                            return;
                        }

                        if(usernameValid){
                            FirebaseUtil.getInstance().setCurrentUserUsername((String)myAccount.get("username"));
                            db.collection("users").document(myAccountId).set(myAccount, SetOptions.merge()).addOnCompleteListener(postTask ->
                                    Toast.makeText(ProfileActivity.this, "Username successfully changed.", Toast.LENGTH_SHORT).show());
                        } else {
                            nameEditText.setText(initialUsername);
                            Toast.makeText(ProfileActivity.this, "Username already registered.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void changeUsernameEditTextActive(){
        nameEditText.setEnabled(editableUsername);
        nameEditText.setFocusable(editableUsername);
        nameEditText.setFocusableInTouchMode(editableUsername);
        nameEditText.setClickable(editableUsername);
        editableUsername = !editableUsername;
    }
}