package com.example.onehomefarm.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.onehomefarm.databinding.ActivityEditProfileBinding;
import com.example.onehomefarm.utillities.Constants;
import com.example.onehomefarm.utillities.ImageLoader;
import com.example.onehomefarm.utillities.PreferenceManager;
import com.example.onehomefarm.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private PreferenceManager preferenceManager;
    private Uri imageProfile, imageProfileBackground;
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private String imageProfileUrl, imageProfileBackgroundUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        loadProfile();
        setListeners();
    }

    private void loadProfile() {
        ImageLoader.loadCircleImage(preferenceManager.getString(Constants.KEY_IMAGE_PROFILE), binding.userProfile);
        ImageLoader.loadImage(preferenceManager.getString(Constants.KEY_IMAGE_PROFILE_BACKGROUND), binding.profileBackground);
        binding.inputName.setText(preferenceManager.getString(Constants.KEY_USERNAME));
        binding.inputEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));

    }

    private void setListeners() {
        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        binding.badgeImageView.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickProfileImage.launch(i);
        });

        binding.editProfileBackground.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickProfileBackgroundImage.launch(i);
        });

        binding.cancelButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        binding.saveButton.setOnClickListener(v -> {
            if (isValidData()) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {

        Map<String, Object> updates = new HashMap<>();
        List<Task<String>> uploadTasks = new ArrayList<>();

        if (imageProfile != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
            Date now = new Date();
            String fileName = formatter.format(now);

            Task<String> profileImageTask = saveProfileImageToStorage(imageProfile, fileName)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            imageProfileUrl = task.getResult();
                            updates.put(Constants.KEY_IMAGE_PROFILE, imageProfileUrl);
                        }
                    });

            uploadTasks.add(profileImageTask);
        }

        if (imageProfileBackground != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
            Date now = new Date();
            String fileName = formatter.format(now);

            Task<String> backgroundImageTask = saveProfileImageToStorage(imageProfileBackground, fileName)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            imageProfileBackgroundUrl = task.getResult();
                            updates.put(Constants.KEY_IMAGE_PROFILE_BACKGROUND, imageProfileBackgroundUrl);
                        }
                    });

            uploadTasks.add(backgroundImageTask);
        }

        Task<List<Object>> allUploadTasks = Tasks.whenAllSuccess(uploadTasks);

        allUploadTasks.continueWithTask(task -> {
            DocumentReference userRef = database.collection("users").document(preferenceManager.getString(Constants.KEY_USER_ID));

            updates.put(Constants.KEY_USERNAME, binding.inputName.getText().toString());
            updates.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());


            return userRef.update(updates);
        }).addOnSuccessListener(aVoid -> {
            preferenceManager.putString(Constants.KEY_USERNAME, binding.inputName.getText().toString());
            preferenceManager.putString(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
            if (imageProfileUrl != null) {
                preferenceManager.putString(Constants.KEY_IMAGE_PROFILE, imageProfileUrl);
            }
            if (imageProfileBackgroundUrl != null) {
                preferenceManager.putString(Constants.KEY_IMAGE_PROFILE_BACKGROUND, imageProfileBackgroundUrl);
            }



            new Handler().postDelayed(() -> {
                Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(i);
                finish();
            }, 2000);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed update profile", Toast.LENGTH_SHORT).show();

        });
    }


    private Task<String> saveProfileImageToStorage(Uri imageUri, String storageField) {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("profileImages/" + storageField + ".jpg");

        UploadTask uploadTask = imageRef.putFile(imageUri);

        TaskCompletionSource<String> tcs = new TaskCompletionSource<>();
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String imageUrl = downloadUri.toString();
                    tcs.setResult(imageUrl);
                } else {
                    tcs.setException(task.getException());
                }
            }
        });

        return tcs.getTask();
    }


    private final ActivityResultLauncher<Intent> pickProfileImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        imageProfile = imageUri;
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.userProfile.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
    );

    private final ActivityResultLauncher<Intent> pickProfileBackgroundImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        imageProfileBackground = imageUri;
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.profileBackground.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
    );

    private Boolean isValidData() {
        int redColor = ContextCompat.getColor(this, R.color.red_D400);
        if (binding.inputName.getText().toString().trim().isEmpty()) {
            binding.inputName.setHint("name cannot be empty");
            binding.inputName.setHintTextColor(redColor);
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            binding.inputName.setHint("email cannot be empty");
            binding.inputName.setHintTextColor(redColor);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            binding.inputName.setHint("please enter the appropriate email");
            binding.inputName.setHintTextColor(redColor);
            return false;
        } else {
            return true;
        }
    }





}
