package com.example.onehomefarm.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onehomefarm.activities.HomePageActivity;
import com.example.onehomefarm.activities.SignUpActivity;
import com.example.onehomefarm.databinding.ActivitySignInBinding;
import com.example.onehomefarm.utillities.MD5Hash;
import com.example.onehomefarm.utillities.PreferenceManager;
import com.example.onehomefarm.utillities.Constants;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private TextView forgotPassword;

    private PreferenceManager preferenceManager;


    private String imageProfileBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
            finish();
        }








        binding.forgotPassword.setPaintFlags(binding.forgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setListener();

    }

    private void setListener(){


        binding.buttonMasuk.setOnClickListener( v -> {
            if(isValidSignInDetail()) {
                signIn();
            }
        });

        binding.layoutDaftar.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        binding.layoutButtonMasuk.setOnClickListener( v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));



//        binding.forgotPassword.setOnClickListener(v -> {
//            DialogUtils.showSimpleDialog(this, "Information", "This feature is still under development. Sorry for the inconvenience.");
//        });
    }



    private void signIn() {
//        showLoadingDialog();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, MD5Hash.md5(binding.inputPassword.getText().toString()))
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_USERNAME, documentSnapshot.getString(Constants.KEY_USERNAME));
                        if (documentSnapshot.contains(Constants.KEY_IMAGE_PROFILE)) {
                            preferenceManager.putString(Constants.KEY_IMAGE_PROFILE, documentSnapshot.getString(Constants.KEY_IMAGE_PROFILE));
                        }
                        if (documentSnapshot.contains(Constants.KEY_EMAIL)) {
                            preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                        }
                        if (documentSnapshot.contains(Constants.KEY_PHONE_NUMBER)) {
                            preferenceManager.putString(Constants.KEY_PHONE_NUMBER, documentSnapshot.getString(Constants.KEY_PHONE_NUMBER));
                        }
                        if (documentSnapshot.contains(Constants.KEY_IS_ADMIN)) {
                            preferenceManager.putBoolean(Constants.KEY_IS_ADMIN, true);

//                            new Handler().postDelayed(() -> {
//                                Intent intent = new Intent(getApplicationContext(), DashboardAdminActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                            }, 2000);
                        } else {

                            new Handler().postDelayed(() -> {
                                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }, 2000);
                        }
                    } else {
//                        showFinishDialog(false, "Verification Failed");
                    }
                });
    }

    private void checkIfUserExists(String email, String username, String imageProfile) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = database.collection("users");

        usersRef.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            preferenceManager.putString(Constants.KEY_USER_ID, document.getId());
                            preferenceManager.putString(Constants.KEY_USERNAME, document.getString(Constants.KEY_USERNAME));
                            preferenceManager.putString(Constants.KEY_EMAIL, document.getString(Constants.KEY_EMAIL));
                            if (document.contains(Constants.KEY_IMAGE_PROFILE)) {
                                preferenceManager.putString(Constants.KEY_IMAGE_PROFILE, document.getString(Constants.KEY_IMAGE_PROFILE));
                            }
                            if (document.contains(Constants.KEY_IMAGE_PROFILE_BACKGROUND)) {
                                preferenceManager.putString(Constants.KEY_IMAGE_PROFILE_BACKGROUND, document.getString(Constants.KEY_IMAGE_PROFILE_BACKGROUND));
                            }
                            if (document.contains(Constants.KEY_PHONE_NUMBER)) {
                                preferenceManager.putString(Constants.KEY_PHONE_NUMBER, document.getString(Constants.KEY_PHONE_NUMBER));
                            }
//                            showFinishDialog(true, "Verification Successfully");
                        } else {
                            saveUserToFirestore(email, username, imageProfile);
                        }
                    } else {
                        Exception exception = task.getException();
                        Log.e("Firestore", "Error checking user existence", exception);
                    }
                });
    }

    private void saveUserToFirestore(String email, String username, String imageProfile) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = database.collection(Constants.KEY_COLLECTION_USERS);

        Map<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_EMAIL, email);
        user.put(Constants.KEY_USERNAME, username);
        user.put(Constants.KEY_IMAGE_PROFILE, imageProfile);
        user.put(Constants.KEY_IMAGE_PROFILE_BACKGROUND, imageProfileBackground);
        user.put(Constants.KEY_VERIFIED_STATUS, "Not Verified");

        usersRef.add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_USERNAME, username);
                    preferenceManager.putString(Constants.KEY_EMAIL, email);
                    preferenceManager.putString(Constants.KEY_IMAGE_PROFILE, imageProfile);
                    preferenceManager.putString(Constants.KEY_IMAGE_PROFILE_BACKGROUND, imageProfileBackground);
//                    showFinishDialog(true, "Verification Successfully");
                })
                .addOnFailureListener(e -> {
//                    showFinishDialog(false, "Verification Failed");
                });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetail() {
        if(binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if(binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }


}