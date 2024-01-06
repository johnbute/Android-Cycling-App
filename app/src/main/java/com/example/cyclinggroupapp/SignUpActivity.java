package com.example.cyclinggroupapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cyclinggroupapp.Users.Account;
import com.example.cyclinggroupapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseFirestore fstore;



    private ActivitySignUpBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String email = "", password = "", username = "", userId = "", role = "";
    private static final String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Configure spinner

        String[] arraySpinner = new String[]{
                 "Cycling club", "Participant"
        };
        Spinner s = (Spinner) binding.spinner1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        firebaseAuth = FirebaseAuth.getInstance();
        //configure progress
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("SignUp");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating your account");
        progressDialog.setCanceledOnTouchOutside(false);


        binding.SignupBtn.setOnClickListener(v -> {
            role = s.getSelectedItem().toString();
            validateData();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //got previous activity when back button of action bar clicked
        return super.onSupportNavigateUp();
    }

    private void validateData(){
        email = binding.emailET.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        username = binding.usernameEt.getText().toString().trim();

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email
            binding.emailET.setError("Invalid email format");

        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordEt.setError("Enter password");

        }

        else if(TextUtils.isEmpty(username)){
            binding.usernameEt.setError("Enter username");
        }
        else{
            firebaseSignUp();
        }
    }

    private void firebaseSignUp(){

        //show progress
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    progressDialog.dismiss();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String email = firebaseUser.getEmail();
                    Toast.makeText(SignUpActivity.this, "Account created\n" + email, Toast.LENGTH_SHORT).show();
                    fstore = FirebaseFirestore.getInstance();
                    userId = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("users").document(email);
                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("email", email);
                    user.put("role", role);
                    Map<String, Object> profileInfo = new HashMap<>();
                    profileInfo.put("Instagram", "");
                    profileInfo.put("Phone", "");
                    profileInfo.put("Name", "");
                    user.put("Profile", profileInfo);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: User profile is created for " + userId);
                        }
                    });

                    startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

}//Connect firebase
