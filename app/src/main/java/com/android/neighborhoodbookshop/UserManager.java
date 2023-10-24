package com.android.neighborhoodbookshop;

// static 변수를 사용하여 로그인한 id를 저장
// 어떤 액티비티에서도 꺼내어 쓸 수 있음
public class UserManager {
    private static String userId;

    public static String getUserId(){
        return userId;
    }

    public static void setUserId(String id){
        userId = id;
    }

}
