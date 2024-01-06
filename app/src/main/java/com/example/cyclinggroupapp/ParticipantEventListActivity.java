package com.example.cyclinggroupapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class ParticipantEventListActivity extends Activity{

    RecyclerView recyclerView;
    RecyclerView searchrecyclerView;
    CollectionReference database;
    EventListAdapter myAdapter;
    ArrayList<Event> list;
    SearchView searchBar;
    EventListAdapter searchadapter;
    ArrayList<Event> searchlist = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_participant_event_list);

        recyclerView = findViewById(R.id.eventList);
        database = FirebaseFirestore.getInstance().collection("Events");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new EventListAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        database.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){}
                else {
                    for (DocumentSnapshot dataSnapshot : value.getDocuments()) {

                        Event event = new Event();
                        event.EventName = (String) dataSnapshot.get("EventName");
                        event.EventRegion = (String) dataSnapshot.get("EventRegion");
                        event.EventType = (String) dataSnapshot.get("EventType");
                        event.EventId = (String) dataSnapshot.getId();
                        list.add(event);

                        myAdapter.eventsFull.clear(); // Clear and update eventsFull
                        myAdapter.eventsFull.addAll(list);

                    }
                    myAdapter.notifyDataSetChanged();
                }

            }
        });

        myAdapter.setOnClickListener(new EventListAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Event event) {

                Intent intent = new Intent(ParticipantEventListActivity.this, ViewEventActivity.class);
                //intent.putExtra("EVENT_ID", )
                intent.putExtra("EVENT_NAME", event.EventName);
                intent.putExtra("EVENT_REGION", event.EventRegion);
                intent.putExtra("EVENT_TYPE", event.EventType);
                intent.putExtra("EVENT_ID", event.EventId);
                startActivity(intent);
            }
        });

        View toolMenuView = findViewById(R.id.toolmenu);
        ImageView rightIcon = findViewById(R.id.right_icon);
        ImageView searchIcon = findViewById(R.id.search_icon);
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


        searchBar = findViewById(R.id.search);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.filter(newText);
                return false;
            }

        });

        Button backButton = findViewById(R.id.backButtonMenu);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParticipantEventListActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button logOffButton = findViewById(R.id.logOffButton);
        logOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOff();
            }
        });

        myAdapter.resetList();
    }

    private void create() {startActivity(new Intent(this, CreateEventForm.class)); finish();}

    private void logOff() {
        // Log out from Firebase Auth
        FirebaseAuth.getInstance().signOut();

        // Redirect to the Login Screen
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    public static final String NEXT_SCREEN = "EventDetails";
}