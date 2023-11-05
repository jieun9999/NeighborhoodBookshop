package com.android.neighborhoodbookshop.loginsignup;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.neighborhoodbookshop.R;
import com.android.neighborhoodbookshop.mylibrary.MainActivity;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private ImageView kakao_btn_login;
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
        kakao_btn_login = findViewById(R.id.imageView20);

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

        //카카오톡이 설치되어 있는지 확인하는 메서드, 카카오에서 제공함. 콜백 객체를 이용함

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            // 콜백 메서드
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                // 토큰이 전달된다면 로그인이 성공한 것이고 토큰이 전달되지 않으면 로그인 실패한다.
                if(oAuthToken != null){
                    updateKakaoLoginUi();
                }else {
                    //로그인 실패
                    Log.e(TAG, "invoke: login fail" );
                }
                return null;
            }
        };

        //카카오 로그인 버튼 클릭 리스너
        kakao_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //해당 기기에 카카오톡이 설치되어 있는지를 확인
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
                }else{
                    //카카오톡이 설치되어 있지 않다면
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
                }
            }
        });
    }

    private void updateKakaoLoginUi(){
        //로그인 여부에 따른 UI 설정
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user != null){

                    // 유저의 아이디
                    Log.d(TAG, "invoke: id =" + user.getId());
                    // 유저의 이메일
                    Log.d(TAG, "invoke: email =" + user.getKakaoAccount().getEmail());
                    // 유저의 닉네임
                    Log.d(TAG, "invoke: nickname =" + user.getKakaoAccount().getProfile().getNickname());
                    //유저의 이미지
                    Log.d(TAG, "invoke: image =" + user.getKakaoAccount().getProfile().getProfileImageUrl());

                    SharedPreferences preferences = getSharedPreferences("가입정보", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    //유저가 카톡에 로그인 되어있다면
                   UserManager.setUserId(String.valueOf(user.getId()));// userId를 static 변수에 저장

                    //가입정보에 해당 아이디로 가입된 데이터가 없는 경우에만 저장하기!
                    // (그렇지 않으면, 항상 초기화가 되면서 해당 아이디 관련 데이터가 유실되는 문제가 생김)

                    if(!preferences.contains(UserManager.getUserId())){
                        // 해당 아이디를 키로 가지고 있는지 확인 (없으면 저장)

                        //유저 데이터 1. 아이디, 닉네임 쉐어드에 저장하기
                        //sharedPreference에 '가입정보' 이라는 파일명을 만들고, key: abc123, value: "피츄"(어레이 리스트로)로 저장한다.
                        ArrayList<String> signupData = new ArrayList<>();
                        signupData.add(user.getKakaoAccount().getProfile().getNickname());

                        JSONArray jsonArray = new JSONArray();
                        for (String data: signupData) {
                            jsonArray.put(data);
                        }

                        editor.putString(UserManager.getUserId(), jsonArray.toString());
                        editor.apply();
                    }

                    //유저 데이터 2. 유저 이미지는 메인 액티비티로 보내주기 (인텐트 활용)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("kakao_id", UserManager.getUserId()); //유저 아이디 보내주기 (string 으로)
                    intent.putExtra("kakao_image", user.getKakaoAccount().getProfile().getThumbnailImageUrl()); // 이미지 보내주기
                    startActivity(intent);

                }else{
                    //유저가 카톡에 로그인 되어있지 않으면
                    Toast.makeText(getApplicationContext(),"카카오톡에 로그인 되지 않은 상태입니다",Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        });
    }
}