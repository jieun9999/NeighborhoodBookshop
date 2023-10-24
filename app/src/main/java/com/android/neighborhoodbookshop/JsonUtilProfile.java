package com.android.neighborhoodbookshop;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtilProfile {
    public static String toJSon(ProfileManager profileManager) {
        try {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("imagePath", profileManager.getImagePath());
            jsonObj.put("location", profileManager.getLocation());
            jsonObj.put("introduction", profileManager.getIntroduction());
            jsonObj.put("instaUrl", profileManager.getInstaURL());

            return jsonObj.toString();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
