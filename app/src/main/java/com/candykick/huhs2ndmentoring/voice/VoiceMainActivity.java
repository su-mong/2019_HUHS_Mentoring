package com.candykick.huhs2ndmentoring.voice;

import android.content.Intent;
import android.os.Bundle;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityVoiceMainBinding;

public class VoiceMainActivity extends BaseActivity<ActivityVoiceMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
    }

    public void btnGotoVoiceSearch() {
        Intent intent = new Intent(VoiceMainActivity.this, VoiceSearchActivity.class);
        startActivity(intent);
    }
    public void btnGotoMakingVoice() {
        Intent intent = new Intent(VoiceMainActivity.this, VoiceReActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_voice_main; }
}
