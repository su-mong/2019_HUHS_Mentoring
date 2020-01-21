package com.candykick.huhs2ndmentoring.voice;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityVoiceBinding;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

import java.security.MessageDigest;

public class VoiceActivity extends BaseActivity<ActivityVoiceBinding> {

    //텍스트->음성 변환 클라이언트 객체
    private TextToSpeechClient ttsClient;

    //텍스트->음성 변환 클라이언트 객체에 쓰이는 변수들
    private int voiceMakingProcess = 0; //음성합성방식. 0: 통계적 합성 방식, 1: 편집 합성 방식.
    private double speechSpeed = 1.0; //발음 속도. d
    private int speechVoice = 1; //목소리. 1: 여성 차분한 낭독체, 2: 여성 밝은 대화체, 3: 남성 차분한 낭독체, 4: 남성 밝은 대화체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);

        //음성인식 SDK 초기화
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        //텍스트->음성 라이브러리 초기화
        TextToSpeechManager.getInstance().initializeLibrary(VoiceActivity.this);


        //RadioButton(음성합성방식 선택하는 부분) 리스너
        binding.rbVoice1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    voiceMakingProcess = 0; //통계적 합성 방식으로 변경
                    binding.sbVoice1.setEnabled(true); //발음 속도 변경 가능하게 변경
                    binding.sbVoice1.setClickable(true); //발음 속도 변경 가능하게 변경
                }
            }
        });
        binding.rbVoice2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    voiceMakingProcess = 1; //편집 합성 방식으로 변경.
                    binding.sbVoice1.setEnabled(false); //발음 속도 변경 불가능하게 변경
                    binding.sbVoice1.setClickable(false); //발음 속도 변경 불가능하게 변경
                }
            }
        });

        //Seekbar(발음 속도 조정하는 부분) 리스너
        binding.sbVoice1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Seekbar는 0~100까지의 값을 가진다. 이를 0.5~4.0 사이의 값으로 바꿔야 한다.
                //발음 속도 값(speechSpeed)을 조정하고, 제목에 발음 속도 값을 표시한다.
                speechSpeed = Math.round((((double)progress / 100 * 3.5) + 0.5)*1000)/1000.0;
                binding.tvVoice4.setText("발음 속도: " + speechSpeed + "(0.5 ~ 4.0)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    //하단부 목소리 선택 버튼들 리스너. 1: 여성 차분한 낭독체, 2: 여성 밝은 대화체, 3: 남성 차분한 낭독체, 4: 남성 밝은 대화체
    //각 버튼에 맞게 목소리 변수를 설정하고,
    // 해당 버튼만 선택된 상태로 바꾼 뒤(버튼의 background를 button_blacklineselected로 변경)
    // 나머지 버튼은 선택되지 않은 상태로 바꾼다.(버튼의 background를 button_blackline으로 변경)
    public void btnWomanVoice1() {
        speechVoice = 1;
        binding.btnVoiceType1.setBackgroundResource(R.drawable.button_blacklineselected);
        binding.btnVoiceType2.setBackgroundResource(R.drawable.button_blackline);
        binding.btnVoiceType3.setBackgroundResource(R.drawable.button_blackline);
        binding.btnVoiceType4.setBackgroundResource(R.drawable.button_blackline);
    }
    public void btnWomanVoice2() {
        speechVoice = 2;
        binding.btnVoiceType1.setBackgroundResource(R.drawable.button_blackline);
        binding.btnVoiceType2.setBackgroundResource(R.drawable.button_blacklineselected);
        binding.btnVoiceType3.setBackgroundResource(R.drawable.button_blackline);
        binding.btnVoiceType4.setBackgroundResource(R.drawable.button_blackline);
    }
    public void btnManVoice1() {
        speechVoice = 3;
        binding.btnVoiceType1.setBackgroundResource(R.drawable.button_blackline);
        binding.btnVoiceType2.setBackgroundResource(R.drawable.button_blackline);
        binding.btnVoiceType3.setBackgroundResource(R.drawable.button_blacklineselected);
        binding.btnVoiceType4.setBackgroundResource(R.drawable.button_blackline);
    }
    public void btnManVoice2() {
        speechVoice = 4;
        binding.btnVoiceType1.setBackgroundResource(R.drawable.button_blackline);
        binding.btnVoiceType2.setBackgroundResource(R.drawable.button_blackline);
        binding.btnVoiceType3.setBackgroundResource(R.drawable.button_blackline);
        binding.btnVoiceType4.setBackgroundResource(R.drawable.button_blacklineselected);
    }

    //텍스트 -> 음성 변환 버튼
    public void btnVoice1() {
        if(binding.etVoice1.getText().toString().replace(" ","").length() == 0) {
            //아무것도 입력된 것이 없거나 공백(스페이스바)만 입력되어 있다면
            //텍스트를 입력해주세요!!란 메세지를 띄운다.
            makeToast("텍스트를 입력해주세요!!");
        } else {
            //입력된 값이 있다면
            //텍스트->음성 변환 클라이언트 객체 초기화한다.
            // voiceMakingProcess : 음성합성방식. 0(통계적 합성 방식), 1(편집 합성 방식).
            // speechSpeed : 발음 속도. 0.5~4.0까지 있다. 초기값은 1.0
            // speechVoice : 목소리. 1(여성 차분한 낭독체), 2(여성 밝은 대화체), 3(남성 차분한 낭독체), 4(남성 밝은 대화체)
            //참고로, 편집 합성 방식의 경우 발음 속도가 제대로 적용되지 않는다고 해서 이 방식을 선택한 경우 속도는 무조건 1.0으로 재생되게 해 놓았다.

            TextToSpeechClient.Builder builder = new TextToSpeechClient.Builder();
            switch (voiceMakingProcess) { //음성합성방식 설정
                case 0:
                    builder.setSpeechMode(TextToSpeechClient.NEWTONE_TALK_1);
                    builder.setSpeechSpeed(speechSpeed); //통계적 합성 방식의 경우 유저가 설정한 발음 속도를 적용.
                    break;
                case 1:
                    builder.setSpeechMode(TextToSpeechClient.NEWTONE_TALK_2);
                    builder.setSpeechSpeed(1.0); //편집 합성 방식의 경우 무조건 속도를 1.0으로 설정.
                    break;
            }
            switch (speechVoice) { //목소리 설정
                case 1:
                    builder.setSpeechVoice(TextToSpeechClient.VOICE_WOMAN_READ_CALM);
                    break;
                case 2:
                    builder.setSpeechVoice(TextToSpeechClient.VOICE_WOMAN_DIALOG_BRIGHT);
                    break;
                case 3:
                    builder.setSpeechVoice(TextToSpeechClient.VOICE_MAN_READ_CALM);
                    break;
                case 4:
                    builder.setSpeechVoice(TextToSpeechClient.VOICE_MAN_DIALOG_BRIGHT);
                    break;
            }
            builder.setListener(ttsListener); //오류 발생 시 동작 설정(해당 리스너는 위에 있다.)

            ttsClient = builder.build();

            //EditText에 입력된 텍스트 재생
            ttsClient.play(binding.etVoice1.getText().toString());
        }
    }

    // 맨 오른쪽 하단 질문? 버튼
    public void btnVoiceQuestion() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developers.kakao.com/docs/android/speech#시작하기"));
        startActivity(intent);
    }

    //텍스트->음성 변환 리스너
    private TextToSpeechListener ttsListener = new TextToSpeechListener() {
        @Override
        public void onFinished() {
        }

        @Override
        public void onError(int code, String message) {
            //에러가 발생한 경우
            //makeToast("에러가 발생했습니다: "+message);
            Log.e("HUHS Voice",message);
        }
    };

    @Override
    public void onDestroy() {
        //액티비티 종료 시 라이브러리 해제
        super.onDestroy();
        TextToSpeechManager.getInstance().finalizeLibrary();
        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_voice; }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
}