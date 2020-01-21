package com.candykick.huhs2ndmentoring.chat;

import android.content.Intent;
import android.os.Bundle;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityChatLoginBinding;

public class ChatLoginActivity extends BaseActivity<ActivityChatLoginBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
    }

    public void entertoChat() {
        if(binding.etChatName.getText().toString().replace(" ","").length() == 0) {
            makeToast("닉네임을 입력해주세요.");
        } else if(binding.etChatName.getText().toString().length() < 2) {
            makeToast("닉네임은 2글자 이상이어야 합니다.");
        } else {
            Intent intent = new Intent(ChatLoginActivity.this, ChatActivity.class);
            intent.putExtra("name", binding.etChatName.getText().toString());
            startActivity(intent);
        }
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_chat_login; }
}
