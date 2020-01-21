package com.candykick.huhs2ndmentoring.drawing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityDrawingDialogBinding;

public class DrawingDialogActivity extends BaseActivity<ActivityDrawingDialogBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_drawing_dialog; }
}
