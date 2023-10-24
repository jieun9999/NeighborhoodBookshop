package com.android.neighborhoodbookshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExploreActivity extends AppCompatActivity implements OnMapReadyCallback {
    //MyLocationActivity는 OnMapReadyCallback 인터페이스를 구현합니다.
    //OnMapReadyCallback 인터페이스는 지도가 준비되었을때 실행할 콜백을 정의하는 것

    private GoogleMap map;
    // GoogleMap 객체를 저장하기 위한 멤버변수를 선언
    // 이 객체는 Google 지도와 상호작용 하는데 사용됩니다

    // 위치 객체
    List<LatLng> latLngList;
    //유저 이미지 객체
    List<String> imgList;

    LinearLayout 내서재;
    LinearLayout 탐색;
    LinearLayout 북클럽;
    LinearLayout 타이머;
    LinearLayout 설정;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //지도 표시를 위한 프래그먼트를 가져옵니다.
        mapFragment.getMapAsync(ExploreActivity.this);
        //mapFragment를 비동기적으로 가져와서 onMapReady 메소드를 호출하기 위해 MyLocationActivity 클래스 자체를 인자로 전달하여 등록

        //쉐어드 파일에서 위도, 경도 데이터를 가져옵니다
        //creates a new instance of SharedPreferences with the name “프로필” and mode MODE_PRIVATE.
        SharedPreferences sharedPreferences = getSharedPreferences("프로필", MODE_PRIVATE);
        // retrieves all key-value pairs from the shared preferences file.
        Map<String, ?> allEntries = sharedPreferences.getAll();
        // initializes a new empty list of type Double.
        List<Double> latList = new ArrayList<>();
        List<Double> lngList = new ArrayList<>();
        imgList = new ArrayList<>();

        // iterates over each key-value pair in the map.
        for(Map.Entry<String, ?> entry:allEntries.entrySet()){
            // retrieves the value of the current key-value pair as a string.
            String json = entry.getValue().toString();
            //attempts to parse the string as a JSON object and retrieve the value associated with the key “lat” as a double.
            // If an exception is thrown, it is caught and printed to the console.
            try {
                JSONObject jsonObject = new JSONObject(json);
                latList.add(jsonObject.getDouble("lat"));
                lngList.add(jsonObject.getDouble("lng"));
                imgList.add(jsonObject.getString("imagePath"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        //여러개의 마커를 찍는 방법
        // latList와 lngList에 저장된 위도와 경도 정보를 이용하여 LatLng 객체를 생성합니다.
        // 이후, GoogleMap 객체의 addMarker() 메소드를 사용하여 지도상에 마커를 추가합니다.

        //1. LatLng 객체 생성
        latLngList = new ArrayList<>();
        for (int i = 0; i < latList.size(); i++) {
            latLngList.add(new LatLng(latList.get(i), lngList.get(i)));
        }


        내서재 = (LinearLayout) findViewById(R.id.mylibrary);
        탐색 = (LinearLayout) findViewById(R.id.explore);
        북클럽 = (LinearLayout) findViewById(R.id.bookclub);
        타이머= (LinearLayout) findViewById(R.id.timer);
        설정 = (LinearLayout) findViewById(R.id.setting);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.mylibrary){
                    Intent myIntent = new Intent(ExploreActivity.this, MainActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.bookclub) {
                    Intent myIntent = new Intent(ExploreActivity.this,BookClubActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.timer) {
                    Intent myIntent = new Intent(ExploreActivity.this, TimerActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.setting) {
                    Intent myIntent = new Intent(ExploreActivity.this,SettingActivity.class);
                    startActivity(myIntent);
                }
            }
        };

        내서재.setOnClickListener(listener);
        북클럽.setOnClickListener(listener);
        타이머.setOnClickListener(listener);
        설정.setOnClickListener(listener);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // 2. GoogleMap 객체에서 addMarker() 메소드를 사용하여 마커 추가
        for (int i = 0; i < latLngList.size(); i++) {

            //마커의 아이콘 크기를 조절
            //kerOptions 객체의 icon() 메소드를 사용하여 BitmapDescriptor 객체를 생성한 후,
            // BitmapDescriptorFactory 클래스의 fromBitmap() 메소드를 사용하여 크기를 조절한 이미지를 생성합니다.
            //이후, MarkerOptions 객체의 icon() 메소드에 생성된 이미지를 전달하여 마커의 아이콘을 설정합니다.
            BitmapDescriptor icon =
                    BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.
                    decodeFile(imgList.get(i)), 100,100,false));

            map.addMarker(new MarkerOptions()
                    .position(latLngList.get(i))
                    .icon(icon));
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0), 10));
        //카메라를 latLngList.get(0) 으로 이동하고 줌 레벨 10으로 설정하여 해당 위치를 확대해서 보여줍니다.
    }
}