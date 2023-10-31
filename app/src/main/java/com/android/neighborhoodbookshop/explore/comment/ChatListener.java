package com.android.neighborhoodbookshop.explore.comment;

import android.content.Context;

// 인터페이스 사용 순서 1. 인터페이스를 생성합니다
public interface ChatListener {
    void onChatNumUpdated(Context context);
    void onChatDataUpdated(Context context);
}
