package com.android.neighborhoodbookshop.bookclub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.net.Uri;
import android.widget.TextView;


import com.android.neighborhoodbookshop.explore.ExploreActivity;
import com.android.neighborhoodbookshop.R;
import com.android.neighborhoodbookshop.setting.SettingActivity;
import com.android.neighborhoodbookshop.timer.TimerActivity;
import com.android.neighborhoodbookshop.mylibrary.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class BookClubActivity extends AppCompatActivity {

    //북클럽 리사이클러뷰 세팅
    private Context mContext;
    // context를 사용하여 앱의 리소스 (레이아웃 파일, 문자열, 이미지 등에 접근할 수 있습니다.)
    // 예를 들어 getApplicationContext()를 호출하여 앱의 전역 컨텍스트를 가져올 수 있으며, 이를 통해 리소스에 접근할 수 있습니다.
    private ArrayList<BookClubItem> mArrayList;
    private BookClubAdapter bookClubAdapter;
    private RecyclerView mRecyclerView;

    //+ 버튼
    private ImageView btn_plus;

    //다이얼로그의 사진
    private ImageView picture;
    //다이얼로그 이미지 경로
    private String imagePath;
    
    //하단의 9가지 카테고리 버튼에 대한 변수들
    int[] category = new int[9];
    //선택된 2가지 카테고리 변수
    int val_category1 = 0;
    int val_category2 = 0;
    // 카테고리 버튼 id를 배열로 관리
    int[] categoryButtonsIds = {
            R.id.btn_it,
            R.id.btn_econo,
            R.id.btn_marketing,
            R.id.btn_social,
            R.id.btn_art,
            R.id.btn_human,
            R.id.btn_literature,
            R.id.btn_invest,
            R.id.btn_poem
    };
    //카테고리 이미지 id를 배열로 관리
    int[] categoryImgIds ={
            R.drawable.it,
            R.drawable.eco,
            R.drawable.market,
            R.drawable.social,
            R.drawable.art,
            R.drawable.human,
            R.drawable.liter,
            R.drawable.invest,
            R.drawable.poem
    };

    // 처음에는 모든 원소가 false로 설정되어 있음
    // 각 버튼은 고유한 상태를 가져야 하므로, 각 버튼의 상태를 따로 추적해야 합니다.
    // 이를 위해서는 buttonStates 배열과 같이 각 버튼마다 별도의 상태를 저장할 배열이 필요합니다.
    boolean[] buttonStates = new boolean[categoryButtonsIds.length];

    //하단 5버튼
    LinearLayout 내서재;
    LinearLayout 탐색;
    LinearLayout 북클럽;
    LinearLayout  타이머;
    LinearLayout 설정;

    private static final int PICK_IMAGE_REQUEST = 1; // 어떤 정수 값도 사용 가능


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookclub_main);

        //북클럽 리사이클러뷰에 속하는 뷰 가져오기
        mContext = getApplicationContext();
        btn_plus = findViewById(R.id.plusbtn);
        mRecyclerView = findViewById(R.id.recycler);

        //레이아웃메니저는 리사이클러뷰의 항목 배치를 어떻게 할지 정하고, 스크롤 동작도 정의한다.
        //수평/수직 리스트 LinearLayoutManager //그리드 리스트 GridLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // mArrayList라는 ArrayList 객체를 생성합니다. 이 객체는 RecyclerView에 표시할 데이터를 저장하는 데 사용될 것입니다.
        mArrayList = new ArrayList<>();
        //초기값 세팅
        mArrayList.add(new BookClubItem("/data/user/0/com.android.neighborhoodbookshop/cache/image6283166426862524173.jpg","세상을 보는 시각","온라인","세상을 바꾸는 사람들의 이야기를 읽습니다\n" +
                "[스타트업]은 이 사회에 풀고 싶은 문제가 있는 사람들, 기존 질서를 넘어 혁신을 이루고 싶은 꿈을 가진 사람들을 위한 클럽입니다. 예비 창업자, 스타트업 종사자, 스타트업에 관심을 갖기 시작한 분, 스타트업의 문제 해결 방식이 궁금한 분, 그 누구라도 상관없습니다.\n" +
                "\n" +
                "\n" +
                "\n" +
                "세상을 바꾼 사람들은 어떻게 기존 질서를 부수고 우리의 삶을 변화시켰을까요? 불가능한 것 같은 도전을 어떻게 현실로 만들었을까요? 그들의 이야기를 읽으며, 나만의 아이디어를 탄생시켜 봅시다. 지금에 만족하지 않고 기준을 높이고 싶은 분들, 환영합니다.",R.drawable.eco,R.drawable.market));
        mArrayList.add(new BookClubItem("/data/user/0/com.android.neighborhoodbookshop/cache/image2967304806740514114.jpg", "일상적 글쓰기", "오프라인","인문학으로 내 인생의 무게중심 잡기\n" +
                "마이클 샌델, 유발 하라리, 스티브 잡스가 몰고 온 인문학 열풍. 인문학은 내 인생에 어떤 도움이 되는 걸까요? [인문스테디]에서는 인간으로 사는 ‘나’에게 가장 중요한 가치가 무엇인지 들여다 볼 겁니다.\n" +
                "\n" +
                "\n" +
                "\n" +
                "내 삶을 지탱하는 그것들(행복? 돈? 관계?)의 우선순위를 정해봅시다. 어떤 폭풍우에도 흔들리지 않는 무게중심을 가진, 주체적인 나의 세계를 함께 구축해 봐요. 너무 어렵지 않게, 차근차근 스테디하게- 함께해요!", R.drawable.human,R.drawable.liter));

        // bookClubAdapter 클래스의 객체를 생성합니다.
        // mContext는 액티비티에서 가져온 Context 객체로, 어댑터가 액티비티의 리소스에 접근하고 상호 작용할 때 사용됩니다.
        // 이 어댑터는 RecyclerView와 데이터(mContext, mArrayList)를 연결해주는 역할을 합니다.

