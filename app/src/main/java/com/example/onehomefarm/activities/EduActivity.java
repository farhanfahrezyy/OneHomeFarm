package com.example.onehomefarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.onehomefarm.R;
import com.example.onehomefarm.databinding.ActivityEduBinding;
import com.example.onehomefarm.fragments.HomeFragment;

public class EduActivity extends AppCompatActivity {

    private ActivityEduBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEduBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void  setListener(){

        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        binding.whistlist.setOnClickListener(v -> {
            Intent intent = new Intent(EduActivity.this, DetailVideoActivity.class);
            intent.putExtra("data", "Hello, World!");
            startActivity(intent);
        });





    }

}