package com.example.cyclinggroupapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editEventName, editEventRegion, editEventType;
    private CollectionReference db;
    private String editEventId; // To store the original event name
    private String eventName,eventRegion,eventType,activityOrigin;
    Spinner spinner;
    ArrayList<String> courses = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        db = FirebaseFirestore.getInstance().collection("Events");

        CollectionReference database = FirebaseFirestore.getInstance().collection("Event_Type");
        try {
            activityOrigin = getIntent().getStringExtra("ACTIVITY_ORIGIN");
        } catch (java.lang.NullPointerException e) {}

        spinner = findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);


        database.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot dataSnapshot: value.getDocuments()){

                    courses.add((String) dataSnapshot.get("Name"));

                }

                // Create the instance of ArrayAdapter
                // having the list of courses
                ArrayAdapter ad = new ArrayAdapter(EditEventActivity.this, android.R.layout.simple_spinner_item, courses);
                // add the things to the snipper
                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Set the ArrayAdapter (ad) data on the
                // Spinner which binds data to spinner
                spinner.setAdapter(ad);

            }
        });

        //setting origin of activity starting

        activityOrigin = getIntent().getStringExtra("ACTIVITY_ORIGIN");


        editEventName = findViewById(R.id.editEventName);
        editEventRegion = findViewById(R.id.editEventRegion);

        // Retrieve the passed data
        editEventId = getIntent().getStringExtra("EVENT_ID");
        eventName = getIntent().getStringExtra("EVENT_NAME");
        eventRegion = getIntent().getStringExtra("EVENT_REGION");
        eventType = getIntent().getStringExtra("EVENT_TYPE");

        // Set the hints
        editEventName.setHint(eventName);
        editEventRegion.setHint(eventRegion);

        findViewById(R.id.cancelEdit).setOnClickListener(view ->cancelEditPage());
        findViewById(R.id.deleteEvent).setOnClickListener(view ->deleteEventDB());
        findViewById(R.id.saveEventButton).setOnClickListener(view -> saveEventChanges());
    }

    private void fetchEventDetails(String eventName) {
                db.whereEqualTo("EventName", eventName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Assuming you have the event's ID
                        String eventId = documentSnapshot.getId();
                        editEventName.setText(documentSnapshot.getString("EventName"));
                        editEventRegion.setText(documentSnapshot.getString("EventRegion"));
                        editEventType.setText(documentSnapshot.getString("EventType"));
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    private void saveEventChanges() {
        String newEventName = editEventName.getText().toString();
        String newEventRegion = editEventRegion.getText().toString();

        if (newEventName == null || newEventName.equals("")){
            newEventName = eventName;
        }
        if (newEventRegion == null || newEventRegion.equals("")){
            newEventRegion = eventRegion;
        }

        // Proceed with updating the event
        updateEventInFirestore(newEventName, newEventRegion, eventType);
    }

    private void updateEventInFirestore(String eventName, String eventRegion, String eventType) {
        Map<String, Object> event = new HashMap<>();
        event.put("EventName", eventName);
        event.put("EventRegion", eventRegion);
        event.put("EventType", eventType);
        
        db.document(editEventId)
                    .update(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(EditEventActivity.this, ProfileActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(EditEventActivity.this, "Error updating event", Toast.LENGTH_SHORT).show());
    }

    private void deleteEventDB(){
            db.document(editEventId).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        public void onSuccess(Void unused) {

                            startActivity(new Intent(EditEventActivity.this, ProfileActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(EditEventActivity.this, "Error Deleting Event", Toast.LENGTH_SHORT).show());
    }
    private void cancelEditPage() {
        startActivity(new Intent(this, ProfileActivity.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // make toastof name of course
        // which is selected in spinner
        Toast.makeText(EditEventActivity.this, courses.get(i), Toast.LENGTH_LONG);
        eventType = courses.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
