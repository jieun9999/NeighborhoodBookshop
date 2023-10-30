package com.android.neighborhoodbookshop.mylibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.neighborhoodbookshop.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class BookDirectRegisterEditActivity extends AppCompatActivity {

    //관리할 어레이리스트 선언
    private ArrayList<ArrayList<String>> mArrayList;

    //데이터 값 변수설정
    String imagePath;
    String bookTitle;
    String bookWriter;
    String bookCompany;
    String bookDate;
    String bookIsbn;
    String rateNum;
    String rateMemo;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylibrary_book_direct_register_edit);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false); //툴바 타이틀 안보이게

        //인텐트로 넘어온 데이터 받아오기
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);// 인덱스는 0부터 시작하기 초기값을 -1로 해주기

        //sp(메인액티비티)에서 가져온 어레이리스트
        mArrayList = MainActivity.userReviewList;

        //인덱스로 아이템 찾기
        ArrayList bookItem = mArrayList.get(position); //인텐트로 가져온 position으로 아이템을 찾음

        imagePath =  bookItem.get(0).toString();
        bookTitle = bookItem.get(1).toString();
        bookWriter = bookItem.get(2).toString();
        bookCompany = bookItem.get(3).toString();
        bookDate = bookItem.get(4).toString();
        bookIsbn = bookItem.get(5).toString();
        rateNum = bookItem.get(6).toString();
        rateMemo = bookItem.get(7).toString();

        //화면에서 바뀔 항목 지정하기
        ImageView imageView = findViewById(R.id.imageView2);
        TextView textView_title = findViewById(R.id.book_title);
        TextView textView_writer = findViewById(R.id.book_writer);
        TextView textView_company = findViewById(R.id.book_company);
        TextView textView_date = findViewById(R.id.book_date);
        TextView textView_rateNum = findViewById(R.id.textView11);
        RatingBar rb = findViewById(R.id.rb);
        TextInputEditText memo = findViewById(R.id.memo);

        //처음에 등장하는 별표초기값 세팅
        rb.setRating(Float.parseFloat(rateNum));

        //사용자가 정한 별점으로 숫자가 바뀜
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                textView_rateNum.setText(String.valueOf(rating));
            }
        });


        Button edit_btn = findViewById(R.id.button2);

        // 문자열 이미지 파일 경로를 URI로 변환
        Uri imageUri = Uri.parse(imagePath);
        imageView.setImageURI(imageUri);
        textView_title.setText(bookTitle);
        textView_writer.setText(bookWriter);
        textView_company.setText(bookCompany);
        textView_date.setText(bookDate);

        //입력창에 기존에 썼던 값을 넣어줌
        textView_rateNum.setText(rateNum);
        memo.setText(rateMemo);


        //별점, 메모등은 사용자가 바꿀 수 있도록 설정해야 함
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //책 관련 정보 5가지는 위에 인자를 그대로 쓰면됨

                //사용자가 새롭게 입력한 평점과 메모를 가져온다
                String rateNum = textView_rateNum.getText().toString();
                String rateMemo = memo.getText().toString();

                // 클래스 객체 형태로 합쳐서 전달하지 않고 각각 전달한다. - 직렬화 문제 땜에
                Intent resultIntent = new Intent();
                resultIntent.putExtra("rateNum", rateNum);
                resultIntent.putExtra("rateMemo", rateMemo);
                resultIntent.putExtra("position", position);

                //setResult(RESULT_OK, result) 메서드를 호출하여 결과 코드와 함께 결과 Intent를 설정합니다.
                // RESULT_OK는 작업이 성공적으로 완료되었음을 나타내는 상수입니다.
                setResult(RESULT_OK, resultIntent);
                //활동 C를 종료 - 활동 B로 돌아감
                finish();
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