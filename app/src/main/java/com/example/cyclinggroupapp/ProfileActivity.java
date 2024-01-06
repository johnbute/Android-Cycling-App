package com.example.cyclinggroupapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cyclinggroupapp.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.Collections;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    public String username;
    public String role;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Login");

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //logout user by clicking

        binding.logoutBtn.setOnClickListener(view -> {
            firebaseAuth.signOut();
            checkUser();
        });

        binding.nextButton.setOnClickListener(view -> {
            if (role.equals("Admin")) {
                startActivity(new Intent(ProfileActivity.this, AdminLandingActivity.class));
                finish();
            }
            else if (role.equals("Cycling club")) {
                startActivity(new Intent(ProfileActivity.this, ClubEventListActivity.class));
                finish();
            } else {
                startActivity(new Intent(ProfileActivity.this, ParticipantEventListActivity.class));
                finish();
            }
        });

        //For Club Owners

    }

    private void checkUser() {
//check if user is already logged in
        //if already logged in then open profile activity

        //get current user

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //not logged in

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else{

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String document = firebaseAuth.getCurrentUser().getEmail();
            DocumentReference docRef = db.collection("users").document(document);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String TAG= "TAG";
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            username = (String) document.get("username");
                            role = (String) document.get("role");

                            binding.emailTv.setText(username);
                            binding.RoleTv.setText(role);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                }
            });


        }
    }

    public void logOff(View view) {
        // Log out from Firebase Auth
        FirebaseAuth.getInstance().signOut();

        // Redirect to the Login Screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    public void editProfile(View view) {
        Intent intent = new Intent(this, ProfileInfo.class);
        startActivity(intent);
        finish();
    }

}