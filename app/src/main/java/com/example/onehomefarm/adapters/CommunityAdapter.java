package com.example.onehomefarm.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.onehomefarm.databinding.ItemContainerReceivedMessageBinding;
import com.example.onehomefarm.databinding.ItemContainerSentMessageBinding;
import com.example.onehomefarm.models.ChatMessage;
import com.example.onehomefarm.utillities.Constants;
import com.example.onehomefarm.utillities.ImageLoader;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ChatMessage> chatMessages;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public CommunityAdapter(List<ChatMessage> chatMessages, String senderId) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT) {
            return new CommunityAdapter.SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new CommunityAdapter.ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT) {
            CommunityAdapter.SentMessageViewHolder sentHolder = (CommunityAdapter.SentMessageViewHolder) holder;
            ChatMessage chatMessage = chatMessages.get(position);
            sentHolder.setData(chatMessage);
        } else {
            CommunityAdapter.ReceivedMessageViewHolder receivedHolder = (CommunityAdapter.ReceivedMessageViewHolder) holder;
            ChatMessage chatMessage = chatMessages.get(position);
            receivedHolder.setData(chatMessage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            if(chatMessage.getSenderId() != null) {
                DocumentReference userDocument = database.collection("users").document(chatMessage.getSenderId());
                userDocument.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ImageLoader.loadCircleImage(documentSnapshot.getString(Constants.KEY_IMAGE_PROFILE), binding.photoImage);
                        binding.textMessage.setText(chatMessage.message);
                        binding.textDateTime.setText(chatMessage.dateTime);
                    } else {
                        Log.d("Firestore", "Dokumen tidak ditemukan");
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Firestore", "Gagal mengambil data", e);
                });
            }
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding) {
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            if(chatMessage.getSenderId() != null) {
                DocumentReference userDocument = database.collection("users").document(chatMessage.getSenderId());
                userDocument.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ImageLoader.loadCircleImage(documentSnapshot.getString(Constants.KEY_IMAGE_PROFILE), binding.photoImage);
                        binding.textMessage.setText(chatMessage.message);
                        binding.textDateTime.setText(chatMessage.dateTime);
                    } else {
                        Log.d("Firestore", "Dokumen tidak ditemukan");
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Firestore", "Gagal mengambil data", e);
                });
            }
        }
    }
}
