package com.example.cyclinggroupapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cyclinggroupapp.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ToolMenuActivity extends AppCompatActivity {

    private TextView usernameTextView;

    private String userRole;
    private Button logOffButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.togglemenu);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views
        usernameTextView = findViewById(R.id.usernameTextView);
        Button logOffButton = findViewById(R.id.logOffButton);
        Button clubEventButton = findViewById(R.id.ClubEventCreateBtnToggle);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // Fetch and display username
        fetchAndDisplayUsername();


    }

    private void fetchAndDisplayUsername() {

        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String username = task.getResult().getString("username");
                usernameTextView.setText(username);
            } else {
                // Handle error
            }
        });
    }


}
