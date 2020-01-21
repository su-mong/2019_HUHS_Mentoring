package com.candykick.huhs2ndmentoring.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityChatBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends BaseActivity<ActivityChatBinding> {

    String userName;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    ChattingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);

        Intent intent = getIntent();
        userName = intent.getStringExtra("name");

        adapter = new ChattingAdapter(ChatActivity.this, userName);
        binding.lvChatting.setAdapter(adapter);

        reference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addMessage(dataSnapshot, adapter);
                binding.llChattingNoChat.setVisibility(View.GONE);
                binding.lvChatting.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //메세지 전송 버튼 클릭 시
    public void btnSend() {
        String message = binding.etChatting.getText().toString();
        String messageTest = message.replace(" ","");

        if(messageTest.length() == 0) { //띄어쓰기를 제외한 글자가 있는지 검사. 아무것도 안 쓰거나, 띄어쓰기만 겁나게 쓰는 경우 메세지 전송 X.
            makeToast("메세지를 입력해주세요.");
            binding.etChatting.setText("");
        } else {
            ChattingData chat = new ChattingData(userName, message);
            reference.child("chat").push().setValue(chat);
            binding.etChatting.setText("");
        }
    }

    private void addMessage(DataSnapshot snapshot, ChattingAdapter adapter) {
        ChattingData chat = snapshot.getValue(ChattingData.class);
        binding.lvChatting.setSelection(adapter.getCount() - 1);
        adapter.addData(chat);
    }

    private void removeMessage(DataSnapshot snapshot, ChattingAdapter adapter) {
        ChattingData chat = snapshot.getValue(ChattingData.class);
        adapter.removeData(chat);
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_chat; }
}
