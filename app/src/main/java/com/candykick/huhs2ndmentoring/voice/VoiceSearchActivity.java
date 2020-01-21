package com.candykick.huhs2ndmentoring.voice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityVoiceSearchBinding;
import com.kakao.sdk.newtoneapi.SpeechRecognizerActivity;

import java.util.ArrayList;

public class VoiceSearchActivity extends BaseActivity<ActivityVoiceSearchBinding> {

    //네이버 검색 url.
    //이 url 뒤에 검색어를 붙여서 실행하면 검색이 된다.
    private static String NAVER_SEARCH_URL = "https://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query=";
    String naverSearchUrl = NAVER_SEARCH_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
    }


    //'음성으로 네이버 검색하기' 버튼 클릭 시
    public void bntVoiceSearch() {
        //추가한 음성 인식 Activity를 실행한다.
        Intent i = new Intent(VoiceSearchActivity.this, VoiceRecoActivity.class);
        i.putExtra(SpeechRecognizerActivity.EXTRA_KEY_API_KEY, "8b27ed5b92349aa05dffce8c281ef850"); //발급받은 API키를 넣는다.
        // apiKey는 신청과정을 통해 package와 매치되도록 발급받은 APIKey 문자열 값.
        startActivityForResult(i, 0);
    }


    //음성 인식 Activity에서 결과를 넘겨받아서 처리한다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { // 성공
            naverSearchUrl = NAVER_SEARCH_URL; //네이버 검색 URL 초기화

            ArrayList<String> results = data.getStringArrayListExtra(VoiceRecoActivity.EXTRA_KEY_RESULT_ARRAY); //결과값 모음
            Boolean marked = data.getBooleanExtra(VoiceRecoActivity.EXTRA_KEY_MARKED, true); //첫번째 값의 신뢰도가 현저하게 높은지 체

            //리스트에 결과값을 넣는다.
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(VoiceSearchActivity.this, android.R.layout.simple_list_item_1, results);
            binding.lvVoice1.setAdapter(adapter);
            binding.lvVoice1.setOnItemClickListener(new AdapterView.OnItemClickListener() { //리스트 클릭 리스너
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //리스트의 아이템을 클릭 시: 해당 아이템으로 네이버 검색을 수행한다.
                    naverSearchUrl += results.get(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(naverSearchUrl));
                    startActivity(intent);
                }
            });

            if(marked) { //첫번째 값의 신뢰도가 매우 높은 경우. 즉 marked == true인 경우.
                //'첫번째 값'으로 검색하시겠습니까?라는 알림창을 띄운다.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("음성인식 결과");
                builder.setMessage(results.get(0)+"로 검색하시겠습니까?");

                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                naverSearchUrl += results.get(0);
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(naverSearchUrl));
                                startActivity(intent);
                            }
                        }); //예 클릭 시 첫번째 값으로 네이버 검색을 수행한다.

                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}
                        }); //아니오 클릭 시 아무것도 하지 않고 알림창을 닫는다.

                builder.show(); //알림창을 보여준다.
            }
        }
        else if (requestCode == RESULT_CANCELED) { // 실패
            int errorCode = data.getIntExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_CODE, -1); //에러코드
            String errorMsg = data.getStringExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_MESSAGE); //에러 내용
            makeToastLong("오류가 발생했습니다: "+errorMsg); //오류의 내용을 메세지로 표시한다.
        }
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_voice_search; }
}
