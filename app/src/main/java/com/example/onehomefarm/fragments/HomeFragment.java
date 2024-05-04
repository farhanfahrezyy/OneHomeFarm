package com.example.onehomefarm.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.onehomefarm.R;
import com.example.onehomefarm.activities.AskActivity;
import com.example.onehomefarm.activities.ChatActivity;
import com.example.onehomefarm.activities.EduActivity;
import com.example.onehomefarm.activities.MadeActivity;
import com.example.onehomefarm.activities.NewsActivity;
import com.example.onehomefarm.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();

    }

    private void setListeners() {
        binding.FarmNews.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewsActivity.class);
            intent.putExtra("data", "Hello, World!");
            startActivity(intent);
        });

        binding.FarmAsk.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("data", "Hello, World!");
            startActivity(intent);
        });

        binding.MadeFarm.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MadeActivity.class);
            intent.putExtra("data", "Hello, World!");
            startActivity(intent);
        });

        binding.eduFarm.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EduActivity.class);
            intent.putExtra("data", "Hello, World!");
            startActivity(intent);
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        imageSlider();
        setListeners();


    }

    private void imageSlider() {
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide1, null, ScaleTypes.CENTER_INSIDE));
        binding.imageSlider2.setImageList(slideModels);

    }

}