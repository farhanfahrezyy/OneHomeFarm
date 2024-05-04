package com.example.onehomefarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.onehomefarm.R;
import com.example.onehomefarm.databinding.ActivityWhistlistBinding;
import com.example.onehomefarm.fragments.HomeFragment;

public class WhistlistActivity extends AppCompatActivity {


    ActivityWhistlistBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWhistlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();

    }


    private void setListener(){
        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }


}