package com.android.neighborhoodbookshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//사용자 개인 정보를 보호하려면 위치 서비스를 사용하는 앱에서 위치 정보 액세스 권한을 요청해야 합니다.
//어떤 권한을 어떻게 요청하는지는 앱 사용 사례의 위치 요구사항에 따라 다릅니다.
public class ShowCurrentLocation extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {
    //ActivityCompat.OnRequestPermissionsResultCallback은 권한 요청 결과에 대한 콜백입니다
    // 사용자가 권한을 수락했을때 또는 거부했을때 어떤 작업을 수행해야 할지를 정의할 수 있다

    static String addressString; //문자열 주소
    private GoogleMap mMap;
    private Marker currentMarker = null;
    private static final String TAG = "googlemap";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000; //1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; //0.5초
    private static final int PERMISSION_REQUEST_CODE = 100;
    //requestPermissions(ShowCurrentLocation.this, REQUIRED_PERMISSION, PERMISSION_REQUEST_CODE); 시에 써야 하는 코드

    //앱을 실행하기 위한 퍼미션 정의
    String[] REQUIRED_PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Location mCurrentLocation;
    LatLng currentPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    View mLayout; //snackbar 사용하기 위함



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_current_location);

        android.widget.Button button = findViewById(R.id.getlocation);

        //위치요청 설정
        locationRequest = new com.google.android.gms.location.LocationRequest().
                setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)//가장 정확한 위치를 요청합니다. 이 설정을 사용하면 위치 서비스가 GPS를 사용하여 위치를 확인할 가능성이 높습니다.
                .setInterval(UPDATE_INTERVAL_MS) //업데이트 간격
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS); //가장 빠른 업데이트 간격

        //현재 위치 설정 받기
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        // LocationSettingsRequest.Builder를 만들고 하나 이상의 위치 요청을 추가합니다.

        //위치 서비스 클라이언트 만들기
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //나의 위치 가져오기 버튼을 누르면, MainActivity로 되돌아가기...
                // 1. addressString을 인텐트로 보내줌
                // 2. mCurrentLocation의 위도와 경도로 가지고 와서 인텐트로 넘겨주기

                Intent intentR = new Intent();
                intentR.putExtra("myLocation", addressString); //나의 현재위치를 전달함
                intentR.putExtra("myLat", mCurrentLocation.getLatitude());
                intentR.putExtra("myLng", mCurrentLocation.getLongitude());
                // The data type of the data you put as an extra will be preserved when it is received by the next activity.
                setResult(RESULT_OK, intentR); //결과를 저장
                finish(); // 액티비티 종료
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady: 들어옴 ");
        mMap = googleMap;

        // 지도의 초기위치 이동
        setDefaultLocation();

        //런타임 퍼미션 처리
        //런타임 퍼미션이란?: 앱이 실행 중에 사용자로부터 권한을 요청하는 것
        //이러한 권한은 예를 들어 위치 정보 액세스, 카메라 액세스, 주소록 액세스, 저장소 액세스 등과 같은 것들이 포함됩니다.

        //1.위치 퍼미션을 가지고 있는지 확인합니다
        // 가지고 있으면 0, 없으면 -1을 반환
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            //2-1. 이미 퍼미션을 가지고 있다면
            startLocationUpdates(); //3. 위치 업데이트 실행
        }else{
            //2-2. 퍼미션 요청을 허용한 적이 없다면, 퍼미션 요청하기
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSION [0])){
            //이전에 거부한 경우, 권한 필요성 설명 및 권한 요청
            Snackbar.make( mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE).
                    setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        // 사용자에게 퍼미션 요청, 요청 결과는 onRequestPermisionResult에서 수신
                        ActivityCompat.requestPermissions(ShowCurrentLocation.this, REQUIRED_PERMISSION, PERMISSION_REQUEST_CODE);
                        }
                    }).show();
        }else {
            //처음 요청하는 경우, 그냥 권한 요청
            //요청 결과는 onRequestPermisionResult에서 수신
            ActivityCompat.requestPermissions(ShowCurrentLocation.this, REQUIRED_PERMISSION, PERMISSION_REQUEST_CODE);
        }
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Google Maps Android API를 사용하여 지도에서 "내 위치 버튼"을 활성화하는 코드입니다.
        //버튼을 클릭하면 지도가 현재 위치로 이동하며, 사용자의 현재 위치가 중심으로 표시됩니다.
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick: ");
            }
        });
    }

    LocationCallback locationCallback = new LocationCallback(){
        //FusedLocationProviderClient의 알림을 받는 콜백

        @Override
        //이 메서드는 위치 업데이트 결과를 처리합니다
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations(); //현재 위치 업데이트에 대한 위치 정보를 반환합니다.
            //위치 정보는 Location 객체로 표현되며, 이 객체는 위도, 경도, 고도, 정확도, 시간 등과 같은 위치 관련 정보를 포함합니다.

            if(locationList.size() >0){
                location = locationList.get(locationList.size() -1); //locationList 중 가장 최근 위치를 담는다

                currentPosition = new LatLng(location.getLatitude(), location.getLongitude()); //LatLng 좌표 객체 (위도, 경도)

                String markerTitle = getcurrentAddress(currentPosition);
                String markerSnipet = "위도 :"+ String.valueOf(location.getLatitude())+" 경도 :" +
                        String.valueOf(location.getLongitude());

                //현재 위치에 마커 생성하고 이동
                setCurrnetLocation(location, markerTitle, markerSnipet);

                mCurrentLocation = location;
            }

        }
    };

    // 현재 위치에 대한 주소를 가져옴
    private String getcurrentAddress(LatLng currnetPosition){
        // 지오코더 gps를 문자열 주소로 변환
        //역지오코딩은 (위도, 경도) 좌표를 (부분) 주소로 변환하는 프로세스입니다.

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        //사용자가 앱을 한국에서 실행하는 경우 Locale.getDefault()는 한국 로캘 정보를 반환하고, 따라서 Geocoder는 한국어로 주소를 변환하거나 지리적 좌표를 표시합니다.
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(currnetPosition.latitude, currnetPosition.longitude,1);
            //maxResult: 반환하려는 주소 결과의 최대 수
        } catch (IOException e) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return  "지오코더 서비스 사용 불가";
        }catch(IllegalArgumentException illegalArgumentException){
            Toast.makeText(this,"잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0){
            Toast.makeText(this,"주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }else {
            Address address = addresses.get(0); //public class Address: 주소, 즉 위치를 설명하는 문자열 집합을 나타내는 클래스
            addressString = address.getAddressLine(0).toString();
            return addressString;
            //주어진 인덱스(0부터 시작)로 번호가 매겨진 주소의 라인을 반환하거나, 해당 라인이 없으면 null을 반환합니다.
        }
    }

    //현재 위치를 지도에 표시
    //새롭게 현재 위치정보를 받아와서 해당 위치를 Google Maps 지도 위에 마커로 표시하고, 지도의 중심 위치를 현재 위치로 이동시킴
    private void setCurrnetLocation(Location location, String markerTitle, String markerSnipet){
        if(currentMarker != null){
            currentMarker.remove(); //현재 지도에 표시된 마커(currentMarker)를 제거, 이전 위치 마커를 지워 새로운 위치를 표시하기 위한 작업
        }
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng); //지도에서 마커의 위치에 대한 LatLng 값입니다. 이는 Marker 객체의 유일한 필수 속성입니다.
        markerOptions.title(markerTitle); //사용자가 마커를 눌렀을 때 정보 창에 표시되는 문자열.
        markerOptions.snippet(markerSnipet); //제목 아래에 표시되는 추가 텍스트.
        markerOptions.draggable(true); //사용자가 마커의 위치를 변경할 수 있습니다. 마커 이동 기능을 활성화하려면 마커를 길게 누르세요.

        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);//지도의 중심 위치를 현재 위치로 이동시킵니다
    }

    private void startLocationUpdates(){
        //위치 업데이트 실행

        if(!checkLocationServiceStatus()){
            //GPS_PROVIDER와 NETWORK_PROVIDER 모두 비활성 상태
            showDiologForLocationServiceSetting();
        }else {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED|| hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED ){
                //두 퍼미션 중 하나라도 퍼미션이 없으면 해당 로그 메시지가 출력
                Log.d(TAG, "startLocationUpdates: 퍼미션 없음");
                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            //위치 요청하기
            // locationRequest: 위치 업데이트 요청을 정의하는 데 사용되는 LocationRequest 객체
            // locationCallback:  위치 업데이트 이벤트를 처리, 이 콜백은 위치 업데이트가 발생할 때 호출됨
            // Looper.myLooper : 위치 업데이트 이벤트를 처리할 스레드를 지정합니다. 대개 앱의 메인 스레드에서 호출됨
            
            if(checkPermission()){
                //둘다 퍼미션이 있는 경우에만 사용자의 현재 위치를 지도에 표시
                mMap.setMyLocationEnabled(true); // 간단하게 setMyLocationEnabled 만 하면 지도에 파란점과 현위치 찾기 버튼이 나타납니다.
            }

        }
    }


    //액티비티가 사용자에게 보여지기 전에 호출됩니다.
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        if(checkPermission()){
            //둘다 퍼미션이 있는 경우에만 위치업데이트 요청을 보냄
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            // 3번째 인자로 null을 사용하면 위치 업데이트 이벤트를 현재 스레드에서 처리합니다. 이것은 위치 업데이트 이벤트를 액티비티의 메인 스레드에서 처리하도록 하는 것을 의미합니다.

            //위치 요청하기
            // locationRequest: 위치 업데이트 요청을 정의하는 데 사용되는 LocationRequest 객체
            // locationCallback:  위치 업데이트 이벤트를 처리, 이 콜백은 위치 업데이트가 발생할 때 호출됨
            // Looper.myLooper : 위치 업데이트 이벤트를 처리할 스레드를 지정합니다. 대개 앱의 메인 스레드에서 호출됨

            if(mMap != null){
                mMap.setMyLocationEnabled(true); //// 간단하게 setMyLocationEnabled 만 하면 지도에 파란점과 현위치 찾기 버튼이 나타납니다.
            }
        }

    }

    //사용자에게 더 이상 액티비티가 보이지 않게 되었을 때 호출됩니다.
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");

        if(mFusedLocationClient != null){
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            //앱이 정보를 수집할 필요가 없는 경우 위치 업데이트를 중지하면 전력 소모를 줄이는 데 도움이 될 수 있습니다.
        }
    }


    private boolean checkPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 모든 위치 권한이 부여된 경우
            return true;
        }
        // 하나 이상의 위치 권한이 부여되지 않은 경우
        Log.d(TAG, "startLocationUpdates: 퍼미션 없음");
        return false;
    }

    // 위치 서비스 비활성화를 수정할 것인지 묻는 다이얼로그 세팅
    private void showDiologForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowCurrentLocation.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다. 위치설정을 수정하시겠습니까?");
        builder.setCancelable(true); //뒤로가기 버튼 활성화

        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); // GPS 설정 화면으로 이동
            //startActivity(new Intent(android.provider.Settings."액션"));
            //어플리케이션에서 디바이스 설정창 실행시키는 방법
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            // startActivityForResult: 새 액티비티를 열어줌 + 요청코드 전달
            // int형 requestCode인데 -> 원하는 숫자를 넣어주면 된다. 여러 액티비티를 쓰는 경우, 어떤 Activity인지 식별하는 값이다.

            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.create().show();
    }

    //현재 디바이스에서 위치 서비스(위치 정보)가 활성화되어 있는지 확인하고 그 결과를 반환하는 메소드
    //GPS_PROVIDER와 NETWORK_PROVIDER 중 적어도 하나의 공급자가 활성화되어 있어야 true가 반환됩니다.
    private boolean checkLocationServiceStatus(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //위치 정보를 획득하기 위해 LocationManager를 사용합니다.

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //isProviderEnabled: 지정된 공급자의 현재 활성화/비활성화 상태를 반환, 공급자가 존재하고 활성화된 경우 true
    }

    // 지도의 초기위치
    private void setDefaultLocation() {
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치 정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 여부를 확인하세요";

        if(currentMarker != null){
            currentMarker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);
    }

}