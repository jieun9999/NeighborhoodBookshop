package com.android.neighborhoodbookshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class BookDirectRigisterActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_direct_rigister2);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false); //툴바 타이틀 안보이게

        final TextView textView = (TextView) findViewById(R.id.textView11);
        final RatingBar rb = (RatingBar) findViewById(R.id.rb);

        //사용자가 정한 별점으로 숫자가 바뀜
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                textView.setText(String.valueOf(rating));
            }
        });

        //이전 액티비티에서 책관련 정보 가져오기
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("Image");
        String book_title = intent.getStringExtra("BookTitle");
        String book_writer = intent.getStringExtra("BookWriter");
        String book_company = intent.getStringExtra("BookCompany");
        String book_date = intent.getStringExtra("BookDate");
        String book_isbn = intent.getStringExtra("BookIsbn");

        //레이아웃에서 바뀔 항목 정해두기
        ImageView picture = findViewById(R.id.imageView2);
        TextView title = findViewById(R.id.book_title);
        TextView writer = findViewById(R.id.book_writer);
        TextView company = findViewById(R.id.book_company);
        TextView date = findViewById(R.id.book_date);

        //사용자가 새로 입력한 항목
        TextView num = findViewById(R.id.textView11);
        TextInputEditText memo = findViewById(R.id.memo);

        //항목 내용 교체하기
        // 문자열 이미지 파일 경로를 URI로 변환
        Uri imageUri = Uri.parse(imagePath);
        picture.setImageURI(imageUri);
        title.setText(book_title);
        writer.setText(book_writer);
        company.setText(book_company);
        date.setText(book_date);

        Button buttonRegister = findViewById(R.id.button2);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //책 관련 정보 5가지는 위에 인자를 그대로 쓰면됨

                //사용자가 새롭게 입력한 평점과 메모를 가져온다
                String rateNum = num.getText().toString();
                String rateMemo = memo.getText().toString();

                // 활동 C에서 결과를 설정하기 위해 Intent 객체를 생성합니다.
                // 클래스 객체 형태로 합쳐서 전달하지 않고 각각 전달한다. - 직렬화 문제 땜에
                Intent resultIntent = new Intent();
                resultIntent.putExtra("imagePath", imagePath);
                resultIntent.putExtra("book_title", book_title);
                resultIntent.putExtra("book_writer", book_writer);
                resultIntent.putExtra("book_company", book_company);
                resultIntent.putExtra("book_date", book_date);
                resultIntent.putExtra("book_isbn",book_isbn);
                resultIntent.putExtra("rateNum", rateNum);
                resultIntent.putExtra("rateMemo", rateMemo);
                //setResult(RESULT_OK, result) 메서드를 호출하여 결과 코드와 함께 결과 Intent를 설정합니다.
                // RESULT_OK는 작업이 성공적으로 완료되었음을 나타내는 상수입니다.
                setResult(RESULT_OK, resultIntent);
                //활동 C를 종료
                finish();
            }
        });
    }

    //독서기록 액티비티2에서 내 평점, 메모를 기록한뒤 등록하기 버튼을 누르면, MainActivity 리사이클러뷰에 데이터가 추가된다

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 함때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}