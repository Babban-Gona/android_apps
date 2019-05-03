package com.babbangona.evoucherapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        String staff_name,staff_role,staff_id;
        public static final String TAG = "--Main2Activity--";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            Intent intent = getIntent();
            Bundle b = intent.getExtras();
            staff_name = (String) b.get("staff_name");
            staff_role = (String) b.get("staff_role");
            staff_id = (String) b.get("staff_id");

            SessionManagement  sessionManagement = new SessionManagement(getApplicationContext());
            sessionManagement.CreateLoginSession(staff_name, staff_id, staff_role);

        }catch(Exception e){

        }


        File dir = new File(Environment.getExternalStorageDirectory().getPath(),"EVOUCHER");
        try{
            if(dir.mkdir()) {
                File dir2 = new File(Environment.getExternalStorageDirectory().getPath()+"/EVOUCHER","Id_Cards");
                dir2.mkdir();
                File dir3 = new File(Environment.getExternalStorageDirectory().getPath()+"/EVOUCHER","Members");
                dir3.mkdir();
                File dir4 = new File(Environment.getExternalStorageDirectory().getPath()+"/EVOUCHER","Excel_Exports");
                dir4.mkdir();
            } else {

            }
        }catch(Exception e){
            e.printStackTrace();
        }


        CameraSharedPref sessionManagement2 = new CameraSharedPref(getApplicationContext());
        sessionManagement2.CAMERA2("0");
        sessionManagement2.IDCARD("0");
        sessionManagement2.ACCO_Auth("0");

        new Instabug.Builder(getApplication(), "d3dff7b1f0f4a117b3d9fdf9a16ddcbf")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE, InstabugInvocationEvent.SCREENSHOT)
                .build();





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        TextView details  = findViewById(R.id.app_details);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);

        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String,String>user = sessionManagement.getUserDetails();
        String staff_name = user.get(SessionManagement.KEY_STAFF_NAME);
        String staff_id = user.get(SessionManagement.KEY_STAFF_ID);


        details.setText("");
        details.setText("Staff Name: "+staff_name+"\nStaff ID: "+staff_id+"\nApp Version: "+BuildConfig.VERSION_NAME);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_upload_1) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading Transactions (1/2)\nPlease wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            @SuppressLint("StaticFieldLeak") SyncData.UploadCompleteTansactions uploadCompleteTansactions = new SyncData.UploadCompleteTansactions(getApplicationContext()){

                @Override
                protected void onPostExecute(String s){
                    progressDialog.setMessage("Uploading Complaints (2/2)\nPlease wait...");
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            };

            uploadCompleteTansactions.execute();


            @SuppressLint("StaticFieldLeak") SyncData.UploadInCompleteTansactions uploadInCompleteTansactions = new SyncData.UploadInCompleteTansactions(getApplicationContext()){

                @Override
                protected void onPostExecute(String s){
                    progressDialog.dismiss();

                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            };uploadInCompleteTansactions.execute();



        }
       /* else if (id == R.id.nav_upload_2) {

        }*/

        else if (id == R.id.nav_download) {

            BeginDownloads();
        } else if (id == R.id.nav_export) {

            String directory_path = Environment.getExternalStorageDirectory().getPath();

            SQLiteToExcel sqliteToExcel;
            final int[] count = {0};
            GPSValues.initialiseLocationListener(getApplicationContext());
            sqliteToExcel = new SQLiteToExcel(getApplicationContext(), DBContractClass.DATABASE_NAME, directory_path + "/EVOUCHER/Excel_Exports");

            sqliteToExcel.exportSingleTable(DBContractClass.TABLE_EVOUCHER,"evoucher_data_"+staff_id+"_"+GPSValues.time+".xls", new SQLiteToExcel.ExportListener() {
                @Override
                public void onStart() {
                    Toast.makeText(Main2Activity.this, "Export Operations Started", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCompleted(String filePath) {
                    Toast.makeText(Main2Activity.this, "Export Operations Completed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(Main2Activity.this, "Export Failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,e.toString());
                }
            });

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);





        return true;
    }

    public void btnInputDistribution1(View v){
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getApplicationContext());
        String x = dbOpenHelper.getDBCount();
        if(x.matches("0")){


            AlertDialog alertDialog = new AlertDialog.Builder(Main2Activity.this).create();
            alertDialog.setTitle("E Voucher");
            alertDialog.setMessage("You have not downloaded the data needed to use this application, click on download and try again.\n\n(!)Ensure you are connected to the internet");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Download",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            BeginDownloads();
                        }

                    } );
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            onResume();
                            dialog.dismiss();
                        }

                    } );

            alertDialog.show();

        }else{
            startActivity(new Intent(getApplicationContext(),TokenValidator.class));
        }

    }

    public void btnInputDistribution2(View v){
       // startActivity(new Intent(getApplicationContext(),TokenValidator.class));
    }
    public void btnViewStats(View v){
       startActivity(new Intent(getApplicationContext(),StatisticsPage.class));
    }



    private void BeginDownloads(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading Data (1/6) \nPlease wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        @SuppressLint("StaticFieldLeak") SyncData.DownloadFieldT downloadApplication = new SyncData.DownloadFieldT(getApplicationContext()){

            @Override
            protected void onPostExecute(String s){
                progressDialog.setMessage("Downloading Data (2/6) \nPlease wait...");

            }
        };

        downloadApplication.execute();

        @SuppressLint("StaticFieldLeak") SyncData.DownloadMemberDetails memberDetails = new SyncData.DownloadMemberDetails(getApplicationContext()){

            @Override
            protected void onPostExecute(String s){
                progressDialog.setMessage("Downloading Data (3/6) \nPlease wait...");

            }
        };

        memberDetails.execute();

        @SuppressLint("StaticFieldLeak") SyncData.DownloadProductQuantityT productQuantityT = new SyncData.DownloadProductQuantityT(getApplicationContext()){

            @Override
            protected void onPostExecute(String s){
                progressDialog.setMessage("Downloading Data (4/6) \nPlease wait...");

            }
        };

        productQuantityT.execute();

        @SuppressLint("StaticFieldLeak") SyncData.DownloadLmdData lmdData = new SyncData.DownloadLmdData(getApplicationContext()){

            @Override
            protected void onPostExecute(String s){
                progressDialog.setMessage("Downloading Data (5/6) \nPlease wait...");

            }
        };

        lmdData.execute();

        @SuppressLint("StaticFieldLeak") SyncData.DownloadAccoData downloadAccoData = new SyncData.DownloadAccoData(getApplicationContext()){

            @Override
            protected void onPostExecute(String s){
                progressDialog.setMessage("Downloading Data (6/6) \nPlease wait...");

            }
        };

        downloadAccoData.execute();

        @SuppressLint("StaticFieldLeak") SyncData.DownloadUsedTokens downloadUsedTokens = new SyncData.DownloadUsedTokens(getApplicationContext()){

            @Override
            protected void onPostExecute(String s){
                progressDialog.dismiss();

            }
        };

        downloadUsedTokens.execute();
    }
}
