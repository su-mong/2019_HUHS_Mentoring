package com.candykick.huhs2ndmentoring.map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;

import com.candykick.huhs2ndmentoring.R;
import com.candykick.huhs2ndmentoring.base.BaseActivity;
import com.candykick.huhs2ndmentoring.databinding.ActivityMapBinding;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MapActivity extends BaseActivity<ActivityMapBinding> implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener  {

    ArrayList<MapData> mapDataArrayList = new ArrayList<>();

    Double currentLatitude, currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);

        //현재 위치를 불러옴. 이전에 실행한 시점에서 현재 위치가 변하지 않은 경우 이 데이터를 사용함.
        SharedPreferences sf = getSharedPreferences("currentLocation",MODE_PRIVATE);
        currentLatitude = (double)sf.getFloat("currentLatitude",1.0f);
        currentLongitude = (double)sf.getFloat("currentLongitude",1.0f);

        //현재 위치의 다음 지도를 containerMap에 추가
        binding.mvMap.setCurrentLocationEventListener(this);
        binding.mvMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        //지도에서 마커 클릭 시 해당 위치의 정보를 보여주는 창으로 이동하도록 리스너 설정
        binding.mvMap.setMapViewEventListener(this);
        binding.mvMap.setPOIItemEventListener(this);

        //탭 클릭 시 현재 위치 주변의 맛집/PC방/노래방을 검색한다.
        binding.tlMap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                new SearchPlace().execute(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    //현재 위치 주변의 맛집/PC방/노래방을 검색해주는 객체
    private class SearchPlace extends AsyncTask<String, Void, Void> {
        boolean isWorkWell = true;
        String result;

        @Override
        protected Void doInBackground(String... params) {
            //검색 api에서 현재 위치 주변의 맛집/PC방/노래방 정보를 json으로 받아온다.
            OkHttpClient client = new OkHttpClient();

            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("http")
                    .host("dapi.kakao.com")
                    .addPathSegment("/v2/local/search/keyword.json")
                    .addQueryParameter("query", params[0])
                    .addQueryParameter("x", currentLongitude.toString())
                    .addQueryParameter("y", currentLatitude.toString())
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Authorization","KakaoAK 0e52d15975a3228dfcbc7b012531a83b")
                    .url(httpUrl)
                    .build();

            try { //정상적으로 작동한 경우 결과값 json을 셋팅한다.
                Response response = client.newCall(request).execute();

                result = response.body().string();
                isWorkWell = true;
            } catch (Exception e) { //오류가 발생한 경우 오류 내용을 셋팅한다.
                result = e.getMessage();
                isWorkWell = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            if(!isWorkWell) { //오류가 발생한 경우 알려준다.
                makeToastLong("오류가 발생했습니다: "+result);
                //progressOff();
            } else {
                try {//정상적으로 결과값이 들어온 경우
                    //1. 초기화 작업을 한다.
                    mapDataArrayList.clear(); //arrayList 비우고
                    binding.mvMap.removeAllPOIItems(); //맵 위의 마커들 다 지우고
                    binding.mvMap.setZoomLevel(5, true); //지도 줌을 5로 축소하고
                    binding.mvMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff); //현재 위치 추적 기능을 끈다.
                    //* 현재 위치 추적 기능을 끄지 않으면, 중간에 현재 위치가 바뀔 때 화면이 현재 위치 쪽으로 이동해버린다. 이를 방지하기 위한 것.

                    //2. 검색 결과로 들어온 json에서 필요한 정보만 꺼낸다.
                    JSONObject jsonObject = new JSONObject(result);
                    String jsonArraytmp = jsonObject.getString("documents");
                    JSONArray jsonArray = new JSONArray(jsonArraytmp);

                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject mapObject = jsonArray.getJSONObject(i);

                        //arrayList에 필요한 정보 셋팅
                        MapData mapData = new MapData();
                        mapData.address_name = mapObject.getString("address_name");
                        mapData.place_name = mapObject.getString("place_name");
                        mapData.place_url = mapObject.getString("place_url");
                        mapData.x = mapObject.getString("x");
                        mapData.y = mapObject.getString("y");
                        mapDataArrayList.add(mapData);

                        //지도에 해당하는 장소 마커 찍기
                        MapPOIItem marker = new MapPOIItem();
                        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(mapData.y), Double.parseDouble(mapData.x));
                        marker.setItemName(mapData.place_name);
                        marker.setTag(i);
                        marker.setMapPoint(mapPoint);
                        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                        binding.mvMap.addPOIItem(marker);
                    }

                    //arrayList를 가지고 결과값 리스트 생성
                    MapListAdapter adapter = new MapListAdapter(MapActivity.this, mapDataArrayList);
                    binding.lvMap.setAdapter(adapter);
                    binding.lvMap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //리스트의 아이템 클릭 시 아래의 작업을 수행한다.
                            binding.mvMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(mapDataArrayList.get(position).y), Double.parseDouble(mapDataArrayList.get(position).x)), true); //해당 장소가 있는 위치로 이동하고
                            binding.mvMap.setZoomLevel(3, true); //지도 줌을 3으로 확대한 뒤
                            binding.mvMap.selectPOIItem(binding.mvMap.getPOIItems()[position], true); //해당 마커를 연다.
                        }
                    });

                    //progressOff();
                } catch (JSONException e) { //오류가 발생한 경우 알려준다.
                    makeToastLong("오류가 발생했습니다: "+e.toString());
                    //progressOff();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        //Activity 종료 시 현재 위치 추적 모드를 꺼 줘야 한다.
        super.onDestroy();
        binding.mvMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        binding.mvMap.setShowCurrentLocationMarker(false);
    }

    @Override
    protected int getLayoutId() { return R.layout.activity_map; }

    //아래 4개 함수는 현재 위치 관련 함수들이다. (MapView.CurrentLocationEventListener를 구현한 것이다.)
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float v) { //현재 위치가 바뀐 경우
        //현재 위치를 변수에 넣는다.
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        currentLatitude = mapPointGeo.latitude;
        currentLongitude = mapPointGeo.longitude;

        //현재 위치를 따로 저장한다.
        SharedPreferences sf = getSharedPreferences("currentLocation",MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putFloat("currentLatitude",Float.parseFloat(currentLatitude.toString()));
        editor.putFloat("currentLongitude",Float.parseFloat(currentLongitude.toString()));
        editor.apply();
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {} //현재 바라보고 있는 방향이 바뀐 경우

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) { //현재 위치를 찾는 데 실패한 경우
        makeToastLong("현재 위치를 찾을 수 없습니다. 다시 시도해 주세요.");
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) { //현재 위치를 찾는 작업이 취소된 경우
        makeToastLong("현재 위치를 찾는 작업이 취소되었습니다. 다시 시도해 주세요.");
    }

    //아래 4개 함수는 지도 위의 마커 관련 함수들이다. (MapView.POIItemEventListener를 구현한 것이다.)
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {} //마커가 선택된 경우. 말풍선이 나오므로 따로 쓸 건 없다.

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) { //말풍선을 클릭한 경우: 해당 장소를 설명하는 URL로 이동한다.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapDataArrayList.get((int)mapPOIItem.getTag()).place_url));
        startActivity(intent);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {} //이것도 말풍선을 클릭한 경우인데, 딱히 구현할 필요 없다.

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {} //드래그 가능한 마커가 움직인 경우. 드래그 가능한 마커가 없으므로 구현할 필요 없다.

    //아래 9개 함수는 지도 관련 함수들이다. (MapView.MapViewEventListener를 구현한 것이다.)
    //딱히 구현할 필요는 없어서 다 비워 놨는데, 이게 없으면 마커 관련 함수들이 작동을 안 해서(...) 써 놓았다.
    @Override
    public void onMapViewInitialized(MapView mapView) {} //지도가 처음 생성됬을 때

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {} //지도의 중심점이 움직였을 때

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {} //지도의 줌이 바뀌었을 때(지도가 확대되거나, 축소되거나)

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {} //지도 위의 아무 곳을 한 번 클릭했을 때

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {} //지도 위의 아무 곳을 더블클릭했을 때

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {} //지도 위의 아무 곳을 길게 눌렀을 때

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {} //지도 위에서 드래그를 시작했을 때

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {} //지도 위에서 드래그를 끝냈을 때

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {} //?
}
