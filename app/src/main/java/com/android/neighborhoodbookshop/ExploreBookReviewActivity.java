package com.android.neighborhoodbookshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class ExploreBookReviewActivity extends AppCompatActivity {

    //1. 다른 유저의 책리뷰를 볼 수 있음
    //2. 다른 유저의 책리뷰에 좋아요를 남길 수 있음 (색깔 진하게, +1)/ 쉐어드에 저장
    //3. 다른 유저의 책리뷰에 댓글을 남길 수 있음 (리사이클러뷰 사용)/ 쉐어드에 저장

    ArrayList<BookReviewItem> bookReviewItems; // BookReviewListActivity의 bookReviewItems에서 가져옴 (ex 총균쇠라고 칠때 나오는 총균쇠 모든 리뷰들)
    int position; //리사이클러뷰 아이템의 위치
    BookReviewItem bookReviewItem; //선택한 리사이클러뷰 아이템
    String key; //선택한 리사이클러뷰 책리뷰 아이템의 쉐어드 키
    String reviewUserId; // 선택한 리사이클러뷰 책리뷰 아이템의 작성자 아이디
    JSONArray jsonArray; // 책리뷰 쉐어드에서 가져온 데이터 어레이
    String jsonString; // 책리뷰 쉐어드에서 가져온 데이터 어레이를 문자열로 변환

    //아이템의 유저이름, 유저사진, 유저위치
    String review_userName;
    String review_userImage;
    String review_userLocation;

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
    TextView like_num;
    TextView chat_num;
    Button likeBtn;
    Button chatBtn;
    ImageView user_image;
    TextView user_name;
    TextView user_location;

    //좋아요, 댓글 갯수 변수
    int likeBtn_num; //쉐어드 파일에서 가져온 초기값 사용
    int chatBtn_num;

    // 좋아요 누가 눌렀는지 나타내는 변수 {"abc123":true,"kfc456":true}
    JSONObject userLikes;


    // 현재 로그인된 계정의 유저 아이디
    String userId;
    // 현재 로그인된 계정의 이름
    String userName;
    // 현재 로그인된 계정의 유저 사진
    String userImagePath;
    //현재 로그인된 계정의 유저 위치
    String userLocation;
    //유저가 남긴 코멘트
    String comment;
    //유저가 게시물 남긴 시간
    String time;
    ProfileManager commentProfileManager;


    //댓글 리사이클러뷰
    RecyclerView commentRecyclerView;
    //댓글 어댑터
    CommentAdapter commentAdapter;
    private ArrayList<CommentItem> commentList;
    //댓글 등록 버튼
    ImageView uploadBtn;
    //텍스트 인풋창
    TextInputEditText textInputEditText;
    CommentItem newCommentItem;
    ImageView closeBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_book_review);

        //UserManager에서 static으로 사용자 ID를 가져옴
        userId = UserManager.getUserId();

        //화면에서 바뀔 항목 지정하기
        //책리뷰
        imageView = findViewById(R.id.imageView2);
        textView_title = findViewById(R.id.book_title);
        textView_writer = findViewById(R.id.book_writer);
        textView_company = findViewById(R.id.book_company);
        textView_date = findViewById(R.id.book_date);
        textView_rateNum = findViewById(R.id.textView11);
        ratingBar = findViewById(R.id.rb);
        memo =findViewById(R.id.memo);

        //하단의 프로필
        user_image = findViewById(R.id.imageView15);
        user_name = findViewById(R.id.textView18);
        user_location = findViewById(R.id.textView19);

        //좋아요, 댓글
        likeBtn = findViewById(R.id.button);
        chatBtn = findViewById(R.id.button5);
        like_num = findViewById(R.id.textView23);
        chat_num = findViewById(R.id.textView21);
        userLikes = new JSONObject(); //userLikes 객체 초기화


        bookReviewItems = BookReviewListActivity.bookReviewItems; //BookReviewListActivity의 bookReviewItems에서 가져옴
        //인텐트로 넘어온 데이터 받아오기
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);// 인덱스는 0부터 시작하기 초기값을 -1로 해주기

        //fullName key 를 얻었다 (userId_bookName_X)
        bookReviewItem = bookReviewItems.get(position);
        key = bookReviewItem.getKey();
        reviewUserId = key.split("_")[0];

        SharedPreferences sharedPreferences = getSharedPreferences("책리뷰", MODE_PRIVATE);
        String jsonData = sharedPreferences.getString(key, null);
        if(jsonData != null){
            try {
                jsonArray = new JSONArray(jsonData);

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

                //만약 좋아요 갯수, 좋아요 불린, 댓글 갯수가 null 인지 존재하는지 구분해야함

                if(jsonArray.length() == 11){
                    //쉐어드에 좋아요 갯수, 불린, 댓글 있으면, 쉐어드에서 불러와서 내용 설정

                    //This modified code first checks the length of the JSON array (jsonArray.length())
                    // to make sure it has at least 10 elements before attempting to access elements at indices 8 and 9

                    likeBtn_num = jsonArray.getInt(8);
                    like_num.setText(String.valueOf(likeBtn_num));
                    userLikes = jsonArray.getJSONObject(9);

                    if((userLikes.has(userId))){ // userLikes에 userId가 존재하니? (불린 데이터 있지만, userId의 값은 없을 수도 있음)
                        if (userLikes.getBoolean(userId)) {
                            // userId의 불린값이 있고, true 면
                            likeBtn.setBackgroundResource(R.drawable.likebtn_clicked);
                        } else {
                            // userId의 불린값이 있고, false 면
                            likeBtn.setBackgroundResource(R.drawable.likebtn); // 원래 상태의 배경 이미지
                        }
                       // userLikes에 userId가 없을때
                    }else{
                        likeBtn.setBackgroundResource(R.drawable.likebtn); // 원래 상태의 배경 이미지
                        userLikes.put(userId, false); //userId false 값으로 초기화
                    }
                    chatBtn_num = jsonArray.getInt(10);
                    chat_num.setText(String.valueOf(chatBtn_num));

                }else if(jsonArray.length() == 10){ //8번째 인덱스는 반드시 likeNum 이라고 가정한다. (10번째 인덱스에 오는 데이터는 어레이리스트가 될 것임, 현재 jsonObject가 아닌, jsonArray의 한계)
                    //쉐어드에 좋아요 갯수, 불린 값만 있으면
                    likeBtn_num = jsonArray.getInt(8);
                    like_num.setText(String.valueOf(likeBtn_num));
                    userLikes = jsonArray.getJSONObject(9);

                    if((userLikes.has(userId))){ // userLikes에 userId가 존재하니? (불린 데이터 있지만, userId의 값은 없을 수도 있음)
                        if (userLikes.getBoolean(userId)) {
                            // userId의 불린값이 있고, true 면
                            likeBtn.setBackgroundResource(R.drawable.likebtn_clicked);
                        } else {
                            // userId의 불린값이 있고, false 면
                            likeBtn.setBackgroundResource(R.drawable.likebtn); // 원래 상태의 배경 이미지
                        }
                        // userLikes에 userId가 없을때
                    }else{
                        likeBtn.setBackgroundResource(R.drawable.likebtn); // 원래 상태의 배경 이미지
                        userLikes.put(userId, false); //userId false 값으로 초기화
                    }
                    //댓글은 초기값
                    chatBtn_num = 0;
                    chat_num.setText(String.valueOf(chatBtn_num));

                }else{
                    //쉐어드가 아예 없으면
                    // 모두 초기값
                    likeBtn_num = 0;
                    chatBtn_num = 0;
                    like_num.setText(String.valueOf(likeBtn_num));
                    likeBtn.setBackgroundResource(R.drawable.likebtn);
                    chat_num.setText(String.valueOf(chatBtn_num));
                    userLikes.put(userId, false); //userId false 값으로 초기화
                }
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
        String json= sharedPreferences2.getString(reviewUserId, null); //기본값 null
        //키인 userId로 뽑은 객체인 profileManager
        // profileManager가 null이 아닌 경우에만 쉐어드에서 뽑아와서 할당한다
        if(json != null){
            profileManager = gson.fromJson(json, ProfileManager.class);
        }

        review_userName = intent.getStringExtra("userName"); //userName만 인텐트로 가져오기
        review_userImage = profileManager.getImagePath();
        review_userLocation = profileManager.getLocation().substring(5);

        Uri imageUri = Uri.parse(review_userImage);
        user_image.setImageURI(imageUri);
        user_name.setText(review_userName);
        user_location.setText(review_userLocation);

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 토글 상태 변경
                try {
                    if(userLikes != null) {
                        // userId에 대한 값을 변경
                        userLikes.put(userId, !userLikes.getBoolean(userId)); //userId에 대한 값이 일단 존재하는 상태
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // 배경 이미지 변경
                try {
                    if (userLikes.getBoolean(userId)) {
                        likeBtn.setBackgroundResource(R.drawable.likebtn_clicked);
                        likeBtn_num += 1;
                    } else {
                        likeBtn.setBackgroundResource(R.drawable.likebtn); // 원래 상태의 배경 이미지
                        likeBtn_num += -1;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                like_num.setText(String.valueOf(likeBtn_num));
                //어레이리스트 상에서 값을 설정해주기
                try {
                    jsonArray.put(8, likeBtn_num); //8번째 인덱스에 좋아요 갯수 설정
                    jsonArray.put(9, userLikes); //9번째 인덱스에 좋아요 색깔 설정

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //책리뷰 관련 모든 데이터 쉐어드에 저장
                jsonString = jsonArray.toString();

                // 이제 jsonString을 SharedPreferences에 저장할 수 있습니다.
                SharedPreferences preferences = getSharedPreferences("책리뷰", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(key, jsonString);
                editor.apply();

            }
        });

        //바텀 다어얼로그 초기화
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ExploreBookReviewActivity.this); //바텀 다어얼로그
        View bottomSheetView = getLayoutInflater().inflate(R.layout.comment_layout, null); //바텀 다이얼로그 뷰
        bottomSheetDialog.setContentView(bottomSheetView);

        //바텀 시트 속 등록버튼, 입력창 초기화
        uploadBtn = bottomSheetView.findViewById(R.id.imageView17);
        textInputEditText = bottomSheetView.findViewById(R.id.input);
        commentRecyclerView = bottomSheetView.findViewById(R.id.recyclerView10);

        //다음 과정은 onCreate 바로 안에서 설정해준다. (btn.setOnClickListener 안이 아니라)
        //commentList의 형태 잡아줌
        //CommentItem : String imagePath, String userName, String userLocation, String comment, String time
        commentList = new ArrayList<CommentItem>();

        // error: int java.util.ArrayList.size()' on a null object reference
        //if CommentItem is the type of objects you want to store in the list, you should initialize it like this:
        //initiate adapter
        commentAdapter = new CommentAdapter();
        //onCreate 바로 안에 써주기
        //어댑터에게 리사이클러뷰 변동됨을 알림
        commentAdapter.setCommentList(commentList);

        //initiate recyclerview
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(ExploreBookReviewActivity.this));

        // 버튼 속 버튼 함수를 만들지 말고, 따로 따로 선언하는 것이 보기 좋다
        //바텀 다어얼로그 등장 설정
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //step1. 렌더링
                // 배경 이미지 변경
                chatBtn.setBackgroundResource(R.drawable.chatbtn_clicked);
                bottomSheetDialog.show();

                try {
                    jsonArray.put(10, chatBtn_num); // 10번째 인덱스에 댓글 갯수 설정
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                chat_num.setText(String.valueOf(chatBtn_num));
                // 댓글 수 쉐어드에 저장
                jsonString = jsonArray.toString();

                // 이제 jsonString을 SharedPreferences에 저장할 수 있습니다.
                SharedPreferences preferences = getSharedPreferences("책리뷰", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(key, jsonString);
                editor.apply();

            }
        });

        // 바텀 다이얼로그 사라지게
        closeBtn = bottomSheetView.findViewById(R.id.imageView18);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. String userImagePath, String userLocation에 해당하는 데이터를 가져온다
                // 프로필 객체를 만들어준다. 객체를 먼저 만들어야, 해당 객체의 기능을 사용할 수 있다
                commentProfileManager = new ProfileManager();
                //userId를 가지고 프로필을 찾는다
                SharedPreferences sharedPreferences2 = getSharedPreferences("프로필", MODE_PRIVATE);
                Gson gson = new Gson();
                String json= sharedPreferences2.getString(userId, null); //기본값 null
                //키인 userId로 뽑은 객체인 CommentProfileManager
                // CommentProfileManager가 null이 아닌 경우에만 쉐어드에서 뽑아와서 할당한다
                if(json != null){
                    commentProfileManager = gson.fromJson(json, ProfileManager.class);
                }

                // 2. String userName에 해당하는 데이터를 가져온다.
                SharedPreferences pref = getSharedPreferences("가입정보", MODE_PRIVATE);
                String jsonData = pref.getString(userId, null);
                if (jsonData != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        userName = jsonArray.getString(0);//쉐어드 유저이름을 저장할 변수
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                // 3. String comment, String time 데이터를 가져온다
                comment = textInputEditText.getText().toString();
                time = "10분 전"; // 추후 변경 예정
                newCommentItem = new CommentItem(commentProfileManager.getImagePath(), userName, commentProfileManager.getLocation().substring(5),comment,time);
                commentList.add(newCommentItem);

                //새롭게 아이템이 추가되었음을 알림
                commentAdapter.notifyItemInserted(commentList.size() -1);
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
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