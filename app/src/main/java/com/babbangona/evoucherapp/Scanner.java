package com.babbangona.evoucherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import javax.xml.transform.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.text.TextUtils.isDigitsOnly;

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    String Response;
    private static final String TAG = "ReceiptScanner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);

    }

    @Override
    public void onResume(){
        super.onResume();

        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    public void onPause(){
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(final com.google.zxing.Result result) {

        ReturnResult(result.getText().toString().split("\\*")[0]);

    }


    public void ReturnResult(String result){

        Intent data = new Intent();
        data.putExtra("result",""+result);

        setResult(RESULT_OK,data);
        finish();

    }
}
