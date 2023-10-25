package com.android.neighborhoodbookshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;

public class bookReviewListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_review_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        Intent intent = getIntent();
        String value = intent.getStringExtra("bookName"); //사용자가 입력한 책이름

        SearchView searchView = findViewById(R.id.searchview0);
        searchView.setQuery(value, false);
        //첫 번째 매개변수는 추가할 텍스트입니다. 두 번째 매개변수는 검색어를 제출할지 여부를 나타내는 불리언 값입니다.
        searchView.setIconified(false); //커서가 깜빡거리는 상태로 유지

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