package com.example.onehomefarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.onehomefarm.R;
import com.example.onehomefarm.databinding.ActivityDetailNewsBinding;

public class DetailNewsActivity extends AppCompatActivity {

    ActivityDetailNewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}