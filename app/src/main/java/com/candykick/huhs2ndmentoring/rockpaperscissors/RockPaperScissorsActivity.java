package com.candykick.huhs2ndmentoring.rockpaperscissors;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityRockPaperScissorsBinding;

import java.util.Random;

public class RockPaperScissorsActivity extends BaseActivity<ActivityRockPaperScissorsBinding> {

    static int imageResource[] = {R.drawable.scissors, R.drawable.rock, R.drawable.paper};
    static int imagechanger = 0;
    static int time = 61;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        binding.setActivity(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void btnScissors() {
        binding.ivRPSLeft.setImageResource(imageResource[0]);
        mainHandler.removeMessages(0);
        imageHandler.removeMessages(0);

        int computerResult = new Random().nextInt(3);
        switch (computerResult) {
            case 0:
                binding.ivRPSRight.setImageResource(imageResource[0]);
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("DRAW");
                break;
            case 1:
                binding.ivRPSRight.setImageResource(imageResource[1]);
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("YOU LOSE ㅠㅠ");
                score -= 100;
                binding.tvRPSScore.setText("점수: "+score);
                break;
            case 2:
                binding.ivRPSRight.setImageResource(imageResource[2]);
                score += 100;
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("YOU WIN ^.^");
                binding.tvRPSScore.setText("점수: "+score);
                break;
        }
    }

    public void btnRock() {
        binding.ivRPSLeft.setImageResource(imageResource[1]);
        mainHandler.removeMessages(0);
        imageHandler.removeMessages(0);

        int computerResult = new Random().nextInt(3);
        switch (computerResult) {
            case 0:
                binding.ivRPSRight.setImageResource(imageResource[0]);
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("YOU WIN ^.^");
                score += 100;
                binding.tvRPSScore.setText("점수: "+score);
                break;
            case 1:
                binding.ivRPSRight.setImageResource(imageResource[1]);
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("DRAW");
                break;
            case 2:
                binding.ivRPSRight.setImageResource(imageResource[2]);
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("YOU LOSE ㅠㅠ");
                score -= 100;
                binding.tvRPSScore.setText("점수: "+score);
                break;
        }
    }

    public void btnPaper() {
        binding.ivRPSLeft.setImageResource(imageResource[2]);
        mainHandler.removeMessages(0);
        imageHandler.removeMessages(0);

        int computerResult = new Random().nextInt(3);
        switch (computerResult) {
            case 0:
                binding.ivRPSRight.setImageResource(imageResource[0]);
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("YOU LOSE ㅠㅠ");
                score -= 100;
                binding.tvRPSScore.setText("점수: "+score);
                break;
            case 1:
                binding.ivRPSRight.setImageResource(imageResource[1]);
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("YOU WIN ^.^");
                score += 100;
                binding.tvRPSScore.setText("점수: "+score);
                break;
            case 2:
                binding.ivRPSRight.setImageResource(imageResource[2]);
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("DRAW");
                break;
        }
    }

    public void btnRegame() {
        time = 61;

        binding.llRPS.setVisibility(View.GONE);
        binding.tvRPSVersus.setVisibility(View.VISIBLE);
        binding.btnRPSStart.setText("다시하기");

        mainHandler.sendEmptyMessageDelayed(0,1000);
        imageHandler.sendEmptyMessageDelayed(0,100);
    }

    Handler mainHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mainHandler.sendEmptyMessageDelayed(0,1000);
            time--;
            binding.tvRPSTime.setText(String.valueOf(time));

            if(time == 0) {
                binding.tvRPSTime.setText(String.valueOf(time));
                mainHandler.removeMessages(0);
                imageHandler.removeMessages(0);
                binding.llRPS.setVisibility(View.VISIBLE);
                binding.tvRPSVersus.setVisibility(View.GONE);
                binding.tvRPSGameResult.setText("YOU LOSE ㅠㅠ");
                score -= 100;
                binding.tvRPSScore.setText("점수: "+score);
            }
        }
    };

    Handler imageHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            imageHandler.sendEmptyMessageDelayed(0,100);
            binding.ivRPSRight.setImageResource(imageResource[imagechanger%3]);
            binding.ivRPSLeft.setImageResource(imageResource[(imagechanger+1)%3]);
            imagechanger++;
        }
    };

    @Override
    protected int getLayoutId() { return R.layout.activity_rock_paper_scissors; }
}