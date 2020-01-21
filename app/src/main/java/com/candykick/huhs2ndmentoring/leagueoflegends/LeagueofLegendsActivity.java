package com.candykick.huhs2ndmentoring.leagueoflegends;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityLeagueofLegendsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LeagueofLegendsActivity extends BaseActivity<ActivityLeagueofLegendsBinding> {

    int userLevel1, userLevel2;
    int profileIcon1, profileIcon2;
    String userId1, userId2;
    String userName1, userName2;

    String riot_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);

        FirebaseFirestore.getInstance().collection("Riotkey").document("key")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                riot_key = documentSnapshot.get("key").toString();
            }
        });
    }

    //VS 버튼 클릭 시
    public void gotoVS() {
        new SearchUserId1().execute();
    }

    private class SearchUserId1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .addHeader("X-Riot-Token",riot_key)
                    .url("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+binding.etLolSummorName1.getText().toString())
                    .build();

            try { //정상적으로 작동한 경우 결과값 json을 셋팅한다.
                Response response = client.newCall(request).execute();

                result = response.body().string();
            } catch (Exception e) { //오류가 발생한 경우 오류 내용을 셋팅한다.
                result = e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                userName1 = jsonObject.getString("name");
                userId1 = jsonObject.getString("id");
                userLevel1 = jsonObject.getInt("summonerLevel");
                profileIcon1 = jsonObject.getInt("profileIconId");

                new SearchUserId2().execute();
            } catch (Exception e) {
                makeToastLong("오류가 발생했습니다: "+e.getMessage());
            }
        }
    }

    private class SearchUserId2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .addHeader("X-Riot-Token",riot_key)
                    .url("https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+binding.etLolSummorName2.getText().toString())
                    .build();

            try { //정상적으로 작동한 경우 결과값 json을 셋팅한다.
                Response response = client.newCall(request).execute();

                result = response.body().string();
            } catch (Exception e) { //오류가 발생한 경우 오류 내용을 셋팅한다.
                result = e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                userName2 = jsonObject.getString("name");
                userId2 = jsonObject.getString("id");
                userLevel2 = jsonObject.getInt("summonerLevel");
                profileIcon2 = jsonObject.getInt("profileIconId");

                Intent intent = new Intent(LeagueofLegendsActivity.this, LeagueofLegendsActivity2.class);
                intent.putExtra("riot_key",riot_key);
                intent.putExtra("userName1",userName1);
                intent.putExtra("userLevel1",userLevel1);
                intent.putExtra("profileIcon1",profileIcon1);
                intent.putExtra("userId1",userId1);
                intent.putExtra("userName2",userName2);
                intent.putExtra("userLevel2",userLevel2);
                intent.putExtra("profileIcon2",profileIcon2);
                intent.putExtra("userId2",userId2);
                startActivity(intent);
            } catch (Exception e) {
                makeToastLong("오류가 발생했습니다: "+e.getMessage());
            }
        }
    }


    @Override
    protected int getLayoutId() { return R.layout.activity_leagueof_legends; }
}
