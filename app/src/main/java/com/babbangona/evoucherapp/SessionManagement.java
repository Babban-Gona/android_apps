package com.babbangona.evoucherapp;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";



    public static final String KEY_STAFF_NAME  = "staffname";
    public static final String KEY_STAFF_ID = "staffid";
    public static final String KEY_STAFF_ROLE = "staff_role";

    public static final String TKN_TOKEN_ENTERED = "token_entered";
    public static final String TKN_IKNUMBER = "ik_number";
    public static final String KEY_MEMBERID = "member_id";
    public static final String TKN_LMDID = "lmd_id";
    public static final String TKN_SEASON = "season";
    public static final String TKN_LOAN_FIELDSIZE = "loan_field_size";
    public static final String TKN_SEED_TYPE = "seed_type";



    public static final String KEY_FILENAME = "file_name";
    public static final String KEY_FILEPATH = "file_path";
    public static final String KEY_TXNID = "transaction_id";
    public static final String KEY_ACCO_ID = "acco_id";







    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public SessionManagement() {

    }


    public void CreateLoginSession(String staffname, String staffid, String staff_role){
        // Storing username in pref
        editor.putString(KEY_STAFF_NAME, staffname);
        // Storing role in pref
        editor.putString(KEY_STAFF_ID, staffid);
        editor.putString(KEY_STAFF_ROLE, staff_role);

        // commit changes
        editor.commit();
    }

    public void saveToken(String token){
        editor.putString(TKN_TOKEN_ENTERED, token);
        editor.commit();
    }

    public void saveTxnId(String txnid){
        editor.putString(KEY_TXNID, txnid);
        editor.commit();
    }

    public void saveAccoId(String acco){
        editor.putString(KEY_ACCO_ID, acco);
        editor.commit();
    }

    public void saveMemberID(String mem_id){
        editor.putString(KEY_MEMBERID, mem_id);
        editor.commit();
    }

    public void savePicParams(String file_path,String file_name){
        editor.putString(KEY_FILEPATH, file_path);
        editor.putString(KEY_FILENAME, file_name);
        editor.commit();
    }

    public void tokenSession( String iknumber,String lmdid, String season, String loan_field_size, String seed_type){
        // Storing username in pref

        // Storing role in pref
        editor.putString(TKN_IKNUMBER, iknumber);
        editor.putString(TKN_LMDID, lmdid);
        editor.putString(TKN_LOAN_FIELDSIZE, loan_field_size);
        editor.putString(TKN_SEASON, season);
        editor.putString(TKN_SEED_TYPE, seed_type);

        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_STAFF_NAME, pref.getString(KEY_STAFF_NAME, null));
        user.put(KEY_STAFF_ID, pref.getString(KEY_STAFF_ID, "T-10000000000000AA"));
        user.put(KEY_STAFF_ROLE,pref.getString(KEY_STAFF_ROLE,"x"));

        user.put(KEY_FILEPATH,pref.getString(KEY_FILEPATH,"x"));
        user.put(KEY_FILENAME,pref.getString(KEY_FILENAME,"x"));
        user.put(KEY_TXNID,pref.getString(KEY_TXNID,"x"));

        user.put(KEY_ACCO_ID,pref.getString(KEY_ACCO_ID,"x"));

        return user;
    }

    public HashMap<String, String> getTokenDetails(){
        HashMap<String, String> token = new HashMap<String, String>();

        token.put(TKN_TOKEN_ENTERED, pref.getString(TKN_TOKEN_ENTERED, null));
        token.put(TKN_SEED_TYPE, pref.getString(TKN_SEED_TYPE, "T-1123"));
        token.put(TKN_LOAN_FIELDSIZE,pref.getString(TKN_LOAN_FIELDSIZE,null));
        token.put(TKN_SEASON,pref.getString(TKN_SEASON,null));
        token.put(TKN_LMDID,pref.getString(TKN_LMDID,null));
        token.put(TKN_IKNUMBER,pref.getString(TKN_IKNUMBER,null));
        token.put(KEY_MEMBERID,pref.getString(KEY_MEMBERID,null));

        // return user
        return token;
    }


}