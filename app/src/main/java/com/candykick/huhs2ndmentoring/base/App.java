package com.candykick.huhs2ndmentoring.base;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;

import com.candykick.huhs2ndmentoring.R;

/**
 * Created by candykick on 2019. 8. 18..
 */

public class App extends Application {
    private static volatile App instance = null;
    private static volatile Activity currentActivity = null;

    AppCompatDialog progressDialog;

    private int windowWidth;
    private int windowHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        App.currentActivity = currentActivity;
    }

    public static App getGlobalApplicationContext() {
        if(instance == null) {
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    //로딩창 켜기
    public void progressOn(Activity activity) {

        if(activity == null || activity.isFinishing()) {
            return;
        }

        if(progressDialog != null && progressDialog.isShowing()) {

        } else {
            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.dialog_loading);
            progressDialog.show();
        }
    }

    //로딩창 끄기
    public void progressOff() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
