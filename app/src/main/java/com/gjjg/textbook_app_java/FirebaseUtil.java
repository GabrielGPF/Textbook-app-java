package com.gjjg.textbook_app_java;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gjjg.textbook_app_java.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseUtil {
    private static FirebaseUtil instance;

    private String currentUser;
    private String currentUserUsername;
    private ArrayList<String> currentUserImages;

    public String getCurrentUserUsername() {
        return currentUserUsername;
    }

    public void setCurrentUserUsername(String currentUserUsername) {
        this.currentUserUsername = currentUserUsername;
    }

    public ArrayList<String> getCurrentUserImages() {
        return currentUserImages;
    }

    public void setCurrentUserImages(ArrayList<String> currentUserImages) {
        this.currentUserImages = currentUserImages;
    }

    private FirebaseUtil(){}

    public static FirebaseUtil getInstance(){
        if (instance == null){
            instance = new FirebaseUtil();
        }

        return instance;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    protected static void getPosts(Context context, RecyclerView recyclerView){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(context, "Error getting posts.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Post> currentFeed = new ArrayList<Post>();
                for (QueryDocumentSnapshot doc : snapshot) {
                    if (doc.get("image") != null) {
                        DocumentReference senderRef = (DocumentReference)doc.getData().get("sender");
                        Post newPost = new Post((String)doc.getData().get("image"), "", (String)senderRef.getId() );
                        Task senderTask = senderRef.get();
                        while(!senderTask.isComplete()){}
                        newPost.setSenderName((String)((DocumentSnapshot)senderTask.getResult()).getData().get("username"));
                        currentFeed.add(newPost);
                    }
                }
                MainFeedViewAdapter mainFeedViewAdapter = new MainFeedViewAdapter(context, currentFeed);
                recyclerView.setAdapter(mainFeedViewAdapter);

            }
        });

    }

    protected static void getUserProfileData(Context context, RecyclerView recyclerView, EditText nameEditText, String userId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String username = (String)task.getResult().get("username");
                            ArrayList<String> images = (ArrayList<String>)task.getResult().get("images");
                            Collections.reverse(images);
                            FirebaseUtil.getInstance().setCurrentUserUsername(username);
                            FirebaseUtil.getInstance().setCurrentUserImages(images);
                            nameEditText.setText(username);
                            ProfileAdapter profileAdapter = new ProfileAdapter(context, images);
                            recyclerView.setAdapter(profileAdapter);
                        } else {
                            Toast.makeText(context, "Error getting posts.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
