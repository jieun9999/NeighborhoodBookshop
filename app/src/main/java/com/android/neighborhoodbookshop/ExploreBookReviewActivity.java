package com.android.neighborhoodbookshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExploreBookReviewActivity extends AppCompatActivity {

    ArrayList<BookReviewItem> bookReviewItems;
    int position; //리사이클러뷰 아이템의 위치
    BookReviewItem bookReviewItem; //선택한 리사이클러뷰 아이템
    String key; //선택한 리사이클러뷰 아이템의 쉐어드 키
    String userId;

    //아이템의 유저이름, 유저사진, 유저위치
    String userName;
    String userImage;
    String userLocation;
    //유저 프로필 클래스
    ProfileManager profileManager;


    //화면에서 바뀔 항목 지정하기
    ImageView imageView;
    TextView textView_title;
    TextView textView_writer;
    TextView textView_company;
    TextView textView_date;
    TextView textView_rateNum;
    RatingBar ratingBar;
    TextView memo;

    //좋아요, 댓글 갯수
    int likeBtn_num = 0; //나중에는 쉐어드 파일에서 가져온 초기값 사용
    int chatBtn_num = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_book_review);

        //화면에서 바뀔 항목 지정하기
        imageView = findViewById(R.id.imageView2);
        textView_title = findViewById(R.id.book_title);
        textView_writer = findViewById(R.id.book_writer);
        textView_company = findViewById(R.id.book_company);
        textView_date = findViewById(R.id.book_date);
        textView_rateNum = findViewById(R.id.textView11);
        ratingBar = findViewById(R.id.rb);
        memo =findViewById(R.id.memo);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        bookReviewItems = BookReviewListActivity.bookReviewItems; //BookReviewListActivity의 bookReviewItems에서 가져옴
        //인텐트로 넘어온 데이터 받아오기
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);// 인덱스는 0부터 시작하기 초기값을 -1로 해주기

        //fullName key 를 얻었다 (userId_bookName_X)
        bookReviewItem = bookReviewItems.get(position);
        key = bookReviewItem.getKey();
        userId = key.split("_")[0];

        SharedPreferences sharedPreferences = getSharedPreferences("책리뷰", MODE_PRIVATE);
        String jsonData = sharedPreferences.getString(key, null);
        if(jsonData != null){
            try {
                JSONArray jsonArray = new JSONArray(jsonData);

                // 문자열 이미지 파일 경로를 URI로 변환
                Uri imageUri2 = Uri.parse(jsonArray.get(0).toString());
                imageView.setImageURI(imageUri2);
                textView_title.setText(jsonArray.get(1).toString());
                textView_writer.setText(jsonArray.get(2).toString());
                textView_company.setText(jsonArray.get(3).toString());
                textView_date.setText(jsonArray.get(4).toString());
                textView_rateNum.setText(jsonArray.get(6).toString());
                ratingBar.setRating(Float.parseFloat(jsonArray.get(6).toString()));
                memo.setText(jsonArray.get(7).toString());
                memo.setMovementMethod(new ScrollingMovementMethod());
                ratingBar.setIsIndicator(true); // 사용자 입력 비활성화

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //프로필 중 프로필 사진, 유저 위치 데이터를 가져온다.
        // 프로필 객체를 만들어준다. 객체를 먼저 만들어야, 해당 객체의 기능을 사용할 수 있다
        profileManager = new ProfileManager();
        //userId를 가지고 프로필을 찾는다
        SharedPreferences sharedPreferences2 = getSharedPreferences("프로필", MODE_PRIVATE);
        Gson gson = new Gson();
        String json= sharedPreferences2.getString(userId, null); //기본값 null
        //키인 userId로 뽑은 객체인 profileManager
        // profileManager가 null이 아닌 경우에만 쉐어드에서 뽑아와서 할당한다
        if(json != null){
            profileManager = gson.fromJson(json, ProfileManager.class);
        }

        userName = intent.getStringExtra("userName"); //userName만 인텐트로 가져오기
        userImage = profileManager.getImagePath();
        userLocation = profileManager.getLocation().substring(5);

        //하단의 프로필 내용 교체하기
        ImageView user_image = findViewById(R.id.imageView15);
        TextView user_name = findViewById(R.id.textView18);
        TextView user_location = findViewById(R.id.textView19);

        Uri imageUri = Uri.parse(userImage);
        user_image.setImageURI(imageUri);
        user_name.setText(userName);
        user_location.setText(userLocation);


        //좋아요, 댓글 기능 구현
        Button likeBtn = findViewById(R.id.button);
        Button chatBtn = findViewById(R.id.button5);

        // 초기 상태 설정
        final boolean[] isLikeClicked = {false};
        final boolean[] isChatClicked = {false};

        TextView like_num = findViewById(R.id.textView23);
        TextView chat_num = findViewById(R.id.textView21);

        //초기값
        like_num.setText(String.valueOf(likeBtn_num));
        chat_num.setText(String.valueOf(chatBtn_num));

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 토글 상태 변경
                isLikeClicked[0] = !isLikeClicked[0];
                // 배경 이미지 변경
                if (isLikeClicked[0]) {
                    likeBtn.setBackgroundResource(R.drawable.likebtn_clicked);
                    likeBtn_num += 1;
                } else {
                    likeBtn.setBackgroundResource(R.drawable.likebtn); // 원래 상태의 배경 이미지
                    likeBtn_num += -1;
                }
                like_num.setText(String.valueOf(likeBtn_num));
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 토글 상태 변경
                isChatClicked[0] = !isChatClicked[0];
                // 배경 이미지 변경
                if (isChatClicked[0]) {
                    chatBtn.setBackgroundResource(R.drawable.chatbtn_clicked);
                    chatBtn_num += 1;
                } else {
                    chatBtn.setBackgroundResource(R.drawable.chatbtn); // 원래 상태의 배경 이미지
                    chatBtn_num += -1;
                }
                chat_num.setText(String.valueOf(chatBtn_num));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}