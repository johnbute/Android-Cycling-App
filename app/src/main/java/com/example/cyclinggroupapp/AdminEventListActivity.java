package com.example.cyclinggroupapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyclinggroupapp.databinding.ActivityAdminEventListBinding;
import com.example.cyclinggroupapp.databinding.ActivityCreateEventFormBinding;
import com.example.cyclinggroupapp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminEventListActivity extends Activity {

    RecyclerView recyclerView;
    CollectionReference database;
    EventListAdapter myAdapter;
    ArrayList<Event> list;

    private View toolMenuView;

    private ImageView rightIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_list);

        Button logOffButton = findViewById(R.id.logOffButton);
        toolMenuView = findViewById(R.id.toolmenu);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);






        recyclerView = findViewById(R.id.eventList);
        database = FirebaseFirestore.getInstance().collection("Events");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new EventListAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnClickListener(new EventListAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Event event) {

                Intent intent = new Intent(AdminEventListActivity.this, EditEventActivity.class);
                intent.putExtra(NEXT_SCREEN,event);
                //intent.putExtra("EVENT_ID", )
                intent.putExtra("EVENT_NAME", event.EventName);
                intent.putExtra("EVENT_REGION", event.EventRegion);
                intent.putExtra("EVENT_TYPE", event.EventType);
                intent.putExtra("EVENT_ID",event.EventId);
                startActivity(intent);
            }
        });


        database.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){}
                else{for(DocumentSnapshot dataSnapshot: value.getDocuments()){

                    Event event = new Event();
                    event.EventName = (String) dataSnapshot.get("EventName");
                    event.EventRegion = (String) dataSnapshot.get("EventRegion");
                    event.EventType = (String) dataSnapshot.get("EventType");
                    event.EventId = (String) dataSnapshot.getId();
                    list.add(event);

                }
                myAdapter.notifyDataSetChanged();}

            }
        });


        Button createButton = findViewById(R.id.EventCreatebtn);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace this with your actual logic for the button click
                create();
            }


        });

        /*Button createType = findViewById(R.id.createEventType);
        createType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminEventListActivity.this, CreateTypeActivity.class)); finish();
            }


        });*/

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



        Button backButton = findViewById(R.id.backButtonMenu);

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminEventListActivity.this, AdminLandingActivity.class);
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