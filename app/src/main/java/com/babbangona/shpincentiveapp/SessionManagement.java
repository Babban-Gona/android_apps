package com.babbangona.shpincentiveapp;


import android.content.Context;
import android.content.Intent;
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


    // Staff Name (make variable public to access from outside)
    public static final String KEY_STAFF_NAME  = "staffname";


    // Staff name and role  (make variable public to access from outside)
    public static final String KEY_STAFF_ID = "staffid";
    public static final String KEY_ROLE = "role";


//Declaration for latitude variable
    public static final String LATITUDE = "latitude";
//Declaration for longitude variable
    public static final String LONGITUDE = "longitude";
//Declaration of last synced time
    public static final String LAST_SYNCED = "last_synced";

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public SessionManagement() {

    }


    // This method stores the log in details of the user into shared preference
    public void CreateLoginSession(String staffname, String staffid, String staff_role){
        // Storing username in pref
        editor.putString(KEY_STAFF_NAME, staffname);
        // Storing role in pref
        editor.putString(KEY_STAFF_ID, staffid);
        editor.putString(KEY_ROLE, staff_role);

        // commit changes
        editor.commit();
    }

    // This method saves the last synced time to shared preference
    public void lastSyncedTime(String time){
        // Storing username in pref
        editor.putString(LAST_SYNCED, time);

        // commit changes
        editor.commit();
    }

    // This method stores the longitude and latitude generated from the Master class
    public void createGPSSession(String latitude, String longitude){
        // Storing username in pref
        editor.putString(LATITUDE, latitude);
        // Storing role in pref
        editor.putString(LONGITUDE, longitude);


        // commit changes
        editor.commit();
    }

    // This method retrieves data from shared preference
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_STAFF_NAME, pref.getString(KEY_STAFF_NAME, null));

        // user email id
        user.put(KEY_STAFF_ID, pref.getString(KEY_STAFF_ID, "T-1XXXXXXXXXXX"));

        user.put(KEY_ROLE,pref.getString(KEY_ROLE,null));



        user.put(LATITUDE, pref.getString(LATITUDE, null));
        user.put(LONGITUDE, pref.getString(LONGITUDE, null));
        user.put(LAST_SYNCED, pref.getString(LAST_SYNCED, "0000-00-00 00:00:00"));
        // return user
        return user;
    }

    /**
     * Clear session details
     * */





}