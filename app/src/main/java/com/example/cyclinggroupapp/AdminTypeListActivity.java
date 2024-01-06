package com.example.cyclinggroupapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminTypeListActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    CollectionReference database;
    EventTypeListAdapter myAdapter;
    ArrayList<EventType> list;

    private View toolMenuView;

    private ImageView rightIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_type_list);

        Button logOffButton = findViewById(R.id.logOffButton);
        toolMenuView = findViewById(R.id.toolmenu);
        ImageView rightIcon = findViewById(R.id.right_icon);
        Button backButton = findViewById(R.id.backButtonMenu);

        Button createButton = findViewById(R.id.EventTypeCreatebtn);

        TextView title = findViewById(R.id.toolbar_title);

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
                Intent intent = new Intent(AdminTypeListActivity.this, AdminLandingActivity.class);
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

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace this with your actual logic for the button click
                create();
            }


        });

        recyclerView = findViewById(R.id.eventTypeList);
        database = FirebaseFirestore.getInstance().collection("Event_Type");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new EventTypeListAdapter(this, list);
        recyclerView.setAdapter(myAdapter);


        myAdapter.setOnClickListener(new EventTypeListAdapter.OnClickListener() {
            @Override
            public void onClick(int position, EventType event) {

                Intent intent = new Intent(AdminTypeListActivity.this, CreateTypeActivity.class);
                intent.putExtra(NEXT_SCREEN,event);
                intent.putExtra("EVENTTYPE_NAME", event.EventType);
                intent.putExtra("EVENTTYPE_DESCRIPTION", event.EventDescription);
                intent.putExtra("EDIT", true);
                startActivity(intent);
            }
        });



        database.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){}
                else {
                    for (DocumentSnapshot dataSnapshot : value.getDocuments()) {


                        EventType event = new EventType();
                        event.EventType = (String) dataSnapshot.get("Name");
                        event.EventDescription = (String) dataSnapshot.get("Description");
                        list.add(event);

                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });






    }

    private void logOff() {
        // Log out from Firebase Auth
        FirebaseAuth.getInstance().signOut();

        // Redirect to the Login Screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    private void create() {startActivity(new Intent(this, CreateTypeActivity.class)); finish();}

    public static final String NEXT_SCREEN = "EventTypeDetails";


}