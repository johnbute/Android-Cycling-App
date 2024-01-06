package com.example.cyclinggroupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class AccountModerationEditActivity extends AppCompatActivity {

    private EditText editAccountUsername, editAccountEmail, editAccountRole;
    private CollectionReference db;
    private String accountUsername,accountRole,accountEmail;


    private FirebaseFirestore fstore;

    private View toolMenuView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_moderation_edit);
        db = FirebaseFirestore.getInstance().collection("Events");

        //setting origin of activity starting

        Button logOffButton = findViewById(R.id.logOffButton);
        toolMenuView = findViewById(R.id.toolmenu);
        ImageView rightIcon = findViewById(R.id.right_icon);
        Button backButton = findViewById(R.id.backButtonMenu);


        editAccountEmail = findViewById(R.id.editAccountEmail);
        editAccountUsername = findViewById(R.id.editAccountUsername);


        // Retrieve the passed data
        accountUsername = getIntent().getStringExtra("ACCOUNT_USERNAME");
        accountEmail = getIntent().getStringExtra("ACCOUNT_EMAIL");
        accountRole = getIntent().getStringExtra("ACCOUNT_ROLE");

        String[] arraySpinner = new String[]{
                "Cycling club", "Participant"
        };
        Spinner s = findViewById(R.id.editAccountRole);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        int position = -1;
        for (int i = 0; i < arraySpinner.length; i++) {
            if (arraySpinner[i].equals(accountRole)) {
                position = i; // Set the position when the value is found
                break; // Exit the loop when the value is found
            }
        }
        s.setSelection(position);

        // Set the hints
        editAccountEmail.setText(accountEmail);
        editAccountUsername.setText(accountUsername);

        findViewById(R.id.cancelEditAccount).setOnClickListener(view ->cancelEditPage());
        Button deleteButton = findViewById(R.id.deleteAccount);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference db = FirebaseFirestore.getInstance().collection("users");
                db.document(accountEmail).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void unused) {
                                Toast.makeText(AccountModerationEditActivity.this,"Delete successful", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(AccountModerationEditActivity.this, "Error Deleting Event", Toast.LENGTH_SHORT).show());

                startActivity(new Intent(AccountModerationEditActivity.this, AccountModerationListActivity.class));
                finish();
            }

        });

        findViewById(R.id.saveAccountButton).setOnClickListener(view -> saveEventChanges(s));
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountModerationEditActivity.this, AccountModerationListActivity.class);
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

    private void fetchEventDetails(String eventName) {
        db.whereEqualTo("EventName", eventName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Assuming you have the event's ID
                        String eventId = documentSnapshot.getId();
                        editAccountUsername.setText(documentSnapshot.getString("usernme"));
                        editAccountEmail.setText(documentSnapshot.getString("email"));
                        editAccountRole.setText(documentSnapshot.getString("role"));
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    private void saveEventChanges(Spinner s) {
        String newUsername = editAccountUsername.getText().toString();
        String newEmail = editAccountEmail.getText().toString();
        String newRole = s.getSelectedItem().toString();



        if (Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            EditDB(newUsername, newEmail, newRole);
        } else {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_LONG).show();
        }
    }

    private void EditDB(String name, String email, String role) {
        CollectionReference db = FirebaseFirestore.getInstance().collection("users");
        db.document(accountEmail).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    public void onSuccess(Void unused) {
                        Toast.makeText(AccountModerationEditActivity.this,"Delete successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AccountModerationEditActivity.this, "Error Deleting Event", Toast.LENGTH_SHORT).show());

        updatedDB(name, email, role);




    }


    private void updatedDB(String name, String email, String role) {
        fstore = FirebaseFirestore.getInstance();

        String id = email;

        Map<String, Object> account = new HashMap<>();
        account.put("username", name);
        account.put("email", email);
        account.put("role", role);


        fstore.collection("users").document(id).set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(AccountModerationEditActivity.this, AccountModerationListActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountModerationEditActivity.this, "did not work", Toast.LENGTH_SHORT).show();
            }
        });




    }


    private void cancelEditPage() {
        startActivity(new Intent(this, AccountModerationListActivity.class));
        finish();
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