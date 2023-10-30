package com.android.neighborhoodbookshop.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.neighborhoodbookshop.R;
import com.android.neighborhoodbookshop.mylibrary.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView btn_signup;

    private EditText userID, password;

    // onCreate 메서드
    // 초기화 작업 (정의된 사용자 UI를 올리기 위해 레이아웃 리소스 파일을 읽어와 객체를 생성)
    @Override
    protected void onCreate(Bundle savedInstanceState) { //avedInstanceState: 액티비티의 이전 상태를 복원하거나 초기 상태를 설정
        super.onCreate(savedInstanceState);
        // 1. 부모 클래스인 Activity 클래스의 onCreate 메서드를 호출
        // 2." super " 키워드가 사용되지 않으면 SuperNotCalledException 예외가 발생
        // 3. 생명주기 관리

        setContentView(R.layout.activity_login);
        // 1. activity와 layout 연결
        // 2. 매니페스트에 등록

        btn_login = findViewById(R.id.button);
        btn_signup = findViewById(R.id.button3);
        userID= findViewById(R.id.아이디입력);
        password = findViewById(R.id.비밀번호입력);


        //이벤트리스터 객체를 만든 다음에, 버튼의 이벤트리스너에 인자로서 넣기, 여러개의 함수를 한번에 정의하기 용이함
        View.OnClickListener listener = new View.OnClickListener(){ //익명내부 클래스 : 이벤트 처리에 용이함
            @Override
            public void onClick(View view) {

                if(view.getId() == R.id.button){
                    //쉐어드 파일명 '가입정보'의 id키의 값 중 비밀번호가 유저가 입력한 비밀번호와 동일할 경우, 다음 액티비티로 넘어간다
                    SharedPreferences preferences = getSharedPreferences("가입정보", MODE_PRIVATE);
                    String jsonData = preferences.getString(userID.getText().toString(), null);

                    if(jsonData != null){// 입력한 아이디로 가입된 아이디가 존재하면
                        try {
                            JSONArray jsonArray = new JSONArray(jsonData);
                            //jsonData =["피츄","asdf!@#$"]
                            String password_sp = jsonArray.getString(1);//쉐어드 비밀번호를 저장할 변수
                            String password_val = password.getText().toString(); //사용자가 입력한 비법
                            // == 는 주소를 비교한 것이다
                            // 문자열 값을 비교하기 위해서는 equals를 사용해야 한다
                           if(password_sp.equals(password_val)){ //비밀번호 쉐어드와 일치 통과함
                               String userId = userID.getText().toString();
                               UserManager.setUserId(userId);// userId를 static 변수에 저장

                               Toast.makeText(getApplicationContext(),"로그인이 완료되었습니다",Toast.LENGTH_SHORT).show();
                               //Intent 객체가 바로 한 액티비티에서 다른 액티비티로 전환할 수 있도록 해준다
                               //첫번째 매개변수: 처음 액티비트이름.this와 같은 형태로 context를 가져온다, 두번째 매개변수: 다음의 액티비티 클래스
                               Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                               myIntent.putExtra("userId", userID.getText().toString()); // 메인 액티비티로 아이디를 넘겨준다
                               startActivity(myIntent);
                           }else {
                               Toast.makeText(getApplicationContext(),"가입된 회원이 아니거나 비밀번호를 잘못입력하셨습니다",Toast.LENGTH_SHORT).show();
                           }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }else {// 입력한 아이디로 가입된 아이디가 존재하지 않으면
                        Toast.makeText(getApplicationContext(),"가입된 회원이 아니거나 비밀번호를 잘못입력하셨습니다",Toast.LENGTH_SHORT).show();
                    }
                } else if (view.getId() == R.id.button3) {
                    Toast.makeText(getApplicationContext(),"회원가입으로 이동합니다",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(myIntent);
                }
            }

        };

        btn_login.setOnClickListener(listener);
        btn_signup.setOnClickListener(listener);

    }

}