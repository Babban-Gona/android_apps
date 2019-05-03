package com.babbangona.shpincentiveapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ProgressBar;
import android.widget.TextView;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String staff_name,staff_role,staff_id;
    SessionManagement sessionManagement;

    ArrayList data;
    List<ActivityClass> userlist;

    TextView tvToday,tvWeek,tvPeriod,tvYear;
    TextView tvOperationPeriod;

    RecyclerView recyclerView;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;
    ComponentName componentName;
    HashMap map3;
    private static final int JOB_ID =101;
    Map<String,String> PeriodWeek;

    List<WorkingParams> workingParams;

    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


        // This is the declaration for the recycler view that adapts data for Today, This Week, This Payment Period, and Since year started card
        recyclerView = findViewById(R.id.recylcerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvToday = findViewById(R.id.visualisation_today);
        tvWeek = findViewById(R.id.visualisation_week);
        tvPeriod = findViewById(R.id.visualisation_month);
        tvYear = findViewById(R.id.visualisation_year);
        tvOperationPeriod = findViewById(R.id.operation_period);

        try {
            Intent intent = getIntent();
            Bundle b = intent.getExtras();
            staff_name = (String) b.get("staff_name");
            staff_role = (String) b.get("staff_role");
            staff_id = (String) b.get("staff_id");

            sessionManagement = new SessionManagement(getApplicationContext());
            sessionManagement.CreateLoginSession(staff_name, staff_id, staff_role);



        }catch(Exception e){

        }






        tvToday.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        tvToday.setTextColor(getResources().getColor(R.color.txt_white_color));
        CursorController("today");


        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvToday.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvToday.setTextColor(getResources().getColor(R.color.txt_white_color));

                tvWeek.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvWeek.setTextColor(getResources().getColor(R.color.txt_black_color));
                tvPeriod.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvPeriod.setTextColor(getResources().getColor(R.color.txt_black_color));
                tvYear.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvYear.setTextColor(getResources().getColor(R.color.txt_black_color));

                CursorController("today");
            }
        });

        tvWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvWeek.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvWeek.setTextColor(getResources().getColor(R.color.txt_white_color));

                tvToday.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvToday.setTextColor(getResources().getColor(R.color.txt_black_color));
                tvPeriod.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvPeriod.setTextColor(getResources().getColor(R.color.txt_black_color));
                tvYear.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvYear.setTextColor(getResources().getColor(R.color.txt_black_color));

                CursorController("week");
            }
        });
        tvPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPeriod.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvPeriod.setTextColor(getResources().getColor(R.color.txt_white_color));

                tvToday.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvToday.setTextColor(getResources().getColor(R.color.txt_black_color));
                tvWeek.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvWeek.setTextColor(getResources().getColor(R.color.txt_black_color));
                tvYear.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvYear.setTextColor(getResources().getColor(R.color.txt_black_color));

                CursorController("period");
            }
        });
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvYear.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                tvYear.setTextColor(getResources().getColor(R.color.txt_white_color));

                tvToday.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvToday.setTextColor(getResources().getColor(R.color.txt_black_color));
                tvWeek.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvWeek.setTextColor(getResources().getColor(R.color.txt_black_color));
                tvPeriod.setBackgroundColor(getResources().getColor(R.color.layout_white));
                tvPeriod.setTextColor(getResources().getColor(R.color.txt_black_color));

                CursorController("year");
            }
        });





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            AlertDialog alertDialog = new AlertDialog.Builder(Main2Activity.this).create();
            alertDialog.setTitle("SHP Incentive App");
            alertDialog.setMessage("Click exit to close this application");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Exit",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                            dialog.cancel();
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        TextView tvStaffName = findViewById(R.id.staff_name);
        TextView tvStaffID = findViewById(R.id.staff_id);

        SessionManagement personnel = new SessionManagement(getApplicationContext());
        HashMap<String,String> user = personnel.getUserDetails();
        String staff_name = user.get(SessionManagement.KEY_STAFF_NAME);
        String staff_role = user.get(SessionManagement.KEY_ROLE);
        String staff_id  = user.get(SessionManagement.KEY_STAFF_ID);
        String lastSynced = user.get(SessionManagement.LAST_SYNCED);

        tvStaffName.setText(staff_name+"\n"+staff_role+"\n"+staff_id+"\n\nApp Version "+BuildConfig.VERSION_NAME+"\nLast Synced: "+lastSynced);


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

        if (id == R.id.MyProgress) {
            startActivity(new Intent(getApplicationContext(),Main2Activity.class));
        } else if (id == R.id.MyDashboard) {
            startActivity(new Intent(getApplicationContext(),FullDashboard.class));

        } else if (id == R.id.MyAttendance) {
            startActivity(new Intent(getApplicationContext(),QRCodeGenerator.class));

        } else if (id == R.id.MyProfile) {

        }else if(id == R.id.sync_controller){


            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.syncing_dialog, null);
            dialogBuilder.setView(dialogView);

            final ProgressBar progressBar = dialogView.findViewById(R.id.activity_progress);
            final TextView textView = dialogView.findViewById(R.id.textController);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();



            final int[] sum = {0};
            final int[] y = {0};


            @SuppressLint("StaticFieldLeak") SyncData.DownloadApplication downloadApplication = new SyncData.DownloadApplication(getApplicationContext()){
                @Override
                protected void onPostExecute(String s){
                    Log.d("REGISTER_NOW",s);

                    sum[0] +=1;
                    int x = y[0];

                    y[0] = sum[0] * 20;
                    ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,x,y[0]);
                    animation.setDuration(1000);
                    progressBar.startAnimation(animation);
                    progressBar.setProgress(y[0]);
                    textView.setText("");
                    textView.setText(y[0]+"% Complete, Please Wait...");


                }

            };downloadApplication.execute();

            @SuppressLint("StaticFieldLeak") SyncData.DownloadPeriodWeek downloadPeriodWeek = new SyncData.DownloadPeriodWeek(getApplicationContext()){
                @Override
                protected void onPostExecute(String s){
                    Log.d("REGISTER_NOW",s);
                    sum[0] +=1;
                    int x = y[0];

                    y[0] = sum[0] * 20;
                    ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,x,y[0]);
                    animation.setDuration(1000);
                    progressBar.startAnimation(animation);
                    progressBar.setProgress(y[0]);
                    textView.setText("");
                    textView.setText(y[0]+"% Complete, Please Wait...");
                }

            };downloadPeriodWeek.execute();

            @SuppressLint("StaticFieldLeak") SyncData.DownloadSuccessfulStats downloadSuccessfulStats = new SyncData.DownloadSuccessfulStats(getApplicationContext()){
                @Override
                protected void onPostExecute(String s){
                    Log.d("REGISTER_NOW",s);
                    sum[0] +=1;
                    int x = y[0];

                    y[0] = sum[0] * 20;
                    ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,x,y[0]);
                    animation.setDuration(1000);
                    progressBar.startAnimation(animation);
                    progressBar.setProgress(y[0]);
                    textView.setText("");
                    textView.setText(y[0]+"% Complete, Please Wait...");
                }

            };downloadSuccessfulStats.execute();


            @SuppressLint("StaticFieldLeak") SyncData.DownloadPaymentStructure downloadPaymentStructure = new SyncData.DownloadPaymentStructure(getApplicationContext()){
                @Override
                protected void onPostExecute(String s){
                    Log.d("REGISTER_NOW",s);
                    sum[0] +=1;
                    int x = y[0];

                    y[0] = sum[0] * 20;
                    ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,x,y[0]);
                    animation.setDuration(1000);
                    progressBar.startAnimation(animation);
                    progressBar.setProgress(y[0]);
                    textView.setText("");
                    textView.setText(y[0]+"% Complete, Please Wait...");
                }

            };downloadPaymentStructure.execute();

            @SuppressLint("StaticFieldLeak") SyncData.DownloadPaymentHistory downloadPaymentHistory = new SyncData.DownloadPaymentHistory(getApplicationContext()){
                @Override
                protected void onPostExecute(String s){
                    Log.d("REGISTER_NOW",s);
                    sum[0] +=1;
                    int x = y[0];

                    y[0] = sum[0] * 20;
                    ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,x,y[0]);
                    animation.setDuration(1000);
                    progressBar.startAnimation(animation);
                    progressBar.setProgress(y[0]);
                    textView.setText("");
                    textView.setText("Your data has been downloaded");

                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {

                            alertDialog.dismiss();
                            textView.setText("");
                            textView.setText("Your data has been downloaded");
                        }
                    }, 3000);
                }

            };downloadPaymentHistory.execute();

            if  (y[0] == 100){

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        alertDialog.dismiss();
                        textView.setText("");
                        textView.setText("Your data has been downloaded");
                    }
                }, 2000);
                alertDialog.dismiss();
            }


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
     * The method below handles the analysis of the visualisation rendered on the Screen
     * This analysis is done on a 'weekly','payment period' and 'yearly' basis
     *
     * */

    public void CursorController( String module){

        sessionManagement  = new SessionManagement(getApplicationContext());
        HashMap<String,String> controller = sessionManagement.getUserDetails();
        String mStaff_id = controller.get(SessionManagement.KEY_STAFF_ID);

        if (module.trim().equalsIgnoreCase("today")){
            NotesContentProvider.DatabaseHelper notesContentProvider = new NotesContentProvider.DatabaseHelper(getApplicationContext());
            data = notesContentProvider.recordsControllerToday(mStaff_id,module);


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String date_updated  = dateFormat.format(date);


            String weekDay =null;
            tvOperationPeriod.setText("");
            String input_date_string="2015-02-24";
            SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
            Date date1;
            try {
                date1 = dateformat.parse(date_updated);




                DateFormat dayFormate=new SimpleDateFormat("EE dd, MMM");
                weekDay =dayFormate.format(date1);




            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



            tvOperationPeriod.setText("");
            if(module.trim().equalsIgnoreCase("today")) tvOperationPeriod.setText(weekDay);


            Log.d("JJJJJJ",data+"");
            userlist = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject Quiz = jsonArray.getJSONObject(i);
                    if(!Quiz.getString("activity_name").trim().equalsIgnoreCase("null")){
                    userlist.add(new ActivityClass(
                            Quiz.getString("activity_name"),
                            Quiz.getString("count"),
                            module
                    ));}
                }
            }

            catch (JSONException e1) {
                Log.e("FAILED", "Json parsing error: " + e1.getMessage());
            }

            ActivityRecycler adapter = new ActivityRecycler(this, userlist);
            recyclerView.setAdapter(adapter);

        }else{






            NotesContentProvider.DatabaseHelper notesContentProvider = new NotesContentProvider.DatabaseHelper(getApplicationContext());

            PeriodWeek = new HashMap<>();
            PeriodWeek = notesContentProvider.getPeriodWeek(mStaff_id);

            if (PeriodWeek == null){
                PeriodWeek = new HashMap<>();
                PeriodWeek.put("period_start","0000-00-00");
                PeriodWeek.put("period_end","0000-00-00");
                PeriodWeek.put("week_start","0000-00-00");
                PeriodWeek.put("week_end","0000-00-00");

            }

            data = notesContentProvider.recordsController(mStaff_id,module,PeriodWeek.get("period_start"),PeriodWeek.get("period_end"),PeriodWeek.get("week_start"),PeriodWeek.get("week_end"));
            String weekStart = null,weekEnd=null,periodStart=null,periodEnd = null;
            tvOperationPeriod.setText("");
            String input_date_string="2015-02-24";
            SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
            Date date1,date2,date3,date4;
            try {
                date1 = dateformat.parse(PeriodWeek.get("week_start"));
                date2 = dateformat.parse(PeriodWeek.get("week_end"));
                date3 = dateformat.parse(PeriodWeek.get("period_start"));
                date4 = dateformat.parse(PeriodWeek.get("period_end"));



                DateFormat dayFormate=new SimpleDateFormat("EE dd, MMM");
                weekStart =dayFormate.format(date1);
                weekEnd =dayFormate.format(date2);
                periodStart =dayFormate.format(date3);
                periodEnd =dayFormate.format(date4);



            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SimpleDateFormat dateformat2 =new SimpleDateFormat("yyyy");
            Date datex = new Date();


            String time = dateformat2.format(datex);

            if(module.trim().equalsIgnoreCase("week")) tvOperationPeriod.setText(weekStart+"  To  "+weekEnd);
            else if(module.trim().equalsIgnoreCase("period")) tvOperationPeriod.setText(periodStart+"  To  "+periodEnd);
           else if(module.trim().equalsIgnoreCase("year")) tvOperationPeriod.setText(time);






            Log.d("JJJJJJ",data+"");
            userlist = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject Quiz = jsonArray.getJSONObject(i);
                    userlist.add(new ActivityClass(
                            Quiz.getString("staffid_activitycode_time"),
                            Quiz.getString("target"),
                            Quiz.getString("activity_id"),
                            Quiz.getString("process_completed"),
                            Quiz.getString("time_completed"),
                            Quiz.getString("activity_name"),
                            module
                    ));
                }
            }

            catch (JSONException e1) {
                Log.e("FAILED", "Json parsing error: " + e1.getMessage());
            }

            YearActivityRecycler adapter = new YearActivityRecycler(this, userlist);
            recyclerView.setAdapter(adapter);

        }



    }




}
