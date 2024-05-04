package com.example.onehomefarm.fragments;

import android.annotation.SuppressLint;
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
import com.example.onehomefarm.activities.SignInActivity;
import com.example.onehomefarm.adapters.CommunityAdapter;
import com.example.onehomefarm.databinding.FragmentChatBinding;
import com.example.onehomefarm.models.ChatMessage;
import com.example.onehomefarm.utillities.Constants;
import com.example.onehomefarm.utillities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
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


public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FragmentChatBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> chatMessages;
    private CommunityAdapter communityAdapter;
    private FirebaseFirestore database;



   private void init(){
       preferenceManager = new PreferenceManager(requireContext());
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
//            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
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

//                    Toast.makeText(this, documentChange.getDocument().getString(Constants.KEY_MESSAGE), Toast.LENGTH_SHORT).show();

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
            requireActivity().onBackPressed();
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentChatBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        loadVerifiedStatus();
        init(); // Panggil init() sebelum sendMessage()
        setListeners();
        listenMessages();
        sendMessage();




    }
}