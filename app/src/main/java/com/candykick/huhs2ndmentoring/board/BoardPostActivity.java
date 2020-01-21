package com.candykick.huhs2ndmentoring.board;

import android.content.Intent;
import android.os.Bundle;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityBoardPostBinding;

public class BoardPostActivity extends BaseActivity<ActivityBoardPostBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);

        Intent intent = getIntent();
        binding.tvPostTitle.setText(intent.getStringExtra("title"));
        binding.tvPostContents.setText(intent.getStringExtra("contents").replace("\\n",System.getProperty("line.separator")));
        binding.tvPostUsername.setText(intent.getStringExtra("user"));
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_board_post; }
}
