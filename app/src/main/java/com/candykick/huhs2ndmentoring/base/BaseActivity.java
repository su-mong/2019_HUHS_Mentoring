package com.candykick.huhs2ndmentoring.base;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by candykick on 2019. 8. 18..
 */

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    protected B binding;
    protected Resources resources;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 화면 세로 고정
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // 상태바 글씨 검정
        }
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        resources = getResources();
    }

    public void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    public void makeToastLong(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    //로딩창 관련
    public void progressOn() {
        App.getGlobalApplicationContext().progressOn(this);
    }
    public void progressOff() {
        App.getGlobalApplicationContext().progressOff();
    }
}
