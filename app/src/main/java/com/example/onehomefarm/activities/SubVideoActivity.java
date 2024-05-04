package com.example.onehomefarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.onehomefarm.R;
import com.example.onehomefarm.databinding.ActivitySubVideoBinding;
import com.example.onehomefarm.fragments.HomeFragment;

public class SubVideoActivity extends AppCompatActivity {

    ActivitySubVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();
    }


    private void setListener(){
        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}