package com.example.onehomefarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.onehomefarm.utillities.Constants;
import com.example.onehomefarm.adapters.CommunityAdapter;
import com.example.onehomefarm.databinding.ActivityAskBinding;
import com.example.onehomefarm.models.ChatMessage;
import com.example.onehomefarm.utillities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AskActivity extends AppCompatActivity {
    private ActivityAskBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> chatMessages;
    private CommunityAdapter communityAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        listenMessages();
        setListeners();
    }

    private void init() {
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        preferenceManager.getString(Constants.KEY_USER_ID);
        communityAdapter = new CommunityAdapter(
                chatMessages,
                preferenceManager.getString(Constants.KEY_USER_ID)
        );
        binding.communityRecyclerView.setAdapter(communityAdapter);
        database = FirebaseFirestore.getInstance();

    }

    private void listenMessages() {
        database.collection(Constants.KEY_COLLECTION_COMMUNITY)
                .addSnapshotListener(eventListener);
    }

    @SuppressLint("NotifyDataSetChanged")
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null) {
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        if(value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chats = new ChatMessage();
                    chats.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
//                    chats.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chats.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chats.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chats.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);

                    Toast.makeText(this, documentChange.getDocument().getString(Constants.KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                    chatMessages.add(chats);
                }
            }
            chatMessages.sort(Comparator.comparing(obj -> obj.dateObject));
            if (count == 0) {
                communityAdapter.notifyDataSetChanged();
            } else {
                communityAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.communityRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
        }
    };

    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
    }

    private void setListeners() {
        binding.layoutSend.setOnClickListener(v -> sendMessage());

        binding.backButton.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void sendMessage() {
        if (!binding.inputMessage.getText().toString().isEmpty()) {
            String messageValue = binding.inputMessage.getText().toString();
            binding.inputMessage.setText(null);
            HashMap<String, Object> message = new HashMap<>();
            message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            message.put(Constants.KEY_MESSAGE, messageValue);
            message.put(Constants.KEY_TIMESTAMP, new Date());
            database.collection(Constants.KEY_COLLECTION_COMMUNITY).add(message);
        }
    }
}