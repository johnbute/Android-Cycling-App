package com.example.cyclinggroupapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateEventForm extends Activity implements AdapterView.OnItemSelectedListener {
    String eventType;
    CollectionReference database;

    EditText nameET, regionET;
    private FirebaseFirestore fstore;
    Button submitButton, backButton;
    Spinner spinner;

    ArrayList<String> courses = new ArrayList<>();
    private FirebaseAuth firebaseAuth;

    String activityOrigin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_form);

        nameET = findViewById(R.id.eventNameEditText);
        regionET = findViewById(R.id.regionEditText);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton2);
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // set simple layout resource file
        // for each item of spinner


        database = FirebaseFirestore.getInstance().collection("Event_Type");
        try {
            activityOrigin = getIntent().getStringExtra("ACTIVITY_ORIGIN");
        } catch (java.lang.NullPointerException e) {}


        database.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot dataSnapshot: value.getDocuments()){

                    courses.add((String) dataSnapshot.get("Name"));

                }

                // Create the instance of ArrayAdapter
                // having the list of courses
                ArrayAdapter ad = new ArrayAdapter(CreateEventForm.this, android.R.layout.simple_spinner_item, courses);
                // add the things to the snipper
                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Set the ArrayAdapter (ad) data on the
                // Spinner which binds data to spinner
                spinner.setAdapter(ad);

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateEventForm.this, "Clicked", Toast.LENGTH_LONG);
                String name = nameET.getText().toString();
                String region = regionET.getText().toString();
                if (eventType == null || name.equals("") || region.equals("")) {
                    Toast.makeText(CreateEventForm.this, "choose type", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(CreateEventForm.this, "Success", Toast.LENGTH_LONG);
                    updatedDB(name, region, eventType);

                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                back();
            }
        });

    }

    private void back() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    private void updatedDB(String name, String region, String type) {
        fstore = FirebaseFirestore.getInstance();

        long time = System.currentTimeMillis();
        String id = time + "";

        Map<String, Object> event = new HashMap<>();
        event.put("EventName", name);
        event.put("EventRegion", region);
        event.put("EventType", type);
        ArrayList<Integer> ratings = new ArrayList<>();
        event.put("EventRatings", ratings);
        ArrayList<String> participants = new ArrayList<>();
        event.put("Participants", participants);
        firebaseAuth = FirebaseAuth.getInstance();
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
                        String username = (String) document.get("username");
                        event.put("EventOwner", username);

                        fstore.collection("Events").document(id).set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                    startActivity(new Intent(CreateEventForm.this, ProfileActivity.class));
                    finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateEventForm.this, "did not work", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // make toastof name of course
        // which is selected in spinner
        Toast.makeText(CreateEventForm.this, courses.get(i), Toast.LENGTH_LONG);
        eventType = courses.get(i);
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}