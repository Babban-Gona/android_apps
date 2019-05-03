package com.babbangona.evoucherapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandAuthActivity;
import com.babbangona.bg_face.LuxandInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValidateIDCard extends AppCompatActivity {

    Spinner spnIdCardStatus, spnAccoName;
    Button btnNext;
    TextView tvCaptureIDcard, tvCaptureAcco;
    String[] id_status = {"Yes","No"};
    List acco;
    SessionManagement sessionManagement;
    CameraSharedPref cameraSharedPref;
    String idStatus,accoStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_idcard);


        spnIdCardStatus = findViewById(R.id.spn_idcard_status);
        spnAccoName = findViewById(R.id.spn_acco_status);
        btnNext = findViewById(R.id.btn_next);
        tvCaptureIDcard = findViewById(R.id.tv_idcard_pics);
        tvCaptureAcco = findViewById(R.id.tv_acco_pics);


        cameraSharedPref = new CameraSharedPref(getApplicationContext());
        HashMap<String,String> vals = cameraSharedPref.getUserDetails();

        idStatus = vals.get(CameraSharedPref.KEY_ID_CARD);
        accoStatus = vals.get(CameraSharedPref.KEY_ACCO_AUTH);
        if(idStatus.matches("1")) tvCaptureIDcard.setBackgroundColor(getResources().getColor(R.color.view_green));
        if(accoStatus.matches("1")) tvCaptureAcco.setBackgroundColor(getResources().getColor(R.color.view_green));



        ArrayAdapter<String> dataAdapterx = new ArrayAdapter<String>(ValidateIDCard.this,
                android.R.layout.simple_spinner_item, id_status);
        dataAdapterx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIdCardStatus.setAdapter(dataAdapterx);


        acco = new ArrayList();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getApplicationContext());
        acco = dbOpenHelper.getAcco();

        ArrayAdapter<String> dataAdapter_acco = new ArrayAdapter<String>(ValidateIDCard.this,
                android.R.layout.simple_spinner_item, acco);
        dataAdapter_acco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAccoName.setAdapter(dataAdapter_acco);


        spnIdCardStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(spnIdCardStatus.getSelectedItem().toString().equalsIgnoreCase("Yes")){
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          tvCaptureIDcard.setVisibility(View.VISIBLE);
                          tvCaptureIDcard.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {

                                  if(idStatus.matches("1")){
                                      Toast.makeText(ValidateIDCard.this, "You have captured the ID card", Toast.LENGTH_SHORT).show();

                                  }else{

                                        File imageDirectory = new File(Environment.getExternalStorageDirectory().getPath(), "EVOUCHER/Id_Cards");

                                        if(!imageDirectory.exists() && !imageDirectory.mkdirs()){
                                            imageDirectory = null;
                                        }else {
                                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                            StrictMode.setVmPolicy(builder.build());

                                            SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
                                            HashMap<String, String> user = sessionManagement.getTokenDetails();
                                            String ik_number = user.get(SessionManagement.TKN_IKNUMBER);


                                            Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                            Uri uriSavedImage = Uri.fromFile(new File(imageDirectory.getPath() + File.separator + ik_number + "_idcard.jpg"));
                                            camera.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                                            startActivity(camera);
                                }

                                 }
                              }
                          });
                      }
                  },500);

               }else{
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           tvCaptureIDcard.setVisibility(View.GONE);
                       }
                   },500);


               }


           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }




       });

        spnAccoName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spnAccoName.getSelectedItem().toString().equalsIgnoreCase("select acco name")){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvCaptureAcco.setVisibility(View.VISIBLE);
                            tvCaptureAcco.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(accoStatus.matches("1")){
                                        Toast.makeText(ValidateIDCard.this, "Acco has been verified", Toast.LENGTH_SHORT).show();
                                    }else {
                                        String template;
                                        DBOpenHelper dbOpenHelper = new DBOpenHelper(getApplicationContext());
                                        template = dbOpenHelper.getAccoTemplate(spnAccoName.getSelectedItem().toString().split("_")[1]);


                                        new LuxandInfo(getApplicationContext()).putTemplate(template);
                                        Intent intent = new Intent(getApplicationContext(), LuxandAuthActivity.class);
                                        startActivityForResult(intent, 419);
                                    }
                                }
                            });
                        }
                    },500);
                }else{

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvCaptureAcco.setVisibility(View.GONE);
                        }
                    },2000);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSharedPref = new CameraSharedPref(getApplicationContext());
                HashMap<String,String> token = cameraSharedPref.getUserDetails();
                if(token.get(CameraSharedPref.KEY_ID_CARD).trim().matches("0")){
                    Toast.makeText(ValidateIDCard.this, "You have not taken ID card picture ", Toast.LENGTH_SHORT).show();

                }else if(token.get(CameraSharedPref.KEY_ACCO_AUTH).trim().matches("0")){
                    Toast.makeText(ValidateIDCard.this, "You have not validated the ACCO's face ", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(getApplicationContext(),TokenResult.class));
                }


            }
        });




    }


    public String CheckPicStatus(String folder_name, String filename){

        String status = "";
        File ChkimgDirectory = null;
        String storageState = Environment.getExternalStorageState();

        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            ChkimgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), folder_name+"");

            if (!ChkimgDirectory.exists() && !ChkimgDirectory.mkdirs()) { }
            else {
                Bitmap newBitmap = BitmapFactory.decodeFile(ChkimgDirectory.getPath() + File.separator +filename+"_idcard.jpg");

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
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = sessionManagement.getTokenDetails();
        String ik_number = user.get(SessionManagement.TKN_IKNUMBER);

        cameraSharedPref = new CameraSharedPref(getApplicationContext());
        HashMap<String,String> camera = cameraSharedPref.getUserDetails();


        try {
            if(!camera.get(CameraSharedPref.KEY_ID_CARD).trim().matches("1"))
                member = CheckPicStatus("EVOUCHER/Id_Cards", ik_number);
        } catch (Exception e) {
        }

        int check = 0;
        if (member.matches("1")) {
            cameraSharedPref = new CameraSharedPref(getApplicationContext());
            cameraSharedPref.IDCARD("1");

            check = 1;
        }

        if (check == 1) { tvCaptureIDcard.setBackgroundColor(getResources().getColor(R.color.view_green)); }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 419 && data != null){
            if(data.getIntExtra("RESULT",0) == 1)
            {
                //TODO ON SUCCESS

                cameraSharedPref = new CameraSharedPref(getApplicationContext());
                tvCaptureAcco.setBackgroundColor(getResources().getColor(R.color.view_green));
                cameraSharedPref.ACCO_Auth("1");

                sessionManagement = new SessionManagement(getApplicationContext());
                sessionManagement.saveAccoId(spnAccoName.getSelectedItem().toString().split("_")[1]);
            }

            else
                {
                    Toast.makeText(this, "Ensure you validate "+spnAccoName.getSelectedItem().toString()+"'s face", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
