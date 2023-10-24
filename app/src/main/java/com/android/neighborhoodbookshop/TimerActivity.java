package com.android.neighborhoodbookshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TimerActivity extends AppCompatActivity {

    ArrayList<Drawable> imageList = new ArrayList<>();
    //Drawable 객체들의 목록을 저장하는 목적으로 사용됩니다.
    ImageView imageView;
    int currentImageIndex = 0;
    TextView textView2;
    TextView textView; //초를 나타낼 TextView
    ImageView start, pause, reset; //시작, 일시정지, 리셋 버튼
    long MillisecondTime = 0L; // 스탑워치 시작 버튼 누르고 흐른시간
    long StartTime = 0L; // 스탑워치 시작버튼 누르고, 난 이후부터의 시간
    long TimeInterval = 0L; // 스탑워치 일시정지 버튼을 누르고 난 이후부터의 시간
    long UpdateTime = 0L; // 스탑워치 일시정지 버튼 눌렀을때의 총 시간 + 시작 버튼 누르고 난 이후부터의 시간   = 총시간
    Handler handler; //Handler는 UI 업데이트 및 시간경과를 처리하는데 사용됩니다
    //핸들러는 메인(ui) 스레드와 서브 스레드 사이의 통신을 관리하는 데 사용되는 클래스이다.
    Handler quoteHandler;
    int Seconds,Minutes,MilliSeconds;

    LinearLayout 내서재;
    LinearLayout 탐색;
    LinearLayout 북클럽;
    LinearLayout  타이머;
    LinearLayout 설정;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //하단 명언 배너
        //imageList 어레이리스트에 drawable 객체를 넣는다
        Resources res = getResources();
        imageList.add(res.getDrawable(R.drawable.quote1));
        imageList.add(res.getDrawable(R.drawable.quote2));
        imageList.add(res.getDrawable(R.drawable.quote3));

        imageView = findViewById(R.id.imageView5);
        startImageRotation();

        // 스탑워치
        //각각의 뷰를 inflate 해준다
        textView = findViewById(R.id.timernum);
        start = findViewById(R.id.start);
        pause = findViewById(R.id.pause);
        reset = findViewById(R.id.reset);

        textView2 = findViewById(R.id.textView2);

        handler = new Handler();
        // Handler 클래스의 인스턴스를 생성하고 handler 변수에 할당합니다
        // 보통 한 액티비티에서 하나의 Handler를 사용하는 것이 일반적입니다

        //스탑워치 시작 버튼
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTime = SystemClock.uptimeMillis(); //스탑워치가 시작된 시간을 저장
                handler.postDelayed(runnable, 0);
                //0초 딜레이 이후에 바로 실행된다. 이 Runnable은 스톱워치의 시간 업데이트와 표시를 처리한다

                reset.setClickable(false); //이벤트 클릭을 비활성화
                //리셋버튼은 스톱워치가 동작 중일때 비활성화됨

                textView2.setText("책 읽는 중\uD83D\uDD25");
            }
        });

        //스탑워치 일시정지 버튼
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeInterval += MillisecondTime; // TimeInterval 변수에 현재 스톱워치 경과 시간(러너블에서 옴)  MillisecondTime 을추가
                //일시정지 버튼이 눌릴때마다 총 경과시간을 TimeInterval에 누적한다

                handler.removeCallbacks(runnable);
                //handler 객체를 사용하여 runnable 객체를 제거합니다.
                //이렇게 하면 runnable이 멈춘다

                reset.setClickable(true);
                //리셋 버튼 클릭 활성화

                textView2.setText("책 읽을 준비중☕️");

            }
        });

        //스탑워치 리셋 버튼
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //측정시간을 모두 0으로 리셋시켜준다
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeInterval = 0L;
                UpdateTime = 0L;
                Seconds =0;
                Minutes =0;
                MilliSeconds = 0;

                //초를 나타내는 TextView를 0초로 갱신시켜준다
                textView.setText("00:00:00");
            }
        });


        내서재 = (LinearLayout) findViewById(R.id.mylibrary);
        탐색 = (LinearLayout) findViewById(R.id.explore);
        북클럽 = (LinearLayout) findViewById(R.id.bookclub);
        타이머 = (LinearLayout) findViewById(R.id.timer);
        설정 = (LinearLayout) findViewById(R.id.setting);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.mylibrary){
                    Intent myIntent = new Intent(TimerActivity.this, MainActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.explore) {
                    Intent myIntent = new Intent(TimerActivity.this,ExploreActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.bookclub) {
                    Intent myIntent = new Intent(TimerActivity.this,BookClubActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.setting) {
                    Intent myIntent = new Intent(TimerActivity.this,SettingActivity.class);
                    startActivity(myIntent);
                }
            }
        };

        내서재.setOnClickListener(listener);
        탐색.setOnClickListener(listener);
        북클럽.setOnClickListener(listener);
        설정.setOnClickListener(listener);

    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //스탑워치 시작버튼 누르고 흐른 시간
            MillisecondTime = SystemClock.uptimeMillis() - StartTime; // 현재시간 - 시작버튼 누른시간

            //총시간
            UpdateTime = TimeInterval + MillisecondTime; //스탑워치 일시정지 버튼 눌렀을때 총 시간 + 다음 시작버튼 누르고 난 이후의 시간

            Seconds = (int) (UpdateTime /1000);
            Minutes = Seconds /60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            textView.setText(""+Minutes+":"+ String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this,0);
        }
    };

   private void startImageRotation(){
       // quoteHandler를 초기화
       quoteHandler = new Handler();
       //handler 객체 선언
       Runnable imageRotationRunnable = new Runnable() {
           // 주기적으로 이미지를 변경해주는 러너블을 만듦
           //주기적으로 실행하려는 코드를 러너블에 적고, 그 러너블을 스레드에 넣어서 실행한다
           @Override
           public void run() {
            currentImageIndex = (currentImageIndex + 1) % imageList.size();
            //인덱스가 리스트의 크기를 넘어가면, 다시 처음 이미지부터 표시하기 위해서 연산자 (%)를 사용함
            Drawable drawable = imageList.get(currentImageIndex);
            imageView.setImageDrawable(drawable);
            quoteHandler.postDelayed(this, 3000); //3초 뒤에 러너블 실행
           }
       };
       //handler를 사용하여 runnable을 일정한 간격으로 실행
       //handler가 메인스레드에게 runnable를 인자로 주면서 ui 업데이트를 요청함
       quoteHandler.postDelayed(imageRotationRunnable, 3000); //처음 시작된지 3초후에 러너블 실행
   }
}

