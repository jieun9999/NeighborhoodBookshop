package com.android.neighborhoodbookshop;

import android.content.Context;

// 1. 인터페이스를 생성합니다
public interface ChatListener {
    void onChatNumUpdated(Context context);
    void onChatDataUpdated(Context context);
}
