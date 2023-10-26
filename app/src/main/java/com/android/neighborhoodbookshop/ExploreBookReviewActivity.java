package com.android.neighborhoodbookshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

public class ExploreBookReviewActivity extends AppCompatActivity {

    ArrayList<BookReviewItem> bookReviewItems;
    int position; //리사이클러뷰 아이템의 위치
    BookReviewItem bookReviewItem; //선택한 리사이클러뷰 아이템
    String key; //선택한 리사이클러뷰 아이템의 쉐어드 키


    //화면에서 바뀔 항목 지정하기
    ImageView imageView;
    TextView textView_title;
    TextView textView_writer;
    TextView textView_company;
    TextView textView_date;
    TextView textView_rateNum;
    RatingBar ratingBar;
    TextView memo;


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

        SharedPreferences sharedPreferences = getSharedPreferences("책리뷰", MODE_PRIVATE);
        String jsonData = sharedPreferences.getString(key, null);
        if(jsonData != null){
            try {
                JSONArray jsonArray = new JSONArray(jsonData);

                // 문자열 이미지 파일 경로를 URI로 변환
                Uri imageUri = Uri.parse(jsonArray.get(0).toString());
                imageView.setImageURI(imageUri);
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