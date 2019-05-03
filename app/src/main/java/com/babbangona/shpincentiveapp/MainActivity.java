package com.babbangona.shpincentiveapp;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends Activity {

    /*
    * This the default launcher of this application
    * This app is fully integrated with access control meaning that the main activity which is the default launcher
    * simply launches access control
    */

    private JobScheduler jobScheduler;
    private JobInfo jobInfo;
    ComponentName componentName;
    HashMap map3;
    private static final int JOB_ID =101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Initialization for the GPS Co-ordinates
        Master master = new Master();
        master.enableLocations(this);
        String response  = Master.initialiseLocationListener(getApplicationContext());



        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String,String> user = sessionManagement.getUserDetails();
        String staff_id = user.get(SessionManagement.KEY_STAFF_ID);



    }


    // On Click method that launches access control
    public void next(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.babbangona.accesscontrol", "com.babbangona.accesscontrol.MainActivity"));
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, "You have not installed access control", Toast.LENGTH_SHORT).show();
        }
    }

}
