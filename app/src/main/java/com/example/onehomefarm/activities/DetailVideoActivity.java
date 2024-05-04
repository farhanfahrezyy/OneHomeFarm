package com.example.onehomefarm.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.extractor.Extractor;
import androidx.media3.ui.PlayerView;

import com.example.onehomefarm.R;
import com.example.onehomefarm.databinding.ActivityDetailVideoBinding;
import com.example.onehomefarm.fragments.HomeFragment;
import com.example.onehomefarm.utillities.PlayerUiController;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;


public class DetailVideoActivity extends AppCompatActivity {

    private ActivityDetailVideoBinding binding;

    private ExoPlayer player;

    private String videoUrl = "https://drive.google.com/drive/folders/1I0WevtrX0pJOcy10i-tqTq2yUBgW4NUd";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListener();


       YouTubePlayerView youTubePlayerView = binding.youtubePlayer;
       getLifecycle().addObserver(youTubePlayerView);
       youTubePlayerView.setEnableAutomaticInitialization(false);

       View controlsUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_controls);
        YouTubePlayerListener youTubePlayerListener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                PlayerUiController controller = new PlayerUiController(controlsUi, youTubePlayer, youTubePlayerView);
                youTubePlayer.addListener(controller);

                YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), "9K2-y1ih4j8", 0F);
            }
        };

        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();
        youTubePlayerView.initialize(youTubePlayerListener, options);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }


    private void setListener(){
        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }
}