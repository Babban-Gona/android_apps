package com.babbangona.evoucherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GPSValues {

    public  static String time="";



    public static String initialiseLocationListener(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        final Context cxt = context;
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(location.getTime());

                if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                    //Toast.makeText(cxt, "We have GPS Provider" + time, Toast.LENGTH_LONG).show();
                    //Log.d("Location", "Time GPS: " + time);
                }// This is what we want!
                else {
                    //Toast.makeText(cxt, "We have Location Phone Provider " + location.getProvider() + time, Toast.LENGTH_LONG).show();
                    //Log.d("Location", "Time Device (" + location.getProvider() + "): " + time);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        //ActivityCompat.requestPermissions(Test.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

        if (android.support.v4.content.ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(Test.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener);
            // Note: To Stop listening use: locationManager.removeUpdates(locationListener)
        } catch (NullPointerException e) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            e.printStackTrace();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            time = dateFormat.format(date);
        }
        if (time.matches("")){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            time = dateFormat.format(date);
        }
        return time;
    }

    public static void checkPermission(Context context){

        if (android.support.v4.content.ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},5);
        }
//If the app does have this permission, then return true//

    }

}
