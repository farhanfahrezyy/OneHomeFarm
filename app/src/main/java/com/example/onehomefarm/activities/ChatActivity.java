package com.example.onehomefarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.onehomefarm.R;
import com.example.onehomefarm.databinding.ActivityChatBinding;
import com.example.onehomefarm.fragments.ChatFragment;
import com.example.onehomefarm.fragments.ChatbotFragment;
import com.example.onehomefarm.fragments.HomeFragment;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Navbar();
    }

    private void Navbar(){

        LinearLayout layoutAhliPertanian = binding.layoutAhliPertanian;
        LinearLayout layoutAI = binding.layoutAI;

        TextView textAhliPertanian =binding.textAhliPertanian;
        TextView textAI  = binding.textAI;


        //set home fragment by default
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, ChatFragment.class, null)
                .commit();


        layoutAhliPertanian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab != 1) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ChatFragment.class, null)
                            .commit();

//                    DonateTxt.setVisibility(View.GONE);
//                    LovelistTxt.setVisibility(View.GONE);
//                    ProfileTxt.setVisibility(View.GONE);

                    //unselected
                    layoutAI.setBackgroundResource(R.drawable.bg_round_neutral);
                    textAI.setTextColor(getResources().getColor(R.color.gray));


//

                    // Selected home tab
                    textAhliPertanian.setTextColor(getResources().getColor(R.color.blue1));
//                    ImageHome.setImageResource(R.drawable.ic_home_selected);
                    layoutAhliPertanian.setBackgroundResource(R.drawable.bg_round_white);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    layoutAhliPertanian.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });

        layoutAI.setOnClickListener(new View.OnClickListener() {
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
                    textAhliPertanian.setTextColor(getResources().getColor(R.color.gray));
//                    ImageHome.setImageResource(R.drawable.ic_home_selected);
                    layoutAhliPertanian.setBackgroundResource(R.drawable.bg_round_neutral);



//                    HomeLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    LoveListLayout.setBackgroundResource(R.drawable.bg_round_50);
//                    ProfileLayout.setBackgroundResource(R.drawable.bg_round_50);

                    // Selected Donate tab
                    textAI.setTextColor(getResources().getColor(R.color.blue1));
//                    ImageHome.setImageResource(R.drawable.ic_home_selected);
                    layoutAI.setBackgroundResource(R.drawable.bg_round_white);
//                    DonateLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //Create Animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    layoutAI.startAnimation(scaleAnimation);

                    selectedTab =2;

                }
            }
        });

    }




}