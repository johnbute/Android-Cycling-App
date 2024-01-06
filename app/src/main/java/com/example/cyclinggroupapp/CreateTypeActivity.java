package com.example.cyclinggroupapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateTypeActivity extends Activity {

    private EditText nameET, regionET;
    private String EventTypeName, Description;
    private boolean EditBool;
    private FirebaseFirestore fstore;
    private Button submitButton, cancelButton, deleteButton;
    private ArrayList<String> courses = new ArrayList<>();
    private View toolMenuView;

    private ImageView rightIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_type);

        nameET = findViewById(R.id.eventTypeNameEditText);
        regionET = findViewById(R.id.descriptionEditText);
        submitButton = findViewById(R.id.submitButton2);
        deleteButton = findViewById(R.id.deleteEventType);
        cancelButton = findViewById(R.id.cancelEditType);
        Button logOffButton = findViewById(R.id.logOffButton);
        toolMenuView = findViewById(R.id.toolmenu);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);
        Button createButton = findViewById(R.id.EventCreatebtn);
        Button backButton = findViewById(R.id.backButtonMenu);


        //Retrieve passed data
        EventTypeName = getIntent().getStringExtra("EVENTTYPE_NAME");
        Description = getIntent().getStringExtra("EVENTTYPE_DESCRIPTION");
        EditBool = getIntent().getBooleanExtra("EDIT",false);


        // Set the hints
        nameET.setHint(EventTypeName);
        regionET.setHint(Description);


        // set simple layout resource file
        // for each item of spinner

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateTypeActivity.this, AdminTypeListActivity.class));
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference db = FirebaseFirestore.getInstance().collection("Event_Type");
                db.document(EventTypeName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void unused) {
                                Toast.makeText(CreateTypeActivity.this,"Delete successful", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(CreateTypeActivity.this, "Error Deleting Event", Toast.LENGTH_SHORT).show());

                startActivity(new Intent(CreateTypeActivity.this, AdminTypeListActivity.class));
                finish();
            }

        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                String region = regionET.getText().toString();
                if(EditBool == false){
                    updatedDB(name, region);
                }
                else{
                    EditDB(name, region);
                }

                Toast.makeText(CreateTypeActivity.this, "Success", Toast.LENGTH_LONG);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateTypeActivity.this, AdminTypeListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOff();
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


    }

    private void EditDB(String name, String region) {
        CollectionReference db = FirebaseFirestore.getInstance().collection("Event_Type");
        db.document(EventTypeName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void unused) {
                Toast.makeText(CreateTypeActivity.this,"Delete successful", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(e -> Toast.makeText(CreateTypeActivity.this, "Error Deleting Event", Toast.LENGTH_SHORT).show());

        updatedDB(name, region);




    }


    private void updatedDB(String name, String region) {
        fstore = FirebaseFirestore.getInstance();

        String id = name;

        Map<String, Object> event = new HashMap<>();
        event.put("Name", name);
        event.put("Description", region);

        fstore.collection("Event_Type").document(id).set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(CreateTypeActivity.this, AdminTypeListActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateTypeActivity.this, "did not work", Toast.LENGTH_SHORT).show();
            }
        });




    }
    private void logOff() {
        // Log out from Firebase Auth
        FirebaseAuth.getInstance().signOut();

        // Redirect to the Login Screen
        Intent intent = new Intent(this, AdminTypeListActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }



}