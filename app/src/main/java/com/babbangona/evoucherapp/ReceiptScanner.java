package com.babbangona.evoucherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.babbangona.evoucherapp.R;

import java.util.HashMap;

public class ReceiptScanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_scanner);

    }

    public void ScanQRCode(View view) {
        Intent intent  = new Intent(getApplicationContext(),Scanner.class);
        startActivityForResult(intent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCode) {
            if (resultCode == RESULT_OK) {

                String result = data.getStringExtra("result");




                SessionManagement  sessionManagement = new SessionManagement(getApplicationContext());
                HashMap<String,String> user = sessionManagement.getUserDetails();
                String TxnId  = user.get(SessionManagement.KEY_TXNID);

                if(result.startsWith("V") && result.length() == 15){
                    DBOpenHelper dbOpenHelper = new DBOpenHelper(getApplicationContext());
                    dbOpenHelper.updateRecieptId(TxnId,result);


                    Toast.makeText(this, getResources().getString(R.string.toast_scanner), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                }
                else{
                    Toast.makeText(this, "You have scanned the wrong QR code", Toast.LENGTH_SHORT).show();

                }

            }
        }
    }
}