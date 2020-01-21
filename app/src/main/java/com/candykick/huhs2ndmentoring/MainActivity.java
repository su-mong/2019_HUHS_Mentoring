package com.candykick.huhs2ndmentoring;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.board.BoardListActivity;
import com.candykick.huhs2ndmentoring.calculator.CalculatorActivity;
import com.candykick.huhs2ndmentoring.chat.ChatLoginActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityMainBinding;
import com.candykick.huhs2ndmentoring.drawing.DrawingActivity;
import com.candykick.huhs2ndmentoring.leagueoflegends.LeagueofLegendsActivity;
import com.candykick.huhs2ndmentoring.map.MapActivity;
import com.candykick.huhs2ndmentoring.rockpaperscissors.RockPaperScissorsActivity;
import com.candykick.huhs2ndmentoring.voice.VoiceActivity;
import com.candykick.huhs2ndmentoring.voice.VoiceMainActivity;
import com.candykick.huhs2ndmentoring.voice.VoiceSearchActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.setActivity(this);

        SharedPreferences pref = getSharedPreferences("firstexe", MODE_PRIVATE);
        if (pref.getBoolean("firstexe", true)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstexe", false);
            editor.commit();

            PermissionListener permissionListener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(MainActivity.this, "권한 허용을 해 주셔야 정상적인 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            };

            TedPermission.with(this)
                    .setPermissionListener(permissionListener)
                    .setRationaleMessage("앱의 정상적인 이용을 위해 권한 설정이 필요합니다.")
                    .setDeniedMessage("[설정] > [권한]에서 권한을 허용할 수 있습니다.")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        }
    }

    public void btnMain1() {
        Intent intent = new Intent(MainActivity.this, RockPaperScissorsActivity.class);
        startActivity(intent);
    }
    public void btnMain2() {
        Intent intent = new Intent(MainActivity.this, VoiceMainActivity.class);
        startActivity(intent);
    }
    public void btnMain3() {
        Intent intent = new Intent(MainActivity.this, LeagueofLegendsActivity.class);
        startActivity(intent);
    }
    public void btnMain4() {
        Intent intent = new Intent(MainActivity.this, DrawingActivity.class);
        startActivity(intent);
    }
    public void btnMain5() {
        Intent intent = new Intent(MainActivity.this, ChatLoginActivity.class);
        startActivity(intent);
    }
    public void btnMain6() {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }
    public void btnMain7() {
        Intent intent = new Intent(MainActivity.this, BoardListActivity.class);
        startActivity(intent);
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_main; }
}
