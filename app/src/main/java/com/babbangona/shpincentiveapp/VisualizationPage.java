package com.babbangona.shpincentiveapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

public class VisualizationPage extends AppCompatActivity {

    ProgressBar Progressbar_Done,Progressbar_Upload;
    TextView Report;
    int done = 0;
    int uploaded = 0;
    String staff_id;
    SessionManagement sessionManagement;
    NotesContentProvider.DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization_page);

        DisplayVisualisation();


    }


    public void DisplayVisualisation() {


//widget initialization for this class
        Progressbar_Done = findViewById(R.id.progress_done);
        Progressbar_Upload  = findViewById(R.id.progress_upload);
        Report = findViewById(R.id.tv_records);

        // This block of code retrieves the staff id of the logged on  user that was saved in shared preferences in the Main2Activity class
        sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String,String> user = sessionManagement.getUserDetails();
        staff_id = user.get(SessionManagement.KEY_STAFF_ID);


        // This block of code helps to read the total number of test the user has uploaded and the total number of operations he has done
        databaseHelper = new NotesContentProvider.DatabaseHelper(getApplicationContext());
        done = databaseHelper.getRecordsCount(staff_id);
        uploaded  = databaseHelper.getUploadedRecordsCount(staff_id);

        Report.setText(Report.getText()+"You have completed "+done+" TFM out of  50 TFM \nYou have uploaded "+uploaded+" so far");




        long Done = Math.round(Double.parseDouble(done+""));
             Done = Done*2;
        long Uploaded = Math.round(Double.parseDouble(uploaded+""));


        Progressbar_Done.setProgress(Integer.parseInt(String.valueOf(Done)));
        Progressbar_Upload.setProgress(Integer.parseInt(String.valueOf(Uploaded)));


        //RECRUITMENT PROGRESS BARS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (Done <= 25) {
                Progressbar_Done.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
                Progressbar_Done.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
            } else if (Done > 25 && Done <= 50) {
                Progressbar_Done.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ff5c00")));
                Progressbar_Done.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff5c00")));
            } else if (Done > 50 && Done <= 75) {
                Progressbar_Done.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ffff00")));
                Progressbar_Done.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7d7d7d")));

            } else if (Done > 75 && Done <= 99) {
                Progressbar_Done.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#32cb32")));
                Progressbar_Done.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#32cb32")));

            } else if (Done == 100) {
                Progressbar_Done.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
                Progressbar_Done.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
            }


            if (Uploaded <= 25) {
                Progressbar_Upload.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
                Progressbar_Upload.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
            } else if (Uploaded > 25 && Uploaded <= 50) {
                Progressbar_Upload.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ff5c00")));
                Progressbar_Upload.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff5c00")));
            } else if (Uploaded > 50 && Uploaded <= 75) {
                Progressbar_Upload.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#ffff00")));
                Progressbar_Upload.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7d7d7d")));

            } else if (Uploaded > 75 && Uploaded <= 99) {
                Progressbar_Upload.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#32cb32")));
                Progressbar_Upload.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#32cb32")));

            } else if (Uploaded == 100) {
                Progressbar_Upload.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
                Progressbar_Upload.setProgressBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#008000")));
            }



        }

    }

    public void Homepage(View view) {
        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
    }
}
