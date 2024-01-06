package com.example.cyclinggroupapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cyclinggroupapp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    protected ActivityLoginBinding binding;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    private String email = "", password = "";
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging in");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

        checkUser();
        //if do not have account click to go to signin

        binding.haveAccount.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));
        binding.LoginBtn.setOnClickListener(v -> validateData());




    }

    private void checkUser() {
        //check if user is already logged in
        //if already logged in then open profile activity

        //get current user

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            //alr logged in

            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }
    }

    protected void validateData(){
        email = binding.emailET.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        if(email.equals("admin") && password.equals("admin")) {
            firebaseAdminLogin();
        }

        if (email.equals("gccadmin") && password.equals("GCCRocks!")) {
            firebaseGccLogin();
        }

        if (email.equals("cyclingaddict") && password.equals("cyclingIsLife!")) {
            firebaseCALogin();
        }

        //validate data
        if(!email.equals("admin") && email.equals("gccadmin") && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email

            binding.emailET.setError("Invalid email format");

        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordEt.setError("Enter password");

        }

        else{
            firebaseLogin();
        }

    }

    private void firebaseLogin() {
        //show progress
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String email = firebaseUser.getEmail();
                    Toast.makeText(LoginActivity.this, "Logged in \n" + email, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    //login failed
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void firebaseAdminLogin() {
        //show progress
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword("admin@admin.admin", "admin123")
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String email = firebaseUser.getEmail();
                    Toast.makeText(LoginActivity.this, "Logged in \n" + email, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    //login failed
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void firebaseGccLogin() {
        //show progress
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword("gccadmin@admin.admin", "GCCRocks!")
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String email = firebaseUser.getEmail();
                    Toast.makeText(LoginActivity.this, "Logged in \n" + email, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    //login failed
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void firebaseCALogin() {
        //show progress
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword("cyclingaddict@gmail.com", "cyclingIsLife!")
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String email = firebaseUser.getEmail();
                    Toast.makeText(LoginActivity.this, "Logged in \n" + email, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    //login failed
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}