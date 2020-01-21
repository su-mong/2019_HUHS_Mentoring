package com.candykick.huhs2ndmentoring.voice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.candykick.huhs2ndmentoring.R;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;

public class VoiceReActivity extends AppCompatActivity {

    //텍스트->음성 변환 클라이언트 객체
    private TextToSpeechClient ttsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_re);

        //음성인식 SDK 초기화
        SpeechRecognizerManager.getInstance().initializeLibrary(this);
        //텍스트->음성 라이브러리 초기화
        TextToSpeechManager.getInstance().initializeLibrary(VoiceReActivity.this);

        EditText editText = findViewById(R.id.editText1);
        Button button = findViewById(R.id.button1);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(VoiceReActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();

                TextToSpeechClient.Builder builder = new TextToSpeechClient.Builder();
                builder.setSpeechMode(TextToSpeechClient.NEWTONE_TALK_1);
                builder.setSpeechSpeed(1.0);
                builder.setSpeechVoice(TextToSpeechClient.VOICE_MAN_DIALOG_BRIGHT);
                builder.setListener(ttsListener); //오류 발생 시 동작 설정(해당 리스너는 위에 있다.)

                ttsClient = builder.build();

                //EditText에 입력된 텍스트 재생
                ttsClient.play(editText.getText().toString());
            }
        });
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
}