package com.example.onehomefarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.onehomefarm.databinding.ActivitySignUpBinding;
import com.example.onehomefarm.utillities.Constants;
import com.example.onehomefarm.utillities.PreferenceManager;

import com.example.onehomefarm.utillities.MD5Hash;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
            finish();
        }



        setListener();

    }

    private void setListener(){
        binding.buttonDaftar.setOnClickListener(v -> {
            if(isValidSignUpDetail()) {
                checkIfUserExists(binding.inputEmail.getText().toString());
            }
        });

        binding.layoutMasuk.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)));
//        binding.backButtonSignUp.setOnClickListener(v -> {
//            startActivity(new Intent(getApplicationContext(), CTAActivity.class));
//        });
    }

    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        String defaultProfile = "https://firebasestorage.googleapis.com/v0/b/swiftcare-86318.appspot.com/o/profileImages%2FdefaultProfile.png?alt=media&token=6533709d-83be-4585-81b7-2d2efdb06576";
        user.put(Constants.KEY_USERNAME, binding.inputUsername.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, MD5Hash.md5(binding.inputPassword.getText().toString()));
//        user.put(Constants.KEY_PHONE_NUMBER, binding.inputPhoneNumber.getText().toString());
        user.put(Constants.KEY_IMAGE_PROFILE, defaultProfile);
        user.put(Constants.KEY_ACCOUNT_TYPE, Constants.KEY_NORMAL_ACCOUNT);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_ACCOUNT_TYPE, Constants.KEY_NORMAL_ACCOUNT);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_USERNAME, binding.inputUsername.getText().toString());
                    preferenceManager.putString(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
//                    preferenceManager.putString(Constants.KEY_PHONE_NUMBER, binding.inputPhoneNumber.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE_PROFILE, defaultProfile);
                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });
    }

    private void checkIfUserExists(String email) {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = database.collection("users");

        usersRef.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            showToast("This email already exists, please use another email");
                            loading(false);
                        } else {
                            signUp();
                        }
                    } else {
                        Exception exception = task.getException();
                        Log.e("Firestore", "Error checking user existence", exception);
                    }
                });
    }

    private String getUsernameFromEmail() {
        String email = binding.inputEmail.getText().toString();
        int atIndex = email.indexOf("@");
        if (atIndex != -1) {
            return email.substring(0, atIndex);
        } else {
            return "username";
        }
    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.buttonDaftar.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonDaftar.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignUpDetail() {
        if(binding.inputUsername.getText().toString().trim().isEmpty()) {
            showToast("Enter username");
            return false;
        } else if(binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if(binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if(binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if(!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Passwords do not match");
            return false;
        } else {
            return true;
        }
    }

}