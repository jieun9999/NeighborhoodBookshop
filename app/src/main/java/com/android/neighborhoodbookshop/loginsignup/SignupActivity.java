package com.android.neighborhoodbookshop.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.neighborhoodbookshop.R;

import org.json.JSONArray;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {
    private EditText name, userID, password, passwordCheck;
    private Button button_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("회원가입");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼

        button_signup = findViewById(R.id.button);
        name = findViewById(R.id.이름입력);
        userID = findViewById(R.id.아이디입력);
        password = findViewById(R.id.비밀번호입력);
        passwordCheck = findViewById(R.id.비밀번호확인);

        // 객체 button_signup에 대해 setOnClickListener 메소드를 호출하고,
        // new View.OnClickListener() {   } 라는 익명클래스를 인자로 넣어주었다
        // button_signup 이 클릭되었을때 onClick메서드가 실행된다.
        button_signup.setOnClickListener(new View.OnClickListener() { //객체에 바로 이벤트리스터 붙이기
            @Override
            public void onClick(View view) {
                String password_val = password.getText().toString();
                String passwordCheck_val = passwordCheck.getText().toString();

                if(password_val.equals(passwordCheck_val)){
                    //유저 가입정보를 저장하기
                    saveSignupData();

                    Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
                    //첫번째 인자: 현재 Context (현재 Activity.this)
                    //두번째 인자: 목표 Component (목표 Activity.class)
                    startActivity(myIntent);
                    Toast.makeText(getApplicationContext(),"회원가입이 완료되었습니다",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"비밀번호가 불일치합니다",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveSignupData(){
        //sharedPreference에 '가입정보' 이라는 파일명을 만들고, key: abc123, value:"asdf!@#$", "피츄"(어레이 리스트로)로 저장한다.
        ArrayList<String> signupData = new ArrayList<>();
        signupData.add(name.getText().toString());
        signupData.add(password.getText().toString());

        SharedPreferences preferences = getSharedPreferences("가입정보", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        JSONArray jsonArray = new JSONArray();
        for (String data: signupData) {
            jsonArray.put(data);
        }

        editor.putString(userID.getText().toString(), jsonArray.toString());
        editor.apply();

        clearInputFields();
    }

    private void clearInputFields(){
        name.getText().clear();
        userID.getText().clear();
        password.getText().clear();
        passwordCheck.getText().clear();
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