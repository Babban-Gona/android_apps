package com.babbangona.evoucherapp;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class TokenValidator extends Activity {
     PinEntryEditText pinEntry1,pinEntry2,pinEntry3,pinEntry4,pinEntry5;

     TextView tvStaffDetails, tvDate;
     String staff_name, staff_id, time;
     public String TAG="TokenDecryptor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_validator);

        pinEntry1 = findViewById(R.id.txt_pin_entry);
        pinEntry2 = findViewById(R.id.txt_pin_entry1);
        pinEntry3 = findViewById(R.id.txt_pin_entry2);
        pinEntry4 = findViewById(R.id.txt_pin_entry3);
        pinEntry5 = findViewById(R.id.txt_pin_entry4);

        tvStaffDetails = findViewById(R.id.tvLmdDetails);
        tvDate = findViewById(R.id.tvDate);

        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String,String> user = sessionManagement.getUserDetails();

        staff_name = user.get(SessionManagement.KEY_STAFF_NAME);
        staff_id = user.get(SessionManagement.KEY_STAFF_ID);

        tvStaffDetails.setText("");
        tvStaffDetails.setText(staff_name+"\n"+staff_id);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd EEE, yyyy");
        Date date = new Date();
        time = dateFormat.format(date);

        tvDate.setText("");
        tvDate.setText(time);




        if (pinEntry1 != null) {
         //   pinEntry.setTypeface(ResourcesCompat.getFont(this, R.font.charmonman_regular));
            pinEntry1.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                   pinEntry2.requestFocus();
                }
            });
        }


        if (pinEntry2 != null) {
            pinEntry2.setAnimateText(true);
            pinEntry2.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    pinEntry3.requestFocus();
                }
            });
        }

        if (pinEntry3 != null) {
            pinEntry3.setAnimateText(true);
            pinEntry3.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    pinEntry4.requestFocus();
                }
            });
        }


        if (pinEntry4 != null) {
            pinEntry4.setAnimateText(true);
            pinEntry4.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {

                    pinEntry5.requestFocus();
                }
            });
        }


        if (pinEntry5 != null) {
            pinEntry5.setAnimateText(true);
            pinEntry5.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {


                }
            });
        }


    }

    public void validate(View v){

       try {
           if (pinEntry1.getText().toString() == null || pinEntry2.getText().toString() == null ||pinEntry3.getText().toString()==null||pinEntry4.getText().toString()==null||pinEntry5.getText().toString()==null) {
               Toast.makeText(this, "Please enter token", Toast.LENGTH_SHORT).show();
           } else {
               TokenDecryptor tokenDecryptor1 = new TokenDecryptor();

               String tkn1 = pinEntry1.getText().toString().trim().toUpperCase();
               String tkn2 = pinEntry2.getText().toString().trim().toUpperCase();
               String tkn3 = pinEntry3.getText().toString().trim().toUpperCase();
               String tkn4 = pinEntry4.getText().toString().trim().toUpperCase();
               String tkn5 = pinEntry5.getText().toString().trim().toUpperCase();



               String x = tokenDecryptor1.TokenDecryptor(tkn1+tkn2+tkn3+tkn4+tkn5, getApplicationContext());
               Log.d("TokenDecryptor","Result is  "+x);
               if(x.trim().equalsIgnoreCase("I")){
                   Toast.makeText(this, "Invalid Token, Ensure you entered the right token", Toast.LENGTH_SHORT).show();
               }

               else if(x.trim().equalsIgnoreCase("R")){

                   Toast.makeText(this, "Corrupted Token, Ensure you have entered the token correctly", Toast.LENGTH_SHORT).show();
               }else if(x.trim().equalsIgnoreCase("D")){
                   Toast.makeText(this, "Expired Token, this token is not for this season", Toast.LENGTH_SHORT).show();
               }else if(x.trim().equalsIgnoreCase("L")){
                   Toast.makeText(this, "Wrong LMD, Ensure you are at the right lmd", Toast.LENGTH_SHORT).show();
               }else if(x.trim().equalsIgnoreCase("U")){
                   Toast.makeText(this, "The token provided has been used", Toast.LENGTH_SHORT).show();
               }

                   else{
                   Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
                   String[] y = x.split("//");
                   SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
                   sessionManagement.tokenSession(y[0],y[2],y[3],y[1],y[4]);

                   startActivity(new Intent(getApplicationContext(),ValidateMember.class));

               }


           }
       }catch(Exception e){
           e.printStackTrace();

           //Toast.makeText(this, "Corrupted Token", Toast.LENGTH_SHORT).show();
       }

    }

    public void clear(View view) {
        pinEntry1.setText(null);
        pinEntry2.setText(null);
        pinEntry3.setText(null);
        pinEntry4.setText(null);
        pinEntry5.setText(null);

        }
}