//        북클럽 어레이리스트 수정해야함
        bookClubAdapter = new BookClubAdapter(mContext, mArrayList);

        // RecyclerView에 어댑터를 설정합니다.
        // 이렇게 하면 어댑터가 RecyclerView와 연결되어 데이터를 표시하고 스크롤할 수 있게 됩니다. 이후에는 어댑터가 데이터를 갱신하면 RecyclerView에 자동으로 반영됩니다.
        mRecyclerView.setAdapter(bookClubAdapter);

        //1. 데이터 추가
        // + 버튼을 입력하면, 다이얼로그가 등장한다.
        // 다이얼로그에 내용을 입력한 후, 등록을 누르면, 어레이리스트에 데이터를 담는다.
        // 그리고 리사이클러뷰에 띄운다.
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Arrays.fill(buttonStates, false); //다이얼로그를 새로 열때마다 모든 버튼 색값이 false(회색)로 초기화
                val_category1 = 0;
                val_category2 = 0;//val_category1 val_category2 각각 초기화
                addItem();

            }
        });

        //2. 각 아이템에 대한 수정과 삭제
        // bookClubAdapter의 메서드 setOnItemClickListener를 호출하고, 이 메서드에 Adapter.OnItemClickListener라는 인터페이스를 구현한 객체를 인자로 넣고 있다.
        // bookClubAdapter에 아이템 클릭 이벤트 리스너를 설정하여 RecyclerView의 아이템(수정, 삭제)이 클릭되었을 때 onEditClick메서드, onDeleteClick메서드가 호출되도록 만드는 것입니다.

        bookClubAdapter.setOnItemClickListener(new BookClubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //바텀시트 다이얼로그를 보여준다.
                //띄울때 해당 아이템 정보로 교체되어있어야 함

                //LayoutInflater는 XML 레이아웃 파일을 Java 코드로 인플레이트(팽창)하는 데 사용되는 클래스입니다. 이 줄에서는 getSystemService 메서드를 사용하여 LAYOUT_INFLATER_SERVICE 상수로 LayoutInflater 인스턴스를 얻습니다.
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //inflater.inflate() 메서드를 사용하여 R.layout.bottom_sheet로 지정된 XML 레이아웃 파일을 View 객체로 인플레이트합니다
                View view = inflater.inflate(R.layout.dialog_bookclub_specific_bottom_sheet, null, false);
                //BottomSheetDialog 클래스의 인스턴스를 생성합니다. 이 클래스는 하단 시트 다이얼로그를 나타내며, this는 현재 액티비티를 나타냅니다.
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BookClubActivity.this);
                // 이전에 인플레이트한 view를 bottomSheetDialog의 컨텐츠로 설정,  하단 시트 다이얼로그의 내용으로 view가 표시됨
                bottomSheetDialog.setContentView(view);

                //기존의 내용들을 변수에 저장한다
                BookClubItem bookClubItem = mArrayList.get(position);
                String imagePath = bookClubItem.getBookclub_image();
                String name = bookClubItem.getBookclub_name();
                String online = bookClubItem.getBookclub_online();
                String introduce = bookClubItem.getBookclub_introduce();
                int category1 = bookClubItem.getBookclub_category1();
                int category2 = bookClubItem.getBookclub_category2();

                //바텀시트 다이얼로그에서 교체할 부분을 설정한다
                ImageView imageView_picture = view.findViewById(R.id.imageView);
                TextView textView_name = view.findViewById(R.id.title);
                TextView textView_online = view.findViewById(R.id.online);
                TextView textView_introduce = view.findViewById(R.id.introduce);
                ImageView imageView_category1 = view.findViewById(R.id.category1);
                ImageView imageView_category2 = view.findViewById(R.id.category2);

                //바텀시트 다이얼로그 내용 교체

                // 문자열 이미지 파일 경로를 URI로 변환
                Uri imageUri = Uri.parse(imagePath);
                imageView_picture.setImageURI(imageUri);
                textView_name.setText(name);
                textView_online.setText(online);
                textView_introduce.setText(introduce);
                imageView_category1.setImageResource(category1); //이미지가 int로 저장된 경우에는 setImageResource 사용!!!
                imageView_category2.setImageResource(category2);

                bottomSheetDialog.show();
            }

            //수정버튼
            @Override
            public void onEditClick(View v, int position) {
                //기존의 내용들을 변수에 저장한다
                BookClubItem bookClubItem = mArrayList.get(position);

                //뷰홀더에서 각각 어떤 항목을 교체할지 적음
                String imagePath = bookClubItem.getBookclub_image();
                String name = bookClubItem.getBookclub_name();
                String online = bookClubItem.getBookclub_online();
                String introduce = bookClubItem.getBookclub_introduce();
                int category1 = bookClubItem.getBookclub_category1();
                int category2 = bookClubItem.getBookclub_category2();

                //기존의 내용을 인자로 가져가면서, editItem 메소드 실행
                editItem(imagePath,name,online,introduce,category1,category2, position);

            }

            //삭제버튼
            @Override
            public void onDeleteClick(View v, int position) {
               //어레이리스트에서 값을 삭제한다
                mArrayList.remove(position);
                bookClubAdapter.notifyItemRemoved(position);
            }
        });

        //하단 5버튼
        내서재 = (LinearLayout) findViewById(R.id.mylibrary);
        탐색 = (LinearLayout) findViewById(R.id.explore);
        북클럽 = (LinearLayout) findViewById(R.id.bookclub);
        타이머 = (LinearLayout) findViewById(R.id.timer);
        설정 = (LinearLayout) findViewById(R.id.setting);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.mylibrary){
                    Intent myIntent = new Intent(BookClubActivity.this, MainActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.explore) {
                    Intent myIntent = new Intent(BookClubActivity.this, ExploreActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.timer) {
                    Intent myIntent = new Intent(BookClubActivity.this, TimerActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.setting) {
                    Intent myIntent = new Intent(BookClubActivity.this, SettingActivity.class);
                    startActivity(myIntent);
                }
            }
        };

        내서재.setOnClickListener(listener);
        탐색.setOnClickListener(listener);
        타이머.setOnClickListener(listener);
        설정.setOnClickListener(listener);

    }



    // AlertDialog를 사용해서 데이터를 추가한다
    // AlertDialog.Builder 다양한 대화 상자(Dialog)를 생성하고 구성하기 위한 유용한 클래스
    private void addItem(){

        //AlertDialog.Builder를 사용하여 AlertDialog를 생성합니다.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // R.layout.alert_dialog 레이아웃을 인플레이트하여 대화상자에 표시할 뷰를 설정합니다.
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bookclub_add_item, null, false);
        // 빌더 객체에서 뷰를 보여줍니다.
        builder.setView(view);
        //dialog 변수를 사용하여 AlertDialog를 표시하거나 닫을 수 있습니다.
        // 예를 들어, dialog.show()를 호출하여 AlertDialog를 화면에 표시하거나 dialog.dismiss()를 호출하여 AlertDialog를 닫음
        final AlertDialog dialog = builder.create();


        //다이얼로그의 등록/취소 버튼
        final Button btn_cancel = view.findViewById(R.id.btn_cancel);
        final Button btn_add = view.findViewById(R.id.btn_add);

        //다이얼로그의 입력창들
        //액티비티 레이아웃이 아닌 다이얼로그의 레이아웃이기때문에 모두 맨 앞에 view를 붙여준다.
        picture = view.findViewById(R.id.bookclub_picture); //onActivityResult의 결과로 picture.setImageBitmap(bitmap);
        final EditText name = view.findViewById(R.id.bookclub_name);
        final Switch switch_online = view.findViewById(R.id.switch_online);
        final EditText introduce = view.findViewById(R.id.bookclub_introduce);
        //카테고리 버튼에 대한 설정은 아래의 반복문으로 해결

        //기본적으로 addItem 실행되면 다이얼로그 보여줌
        dialog.show();

        //다이얼로그 크기 조정
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        // 너비와 높이 설정
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // 화면 너비에 맞게 설정
        layoutParams.height = 2000; // 원하는 높이 설정
        // 레이아웃 파라미터 설정 적용
        dialog.getWindow().setAttributes(layoutParams);

        //다이얼로그에서 갤러리 열기 버튼 클릭
        final ImageView open_gallery = view.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리 열기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        // 각 카테고리 버튼들에 대한 OnClickListener를 설정
        //카테고리 :category0 부터 category8 중에 이미지 리소스가 할당 된 변수만 각각 int val_category1, val_category2 변수에 할당되게 함
        for (int i = 0; i < categoryButtonsIds.length; i++) {
            //뷰에서 각 카테고리 버튼의 위치를 설정하기
            Button categoryButton = view.findViewById(categoryButtonsIds[i]);
            int index = i;
            categoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //만약에 버튼 색깔이 회색이면 보라색으로 변경하고,
                    // 버튼 색깔이 보라색이면, 회색으로 변화시킴
                        int grayColor = Color.parseColor("#D9D9D9");
                        int purpleColor = Color.parseColor("#B2A4FF");
                        if(!buttonStates[index]){ //특정 인덱스의 버튼이 클릭되면
                            view.setBackgroundColor(purpleColor);
                            //버튼 색깔이 보라색으로 변경될때 해당 카테고리에 이미지 리소스 할당
                            category[index] = categoryImgIds[index];
                            // 이미지 리소스가 초기화되면 변수도 초기화
                            if (val_category1 == 0) {
                                val_category1 = categoryImgIds[index];
                            } else if ( val_category2 == 0) {
                                val_category2 = categoryImgIds[index];
                            }
                        } else {
                            view.setBackgroundColor(grayColor);
                            // 버튼 색깔이 회색으로 변경될때, 해당 카테고리 이미지 리소스 초기화
                            category[index] = 0;
                            //이미지 리소스가 할당되면, 각각 선택된 변수에 할당함
                            //문제: 이미 val_category1와 val_category2가 0이 아니기 때문에 재할당이 불가능함
                            //따라서 새롭게 다이얼로그가 열리는 addItem 함수 직전에 val_category1와 val_category2를 각각 0으로 초기화시킴
                            if (val_category1 == categoryImgIds[index]) {
                                val_category1 = 0;
                            } else if (val_category2 == categoryImgIds[index]) {
                                val_category2 = 0;
                            }
                        }
//                    각 버튼은 고유한 상태를 가져야 하므로, 각 버튼의 상태를 따로 추적해야 합니다.
//                    이를 위해서는 buttonStates 배열과 같이 각 버튼마다 별도의 상태를 저장할 배열이 필요합니다.
                      buttonStates[index] = !buttonStates[index]; //회색 => 보라색 => 회색 가능하게
                }
        });
    }

        //다이얼로그의 취소 버튼 클릭
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //다이얼로그의 등록 버튼 클릭
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다이얼로그에 새롭게 입력한 사진, 이름, 온라인여부, 한줄소개, 카테고리 2가지를 가져온다
                //사진: 하단에서 onActivityResult 함수를 통해 이미지 리소스로 담는다

                String val_name = name.getText().toString();
                //온라인 여부: 스위치 불린 값을 담는다
                String val_switch_online;
                if (switch_online.isChecked()) {
                    val_switch_online = "온라인";
                } else {
                    val_switch_online = "오프라인";
                }
                String val_introduce = introduce.getText().toString();

                //입력한 변수로 bookclubitem을 추가한다 (인자를 타입을 고려하여 잘 넣어준다)
                BookClubItem bookClubItem = new BookClubItem(imagePath,val_name, val_switch_online,val_introduce,val_category1, val_category2);

                //어레이리스트에다 data를 추가한다
                mArrayList.add(bookClubItem);

                //어댑터에게 리사이클러뷰에 새로운 아이템이 추가되었음을 알린다
                bookClubAdapter.notifyItemInserted(mArrayList.size()-1);

                //다이얼로그 사라짐
                dialog.dismiss();

            };


    });


}

    //AlertDialog를 사용해서 데이터를 수정한다
    private void editItem(String imagePath, String name, String online, String introduce, int category1, int category2, int position){
        //AlertDialog.Builder를 사용하여 AlertDialog를 생성합니다.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // R.layout.alert_dialog 레이아웃을 인플레이트하여 대화상자에 표시할 뷰를 설정합니다.
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bookclub_edit_item, null, false);
        // 빌더 객체에서 뷰를 보여줍니다.
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        //dialog 변수를 사용하여 AlertDialog를 표시하거나 닫을 수 있습니다.
        // 예를 들어, dialog.show()를 호출하여 AlertDialog를 화면에 표시하거나 dialog.dismiss()를 호출하여 AlertDialog를 닫음

        //다이얼로그의 등록/취소 버튼
        final Button btn_cancel = view.findViewById(R.id.btn_cancel);
        final Button btn_add = view.findViewById(R.id.btn_add);

        //다이얼로그의 입력창들
        //액티비티 레이아웃이 아닌 다이얼로그의 레이아웃이기때문에 모두 맨 앞에 view를 붙여준다.
        picture = view.findViewById(R.id.bookclub_picture);
        final EditText name_edit = view.findViewById(R.id.bookclub_name);
        final Switch switch_online = view.findViewById(R.id.switch_online);
        final EditText introduce_edit = view.findViewById(R.id.bookclub_introduce);


        //기존에 있던 이름, 숫자 값으로 할당함
        //다이얼로그가 뜨면 기존값을 그대로 불러온 채로 edit할 수 있게 만들어줌
        // 문자열 이미지 파일 경로를 URI로 변환
        Uri imageUri = Uri.parse(imagePath);
        picture.setImageURI(imageUri);
        name_edit.setText(name);

        if(online == "온라인"){
            switch_online.setChecked(true);
        }else {
            switch_online.setChecked(false);
        }

        introduce_edit.setText(introduce);

        //카테고리: 어떤 categoryImgIds(이미지 리소스 id)와 일치할 경우, 그 버튼을 찾아서, 색깔을 보라색으로 만든다
        for (int i = 0; i < categoryImgIds.length; i++) {
            int purpleColor = Color.parseColor("#B2A4FF");
            if(category1 == categoryImgIds[i]){
                //그 인덱스의 버튼의 색깔을 보라색으로 만들기
                Button btn1 = view.findViewById(categoryButtonsIds[i]);
                btn1.setBackgroundColor(purpleColor);
            }
            if(category2 == categoryImgIds[i]){
                Button btn2 = view.findViewById(categoryButtonsIds[i]);
                btn2.setBackgroundColor(purpleColor);
            }
        }


        //기본적으로 editItem 실행되면 다이얼로그 보여줌
        dialog.show();

        //레이아웃 크기 조정
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        // 너비와 높이 설정
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // 화면 너비에 맞게 설정
        layoutParams.height = 2000; // 원하는 높이 설정
        // 레이아웃 파라미터 설정 적용
        dialog.getWindow().setAttributes(layoutParams);


        //다이얼로그에서 갤러리 열기 버튼 클릭
        final ImageView open_gallery = view.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리 열기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_REQUEST);
            }
        });

        // 각 카테고리 버튼들에 대한 OnClickListener를 설정
        //카테고리 :category1 부터 category9 중에 이미지 리소스가 할당 된 변수만 각각 int val_category1, val_category2 변수에 할당되게 함
        for (int i = 0; i < categoryButtonsIds.length; i++) {
            //뷰에서 각 카테고리 버튼의 위치를 설정하기
            Button categoryButton = view.findViewById(categoryButtonsIds[i]);
            int index = i;
            categoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //만약에 버튼 색깔이 회색이면 보라색으로 변경하고,
                    // 버튼 색깔이 보라색이면, 회색으로 변화시킴
                    int grayColor = Color.parseColor("#D9D9D9");
                    int purpleColor = Color.parseColor("#B2A4FF");
                    if(!buttonStates[index]){ //특정 인덱스의 버튼이 클릭되면
                        view.setBackgroundColor(purpleColor);
                        //버튼 색깔이 보라색으로 변경될때 해당 카테고리에 이미지 리소스 할당
                        category[index] = categoryImgIds[index];
                        // 이미지 리소스가 초기화되면 변수도 초기화
                        if (val_category1 == 0) {
                            val_category1 = categoryImgIds[index];
                        } else if ( val_category2 == 0) {
                            val_category2 = categoryImgIds[index];
                        }
                    } else {
                        view.setBackgroundColor(grayColor);
                        // 버튼 색깔이 회색으로 변경될때, 해당 카테고리 이미지 리소스 초기화
                        category[index] = 0;
                        //이미지 리소스가 할당되면, 각각 선택된 변수에 할당함
                        //문제: 이미 val_category1와 val_category2가 0이 아니기 때문에 재할당이 불가능함
                        if (val_category1 == categoryImgIds[index]) {
                            val_category1 = 0;
                        } else if (val_category2 == categoryImgIds[index]) {
                            val_category2 = 0;
                        }
                    }
//                    각 버튼은 고유한 상태를 가져야 하므로, 각 버튼의 상태를 따로 추적해야 합니다. 이를 위해서는 buttonStates 배열과 같이 각 버튼마다 별도의 상태를 저장할 배열이 필요합니다.
                    buttonStates[index] = !buttonStates[index]; //회색 => 보라색 => 회색 가능하게
                }
            });
        }

        //다이얼로그의 취소 버튼 클릭
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //다이얼로그의 수정(등록) 버튼 클릭
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다이얼로그에 새롭게 입력한 사진, 이름, 온라인여부, 한줄소개, 카테고리 2가지를 가져온다
                //사진: 하단에서 onActivityResult 함수를 통해 이미지 리소스로 담는다
                String val_imagePath = imagePath;
                String val_name = name_edit.getText().toString();
                //온라인 여부: 스위치 불린 값을 담는다
                String val_switch_online;
                if (switch_online.isChecked()) {
                    val_switch_online = "온라인";
                } else {
                    val_switch_online = "오프라인";
                }
                String val_introduce = introduce_edit.getText().toString();

                //어레이 리스트에서 값을 변경한다
                mArrayList.get(position).setBookclub_image(val_imagePath);
                mArrayList.get(position).setBookclub_name(val_name);
                mArrayList.get(position).setBookclub_online(val_switch_online);
                mArrayList.get(position).setBookclub_introduce(val_introduce);
                mArrayList.get(position).setBookclub_category1(val_category1);
                mArrayList.get(position).setBookclub_category2(val_category2);


                //어댑터에게 리사이클러뷰에 새로운 아이템이 수정되었음을 알린다
                bookClubAdapter.notifyItemChanged(position);

                //다이얼로그 사라짐
                dialog.dismiss();

            };


        });


    }


    //onActivityResult는 갤러리 선택이 끝나고 나서 갤러리 액티비티가 종료된후 실행됨
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // 선택한 이미지를 ImageView에 설정
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                picture.setImageBitmap(bitmap);

                // 이미지를 파일로 저장
                imagePath = saveImageToFile(uri);

                // 이미지 파일의 경로를 저장
                // 이 imagePath를 필요한 곳에서 사용할 수 있습니다.
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