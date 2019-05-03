package com.babbangona.shpincentiveapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;


public class QRCodeGenerator extends AppCompatActivity {
    public final static int QRCodeWidth = 500; //Change based on your requirement. Use for both height and width as QR is best created as a square
    private static final String IMAGE_DIRECTORY = "/GeneratedBarcodes"; // Storage location for Generated QR codes.
    Bitmap bitmap;
    LinearLayout linearLayout;
    private ImageView iv;
    String time,longitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);
        iv = (ImageView) findViewById(R.id.qr_view); // Image view to display QR
        linearLayout = findViewById(R.id.qr_code);

        SessionManagement sessionManagement  = new SessionManagement(getApplicationContext());
        HashMap<String,String >user = sessionManagement.getUserDetails();
        String staff_id = user.get(SessionManagement.KEY_STAFF_ID);

        try {

            String response = Master.initialiseLocationListener(getApplicationContext());
            String[] xy = response.split("//");
            Log.d("ARRAY_SIZE", response + "");
            Log.d("ARRAY_SIZE", xy.length + "");
            time = xy[0];
            longitude = xy[1];
            latitude = xy[2];
        }catch(Exception e){

        }






        @SuppressLint("StaticFieldLeak") GenerateQR generateQR = new GenerateQR(){
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    linearLayout.setVisibility(View.GONE);
                    iv.setImageBitmap(bitmap);

                }
            };


         if(latitude == null || latitude.trim().matches("null")){


             AlertDialog.Builder builder1 = new AlertDialog.Builder(QRCodeGenerator.this);
             builder1.setTitle(getResources().getString(R.string.qrcode_title));
             builder1.setIcon(R.mipmap.ic_launcher);
             builder1.setMessage(getResources().getString(R.string.qrcode_generator));
             builder1.setCancelable(false);

             builder1.setPositiveButton(
                     "TURN ON LOCATION",
                     new DialogInterface.OnClickListener() {

                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                         }
                     });

             builder1.setNegativeButton(
                     "RETURN",
                     new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id) {
                             startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                             dialog.cancel();
                         }
                     });
             AlertDialog alert11 = builder1.create();
             alert11.show();


         }
           else if(latitude.equalsIgnoreCase("")) {
               Toast.makeText(this, "Switch on your location service and try again", Toast.LENGTH_SHORT).show();
             startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
           }else{
               generateQR.execute(staff_id + "//" + latitude + "//" + longitude + "//" + time);
           }
    }

    @Override
    public void onResume(){
        super.onResume();

        SessionManagement sessionManagement  = new SessionManagement(getApplicationContext());
        HashMap<String,String >user = sessionManagement.getUserDetails();
        String staff_id = user.get(SessionManagement.KEY_STAFF_ID);

        try {



            String response = Master.initialiseLocationListener(getApplicationContext());
            String[] xy = response.split("//");
            Log.d("ARRAY_SIZE", response + "");
            Log.d("ARRAY_SIZE", xy.length + "");
            time = xy[0];
            longitude = xy[1];
            latitude = xy[2];
        }catch(Exception e){

        }

        @SuppressLint("StaticFieldLeak") GenerateQR generateQR = new GenerateQR(){
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                linearLayout.setVisibility(View.GONE);
                iv.setImageBitmap(bitmap);

            }
        };

        generateQR.execute(staff_id + "//" + latitude + "//" + longitude + "//" + time);

    }


    public class GenerateQR extends AsyncTask<String,Void ,Bitmap >{
        @Override
        protected Bitmap doInBackground(String... params) {
            try {

               bitmap =  TextToImageEncode(params[0]);

            } catch (WriterException e) {
                e.printStackTrace();
            }

     return bitmap;
        }

    }


    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRCodeWidth, QRCodeWidth, null
            );
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.layout_black) : getResources().getColor(R.color.layout_white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}