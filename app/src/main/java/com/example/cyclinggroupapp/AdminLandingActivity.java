package com.example.cyclinggroupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class AdminLandingActivity extends AppCompatActivity {

    private View toolMenuView;

    private ImageView rightIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

        // Initialize views and buttons after setting the content view
        Button logOffButton = findViewById(R.id.logOffButton);
        toolMenuView = findViewById(R.id.toolmenu);
        ImageView rightIcon = findViewById(R.id.right_icon);
        TextView title = findViewById(R.id.toolbar_title);
        Button eventTypeList = findViewById(R.id.btnEventTypeList);
        Button adminEventList = findViewById(R.id.btnAdminEventList);
        Button accountList = findViewById(R.id.btnAccountModeration);

        accountList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLandingActivity.this, AccountModerationListActivity.class));
                finish();

            }
        });

        eventTypeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLandingActivity.this, AdminTypeListActivity.class));
                finish();

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

        adminEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLandingActivity.this, AdminEventListActivity.class));
                finish();
            }
        });

        logOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOff();
            }
        });

        Button backButton = findViewById(R.id.backButtonMenu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLandingActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
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
}
