package com.android.neighborhoodbookshop.mylibrary;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.neighborhoodbookshop.bookclub.BookClubActivity;
import com.android.neighborhoodbookshop.explore.BookSearchActivity;
import com.android.neighborhoodbookshop.explore.ExploreActivity;
import com.android.neighborhoodbookshop.R;
import com.android.neighborhoodbookshop.setting.SettingActivity;
import com.android.neighborhoodbookshop.explore.ShowCurrentLocation;
import com.android.neighborhoodbookshop.timer.TimerActivity;
import com.android.neighborhoodbookshop.loginsignup.UserManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    //다음 코드의 요약
    //1. 프로필 데이터 (이름, 사진, 위치, 소개글, 인스타 url)을 쉐어드 '가입정보', '프로필'에 저장함
    //2. 읽은책을 등록, 상세보기, 수정, 삭제가 가능한 리사이클러 뷰 만듦
    //3. 해당 리사이클러뷰 어레이리스트를 쉐어드 파일 '책리뷰'에 저장함

    public static final int MY_LOCATION_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_SUB = 10;
    private static final int REQUEST_CODE_DETAILS = 50;
    private static final int RESULT_DELETE = 40;
    private static final int RESULT_EDIT = 120;
    //에러 원인: REQUEST_CODE_SUB와 REQUEST_CODE_DETAILS가 동일한 값을 가지고 있기 때문에, 두 경우 모두 같은 처리가 실행됩니다. 이로 인해 새로운 책을 추가할 때도 삭제 처리

    private Context mContext;
    // context를 사용하여 앱의 리소스 (레이아웃 파일, 문자열, 이미지 등에 접근할 수 있습니다.)
    // 예를 들어 getApplicationContext()를 호출하여 앱의 전역 컨텍스트를 가져올 수 있으며, 이를 통해 리소스에 접근할 수 있습니다.

    //2차원 어레이리스트 userReviewList를 초기화합니다.
    public static ArrayList<ArrayList<String>> userReviewList = new ArrayList<>(); // static으로 변경

    private BookRegisterAdapter bookRegisterAdapter;
    private RecyclerView mRecyclerView;

    LinearLayout 내서재;
    LinearLayout 탐색;
    LinearLayout 북클럽;
    LinearLayout 타이머;
    LinearLayout 설정;

    //바텀 시트의 버튼들
    ImageView plus_btn;
    LinearLayout btn_search;
    LinearLayout btn_write;

    //왔다갔다 할 데이터 값 변수설정
    String imagePath;
    String bookTitle;
    String bookWriter;
    String bookCompany;
    String bookDate;
    String bookIsbn;
    String rateNum;
    String rateMemo;

    //유저정보 데이터
    String userId;
    String userName;

    //유저정보 레이아웃
    TextView textView_userName;
    TextView textView_userLocation;
    ImageView imageView_userPicture;
    TextView textView_introduction;
    TextView textView_insta;

    //유저 프로필 클래스
    ProfileManager profileManager;
    // json object를 쉐어드에 저장하고 불러오는 클래스 데이터
    SharedPreferences mPrefs;

    //읽은 책 수
    int bookNum;
    TextView textView_bookNum;

    private static final int PICK_IMAGE_REQUEST = 0; // 어떤 정수 값도 사용 가능


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylibrary_main);

        //UserManager에서 static으로 사용자 ID를 가져옴
        userId = UserManager.getUserId();

        //프로필의 이름, 가입정보 쉐어드 파일에서 불러오기
        textView_userName = findViewById(R.id.textView28);
        SharedPreferences preferences = getSharedPreferences("가입정보", MODE_PRIVATE);
        String jsonData = preferences.getString(userId, null);
        if (jsonData != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                userName = jsonArray.getString(0);//쉐어드 유저이름을 저장할 변수
                textView_userName.setText(userName);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(getApplicationContext(), "가입정보가 비었습니다", Toast.LENGTH_SHORT).show();
        }
        // 객체를 만들어준다. 객체를 먼저 만들어야 , 해당 객체의 기능을 사용할 수 있다
        profileManager = new ProfileManager();


        //프로필의 4항목, 프로필 쉐어드 파일에서 불러오기
        mPrefs = getSharedPreferences("프로필", MODE_PRIVATE);
        Gson gson = new Gson();
        String json= mPrefs.getString(userId, null); //기본값 null
        //키인 userId로 뽑은 객체인 profileManager
        // profileManager가 null이 아닌 경우에만 쉐어드에서 뽑아와서 할당한다
        if(json != null){

            profileManager = gson.fromJson(json, ProfileManager.class);
        }

        //화면에 쉐어드 파일로 불러온 데이터 반영하기 (프로필 이미지, 위치, 소개글, 인스타 url)

        //1. 프로필 이미지
        imageView_userPicture = findViewById(R.id.imageView6);
        //쉐어드 파일에서 이미지 파일 경로 불러오고 = > uri로 파싱 => 이미지 교체
        // 문자열 이미지 파일 경로를 URI로 변환
        if(profileManager != null && profileManager.getImagePath() != null){
            Uri imageUri = Uri.parse(profileManager.getImagePath().toString());
            imageView_userPicture.setImageURI(imageUri);
        }
        //나의 이미지 설정하기
        imageView_userPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리 열기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        //2. 위치
        // ShowCurrentLocation 액티비티로 이동했다가 다시 돌아올거임
        textView_userLocation = findViewById(R.id.textView31);
        //쉐어드 파일에서 위치 정보 가져와서 렌더링
        if( profileManager != null &&profileManager.getLocation() != null){
            textView_userLocation.setText(profileManager.getLocation().toString());
        }
        textView_userLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, ShowCurrentLocation.class);
                startActivityForResult(myIntent, MY_LOCATION_REQUEST_CODE );
            }
        });

        textView_userName = findViewById(R.id.textView28);

        //3. 소개글
        textView_introduction = findViewById(R.id.textView14);
        // 나의 소개글을 쉐어드 파일에서 가져오기
        if(profileManager != null && profileManager.getIntroduction() != null){
            textView_introduction.setText(profileManager.getIntroduction().toString());
        }
        // 나의 소개글 설정하기
        textView_introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 소개 다이얼로그 띄우기
                //AlertDialog.Builder를 사용하여 AlertDialog를 생성합니다.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // R.layout.alert_dialog 레이아웃을 인플레이트하여 대화상자에 표시할 뷰를 설정합니다.
                View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_mylibrary_userprofile_add_introduction, null, false);
                // 빌더 객체에서 뷰를 보여줍니다.
                builder.setView(view2);
                //dialog 변수를 사용하여 AlertDialog를 표시하거나 닫을 수 있습니다.
                // 예를 들어, dialog.show()를 호출하여 AlertDialog를 화면에 표시하거나 dialog.dismiss()를 호출하여 AlertDialog를 닫음
                final AlertDialog dialog = builder.create();

                //다이얼로그의 등록/취소 버튼
                final ImageView btn_cancel = view2.findViewById(R.id.imageView4);
                final Button btn_add = view2.findViewById(R.id.button);

                //입력
                final TextInputEditText input_introduction = view2.findViewById(R.id.memo);

                //기본적으로 실행되면 다이얼로그 보여줌
                dialog.show();

                //다이얼로그의 취소 버튼 클릭
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                //다이얼로그 등록 버튼 클릭
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //다이얼로그에 새롭게 입력한 내용을 가져온다

                        String input =input_introduction.getText().toString();
                        if(!TextUtils.isEmpty(input)){
                            //json object에 속성을 저장
                            profileManager.setIntroduction(input);
                            textView_introduction.setText(input);

                            //쉐어드에 저장
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(profileManager);
                            prefsEditor.putString(userId, json);
                            prefsEditor.commit();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        //4. 인스타 url
        textView_insta = findViewById(R.id.textView35);
        // 나의 인스타 url 쉐어드 파일에서 가져오기
        if(profileManager != null && profileManager.getInstaURL() != null){
            textView_insta.setText(profileManager.getInstaURL().toString());
        }
        // 나의 인스타 url 설정하기
        textView_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //인스타 다이얼로그 띄우기
                //AlertDialog.Builder를 사용하여 AlertDialog를 생성합니다.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // R.layout.alert_dialog 레이아웃을 인플레이트하여 대화상자에 표시할 뷰를 설정합니다.
                View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_mylibrary_userprofile_add_insta, null, false);
                // 빌더 객체에서 뷰를 보여줍니다.
                builder.setView(view2);
                //dialog 변수를 사용하여 AlertDialog를 표시하거나 닫을 수 있습니다.
                // 예를 들어, dialog.show()를 호출하여 AlertDialog를 화면에 표시하거나 dialog.dismiss()를 호출하여 AlertDialog를 닫음
                final AlertDialog dialog = builder.create();

                //다이얼로그의 등록/취소 버튼
                final ImageView btn_cancel = view2.findViewById(R.id.imageView4);
                final Button btn_add = view2.findViewById(R.id.button);

                //입력
                final TextInputEditText input_instaUrl = view2.findViewById(R.id.memo);

                //기본적으로 실행되면 다이얼로그 보여줌
                dialog.show();

                //다이얼로그의 취소 버튼 클릭
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                //다이얼로그 등록 버튼 클릭
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //다이얼로그에 새롭게 입력한 내용을 가져온다
                        String input = input_instaUrl.getText().toString();
                        if(!TextUtils.isEmpty(input)) {
                            //json object에 속성을 저장
                            profileManager.setInstaURL(input);
                            textView_insta.setText(input);
                            //쉐어드에 저장
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(profileManager);
                            prefsEditor.putString(userId, json);
                            prefsEditor.commit();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

            //핵심 : 책리뷰 리사이클러뷰!
            //쉐어드 파일에서 어레이리스트 데이터 불러오고, 저장함.

            SharedPreferences preferences2 = getSharedPreferences("책리뷰", Context.MODE_PRIVATE);

            //SharedPreferences에서 모든 데이터를 가져와서 'allEntries"에 맵 형태로 저장한다
            Map<String, ?> allEntries = preferences2.getAll();

            //모든 데이터를 순회하는 for 루프를 시작합니다
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                String key = entry.getKey();

                //key에서 사용자 아이디 부분 추출
                String[] parts = key.split("_");
                if (parts.length >= 2) {
                    String user = parts[0];

                    //user와 userId의 값이 동일한 경우에만 처리
                    if (user.equals(userId)) {
                        String xmlData = entry.getValue().toString();

                        try {
                            String jsonString = Html.fromHtml(xmlData).toString();
                            JSONArray jsonArray = new JSONArray(jsonString);

                            //JSON 배열을 ArrayList<String>으로 변환하여 2차원 어레이리스트에 추가
                            ArrayList<String> reviewData = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                reviewData.add(jsonArray.getString(i));
                            }
                            userReviewList.add(reviewData);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }

            }

            //+버튼을 누르면 하단에 다이얼로그가 등장

            //LayoutInflater는 XML 레이아웃 파일을 Java 코드로 인플레이트(팽창)하는 데 사용되는 클래스입니다. 이 줄에서는 getSystemService 메서드를 사용하여 LAYOUT_INFLATER_SERVICE 상수로 LayoutInflater 인스턴스를 얻습니다.
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //inflater.inflate() 메서드를 사용하여 R.layout.bottom_sheet로 지정된 XML 레이아웃 파일을 View 객체로 인플레이트합니다
            View view = inflater.inflate(R.layout.dialog_mylibrary_book_add, null, false);
            //BottomSheetDialog 클래스의 인스턴스를 생성합니다. 이 클래스는 하단 시트 다이얼로그를 나타내며, this는 현재 액티비티를 나타냅니다.
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            // 이전에 인플레이트한 view를 bottomSheetDialog의 컨텐츠로 설정,  하단 시트 다이얼로그의 내용으로 view가 표시됨
            bottomSheetDialog.setContentView(view);

            //플러스 버튼 누르면, 나오는 하단의 다이얼로그 (bottomsheetdialog)
            plus_btn = findViewById(R.id.plus_btn);
            plus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.show();
                }
            });

            //다이얼로그 내에서 두개의 버튼 설정
            //검색하기
            btn_search = (LinearLayout) view.findViewById(R.id.btn_search);
            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                    //검색하기를 누르면, 독서검색 액티비티로 넘어간다
                    Intent intent = new Intent(MainActivity.this, BookSearchActivity.class);
                    startActivity(intent);

                }
            });

            //직접 입력하기
            btn_write = (LinearLayout) view.findViewById(R.id.btn_write);
            btn_write.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                    //직접 입력하기를 누르면, 독서기록 액티비티로 넘어간다
                    // 메인 액티비티에서 서브 액티비티1 호출
                    Intent showB = new Intent(MainActivity.this, BookDirectRegisterActivity1.class);
                    //활동 A에서 활동 B를 호출하는 부분:
                    startActivityForResult(showB, REQUEST_CODE_SUB);

                }
            });

            //리사이클러뷰 - 1.아이템 추가 세팅
            mContext = getApplicationContext();
            mRecyclerView = findViewById(R.id.recyclerView);

            //레이아웃메니저는 리사이클러뷰의 항목 배치를 어떻게 할지 정하고, 스크롤 동작도 정의한다.
            //수평/수직 리스트 LinearLayoutManager
            //그리드 리스트 GridLayoutManager
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);  //한 행(가로)에 3개의 그리드를 표시하도록 설정
            mRecyclerView.setLayoutManager(layoutManager);

            bookRegisterAdapter = new BookRegisterAdapter(mContext, userReviewList);
            // Adapter 클래스의 객체를 생성합니다. 이 어댑터는 RecyclerView와 데이터를 연결해주는 역할을 합니다.
            // mContext는 액티비티에서 가져온 Context 객체로, 어댑터가 액티비티의 리소스에 접근하고 상호 작용할 때 사용됩니다.

            mRecyclerView.setAdapter(bookRegisterAdapter);
            //RecyclerView에 어댑터를 설정합니다. 이렇게 하면 어댑터가 RecyclerView와 연결되어 데이터를 표시하고 스크롤할 수 있게 됩니다. 이후에는 어댑터가 데이터를 갱신하면 RecyclerView에 자동으로 반영됩니다.


            //리사이클러뷰 - 2.아이템 클릭하기(상세화면 보기) 세팅
            //mAdapter의 메서드 setOnItemClickListener를 호출하고, 이 메서드에 Adapter.OnItemClickListener라는 인터페이스를 구현한 객체를 인자로 넣고 있다.
            //mAdapter에 아이템 클릭 이벤트 리스너를 설정하여 RecyclerView의 아이템이 클릭되었을 때 onItemClick 메서드가 호출되도록 만드는 것입니다.


            bookRegisterAdapter.setOnItemClickListener(new BookRegisterAdapter.OnItemClickListener() {
                //어댑터에 연결해서 이벤트리스너를 준 이유는 클릭하는 부분이 리사이클러뷰의 뷰홀더 부분이라서

                @Override
                public void onItemClick(View v, int position) {
                    //______________인텐트 사용하지 않고 쉐어드에서 불러온다
                    //이미 userReviewList에 쉐어드에서 불러온 파일들을 정리해놓음

                    //인덱스로 아이템 찾기
                    ArrayList bookItem = userReviewList.get(position);

                    imagePath = bookItem.get(0).toString();
                    bookTitle = bookItem.get(1).toString();
                    bookWriter = bookItem.get(2).toString();
                    bookCompany = bookItem.get(3).toString();
                    bookDate = bookItem.get(4).toString();
                    bookIsbn = bookItem.get(5).toString();
                    rateNum = bookItem.get(6).toString();
                    rateMemo = bookItem.get(7).toString();

                    Intent intent = new Intent(MainActivity.this, BookSpecificActivity.class);
                    intent.putExtra("position", position); //클릭한 항목의 position만을 상세화면 액티비티에 넘겨준다

                    startActivityForResult(intent, REQUEST_CODE_DETAILS); //상수코드와 함께 상세화면 액티비티로 이동
                }
            });

            // 읽은 책 숫자 변경
        textView_bookNum = findViewById(R.id.textView36);
        bookNum = userReviewList.size();
        textView_bookNum.setText(String.valueOf(bookNum));

             //하단 5버튼
        내서재 = (LinearLayout) findViewById(R.id.mylibrary);
        탐색 = (LinearLayout) findViewById(R.id.explore);
        북클럽 = (LinearLayout) findViewById(R.id.bookclub);
        타이머 = (LinearLayout) findViewById(R.id.timer);
        설정 = (LinearLayout) findViewById(R.id.setting);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.explore) {
                    Intent myIntent = new Intent(MainActivity.this, ExploreActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.bookclub) {
                    Intent myIntent = new Intent(MainActivity.this, BookClubActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.timer) {
                    Intent myIntent = new Intent(MainActivity.this, TimerActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.setting) {
                    Intent myIntent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(myIntent);
                }
            }
        };

        탐색.setOnClickListener(listener);
        북클럽.setOnClickListener(listener);
        타이머.setOnClickListener(listener);
        설정.setOnClickListener(listener);

    }

    //onActivityResult() 메소드는 하위 액티비티에서 setResult() 메서드를 통해 결과가 반환되고, 이 하위 액티비티가 종료될때 호출됩니다.
    // 하위 액티비티가 finish()를 호출하여 종료되고, 그 결과를 반환하면 onActivityResult()가 실행됩니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //requestCode : 어떤 액티비티를 다녀왔는지 알려주는 값 (즉, 같은 액티비티에서 돌아온다면 같은 requestCode를 써야 한다)
        //resultCode: RESULT_OK(원하는 결과를 얻음)와 RESULT_CANCELED(원하는 결과를 못 얻음) 두가지가 많이 쓰이나 사용자가 정의하는 것도 가능하다
        // data: 하위액티비티에서 메인액티비티로 돌아올때 전달된 데이터를 포함한 인텐트이다
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SUB && resultCode == RESULT_OK && data != null) { // BookDirectRegisterActivity2 종료 후 실행되는 코드 (아이템 추가)
            String imagePath = data.getStringExtra("imagePath");
            String bookTitle = data.getStringExtra("book_title");
            String bookWriter = data.getStringExtra("book_writer");
            String bookCompany = data.getStringExtra("book_company");
            String bookDate = data.getStringExtra("book_date");
            String rateNum = data.getStringExtra("rateNum");
            String rateMemo = data.getStringExtra("rateMemo");
            String bookIsbn =data.getStringExtra("book_isbn");

            // 쉐어드에 먼저 데이터를 추가하고, 리사이클러뷰(userReviewList)에 추가한다.

            ArrayList<String> newBookItem = new ArrayList<>();
            newBookItem.add(imagePath);
            newBookItem.add(bookTitle);
            newBookItem.add(bookWriter);
            newBookItem.add(bookCompany);
            newBookItem.add(bookDate);
            newBookItem.add(bookIsbn);
            newBookItem.add(rateNum);
            newBookItem.add(rateMemo);

            //책리뷰라는 쉐어드 파일명에 key를 책이름, value(어레이리스트)로 저장한다
            SharedPreferences preferences = getSharedPreferences("책리뷰", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            bookTitle = bookTitle.replace(" ", ""); // 띄어쓰기를 제거
            //유저id_책이름 키를 만든다
            //여기서 keyToAdd를 userId + "" + bookTitle + "" + 0, 1, 2, ...와 같이 0부터 1씩 증가하는 값을 할당하려면
            String keyToAdd =  userId + "_" + bookTitle +"_"+ userReviewList.size();

            // 어레이리스트 newBookItem을 쉐어드의 value로 넣기 위해서는
            // 어레이리스트 => json Array => jsonString 순서로 변환해서 jsonString 상태로 value 자리에 넣어줘야 함

            JSONArray jsonArray = new JSONArray(newBookItem);
            String jsonString = jsonArray.toString();

            // 이제 jsonString을 SharedPreferences에 저장할 수 있습니다.
            editor.putString(keyToAdd, jsonString);
            editor.apply();

            //userReviewList에서 해당 데이터 추가
            userReviewList.add(newBookItem);
            //어댑터에게 아이템이 추가되었음을 알립니다
            bookRegisterAdapter.notifyItemInserted(userReviewList.size()-1);

            //읽은 책 숫자 변경
            bookNum = userReviewList.size();
            textView_bookNum.setText(String.valueOf(bookNum));

        }
        // 수정과 삭제 모두 같은 액티비티(상세화면 액티비티)에서 메인 액티비티로 돌아오는 것이기 때문에 같은  requestCode를 사용해야 한다
        // 따라서, resultCode를 가지고 수정과 삭제를 구분해야 한다

        else if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_DELETE && data != null) { //BookSpecificActivity 종료후 실행되는 코드 (삭제)
            //삭제하기 전에 삭제데이터의 인덱스를 변수에 담는다
            int positionToDelete = data.getIntExtra("position", -1);

            //쉐어드에서 먼저 데이터를 삭제하고,리사이클러뷰를 삭제한다

            if (positionToDelete != -1) {
                //아이템 찾기
                ArrayList<String> bookItem = userReviewList.get(positionToDelete);
                String bookNameToDelete = bookItem.get(1).toString();
                bookNameToDelete = bookNameToDelete.replace(" ", ""); // 띄어쓰기를 제거

                //쉐어드에서 해당 데이터를 삭제한다
                //쉐어드에서 키가 abc123_인간관계론을 삭제하려면?
                SharedPreferences preferences = getSharedPreferences("책리뷰", MODE_PRIVATE);
                String keyToDelete = userId + "_" +bookNameToDelete; //쉐어드 키 만들기
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(keyToDelete);
                editor.apply();

                //어레이리스트에서 해당 데이터를 삭제합니다.
                userReviewList.remove(positionToDelete);
                //어댑터에게 데이터가 변경되었음을 알려 갱신하도록 만듭니다.
                bookRegisterAdapter.notifyItemRemoved(positionToDelete);

                //읽은 책 숫자 변경
                bookNum = userReviewList.size();
                textView_bookNum.setText(String.valueOf(bookNum));
            }

        } else if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_EDIT && data != null) { //BookSpecificActivity 종료후 실행되는 코드 (수정)
            //수정하기 전에 수정할 데이터의 인덱스를 변수에 담는다
            int positionToEdit = data.getIntExtra("position", -1);
            String rateNum = data.getStringExtra("rateNum");
            String rateMemo = data.getStringExtra("rateMemo");

            //쉐어드에서 먼저 해당 데이터를 수정하고, 리사이클러뷰를 수정한다
            if(positionToEdit != -1){
                //아이템 찾기
                ArrayList<String> bookItem = userReviewList.get(positionToEdit);
                String bookNameToEdit = bookItem.get(1).toString();
                bookNameToEdit =  bookNameToEdit.replace(" ", ""); // 띄어쓰기를 제거

                SharedPreferences preferences = getSharedPreferences("책리뷰", MODE_PRIVATE);
                String keyToEdit = userId + "_" +  bookNameToEdit;
                //1. 쉐어드 파일에서 해당 키에 대한 값을 가져옵니다
                String jsonValue = preferences.getString(keyToEdit, null);

                //2. 가져온 값을 수정한다
                if(jsonValue != null){
                    try {
                        JSONArray jsonArray = new JSONArray(jsonValue);

                        //6번째 인덱스를 rateNum으로 변경
                        jsonArray.put(6, rateNum);
                        // 7번째 인덱스를 rateMemo로 변경
                        jsonArray.put(7,rateMemo);

                        //수정된 JSON 배열을 다시 문자열로 변환
                        String updatedValue = jsonArray.toString();

                        //3. 수정된 값을 다시 쉐어드 파일에 저장합니다
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(keyToEdit, updatedValue);
                        // 기존의 문자열은 자동으로 사라지고 새로운 문자열로 교체됨,  하나의 키는 하나의 값만가짐!! 두개 못가져....
                        editor.apply();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    //4. userReviewList에서 해당 데이터 수정
                    //이유: 리사이클러뷰에 반영되기 위해서, 어댑터에 연결되어 있는 어레이리스트를 수정해야함
                    // 어레이리스트가 바뀌면, 어댑터가 알아서 화면을 바꿔줌 (단, notify 선언해야함)
                    if(positionToEdit != -1){

                        bookItem.set(6, rateNum);
                        // 6번째 인덱스를 rateNum으로 변경

                        bookItem.set(7,rateMemo);
                        // 7번째 인덱스를 rateMemo로 변경
                    }
                    //5. 어댑터에게 어레이리스트가 수정되었음을 알립니다.
                    bookRegisterAdapter.notifyItemChanged(positionToEdit);

                    //읽은 책 숫자 변경
                    bookNum = userReviewList.size();
                    textView_bookNum.setText(String.valueOf(bookNum));
                }
            }
        } else if (requestCode == MY_LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) { //ShowCurrentLocation 액티비티를 종료하고 실행됨
            // 인텐트로 넘겨받은 위도, 위도를 쉐어드 '프로필' 파일에 객체 => 문자열로 변환하여 저장하기

            // myLocation에다가 문자열 주소 할당하기
            String myLocation = data.getExtras().getString("myLocation"); //구글맵에서 온 사용자 위치 정보(문자열)
            textView_userLocation.setText(myLocation);
            double myLat = data.getDoubleExtra("myLat", 0); //기본값 0
            double myLng = data.getDoubleExtra("myLng", 0); //기본값 0

            //json object에 속성을 저장
            profileManager.setLocation(myLocation);
            profileManager.setLat(myLat);
            profileManager.setLng(myLng);

            //쉐어드에 저장
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(profileManager);
            prefsEditor.putString(userId, json);
            prefsEditor.commit();

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) { //유저 프로필 사진 설정, 갤러리 인텐트 끝나고 실행
            Uri uri = data.getData();
            try {
                // 선택한 이미지를 ImageView에 설정
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView_userPicture.setImageBitmap(bitmap);

                // 이미지를 파일로 저장
                imagePath = saveImageToFile(uri);
                // 이미지 파일의 경로를 저장
                // 이 imagePath를 필요한 곳에서 사용할 수 있습니다.

                //json object에 속성을 저장
                profileManager.setImagePath(imagePath);

                //쉐어드에 저장
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(profileManager);
                prefsEditor.putString(userId, json);
                prefsEditor.commit();

                //쉐어드 파일에 저장하기 (프로필- 이미지)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 이미지를 파일경로(String)으로 저장하는 메서드
    private String saveImageToFile(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File outputDir = getCacheDir();
        File outputFile = File.createTempFile("image", ".jpg", outputDir);

        OutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[4 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.close();

        return outputFile.getAbsolutePath(); // 파일 경로 반환
    }

}
