package com.android.neighborhoodbookshop.explore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.SearchView;

import com.android.neighborhoodbookshop.mylibrary.ProfileManager;
import com.android.neighborhoodbookshop.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

public class BookReviewListActivity extends AppCompatActivity {

    //검색한 책이름에 대한 여러 유저의 책리뷰 리스트를 렌더링합니다.
    RecyclerView recyclerView;
    BookReviewListAdapter bookReviewListAdapter;
    public static ArrayList<BookReviewItem> bookReviewItems; //static으로 변경 (static으로 공유하려면 초기화 x)
    //유저 프로필 클래스
    ProfileManager profileManager;
    String bookReview_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_book_review_list);

        //뒤로가기용 툴바
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        //서치뷰
        SearchView searchView = findViewById(R.id.searchview0);
        Intent intent = getIntent();
        String input = intent.getStringExtra("bookName"); //사용자가 입력한 책이름
        input = input.replaceAll("\\s", "");
        // \\s는 정규 표현식으로, 공백 문자(스페이스, 탭, 줄바꿈 등)를 의미합니다. 따라서 replaceAll("\\s", "")는 문자열에서 모든 공백을 제거합니다.

        //이전 액티비티에서 검색한 검색어를 그대로 가져와서 보여줌
        searchView.setQuery(input, false);
        //첫 번째 매개변수는 추가할 텍스트입니다. 두 번째 매개변수는 검색어를 제출할지 여부를 나타내는 불리언 값입니다.
        searchView.setIconified(false); //커서가 깜빡거리는 상태로 유지


        // RecyclerView에 Adapter와 LayoutManager 연결
        //1. 리사이클러뷰 레이아웃
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //2. 어댑터 초기화
        bookReviewListAdapter = new BookReviewListAdapter();
        //3. 리사이클러뷰와 어댑터 연결
        recyclerView.setAdapter(bookReviewListAdapter);
        //4. 리사이클러뷰와 레이아웃 매니저 연결
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //책리뷰 어레이리스트 초기화
        bookReviewItems = new ArrayList<>();
        // bookReviewItem은 책리뷰

        //step1 .shared preference에서 책리뷰 data를 가져옴
        SharedPreferences sharedPreferences = getSharedPreferences("책리뷰", MODE_PRIVATE);

        //책리뷰 쉐어드 파일의 모든 항목을 가져온다
        Map<String, ?> allEntries = sharedPreferences.getAll();

        // 책리뷰 쉐어드 파일에 대한 모든 항목을 도는 반복문이 실행된다
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {

            //키가 유저 input을 포함하는지를 검사한다
            if (entry.getKey().split("_")[1].contains(input)){

                String userId = entry.getKey().split("_")[0];

                //step2 .shared preference에서 가입정보 data를 가져옴
                //프로필 중 이름에 해당하는 데이터를 가져온다.
                SharedPreferences preferences = getSharedPreferences("가입정보", MODE_PRIVATE);
                String jsonData = preferences.getString(userId, null);
                if (jsonData != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        bookReview_userName = jsonArray.getString(0);//책리뷰 유저이름을 저장할 변수
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                //step3 .shared preference에서 프로필 data를 가져옴
                // 프로필 중 프로필 사진, 유저 위치 데이터를 가져온다.
                profileManager = new ProfileManager();
                // 프로필 객체를 만들어준다. 객체를 먼저 만들어야, 해당 객체의 기능을 사용할 수 있다
                SharedPreferences sharedPreferences2 = getSharedPreferences("프로필", MODE_PRIVATE);
                Gson gson = new Gson();
                String json= sharedPreferences2.getString(userId, null); //기본값 null
                //키인 userId로 뽑은 객체인 profileManager
                // profileManager가 null이 아닌 경우에만 쉐어드에서 뽑아와서 할당한다
                if(json != null){
                    profileManager = gson.fromJson(json, ProfileManager.class);
                }

                //현재 항목에 대한 값을 저장한다
                String value = entry.getValue().toString();
                //현재 항목에 대한 키를 뽑는다, 키를 그대로 전달하기 위해서...
                String key = entry.getKey().toString();

                //쉐어드에서 뽑은 데이터를 속성으로 가지는 BookReviewItem을 만들고,
                // bookReviewItems에 저장한다 (읽기만)
                try {
                    String jsonString = Html.fromHtml(value).toString();
                    JSONArray jsonArray = new JSONArray(jsonString);
                    //bookReviewItems에 아이템을 추가
                    bookReviewItems.add(new BookReviewItem(key, jsonArray.get(1).toString(), jsonArray.get(0).toString(), jsonArray.get(2).toString(), profileManager.getImagePath(), bookReview_userName, profileManager.getLocation()));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            }

        bookReviewListAdapter.setArrayList(bookReviewItems);
        // notifyDataSetChanged();
        // 어레이리스트가 변경되었음을 어댑터에게 알린다
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