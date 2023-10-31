package com.android.neighborhoodbookshop.setting;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.android.neighborhoodbookshop.R;
import com.android.neighborhoodbookshop.bookclub.BookClubActivity;
import com.android.neighborhoodbookshop.explore.ExploreActivity;
import com.android.neighborhoodbookshop.mylibrary.MainActivity;
import com.android.neighborhoodbookshop.timer.TimerActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SettingActivity extends AppCompatActivity {

    LinearLayout 내서재;
    LinearLayout 탐색;
    LinearLayout 북클럽;
    LinearLayout  타이머;
    LinearLayout 설정;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main);

        Log.d("getKeyHash", "" + getKeyHash(SettingActivity.this));
        // 이렇게 로그에 키해시 값을 뽑아 주면 로그창에 키해시가 뜹니다!!

        내서재 = (LinearLayout) findViewById(R.id.mylibrary);
        탐색 = (LinearLayout) findViewById(R.id.explore);
        북클럽 = (LinearLayout) findViewById(R.id.bookclub);
        타이머 = (LinearLayout) findViewById(R.id.timer);
        설정 = (LinearLayout) findViewById(R.id.setting);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.mylibrary){
                    Intent myIntent = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.explore) {
                    Intent myIntent = new Intent(SettingActivity.this, ExploreActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.bookclub) {
                    Intent myIntent = new Intent(SettingActivity.this, BookClubActivity.class);
                    startActivity(myIntent);
                } else if (view.getId() == R.id.timer) {
                    Intent myIntent = new Intent(SettingActivity.this, TimerActivity.class);
                    startActivity(myIntent);
                }
            }
        };

        내서재.setOnClickListener(listener);
        탐색.setOnClickListener(listener);
        북클럽.setOnClickListener(listener);
        타이머.setOnClickListener(listener);
    }

    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}