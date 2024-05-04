package com.example.onehomefarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.onehomefarm.R;
import com.example.onehomefarm.databinding.ActivityMadeBinding;
import com.example.onehomefarm.databinding.FragmentHomeBinding;
import com.example.onehomefarm.fragments.HomeFragment;

public class MadeActivity extends AppCompatActivity {

    ActivityMadeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMadeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();


    }

    private void setListener(){
        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}