package com.android.neighborhoodbookshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class BookSpecificActivity extends AppCompatActivity {

    private static final int RESULT_DELETE = 40;
    private static final int RESULT_EDIT = 120;
    private static final int REQUEST_CODE_EDIT = 70;

    //관리할 어레이리스트 선언
    private ArrayList<ArrayList<String>> mArrayList;

    //화면에서 바뀔 항목 지정하기
    ImageView imageView;
    TextView textView_title;
    TextView textView_writer;
    TextView textView_company;
    TextView textView_date;
    TextView textView_rateNum;
    RatingBar ratingBar;
    TextView memo;

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

    boolean isDataModified = false; // 수정 여부 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_specific);

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
        imageView = findViewById(R.id.imageView2);
        textView_title = findViewById(R.id.book_title);
        textView_writer = findViewById(R.id.book_writer);
        textView_company = findViewById(R.id.book_company);
        textView_date = findViewById(R.id.book_date);
        textView_rateNum = findViewById(R.id.textView11);
        ratingBar = findViewById(R.id.rb);
        memo =findViewById(R.id.memo);

        // 문자열 이미지 파일 경로를 URI로 변환
        Uri imageUri = Uri.parse(imagePath);
        imageView.setImageURI(imageUri);
        textView_title.setText(bookTitle);
        textView_writer.setText(bookWriter);
        textView_company.setText(bookCompany);
        textView_date.setText(bookDate);
        textView_rateNum.setText(rateNum);
        ratingBar.setRating(Float.parseFloat(rateNum));
        memo.setText(rateMemo);
        memo.setMovementMethod(new ScrollingMovementMethod());
        ratingBar.setIsIndicator(true); // 사용자 입력 비활성화

        //LayoutInflater는 XML 레이아웃 파일을 Java 코드로 인플레이트(팽창)하는 데 사용되는 클래스입니다. 이 줄에서는 getSystemService 메서드를 사용하여 LAYOUT_INFLATER_SERVICE 상수로 LayoutInflater 인스턴스를 얻습니다.
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater.inflate() 메서드를 사용하여 R.layout.bottom_sheet로 지정된 XML 레이아웃 파일을 View 객체로 인플레이트합니다
        View view = inflater.inflate(R.layout.specific_bottom_sheet, null,false);
        //BottomSheetDialog 클래스의 인스턴스를 생성합니다. 이 클래스는 하단 시트 다이얼로그를 나타내며, this는 현재 액티비티를 나타냅니다.
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        // 이전에 인플레이트한 view를 bottomSheetDialog의 컨텐츠로 설정,  하단 시트 다이얼로그의 내용으로 view가 표시됨
        bottomSheetDialog.setContentView(view);

        //점3 버튼 누르면, 나오는 하단의 다이얼로그 (bottomsheetdialog)
        ImageView settingBtn = (ImageView) findViewById(R.id.dotsbtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show(); //다이얼로그 등장
            }
        });

        // 하단 다이얼로그의 버튼들 (수정, 삭제, 취소)
        TextView 수정하기 = view.findViewById(R.id.textView12);
        TextView 삭제하기 = view.findViewById(R.id.textView9);
        TextView 취소 = view.findViewById(R.id.box2);

        //다이얼로그의 수정하기
        수정하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 수정용 액티비티로 이동하기
                bottomSheetDialog.dismiss(); //다이얼로그 사라짐, 닫아주지 않으면 메모리 누수 문제가 생길 수 있음
                //요청코드와 함께 보낸다
                Intent intent = new Intent(BookSpecificActivity.this, BookDirectRegisterEditActivity.class);
                // 인텐트로 현재 상세화면 액티비티에 있는 정보들을 모두 보내준다

                intent.putExtra("position", position); //클릭한 항목의 position을 수정용 액티비티에 넘겨준다

                startActivityForResult(intent, REQUEST_CODE_EDIT); //상수코드와 함께 상세화면 액티비티로 이동
            }
        });

        //다이얼로그의 삭제하기
        삭제하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss(); // //다이얼로그 사라짐, 닫아주지 않으면 메모리 누수 문제가 생길 수 있음
                // 메인액티비티로 돌아간다 : + 삭제할 아이템의 position을 가지고
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position); //삭제할 항목의 위치를 전달한다
                //setResult(RESULT_DELETE, result) 메서드를 호출하여 결과 코드와 함께 수정결과 Intent를 설정합니다.
                setResult(RESULT_DELETE, resultIntent);
                finish();// 해당 액티비티 종료
            }
        });


        취소.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss(); //다이얼로그 사라짐
            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                //수정여부를 확인하고 수정된 경우에만 데이터를 메인 액티비티로 전송
                if(isDataModified){
                    Intent resultIntent = new Intent();
                    // 수정할 데이터를 인텐트와 함께 보내야 한다
                    resultIntent.putExtra("position", position); //수정할 항목의 위치를 전달한다
                    resultIntent.putExtra("rateNum", rateNum);
                    resultIntent.putExtra("rateMemo", rateMemo);

                    //setResult(RESULT_EDIT, result) 메서드를 호출하여 결과 코드와 함께 수정결과 Intent를 설정합니다.
                    setResult(RESULT_EDIT, resultIntent);
                    finish();
                }else{
                    // 수정되지 않았을 때는 그냥 뒤로가기 버튼 동작
                    finish();
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK && data != null){ //BookDirectRegidterEditActivity 종료 후에 실행되는 코드
            int positionToEdit = data.getIntExtra("position", -1);
            if(positionToEdit != -1){

                //수정한 데이터를 가져오고, BookSpecificActivity에서 내용을 교체한다.
                //책정보는 sp에서 가져오고, 나머지 새롭게 입력한 내용들은 직접 교체해준다.

                //인덱스로 아이템 찾기
                ArrayList bookItem = mArrayList.get(positionToEdit); //인텐트로 가져온 position으로 아이템을 찾음

                imagePath = bookItem.get(0).toString();
                bookTitle =  bookItem.get(1).toString();
                bookWriter =  bookItem.get(2).toString();
                bookCompany =  bookItem.get(3).toString();
                bookDate = bookItem.get(4).toString();
                bookIsbn = bookItem.get(5).toString();

                rateNum = data.getStringExtra("rateNum");
                rateMemo = data.getStringExtra("rateMemo");

                // 문자열 이미지 파일 경로를 URI로 변환
                Uri imageUri = Uri.parse(imagePath);
                imageView.setImageURI(imageUri);
                textView_title.setText(bookTitle);
                textView_writer.setText(bookWriter);
                textView_company.setText(bookCompany);
                textView_date.setText(bookDate);
                textView_rateNum.setText(rateNum);
                ratingBar.setRating(Float.parseFloat(rateNum));
                memo.setText(rateMemo);
                memo.setMovementMethod(new ScrollingMovementMethod());
                ratingBar.setIsIndicator(true); // 사용자 입력 비활성화

                // 데이터가 수정되었으므로 isDataModified를 true로 설정
                isDataModified = true;

            }

        }
    }


}