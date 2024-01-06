package com.example.cyclinggroupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileInfo extends AppCompatActivity {

    private EditText nameField, instagramField, phoneField;
    private Button cancel, back, update, backButton,logOffButton;
    private TextView name, instagram, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);


        logOffButton = findViewById(R.id.logOffButton);
        backButton = findViewById(R.id.backButtonMenu);
        ImageView rightIcon = findViewById(R.id.right_icon);

        nameField = findViewById(R.id.ProfileNameField);
        instagramField = findViewById(R.id.InstagramField);
        phoneField = findViewById(R.id.PhoneField);
        back = findViewById(R.id.Back);
        cancel = findViewById(R.id.Cancel);
        update = findViewById(R.id.Update);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //not logged in

            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String document = firebaseAuth.getCurrentUser().getEmail();
            DocumentReference docRef = db.collection("users").document(document);


            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String TAG = "TAG";
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            HashMap<String, Object> profile = (HashMap<String, Object>) document.get("Profile") ;
                            nameField.setText((String) profile.get("Name"));
                            instagramField.setText((String) profile.get("Instagram"));
                            phoneField.setText((String) profile.get("Phone"));
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                }
            });

            rightIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View tools = findViewById(R.id.tools);
                    if (view.getId() == R.id.right_icon) {
                        // Alternatively, if you're toggling visibility, use:
                        tools.setVisibility(tools.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileInfo.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    public void onClickCancel(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
        finish();
    }

    public void onClickBack(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
        finish();
    }

    public void onClickUpdate(View view) {
        //Date validation

       String instagramFieldValidation = instagramField.getText().toString();
        String phoneFieldValidation = phoneField.getText().toString();
        if(TextUtils.isEmpty(instagramFieldValidation) || !Patterns.WEB_URL.matcher(instagramFieldValidation).matches()){
            instagramField.setError("Enter a valid social media link");
        }
        else if(TextUtils.isEmpty(phoneFieldValidation) || !Patterns.PHONE.matcher(phoneFieldValidation).matches()){
            phoneField.setError("Enter a valid phone number");

        }

        else {
            FirebaseFirestore fstore = FirebaseFirestore.getInstance();


            Map<String, Object> event = new HashMap<>();
            event.put("Name", nameField.getText().toString());
            event.put("Instagram", instagramField.getText().toString());
            event.put("Phone", phoneField.getText().toString());
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String document = firebaseAuth.getCurrentUser().getEmail();
            DocumentReference docRef = db.collection("users").document(document);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String TAG = "TAG";
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            Map<String, Object> in = document.getData();
                            in.put("Profile", event);
                            fstore.collection("users").document(document.getId()).set(in).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(ProfileInfo.this, ProfileActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileInfo.this, "did not work", Toast.LENGTH_SHORT).show();
                                }
                            });


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
}