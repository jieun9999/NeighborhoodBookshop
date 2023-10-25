package com.android.neighborhoodbookshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
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
    //마커 이름 객체
    List<String> userIdList;
    //마커 이미지 객체
    List<String> imgList;

    //바텀시트에 담길 데이터
    String name;
    String profile_image;
    String profile_location;
    String profile_introduce;
    String userId;

    LinearLayout 내서재;
    LinearLayout 탐색;
    LinearLayout 북클럽;
    LinearLayout 타이머;
    LinearLayout 설정;

    //바텀 시트 레이아웃
    ImageView profile;
    TextView userName;
    TextView userLocation;
    TextView userIntroduction;


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
        userIdList = new ArrayList<>();
        imgList = new ArrayList<>();

        // iterates over each key-value pair in the map.
        for(Map.Entry<String, ?> entry:allEntries.entrySet()){
            // retrieves the value of the current key-value pair as a string.
            String key = entry.getKey().toString();
            String json = entry.getValue().toString();
            //attempts to parse the string as a JSON object and retrieve the value associated with the key “lat” as a double.
            // If an exception is thrown, it is caught and printed to the console.
            try {
                JSONObject jsonObject = new JSONObject(json);
                latList.add(jsonObject.getDouble("lat"));
                lngList.add(jsonObject.getDouble("lng"));
                userIdList.add(key);
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

        //서치뷰에 유저가 검색어를 입력하면, 그 검색어를 인텐트로 다음 액티비티에 전송함
        SearchView searchView = (SearchView) findViewById(R.id.searchview0);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // s는 사용자가 입력한 문자열
                Intent intent = new Intent(getApplicationContext(), bookReviewListActivity.class);
                intent.putExtra("bookName", s);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

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

            //1. 단순히 이미지 크기를 줄여서 마커를 만들기
            BitmapDescriptor icon =
                    BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.
                    decodeFile(imgList.get(i)), 100,100,false));

            Marker marker = map.addMarker(new MarkerOptions()
                    .position(latLngList.get(i))
                    .icon(icon));

            marker.setTag(userIdList.get(i)); //마커에 태그(id) 추가
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0), 10));
        //카메라를 latLngList.get(0) 으로 이동하고 줌 레벨 10으로 설정하여 해당 위치를 확대해서 보여줍니다.

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ExploreActivity.this);
                View bottomSheetView = getLayoutInflater().inflate(R.layout.profile_bottomsheet, null);
                bottomSheetDialog.setContentView(bottomSheetView);

                //마커의 정보 가져오기
                userId = marker.getTag().toString();
                //프로필 쉐어드 파일에서 데이터 가져오기
                SharedPreferences sharedPreferences1 = getSharedPreferences("프로필", MODE_PRIVATE);
                String json = sharedPreferences1.getString(userId, null);
                //json 문자열을 jsonObject로 변환
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    profile_image = jsonObject.getString("imagePath");
                    profile_location = jsonObject.getString("location");
                    profile_introduce = jsonObject.getString("introduction");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //가입정보 쉐어드 파일에서 데이터 가져오기
                SharedPreferences sharedPreferences2 = getSharedPreferences("가입정보", MODE_PRIVATE);
                String json2 = sharedPreferences2.getString(userId, null);
                //json 문자열을 jsonObject로 변환
                try {
                    JSONArray jsonArray = new JSONArray(json2);
                    name = jsonArray.getString(0);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // 데이터 렌더링
                profile = bottomSheetView.findViewById(R.id.imageView8);
                userName = bottomSheetView.findViewById(R.id.textView15);
                userLocation = bottomSheetView.findViewById(R.id.textView16);
                userIntroduction = bottomSheetView.findViewById(R.id.textView17);

                Uri uri = Uri.parse(profile_image);
                profile.setImageURI(uri);
                userName.setText(name);
                userLocation.setText(profile_location.substring(5));
                userIntroduction.setText(profile_introduce);

                //textview 흐르게 표시하기
                userName.setSingleLine(true); // 긴 텍스트를 한줄로 표시
                userName.setEllipsize(TextUtils.TruncateAt.MARQUEE); //텍스트가 잘릴경우 (길 경우) 흐르게 만들기
                userName.setSelected(true); //해당 텍스트뷰가 선택된것처럼 만들기
                userLocation.setSingleLine(true);
                userLocation.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                userLocation.setSelected(true);

                bottomSheetDialog.show();

                //바텀시트를 클릭하면, 상대방의 프로필 화면으로 넘어간다 (후순위)
                bottomSheetView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(ExploreActivity.this, ProfileActivity.class);
//                        intent.putExtra("userId", userId);
                    }
                });
                return true;
            }
        });
    }


}