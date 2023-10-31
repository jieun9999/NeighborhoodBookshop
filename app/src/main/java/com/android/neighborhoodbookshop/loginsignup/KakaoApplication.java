package com.android.neighborhoodbookshop.loginsignup;
import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    private static KakaoApplication instance;

    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this,"ca02ddd60a4bcb53624cf4147b930cf6");
    }
}
//매니페스트에는 kakao 로그인 화면으로 이동하기 위한 kakao.sdk에 존재하는 AuthCodeHandler를 추가해줍니다.