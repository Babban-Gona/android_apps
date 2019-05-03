package com.babbangona.evoucherapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class CameraSharedPref {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref1";


    public static  final String KEY_CAMERA_2 = "camera2";
    public static  final String KEY_ID_CARD = "id_card";
    public static  final String KEY_ACCO_AUTH = "acco_auth";





    // Constructor
    public CameraSharedPref(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public CameraSharedPref() {

    }

    public void CAMERA2(String camera2){
        // Storing username in pref
        editor.putString(KEY_CAMERA_2, camera2);
        // commit changes
        editor.commit();
    }

    public void IDCARD(String idCard){
        // Storing username in pref
        editor.putString(KEY_ID_CARD, idCard);
        // commit changes
        editor.commit();
    }

    public void ACCO_Auth(String acco_auth){
        // Storing username in pref
        editor.putString(KEY_ACCO_AUTH, acco_auth);
        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_CAMERA_2,pref.getString(KEY_CAMERA_2,"0"));
        user.put(KEY_ACCO_AUTH,pref.getString(KEY_ACCO_AUTH,"0"));
        user.put(KEY_ID_CARD,pref.getString(KEY_ID_CARD,"0"));

        return user;
    }


}