package com.android.neighborhoodbookshop.explore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.neighborhoodbookshop.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ApplyGoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    //MyLocationActivity는 OnMapReadyCallback 인터페이스를 구현합니다.
    //OnMapReadyCallback 인터페이스는 지도가 준비되었을때 실행할 콜백을 정의하는 것

    private GoogleMap map;
    // GoogleMap 객체를 저장하기 위한 멤버변수를 선언
    // 이 객체는 Google 지도와 상호작용 하는데 사용됩니다

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_my_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //지도 표시를 위한 프래그먼트를 가져옵니다.
        mapFragment.getMapAsync(ApplyGoogleMapsActivity.this);
        //mapFragment를 비동기적으로 가져와서 onMapReady 메소드를 호출하기 위해 MyLocationActivity 클래스 자체를 인자로 전달하여 등록
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng seoul = new LatLng(37.56, 126.97);
        //LatLng 클래스를 사용하여 서울의 위도와 경도를 나타내는 좌표를 생성합니다.

        MarkerOptions options = new MarkerOptions();
        // 마커를 설정하기 위한 MarkerOptions 객체를 생성합니다.
        options.position(seoul)//마커의 위치를 서울로 설정합니다.
                .title("서울") // 마커에 표시할 제목을 설정합니다.
                .snippet("한국의 수도"); //마커에 표시할 추가 정보(스니펫)를 설정합니다.
        map.addMarker(options);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10));
        //카메라를 서울의 위치로 이동하고 줌 레벨 10으로 설정하여 해당 위치를 확대해서 보여줍니다.
    }
}