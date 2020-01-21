package com.candykick.huhs2ndmentoring.leagueoflegends;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityLeagueofLegends2Binding;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LeagueofLegendsActivity2 extends BaseActivity<ActivityLeagueofLegends2Binding> {

    static String PROFILE_IMG_URL = "http://ddragon.leagueoflegends.com/cdn/9.3.1/img/profileicon/";

    String riot_key;

    int userLevel1, userLevel2;
    int profileIcon1, profileIcon2;
    String userId1, userId2;
    String userName1, userName2;

    boolean user1SoloRank = false; boolean user2SoloRank = false;
    int userWin1, userWin2, userLose1, userLose2;
    int userRankPoint1, userRankPoint2;
    String userRank1, userRank2, userTier1, userTier2;

    boolean user1TeamRank = false; boolean user2TeamRank = false;
    int userWinTR1, userWinTR2, userLoseTR1, userLoseTR2;
    int userRankPointTR1, userRankPointTR2;
    String userRankTR1, userRankTR2, userTierTR1, userTierTR2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);

        Intent intent = getIntent();
        riot_key = intent.getStringExtra("riot_key");
        userName1 = intent.getStringExtra("userName1");
        userLevel1 = intent.getIntExtra("userLevel1",0);
        profileIcon1 = intent.getIntExtra("profileIcon1",0);
        userId1 = intent.getStringExtra("userId1");
        userName2 = intent.getStringExtra("userName2");
        userLevel2 = intent.getIntExtra("userLevel2",0);
        profileIcon2 = intent.getIntExtra("profileIcon2",0);
        userId2 = intent.getStringExtra("userId2");

        binding.tvLolName1.setText(userName1); binding.tvLolLevel1.setText("Level: "+userLevel1);
        binding.tvLolName2.setText(userName2); binding.tvLolLevel2.setText("Level: "+userLevel2);

        if(profileIcon1 <= 4016)
            Glide.with(LeagueofLegendsActivity2.this).load(PROFILE_IMG_URL+profileIcon1+".png").into(binding.ivLolProfile1);
        else
            binding.ivLolProfile1.setBackgroundColor(0);

        if(profileIcon2 <= 4016)
            Glide.with(LeagueofLegendsActivity2.this).load(PROFILE_IMG_URL+profileIcon2+".png").into(binding.ivLolProfile2);
        else
            binding.ivLolProfile2.setBackgroundColor(0);

        new SearchRank1().execute();
    }

    private class SearchRank1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;

            OkHttpClient client = new OkHttpClient();

            /*HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("kr.api.riotgames.com")
                    .addPathSegment("lol/league/v4/entries/by-summoner/"+userId1)
                    .build();*/

            Request request = new Request.Builder()
                    .addHeader("X-Riot-Token",riot_key)
                    .url("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/"+userId1)
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
            if(result.equals("[]")) {
                binding.tvLolRank1.setText("UNRANK");
                binding.tvLolPoint1.setText("0 LP");
                binding.tvLolWinLose1.setText("0 Wins / 0 Losses");
                binding.tvLolWinRate1.setText("");
                setTierImage(binding.ivLolTier1, "UNRANK");

                new SearchRank2().execute();
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String queueType = jsonObject.getString("queueType");
                        if(queueType.equals("RANKED_SOLO_5x5")) {
                            user1SoloRank = true;
                            userWin1 = jsonObject.getInt("wins");
                            userLose1 = jsonObject.getInt("losses");
                            userRank1 = jsonObject.getString("rank");
                            userTier1 = jsonObject.getString("tier");
                            userRankPoint1 = jsonObject.getInt("leaguePoints");
                        } else if(queueType.equals("RANKED_FLEX_SR")) {
                            user1TeamRank = true;
                            userWinTR1 = jsonObject.getInt("wins");
                            userLoseTR1 = jsonObject.getInt("losses");
                            userRankTR1 = jsonObject.getString("rank");
                            userTierTR1 = jsonObject.getString("tier");
                            userRankPointTR1 = jsonObject.getInt("leaguePoints");
                        }
                    }

                    if(user1SoloRank) {
                        binding.tvLolRank1.setText(userTier1 + " " + userRank1);
                        binding.tvLolPoint1.setText(userRankPoint1 + " LP");
                        binding.tvLolWinLose1.setText(userWin1 + " Wins / " + userLose1 + " Losses");
                        binding.tvLolWinRate1.setText(((userWin1*100)/(userWin1 + userLose1)) + "%");
                        setTierImage(binding.ivLolTier1, userTier1 + " " + userRank1);
                    } else if(user1TeamRank) {
                        binding.tvLolRank1.setText(userTierTR1 + " " + userRankTR1);
                        binding.tvLolPoint1.setText(userRankPointTR1 + " LP");
                        binding.tvLolWinLose1.setText(userWinTR1 + " Wins / " + userLoseTR1 + " Losses");
                        binding.tvLolWinRate1.setText(((userWinTR1*100) / (userWinTR1 + userLoseTR1)) + "%");
                        setTierImage(binding.ivLolTier1, userTierTR1 + " " + userRankTR1);
                    } else {
                        binding.tvLolRank1.setText("UNRANK");
                        binding.tvLolPoint1.setText("0 LP");
                        binding.tvLolWinLose1.setText("0 Wins / 0 Losses");
                        binding.tvLolWinRate1.setText("");
                        setTierImage(binding.ivLolTier1, "UNRANK");
                    }

                    new SearchRank2().execute();
                } catch (Exception e) {
                    makeToastLong("오류가 발생했습니다: " + e.getMessage());
                }
            }
        }
    }

    private class SearchRank2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result;

            OkHttpClient client = new OkHttpClient();

            /*HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("kr.api.riotgames.com")
                    .addPathSegment("lol/league/v4/entries/by-summoner/"+userId2)
                    .build();*/

            Request request = new Request.Builder()
                    .addHeader("X-Riot-Token",riot_key)
                    .url("https://kr.api.riotgames.com/lol/league/v4/entries/by-summoner/"+userId2)
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
            if (result.equals("[]")) {
                binding.tvLolRank2.setText("UNRANK");
                binding.tvLolPoint2.setText("0 LP");
                binding.tvLolWinLose2.setText("0 Wins / 0 Losses");
                binding.tvLolWinRate2.setText("");
                setTierImage(binding.ivLolTier2, "UNRANK");
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String queueType = jsonObject.getString("queueType");
                        if(queueType.equals("RANKED_SOLO_5x5")) {
                            user2SoloRank = true;
                            userWin2 = jsonObject.getInt("wins");
                            userLose2 = jsonObject.getInt("losses");
                            userRank2 = jsonObject.getString("rank");
                            userTier2 = jsonObject.getString("tier");
                            userRankPoint2 = jsonObject.getInt("leaguePoints");
                        } else if(queueType.equals("RANKED_FLEX_SR")) {
                            user2TeamRank = true;
                            userWinTR2 = jsonObject.getInt("wins");
                            userLoseTR2 = jsonObject.getInt("losses");
                            userRankTR2 = jsonObject.getString("rank");
                            userTierTR2 = jsonObject.getString("tier");
                            userRankPointTR2 = jsonObject.getInt("leaguePoints");
                        }
                    }

                    if(user2SoloRank) {
                        binding.tvLolRank2.setText(userTier2 + " " + userRank2);
                        binding.tvLolPoint2.setText(userRankPoint2 + " LP");
                        binding.tvLolWinLose2.setText(userWin2 + " Wins / " + userLose2 + " Losses");
                        binding.tvLolWinRate2.setText(((userWin2*100) / (userWin2 + userLose2)) + "%");
                        setTierImage(binding.ivLolTier2, userTier2 + " " + userRank2);
                    } else if(user2TeamRank) {
                        binding.tvLolRank2.setText(userTierTR2 + " " + userRankTR2);
                        binding.tvLolPoint2.setText(userRankPointTR2 + " LP");
                        binding.tvLolWinLose2.setText(userWinTR2 + " Wins / " + userLoseTR2 + " Losses");
                        binding.tvLolWinRate2.setText(((userWinTR2*100) / (userWinTR2 + userLoseTR2)) + "%");
                        setTierImage(binding.ivLolTier2, userTierTR2 + " " + userRankTR2);
                    } else {
                        binding.tvLolRank2.setText("UNRANK");
                        binding.tvLolPoint2.setText("0 LP");
                        binding.tvLolWinLose2.setText("0 Wins / 0 Losses");
                        binding.tvLolWinRate2.setText("");
                        setTierImage(binding.ivLolTier2, "UNRANK");
                    }
                } catch (Exception e) {
                    makeToastLong("오류가 발생했습니다: " + e.getMessage());
                }
            }
        }
    }

    private void setTierImage(ImageView tierImage, String tier) {
        switch (tier) {
            case "CHALLENGER I":
                tierImage.setImageResource(R.drawable.lol_challenger_1);
                break;
            case "GRANDMASTER I":
                tierImage.setImageResource(R.drawable.lol_grandmaster_1);
                break;
            case "MASTER I":
                tierImage.setImageResource(R.drawable.lol_master_1);
                break;
            case "DIAMOND I":
                tierImage.setImageResource(R.drawable.lol_diamond_1);
                break;
            case "DIAMOND II":
                tierImage.setImageResource(R.drawable.lol_diamond_2);
                break;
            case "DIAMOND III":
                tierImage.setImageResource(R.drawable.lol_diamond_3);
                break;
            case "DIAMOND IV":
                tierImage.setImageResource(R.drawable.lol_diamond_4);
                break;
            case "PLATINUM I":
                tierImage.setImageResource(R.drawable.lol_platinum_1);
                break;
            case "PLATINUM II":
                tierImage.setImageResource(R.drawable.lol_platinum_2);
                break;
            case "PLATINUM III":
                tierImage.setImageResource(R.drawable.lol_platinum_3);
                break;
            case "PLATINUM IV":
                tierImage.setImageResource(R.drawable.lol_platinum_4);
                break;
            case "GOLD I":
                tierImage.setImageResource(R.drawable.lol_gold_1);
                break;
            case "GOLD II":
                tierImage.setImageResource(R.drawable.lol_gold_2);
                break;
            case "GOLD III":
                tierImage.setImageResource(R.drawable.lol_gold_3);
                break;
            case "GOLD IV":
                tierImage.setImageResource(R.drawable.lol_gold_4);
                break;
            case "SILVER I":
                tierImage.setImageResource(R.drawable.lol_silver_1);
                break;
            case "SILVER II":
                tierImage.setImageResource(R.drawable.lol_silver_2);
                break;
            case "SILVER III":
                tierImage.setImageResource(R.drawable.lol_silver_3);
                break;
            case "SILVER IV":
                tierImage.setImageResource(R.drawable.lol_silver_4);
                break;
            case "BRONZE I":
                tierImage.setImageResource(R.drawable.lol_bronze_1);
                break;
            case "BRONZE II":
                tierImage.setImageResource(R.drawable.lol_bronze_2);
                break;
            case "BRONZE III":
                tierImage.setImageResource(R.drawable.lol_bronze_3);
                break;
            case "BRONZE IV":
                tierImage.setImageResource(R.drawable.lol_bronze_4);
                break;
            case "IRON I":
                tierImage.setImageResource(R.drawable.lol_iron_1);
                break;
            case "IRON II":
                tierImage.setImageResource(R.drawable.lol_iron_2);
                break;
            case "IRON III":
                tierImage.setImageResource(R.drawable.lol_iron_3);
                break;
            case "IRON IV":
                tierImage.setImageResource(R.drawable.lol_iron_4);
                break;
            case "UNRANK":
                tierImage.setImageResource(R.drawable.lol_unrank);
                break;
        }
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_leagueof_legends2; }
}
