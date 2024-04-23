package com.example.onehomefarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.onehomefarm.R;
import com.example.onehomefarm.databinding.ActivityHomePageBinding;
import com.example.onehomefarm.fragments.ChatFragment;
import com.example.onehomefarm.fragments.ChatbotFragment;
import com.example.onehomefarm.fragments.HomeFragment;
import com.example.onehomefarm.fragments.KebunkuFragment;
import com.example.onehomefarm.fragments.ProfileFragment;

public class HomePageActivity extends AppCompatActivity {

    ActivityHomePageBinding binding;

    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Navbar();
    }

    private void Navbar(){

        LinearLayout HomeLayout = binding.HomeLayout;
        LinearLayout ChatLayout = binding.ChatLayout;
        LinearLayout KebunLayout= binding.KebunLayout;
        LinearLayout ProfileLayout = binding.ProfileLayout;

        ImageView ImageHome = binding.beranda;
        ImageView ImageChat = binding.chat;
        ImageView ImageKebun = binding.stat;
        ImageView ImageProfile = binding.profile;

        TextView HomeTxt = binding.berandaTxt;
        TextView ChatTxt = binding.chatTxt;
        TextView KebunTxt = binding.stattxt;
        TextView ProfileTxt = binding.profiletxt;


        //set home fragment by default
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, HomeFragment.class, null)
                .commit();

        HomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 1) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HomeFragment.class, null)
                            .commit();

//                    DonateTxt.setVisibility(View.GONE);
//                    LovelistTxt.setVisibility(View.GONE);
//                    ProfileTxt.setVisibility(View.GONE);
//
                    ImageChat.setImageResource(R.drawable.ic_chat_unselected);
                    ImageKebun.setImageResource(R.drawable.ic_stat_unselected);
                    ImageProfile.setImageResource(R.drawable.ic_profile_unselected);
//
//                    DonateLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    LoveListLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    ProfileLayout.setBackgroundResource(R.drawable.bg_round_50);

                    // Selected home tab
                    HomeTxt.setVisibility(View.VISIBLE);
                    ImageHome.setImageResource(R.drawable.ic_home_selected);
//                    HomeLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    HomeLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });

        ChatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 2) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ChatbotFragment.class, null)
                            .commit();

//                    HomeTxt.setVisibility(View.GONE);
//                    LovelistTxt.setVisibility(View.GONE);
//                    ProfileTxt.setVisibility(View.GONE);

                    ImageHome.setImageResource(R.drawable.ic_home_unselected);
                    ImageKebun.setImageResource(R.drawable.ic_stat_unselected);
                    ImageProfile.setImageResource(R.drawable.ic_profile_unselected);

//                    HomeLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    LoveListLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    ProfileLayout.setBackgroundResource(R.drawable.bg_round_50);

                    // Selected Donate tab
                    ChatTxt.setVisibility(View.VISIBLE);
                    ImageChat.setImageResource(R.drawable.ic_chat_selected);
//                    DonateLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    ChatLayout.startAnimation(scaleAnimation);

                    selectedTab =2;

                }
            }
        });

        KebunLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 3) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, KebunkuFragment.class, null)
                            .commit();

//                    HomeTxt.setVisibility(View.GONE);
//                    DonateTxt.setVisibility(View.GONE);
//                    ProfileTxt.setVisibility(View.GONE);

                    ImageHome.setImageResource(R.drawable.ic_home_unselected);
                    ImageChat.setImageResource(R.drawable.ic_chat_unselected);
                    ImageProfile.setImageResource(R.drawable.ic_profile_unselected);

//                    HomeLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    DonateLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    ProfileLayout.setBackgroundResource(R.drawable.bg_round_50);

                    // Selected Kebunku tab
                    KebunTxt.setVisibility(View.VISIBLE);
                    ImageKebun.setImageResource(R.drawable.ic_stat_selected);
//                  KebunLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    KebunLayout.startAnimation(scaleAnimation);

                    selectedTab =3;
                }
            }
        });

        ProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 4) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ProfileFragment.class, null)
                            .commit();
//                    HomeTxt.setVisibility(View.GONE);
//                    LovelistTxt.setVisibility(View.GONE);
//                    DonateTxt.setVisibility(View.GONE);GONE

                    ImageHome.setImageResource(R.drawable.ic_home_unselected);
                    ImageChat.setImageResource(R.drawable.ic_chat_unselected);
                    ImageKebun.setImageResource(R.drawable.ic_stat_unselected);

//                    HomeLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    ChatLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    DonateLayout.setBackgroundResource(R.drawable.bg_round_50);

                    // Selected profile tab
                    ProfileTxt.setVisibility(View.VISIBLE);
                    ImageProfile.setImageResource(R.drawable.ic_profile_selected);
//                    ProfileLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    ProfileLayout.startAnimation(scaleAnimation);


                    selectedTab = 4;
                }
            }
        });


    }
}