package com.example.onehomefarm.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onehomefarm.R;
import com.example.onehomefarm.activities.AskActivity;
import com.example.onehomefarm.activities.EditProfileActivity;
import com.example.onehomefarm.utillities.Constants;
import com.example.onehomefarm.utillities.ImageLoader;
import com.example.onehomefarm.utillities.PreferenceManager;
import com.example.onehomefarm.activities.SignInActivity;
import com.example.onehomefarm.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentProfileBinding binding;

    private PreferenceManager preferenceManager;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setListener(){
        binding.editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("data", "Hello, World!");
            startActivity(intent);
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        preferenceManager = new PreferenceManager(requireContext());
        // Inflate the layout for this fragment
        return binding.getRoot();



    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loadVerifiedStatus();
        loadProfile();
        setListener();

        binding.logout.setOnClickListener(v -> {
            // Hapus preferensi pengguna
            preferenceManager.clear();

            // Arahkan pengguna ke SignInActivity
            Intent i = new Intent(requireActivity(), SignInActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

    }

    private void loadProfile() {
        ImageLoader.loadCircleImage(preferenceManager.getString(Constants.KEY_IMAGE_PROFILE), binding.imageProfile);
        binding.ValueEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));
        binding.valueName.setText(preferenceManager.getString(Constants.KEY_USERNAME));

    }




}