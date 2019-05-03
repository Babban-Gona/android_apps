package com.babbangona.evoucherapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

public class CameraPage extends AppCompatActivity {

    String filename, folder_name;
     TextView camera2;
    Button next;

    SessionManagement sessionManagement;
    CameraSharedPref sessionManagement2;


    String session_camera2;

    private static final String TAG = "Camera_Page";




    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        next = findViewById(R.id.next);

        camera2 = findViewById(R.id.camera2);

        sessionManagement2 = new CameraSharedPref(getApplicationContext());
        HashMap<String, String> user = sessionManagement2.getUserDetails();
        session_camera2 = user.get(CameraSharedPref.KEY_CAMERA_2);



        if(!checkPermission())requestPermission();

        try {



            if (session_camera2.trim().matches("1")) {
                camera2.setBackgroundColor(getResources().getColor(R.color.view_green));
            }

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (session_camera2.trim().matches("0")) {
                        Toast.makeText(CameraPage.this, "Take picture to proceed", Toast.LENGTH_SHORT).show();
                    } else {

                        startActivity(new Intent(getApplicationContext(),ReceiptScanner.class));

                    }
                }
            });
        }catch (Exception e){}
    }

    public void member(View view) {

        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String,String> user = sessionManagement.getTokenDetails();
        folder_name = "EVOUCHER/Members";
        filename = user.get(SessionManagement.TKN_IKNUMBER);


        if(!checkPermission())requestPermission();
        else{
            String res = check_camera(folder_name, filename);

            if(res.trim().equalsIgnoreCase("done")){
                String check = CheckPicStatus(folder_name, filename);

                if(check.trim().matches("1")){
                    sessionManagement2 = new CameraSharedPref(getApplicationContext());
                    sessionManagement2.CAMERA2("1");
                    Intent intent = new Intent(getApplicationContext(),CameraPage.class);
                    startActivity(intent);
                }
            }
        }
    }

    public String check_camera(String folder_name, String filename){
       File imageDirectory = null;
       String storageState = Environment.getExternalStorageState();

       if(storageState.equals(Environment.MEDIA_MOUNTED)) {
           imageDirectory = new File(Environment.getExternalStorageDirectory().getPath(), folder_name); //Name of the folder used for storage.
           if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
               imageDirectory = null;
           } else {


               StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
               StrictMode.setVmPolicy(builder.build());

               Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
               Uri uriSavedImage = Uri.fromFile(new File(imageDirectory.getPath() + File.separator + filename+"_member.jpg"));
               camera.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
               startActivity(camera);
           }
       }
       return "done";
   }



    public String CheckPicStatus(String folder_name, String filename){

        String status = "";
        File ChkimgDirectory = null;
        String storageState = Environment.getExternalStorageState();
        Log.d(TAG,"storage state 1 "+storageState);
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            ChkimgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), folder_name+"");

            if (!ChkimgDirectory.exists() && !ChkimgDirectory.mkdirs()) { }
            else {
                Bitmap newBitmap = BitmapFactory.decodeFile(ChkimgDirectory.getPath() + File.separator +filename+"_member.jpg");

                if (newBitmap != null) {

                    status = "1";
                }else{
                    status = "0";
                }
            }
        }
        return status;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //do something

            String  member = "";
        sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String, String> token = sessionManagement.getTokenDetails();
        String filename = token.get(SessionManagement.TKN_IKNUMBER);
        //String filepath = token.get(SessionManagement.KEY_FILEPATH);


            try {
                if(!session_camera2.trim().matches("1"))
                member = CheckPicStatus("EVOUCHER/Members", filename);
            } catch (Exception e) {
            }

            int check = 0;
            if (member.matches("1")) {
                sessionManagement2 = new CameraSharedPref(getApplicationContext());
                sessionManagement2.CAMERA2("1");
                check = 1;
            }


            if (check == 1) {
                Intent intent = new Intent(getApplicationContext(), CameraPage.class);
                startActivity(intent);
            }
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(CameraPage.this).create();
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setMessage("You can't return to previous page");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.tknresult_btncancel),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                        dialog.dismiss();
                    }

                } );

        alertDialog.show();

    }

    private boolean checkPermission() {

        //Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);


        //If the app does have this permission, then return true//

        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(CameraPage.this,
                            "You are required to enable this permission", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
