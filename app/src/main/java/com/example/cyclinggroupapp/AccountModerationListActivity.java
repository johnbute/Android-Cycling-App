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

public class AccountModerationListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CollectionReference database;
    AccountListAdapter myAdapter;
    ArrayList<Account> list;

    private View toolMenuView;

    private ImageView rightIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_moderation_list);
        Button logOffButton = findViewById(R.id.logOffButton);
        toolMenuView = findViewById(R.id.toolmenu);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);






        recyclerView = findViewById(R.id.AccountList);
        database = FirebaseFirestore.getInstance().collection("users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new AccountListAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnClickListener(new AccountListAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Account account) {

                Intent intent = new Intent(AccountModerationListActivity.this, AccountModerationEditActivity.class);
                //intent.putExtra("EVENT_ID", )
                intent.putExtra("ACCOUNT_USERNAME", account.AccountUsername);
                intent.putExtra("ACCOUNT_EMAIL", account.AccountEmail);
                intent.putExtra("ACCOUNT_ROLE", account.AccountRole);
                startActivity(intent);
            }
        });


        database.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){}
                else{for(DocumentSnapshot dataSnapshot: value.getDocuments()){

                    Account account = new Account();
                    account.AccountEmail = (String) dataSnapshot.get("email");
                    account.AccountRole = (String) dataSnapshot.get("role");
                    account.AccountUsername = (String) dataSnapshot.get("username");
                    list.add(account);

                }
                    myAdapter.notifyDataSetChanged();}

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
                Intent intent = new Intent(AccountModerationListActivity.this, AdminLandingActivity.class);
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