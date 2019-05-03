package com.babbangona.shpincentiveapp;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason Wei
 *
 */
public class NotesContentProvider extends ContentProvider {

    /*
    * This activity contains all the methods that retrieves data from the SQlite database
    * also it has the content provider implementation that allows other application to access its database
    *
    * */

    private static final String TAG = "NotesContentProvider";

    private static final String DATABASE_NAME = "incentive.db";

    private static final String NOTES_TABLE_NAME = "operations";

    private static final String NOTES_TABLE_WORKING_STATS = "incentive_working_stats";

    private static final String NOTES_TABLE_PERIOD_WEEK = "incentive_period_week_binding";

    private static final String NOTES_TABLE_SUCCESSFUL_STATS = "incentive_successful_stats";

    private static final String NOTES_TABLE_PAYMENT_STRUCTURE = "incentive_payment_structure";

    private static final String NOTES_TABLE_PAYMENT_HISTORY = "incentive_payment_history";

    private static final int DATABASE_VERSION = 1;





    public static final String AUTHORITY = "com.babbangona.shpincentiveapp";

    private static final UriMatcher sUriMatcher;

    // url provided to other applications
    static final String URL = "content://" + AUTHORITY + "/"+NOTES_TABLE_NAME;

    private static final int NOTES = 1;

    private static final int NOTES_ID = 2;

    private static HashMap<String, String> notesProjectionMap;

    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //OnCreate method is called to create all the important databases for our android application
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " (" + Note.Notes.OPERATION_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Note.Notes.ACTIVITY_NAME + " VARCHAR(255),"
                    + Note.Notes.STAFF_ID + " LONGTEXT," +Note.Notes.UPLOAD_STATUS+" TEXT,"+Note.Notes.COMPLETION_DATE+" TEXT,"+Note.Notes.UNIQUE_ID+" TEXT,"+Note.Notes.STATISTICS_DATE+" TEXT, "+Note.Notes.ACTIVITY_FLAG+" TEXT );");

            db.execSQL("CREATE TABLE "+NOTES_TABLE_WORKING_STATS+" (" +Note.Notes.STAFFID_ACTIVITYCODE_TIME+" TEXT  PRIMARY KEY,"+Note.Notes.ACTIVITY_ID+" TEXT,"+Note.Notes.ACTIVITY_NAME+" TEXT, "+Note.Notes.STAFF_ID+" TEXT,"+Note.Notes.PROCESS_COMPLETED+
            " INTEGER,"+Note.Notes.TARGET+" INTEGER,"+Note.Notes.TIME_COMPLETED+" TEXT, "+Note.Notes.TIME_CREATED+" TEXT, "+Note.Notes.TIME_UPDATED+" TEXT );");

            db.execSQL("CREATE TABLE "+NOTES_TABLE_SUCCESSFUL_STATS+" (" +Note.Notes.STAFFID_ACTIVITYCODE_TIME+" TEXT  PRIMARY KEY,"+Note.Notes.ACTIVITY_ID+" TEXT,"+Note.Notes.ACTIVITY_NAME+" TEXT, "+Note.Notes.STAFF_ID+" TEXT,"+Note.Notes.PROCESS_COMPLETED+
                    " INTEGER,"+Note.Notes.TIME_COMPLETED+" TEXT, "+Note.Notes.TIME_CREATED+" TEXT, "+Note.Notes.TIME_UPDATED+" TEXT );");

            db.execSQL(" CREATE TABLE "+NOTES_TABLE_PERIOD_WEEK+" ("+Note.Notes.STAFF_ID+" TEXT PRIMARY KEY,"+Note.Notes.PERIOD_START+" TEXT,"+Note.Notes.PERIOD_END+" TEXT,"+Note.Notes.WEEK_START+" TEXT,"+Note.Notes.WEEK_END+" TEXT );");

            db.execSQL(" CREATE TABLE "+NOTES_TABLE_PAYMENT_STRUCTURE+" ("+Note.Notes.STAFFID_ACTIVITYID+" TEXT PRIMARY KEY,"+Note.Notes.STAFF_ID+" TEXT,"+Note.Notes.ACTIVITY_ID+" TEXT,"+Note.Notes.BASE_PAY+" TEXT,"+Note.Notes.SUCCESSFUL_PAY+" TEXT,"+Note.Notes.MISCELLANEOUS_BAL +" TEXT,"+Note.Notes.OPENING_BAL +" TEXT );");

            db.execSQL(" CREATE TABLE "+ NOTES_TABLE_PAYMENT_HISTORY +" ("+Note.Notes.PAYMENT_ID+" TEXT PRIMARY KEY,"+Note.Notes.STAFF_ID+" TEXT,"+Note.Notes.ACTIVITY_ID+" TEXT,"+Note.Notes.ACTIVITY_NAME+" TEXT, "+Note.Notes.MONEY_EARNED+" TEXT,"+Note.Notes.MONEY_PAID+" TEXT,"+Note.Notes.BALANCE_BROUGHT_FORWARD +" TEXT,"+Note.Notes.MISCELLANEOUS_BAL +" TEXT,"+Note.Notes.PAYMENT_PERIOD +" TEXT,"+Note.Notes.PAYMENT_DATE+" TEXT );");

        }

        // OnUpgrade method is called to handle database changes
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        }




        //This method returns the number of records in the users offline table
        public int getRecordsCount( String staff_id) {

            int count = 0;
            SQLiteDatabase database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select count("+Note.Notes.OPERATION_ID+") from "+NOTES_TABLE_NAME+" where "+Note.Notes.STAFF_ID+" = '"+staff_id+"' ", null);

            if(cursor.moveToFirst()){
              count = cursor.getInt(cursor.getPosition());

            }

            cursor.close();

            return count;
        }

        /* This method has been deprecated*/
        public int getUploadedRecordsCount( String staff_id) {

            int count = 0;
            SQLiteDatabase database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select count("+Note.Notes.OPERATION_ID+") from "+NOTES_TABLE_NAME+" where "+Note.Notes.STAFF_ID+" = '"+staff_id+"' and "+Note.Notes.UPLOAD_STATUS+" = '1' ", null);

            if(cursor.moveToFirst()){
                count = cursor.getInt(cursor.getPosition());

            }

            cursor.close();

            return count;
        }


// This method is used to retrieve data that feeds into the 'Today' view for the recycler
        public ArrayList<Map<String,String>> recordsControllerToday(String Staff_id, String Module){

     SQLiteDatabase database = getWritableDatabase();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String date_updated  = dateFormat.format(date);
            Map<String, String> map = null;
            ArrayList<Map<String, String>>wordlist;
            wordlist =new ArrayList<Map<String, String>>();

           Cursor cursor = database.rawQuery("select "+Note.Notes.ACTIVITY_NAME+" as activity_name, count("+Note.Notes.ACTIVITY_NAME+") as count from "+NOTES_TABLE_NAME+" where "+Note.Notes.STATISTICS_DATE+" >= '" +
                    ""+date_updated+"' and "+Note.Notes.STAFF_ID+"  = \""+Staff_id+"\"",null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()) { // this is to move the cursor to the last
                map = new HashMap<>();
                map.put("activity_name", cursor.getString(0));
                map.put("count", cursor.getString(1));


                wordlist.add(map);
                cursor.moveToNext(); // i'm editing this app
            }
            return wordlist;



        }

        // This method is used to retrieve data that feeds into 'This week', 'This payment period' and 'Since year started'
        public ArrayList<Map<String,String>>  recordsController(String Staff_id, String Module,String period_start,String period_end,String weekStart,String weekEnd){


            Cursor cursor = null;
            Cursor cursor1 = null;
            SQLiteDatabase database = getWritableDatabase();

            Map<String, String> map = null;
            ArrayList<Map<String, String>>wordlist;
            wordlist =new ArrayList<Map<String, String>>();
             if (Module.trim().equalsIgnoreCase("week")){



                cursor = database.rawQuery("select a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+",a."+Note.Notes.TIME_CREATED+",a."+Note.Notes.TARGET+",sum(b."+Note.Notes.PROCESS_COMPLETED+"),a."+Note.Notes.ACTIVITY_ID+",a."+Note.Notes.ACTIVITY_NAME+" from "+NOTES_TABLE_SUCCESSFUL_STATS+" b join "+NOTES_TABLE_WORKING_STATS+" a on b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" where a."+Note.Notes.STAFF_ID+" = \""+Staff_id+"\" and a."+Note.Notes.TIME_COMPLETED+" between '"+weekStart+"' and '"+weekEnd+"' group by a."+Note.Notes.ACTIVITY_ID+"",null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    map = new HashMap<>();
                    map.put("staffid_activitycode_time", cursor.getString(0));
                    map.put("time_completed", cursor.getString(1));
                    map.put("target", cursor.getString(2));
                    map.put("process_completed", cursor.getString(3));
                    map.put("activity_id", cursor.getString(4));
                    map.put("activity_name", cursor.getString(5));
                    wordlist.add(map);
                    cursor.moveToNext();

                }

                 cursor1 = database.rawQuery("select b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+",b."+Note.Notes.TIME_CREATED+",sum(b."+Note.Notes.PROCESS_COMPLETED+"),b."+Note.Notes.ACTIVITY_ID+",b."+Note.Notes.ACTIVITY_NAME+" from "+NOTES_TABLE_SUCCESSFUL_STATS+" b left join "+NOTES_TABLE_WORKING_STATS+" a on b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" where a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" is null and  b."+Note.Notes.STAFF_ID+" = \""+Staff_id+"\" and b."+Note.Notes.TIME_COMPLETED+" between '"+weekStart+"' and '"+weekEnd+"' group by b."+Note.Notes.ACTIVITY_ID+"",null);
                 cursor1.moveToFirst();
                 while(!cursor1.isAfterLast()) {
                     map = new HashMap<>();
                     map.put("staffid_activitycode_time", cursor1.getString(0));
                     map.put("time_completed", cursor1.getString(1));
                     map.put("target", "0");
                     map.put("process_completed", cursor1.getString(2));
                     map.put("activity_id", cursor1.getString(3));
                     map.put("activity_name", cursor1.getString(4));
                     wordlist.add(map);
                     cursor1.moveToNext();

                 }
                Log.d("UUIIUU",wordlist+"");
                return wordlist;

            }else if(Module.trim().equalsIgnoreCase("period")){


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                Date date = new Date();
                String date_updated  = dateFormat.format(date);
                String start_date = date_updated+"-01";
                String end_date = date_updated+"-31";


                cursor = database.rawQuery("select a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+",a."+Note.Notes.TIME_CREATED+",a."+Note.Notes.TARGET+",sum(b."+Note.Notes.PROCESS_COMPLETED+"),a."+Note.Notes.ACTIVITY_ID+",a."+Note.Notes.ACTIVITY_NAME+" from "+NOTES_TABLE_SUCCESSFUL_STATS+" b join "+NOTES_TABLE_WORKING_STATS+" a on b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" where a."+Note.Notes.STAFF_ID+" = \""+Staff_id+"\" and a."+Note.Notes.TIME_COMPLETED+" between '"+period_start+"' and '"+period_end+"' group by a."+Note.Notes.ACTIVITY_ID+"",null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                     map = new HashMap<>();
                    map.put("staffid_activitycode_time", cursor.getString(0));
                    map.put("time_completed", cursor.getString(1));
                    map.put("target", cursor.getString(2));
                    map.put("process_completed", cursor.getString(3));
                    map.put("activity_id", cursor.getString(4));
                    map.put("activity_name", cursor.getString(5));
                    wordlist.add(map);
                    cursor.moveToNext();

                }

                 cursor1 = database.rawQuery("select b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+",b."+Note.Notes.TIME_CREATED+",sum(b."+Note.Notes.PROCESS_COMPLETED+"),b."+Note.Notes.ACTIVITY_ID+",b."+Note.Notes.ACTIVITY_NAME+" from "+NOTES_TABLE_SUCCESSFUL_STATS+" b left join "+NOTES_TABLE_WORKING_STATS+" a on b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" where a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" is null and  b."+Note.Notes.STAFF_ID+" = \""+Staff_id+"\" and b."+Note.Notes.TIME_COMPLETED+" between '"+period_start+"' and '"+period_end+"' group by b."+Note.Notes.ACTIVITY_ID+"",null);
                 cursor1.moveToFirst();
                 while(!cursor1.isAfterLast()) {
                     map = new HashMap<>();
                     map.put("staffid_activitycode_time", cursor1.getString(0));
                     map.put("time_completed", cursor1.getString(1));
                     map.put("target", "0");
                     map.put("process_completed", cursor1.getString(2));
                     map.put("activity_id", cursor1.getString(3));
                     map.put("activity_name", cursor1.getString(4));
                     wordlist.add(map);
                     cursor1.moveToNext();

                 }
                 Log.d("UUIIUU",wordlist+"");


                return wordlist;
            }else if(Module.trim().equalsIgnoreCase("year")){

                cursor = database.rawQuery("select a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+",a."+Note.Notes.TIME_CREATED+",a."+Note.Notes.TARGET+",sum(b."+Note.Notes.PROCESS_COMPLETED+"),a."+Note.Notes.ACTIVITY_ID+",a."+Note.Notes.ACTIVITY_NAME+" from "+NOTES_TABLE_SUCCESSFUL_STATS+" b join "+NOTES_TABLE_WORKING_STATS+" a on b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" where a."+Note.Notes.STAFF_ID+" = \""+Staff_id+"\" group by a."+Note.Notes.ACTIVITY_ID+"",null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    map = new HashMap<>();
                    map.put("staffid_activitycode_time", cursor.getString(0));
                    map.put("time_completed", cursor.getString(1));
                    map.put("target", cursor.getString(2));
                    map.put("process_completed", cursor.getString(3));
                    map.put("activity_id", cursor.getString(4));
                    map.put("activity_name", cursor.getString(5));
                    wordlist.add(map);
                    cursor.moveToNext();

                }
                return wordlist;
            }
            cursor.close();

            return wordlist;
        }

        //This method retrieves the 'This week', 'This payment period' and 'Since year started' that feeds into the bar chart
        public ArrayList<Map<String,String>> ActivitData(String staff_id, String activity_id, String duration,String periodStart,String periodEnd, String weekStart, String weekEnd){

            SQLiteDatabase database = getReadableDatabase();
            ArrayList<Map<String,String>> statisticalData = new ArrayList<>();
            Cursor cursor = null;
          /*  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date date = new Date();
            String date_updated  = dateFormat.format(date);
            String start_date = date_updated+"-01";
            String end_date = date_updated+"-31";*/

            if(duration.trim().equalsIgnoreCase("week")){
                cursor = database.rawQuery("select a."+Note.Notes.TIME_COMPLETED+",b."+Note.Notes.PROCESS_COMPLETED+" from "+NOTES_TABLE_WORKING_STATS+" a join "+NOTES_TABLE_SUCCESSFUL_STATS+" b on a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+"  where a."+Note.Notes.STAFF_ID+" = \""+staff_id+"\" and a."+Note.Notes.ACTIVITY_ID+" = \""+activity_id+"\" and  a."+Note.Notes.TIME_COMPLETED+" between '"+weekStart+"' and '"+weekEnd+"' group by a."+Note.Notes.TIME_COMPLETED+" ",null);

            }
            if(duration.trim().equalsIgnoreCase("period")){
                cursor = database.rawQuery("select a."+Note.Notes.TIME_COMPLETED+", sum(b."+Note.Notes.PROCESS_COMPLETED+")  from " +
                        ""+NOTES_TABLE_WORKING_STATS+" a join "+NOTES_TABLE_SUCCESSFUL_STATS+" b on a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" where a."+Note.Notes.ACTIVITY_ID+" = \""+activity_id+"\" and  a."+Note.Notes.STAFF_ID+" =\""+staff_id+"\" and a."+Note.Notes.TIME_COMPLETED+" between '"+periodStart+"' and '"+periodEnd+"' " +
                        "group by date(a."+Note.Notes.TIME_COMPLETED+", 'Weekday 2')  ",null);

            }
            if(duration.trim().equalsIgnoreCase("year")){
                cursor = database.rawQuery("select a."+Note.Notes.TIME_COMPLETED+", sum(b."+Note.Notes.PROCESS_COMPLETED+") " +
                        "from "+NOTES_TABLE_WORKING_STATS+" a join "+NOTES_TABLE_SUCCESSFUL_STATS+" b on a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = b."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" where a."+Note.Notes.ACTIVITY_ID+" = \""+activity_id+"\" and  a."+Note.Notes.STAFF_ID+" =\""+staff_id+"\" " +
                        "group by strftime('%m', a."+Note.Notes.TIME_COMPLETED+" )",null);

            }


            cursor.moveToFirst();
            Map<String, String> map = null;
            while(!cursor.isAfterLast()) {
                map = new HashMap<>();

                map.put("date", cursor.getString(0));
                map.put("frequency", cursor.getString(1));
                statisticalData.add( map);
                cursor.moveToNext();

            }
            return statisticalData;

        }

        // This method has been deprecated
        public ArrayList<Map<String,String>>  recordsWorkingController(String Staff_id, String Module, String Target, String Activity){


            Cursor cursor = null;
            SQLiteDatabase database = getWritableDatabase();

            Map<String, String> map = null;
            ArrayList<Map<String, String>>wordlist;
            wordlist =new ArrayList<Map<String, String>>();
            if(Module.trim().equalsIgnoreCase("today")){

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String date_updated  = dateFormat.format(date);

                String FDate = date_updated+" 00:00:00";
                String LDate = date_updated+" 23:59:59";



                cursor = database.rawQuery("select "+Note.Notes.ACTIVITY_NAME+" as activity_name, count("+Note.Notes.ACTIVITY_NAME+") as count from "+NOTES_TABLE_NAME+" where "+Note.Notes.STATISTICS_DATE+" >= '" +
                        ""+date_updated+"' and "+Note.Notes.STAFF_ID+"  = \""+Staff_id+"\" group by "+Note.Notes.ACTIVITY_NAME+"",null);
                cursor.moveToFirst();

                while(!cursor.isAfterLast()) { // this is to move the cursor to the last
                    map = new HashMap<>();
                    map.put("activity_name", cursor.getString(0));
                    map.put("count", cursor.getString(1));
                    wordlist.add(map);
                    cursor.moveToNext(); // i'm editing this app
                }
                return wordlist;




            }else if (Module.trim().equalsIgnoreCase("week")){



                cursor = database.rawQuery("select  count("+Note.Notes.ACTIVITY_NAME+") as count from "+NOTES_TABLE_NAME+" where DATE("+Note.Notes.STATISTICS_DATE+") >= Date('now','weekday 0','-7 days') group by "+Note.Notes.ACTIVITY_NAME+"",null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    map = new HashMap<>();
                    map.put("activity_name", cursor.getString(0));
                    map.put("count", cursor.getString(1));
                    wordlist.add(map);
                    cursor.moveToNext();

                }
                return wordlist;

            }else if(Module.trim().equalsIgnoreCase("period")){


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
                Date date = new Date();
                String date_updated  = dateFormat.format(date);
                String start_date = date_updated+"-01";
                String end_date = date_updated+"-31";


                cursor = database.rawQuery("select "+Note.Notes.ACTIVITY_NAME+" as activity_name, count("+Note.Notes.ACTIVITY_NAME+") as count from "+NOTES_TABLE_NAME+" where "+Note.Notes.STATISTICS_DATE+" between '"+start_date+"' and '"+end_date+"' group by "+Note.Notes.ACTIVITY_NAME+"",null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    map = new HashMap<>();
                    map.put("activity_name", cursor.getString(0));
                    map.put("count", cursor.getString(1));
                    wordlist.add(map);
                    cursor.moveToNext();

                }
                return wordlist;
            }else if(Module.trim().equalsIgnoreCase("year")){

                cursor = database.rawQuery("select "+Note.Notes.ACTIVITY_NAME+" as activity_name, count("+Note.Notes.ACTIVITY_NAME+") as count from "+NOTES_TABLE_NAME+" group by "+Note.Notes.ACTIVITY_NAME+"",null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    map = new HashMap<>();
                    map.put("activity_name", cursor.getString(0));
                    map.put("count", cursor.getString(1));
                    wordlist.add(map);
                    cursor.moveToNext();

                }
                return wordlist;
            }
            cursor.close();

            return wordlist;
        }

        /*
        * This method inserts the records downloaded from the server into the incentive_working_stats table based on the logged on user
        * */
        public String WorkingInsertController(JSONArray jsonArray){
            SQLiteDatabase database = getWritableDatabase();
            JSONObject jsonObject = null;
            for(int i = 0; i< jsonArray.length(); i++){
                try {
                    int Check  = 0;
                    jsonObject = jsonArray.getJSONObject(i);
                    String check = "select count("+Note.Notes.STAFFID_ACTIVITYCODE_TIME+") from "+NOTES_TABLE_WORKING_STATS+" where "+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = \""+jsonObject.getString("staffid_activitycode_time")+"\"";
                    Cursor cursor = database.rawQuery(check,null);
                    cursor.moveToFirst();

                    if(!cursor.isAfterLast()){
                        Check = cursor.getInt(0);

                    }

                    if (Check > 0){
/*

                        String insert  = " insert into "+NOTES_TABLE_WORKING_STATS+"("+Note.Notes.STAFFID_ACTIVITYCODE_TIME+","+Note.Notes.ACTIVITY_ID+","+Note.Notes.ACTIVITY_NAME+","+Note.Notes.STAFF_ID+","+Note.Notes.PROCESS_COMPLETED+
                        ","+Note.Notes.TARGET+","+Note.Notes.TIME_COMPLETED+","+Note.Notes.TIME_CREATED+","+Note.Notes.TIME_UPDATED+") values(\""+jsonObject.getString("")+"\";
*/




                    }else if(Check == 0){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Note.Notes.STAFFID_ACTIVITYCODE_TIME,jsonObject.getString("staffid_activitycode_time"));
                        contentValues.put(Note.Notes.ACTIVITY_ID,jsonObject.getString("activity_id"));
                        contentValues.put(Note.Notes.ACTIVITY_NAME, jsonObject.getString("activity_name"));
                        contentValues.put(Note.Notes.STAFF_ID,jsonObject.getString("staff_id"));
                        contentValues.put(Note.Notes.PROCESS_COMPLETED, jsonObject.getString("process_completed"));
                        contentValues.put(Note.Notes.TARGET, jsonObject.getString("target"));
                        contentValues.put(Note.Notes.TIME_COMPLETED,jsonObject.getString("time_completed"));
                        contentValues.put(Note.Notes.TIME_CREATED,jsonObject.getString("time_created"));
                        contentValues.put(Note.Notes.TIME_UPDATED, jsonObject.getString("time_updated"));

                        database.insert(NOTES_TABLE_WORKING_STATS,null,contentValues);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            return "";
        }

        //This method returns the sum of the division between the process completed and the target from the incentive_working_stats table
        public String getCompletedTarget(String staff_id,String ActivityID, String date_completed, String module,String period_start,String period_end,String weekStart,String weekEnd){

            String get = "";
            SQLiteDatabase database = getWritableDatabase();
            Cursor cursor = null;

            if(module.trim().equalsIgnoreCase("year")) {
                String Select = "select sum((" + Note.Notes.PROCESS_COMPLETED + "*1.0)/"+Note.Notes.TARGET+") from " + NOTES_TABLE_WORKING_STATS + " where " + Note.Notes.ACTIVITY_ID + " = '" + ActivityID + "' and "+Note.Notes.STAFF_ID+" = \""+staff_id+"\"  group by "+Note.Notes.ACTIVITY_ID+"";
                cursor = database.rawQuery(Select, null);
                cursor.moveToFirst();
                if(!cursor.isAfterLast()) {
                    get = cursor.getString(0);

                }
            }

            else if(module.trim().equalsIgnoreCase("period")) {
                String Select = "select sum((" + Note.Notes.PROCESS_COMPLETED + "*1.0)/"+Note.Notes.TARGET+") from " + NOTES_TABLE_WORKING_STATS + " where " + Note.Notes.ACTIVITY_ID + " = '" + ActivityID + "' and "+Note.Notes.STAFF_ID+" = \""+staff_id+"\" and "+Note.Notes.TIME_COMPLETED+" between '"+period_start+"' and '"+period_end+"'  group by "+Note.Notes.ACTIVITY_ID+"";
                cursor = database.rawQuery(Select, null);
                cursor.moveToFirst();
                if(!cursor.isAfterLast()) {
                    get = cursor.getString(0);

                }
            }

            else if(module.trim().equalsIgnoreCase("week")) {
                String Select = "select sum((" + Note.Notes.PROCESS_COMPLETED + "*1.0)/"+Note.Notes.TARGET+") from " + NOTES_TABLE_WORKING_STATS + " where " + Note.Notes.ACTIVITY_ID + " = '" + ActivityID + "' and "+Note.Notes.STAFF_ID+" = \""+staff_id+"\" and "+Note.Notes.TIME_COMPLETED+" between '"+weekStart+"' and '"+weekEnd+"'  group by "+Note.Notes.ACTIVITY_ID+"";
                cursor = database.rawQuery(Select, null);
                cursor.moveToFirst();
                if(!cursor.isAfterLast()) {
                    get = cursor.getString(0);

                }
            }



            return get;
        }

        // This method returns the data that feeds into the current payment period
        public ArrayList<Map<String,String>> FullActivityData(String staff_id, String periodStart,String periodEnd){

            SQLiteDatabase database = getReadableDatabase();
            ArrayList<Map<String,String>> statisticalData = new ArrayList<>();
            Map<String, String> map = null;



            Cursor getMiscAndAmount1 = null;
            getMiscAndAmount1  = database.rawQuery("select sum("+Note.Notes.OPENING_BAL +") from "+NOTES_TABLE_PAYMENT_STRUCTURE+" where "+Note.Notes.STAFF_ID+" = \""+staff_id+"\"",null);
            getMiscAndAmount1.moveToFirst();

            while(!getMiscAndAmount1.isAfterLast()){

                map = new HashMap<>();
                map.put("date","Open Bal");
                map.put("frequency",getMiscAndAmount1.getString(0));
                statisticalData.add(map);
                getMiscAndAmount1.moveToNext();

            }
            Log.d("HHEELLOO",statisticalData+"");



           // This cursor gets the Miscellaneous balance
            Cursor getMiscAndAmount = null;

            getMiscAndAmount  = database.rawQuery("select sum("+Note.Notes.MISCELLANEOUS_BAL+") from "+NOTES_TABLE_PAYMENT_STRUCTURE+" where "+Note.Notes.STAFF_ID+" = \""+staff_id+"\"",null);
            getMiscAndAmount.moveToFirst();
          if(getMiscAndAmount != null) {
              while (!getMiscAndAmount.isAfterLast()) {
                  map = new HashMap<>();
                  map.put("date", "Misc");
                  map.put("frequency", getMiscAndAmount.getString(0));
                  statisticalData.add(map);
                  getMiscAndAmount.moveToNext();
              }
          }
           Log.d("HHEELLOO",statisticalData+"");



            Cursor getSuccessful = null;
            getSuccessful = database.rawQuery("select c."+Note.Notes.ACTIVITY_NAME+" as statistics_date,(sum(c."+Note.Notes.PROCESS_COMPLETED+")* b."+Note.Notes.SUCCESSFUL_PAY+") as frequency from " +
                    ""+NOTES_TABLE_SUCCESSFUL_STATS+" c  join "+NOTES_TABLE_PAYMENT_STRUCTURE+" b on c."+Note.Notes.ACTIVITY_ID+" = b."+Note.Notes.ACTIVITY_ID+"   where not exists(select 1 from "+NOTES_TABLE_WORKING_STATS+" where "+NOTES_TABLE_WORKING_STATS+"."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = c."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" ) and  c."+Note.Notes.STAFF_ID+" =\""+staff_id+"\" and c."+Note.Notes.TIME_COMPLETED+" between '"+periodStart+"' and '"+periodEnd+"' " +
                    "group by c."+Note.Notes.ACTIVITY_NAME+" order by frequency asc ",null);

            getSuccessful.moveToFirst();

            while(!getSuccessful.isAfterLast()) {
                map = new HashMap<>();

                map.put("date", getSuccessful.getString(0));
                map.put("frequency", getSuccessful.getString(1));
                statisticalData.add( map);
                getSuccessful.moveToNext();

            }



           //This cursor get what has been earned so far for activities
            Cursor cursor = null;
            cursor = database.rawQuery("select a."+Note.Notes.ACTIVITY_NAME+" as statistics_date, ((sum(a."+Note.Notes.PROCESS_COMPLETED+"*1.0/a."+Note.Notes.TARGET+") * b."+Note.Notes.BASE_PAY+")+(sum(c."+Note.Notes.PROCESS_COMPLETED+")* b."+Note.Notes.SUCCESSFUL_PAY+")) as frequency from " +
                    ""+NOTES_TABLE_WORKING_STATS+" a  join "+NOTES_TABLE_PAYMENT_STRUCTURE+" b on a."+Note.Notes.ACTIVITY_ID+" = b."+Note.Notes.ACTIVITY_ID+" join "+NOTES_TABLE_SUCCESSFUL_STATS+" c on a."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = c."+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" where   a."+Note.Notes.STAFF_ID+" =\""+staff_id+"\" and a."+Note.Notes.TIME_COMPLETED+" between '"+periodStart+"' and '"+periodEnd+"' " +
                    "group by a."+Note.Notes.ACTIVITY_NAME+" order by frequency asc ",null);

            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {
                map = new HashMap<>();

                map.put("date", cursor.getString(0));
                map.put("frequency", cursor.getString(1));
                statisticalData.add( map);
                cursor.moveToNext();

            }
            Log.d("HHEELLOO",statisticalData+"");

            // This cursor gets the Miscellaneous balance


            Log.d("HHEELLOO",statisticalData+"");

            // This cursor gets the Miscellaneous balance


            return statisticalData;

        }

        // This method returns the start and end of the week and payment period based on the logged on user
        public Map<String,String> getPeriodWeek(String Staff_id){

            SQLiteDatabase database = getWritableDatabase();
            Map<String, String> map = null;
            ArrayList<Map<String, String>>wordlist;
            wordlist =new ArrayList<Map<String, String>>();

            Cursor cursor = database.rawQuery("select "+Note.Notes.PERIOD_START+","+Note.Notes.PERIOD_END+","+Note.Notes.WEEK_START+","+Note.Notes.WEEK_END+" from "+NOTES_TABLE_PERIOD_WEEK+" where "+Note.Notes.STAFF_ID+"  = \""+Staff_id+"\"",null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {

                // this is to move the cursor to the last
                map = new HashMap<>();
                map.put("period_start", cursor.getString(0));
                map.put("period_end", cursor.getString(1));
                map.put("week_start", cursor.getString(2));
                map.put("week_end", cursor.getString(3));



                cursor.moveToNext(); // i'm editing this app
            }
            return map;



        }

        //this method saves the start and end of the week and payment period based on the logged on user
        public String insertPeriodWeekBinding(JSONArray jsonArray){

            SQLiteDatabase database = getWritableDatabase();

            JSONObject jsonObject = null;

            for(int i = 0; i<jsonArray.length(); i++){

                try {
                    jsonObject = jsonArray.getJSONObject(i);

                    int Check  = 0;

                    String check = "select count("+Note.Notes.STAFF_ID+") from "+NOTES_TABLE_PERIOD_WEEK+" where "+Note.Notes.STAFF_ID+" = \""+jsonObject.getString("staff_id")+"\"";


                    Cursor cursor = database.rawQuery(check,null);
                    cursor.moveToFirst();

                    if(!cursor.isAfterLast()) {
                        Check = cursor.getInt(0);
                    }

                    if(Check == 0) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Note.Notes.STAFF_ID, jsonObject.getString("staff_id"));
                        contentValues.put(Note.Notes.PERIOD_START, jsonObject.getString("period_start"));
                        contentValues.put(Note.Notes.PERIOD_END, jsonObject.getString("period_end"));
                        contentValues.put(Note.Notes.WEEK_START, jsonObject.getString("week_start"));
                        contentValues.put(Note.Notes.WEEK_END, jsonObject.getString("week_end"));


                        database.insert(NOTES_TABLE_PERIOD_WEEK, null, contentValues);
                    }else{

                        ContentValues contentValues = new ContentValues();

                        contentValues.put(Note.Notes.PERIOD_START, jsonObject.getString("period_start"));
                        contentValues.put(Note.Notes.PERIOD_END, jsonObject.getString("period_end"));
                        contentValues.put(Note.Notes.WEEK_START, jsonObject.getString("week_start"));
                        contentValues.put(Note.Notes.WEEK_END, jsonObject.getString("week_end"));




                        String where = Note.Notes.STAFF_ID+"=?";
                        String[] whereArgs = new String[] {String.valueOf(jsonObject.getString("staff_id"))};

                        database.update(NOTES_TABLE_PERIOD_WEEK, contentValues, where, whereArgs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }




            return "";

        }

        // This method saves the successful statistics into the SQlite database
        public String SuccessfulInsertController(JSONArray jsonArray){
            SQLiteDatabase database = getWritableDatabase();
            JSONObject jsonObject = null;
            for(int i = 0; i< jsonArray.length(); i++){
                try {
                    int Check  = 0;
                    jsonObject = jsonArray.getJSONObject(i);
                    String check = "select count("+Note.Notes.STAFFID_ACTIVITYCODE_TIME+") from "+NOTES_TABLE_SUCCESSFUL_STATS+" where "+Note.Notes.STAFFID_ACTIVITYCODE_TIME+" = \""+jsonObject.getString("staffid_activitycode_time")+"\"";
                    Cursor cursor = database.rawQuery(check,null);
                    cursor.moveToFirst();

                    if(!cursor.isAfterLast()){
                        Check = cursor.getInt(0);

                    }

                    if (Check > 0){
/*

                        String insert  = " insert into "+NOTES_TABLE_WORKING_STATS+"("+Note.Notes.STAFFID_ACTIVITYCODE_TIME+","+Note.Notes.ACTIVITY_ID+","+Note.Notes.ACTIVITY_NAME+","+Note.Notes.STAFF_ID+","+Note.Notes.PROCESS_COMPLETED+
                        ","+Note.Notes.TARGET+","+Note.Notes.TIME_COMPLETED+","+Note.Notes.TIME_CREATED+","+Note.Notes.TIME_UPDATED+") values(\""+jsonObject.getString("")+"\";
*/




                    }else if(Check == 0){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Note.Notes.STAFFID_ACTIVITYCODE_TIME,jsonObject.getString("staffid_activitycode_time"));
                        contentValues.put(Note.Notes.ACTIVITY_ID,jsonObject.getString("activity_id"));
                        contentValues.put(Note.Notes.ACTIVITY_NAME, jsonObject.getString("activity_name"));
                        contentValues.put(Note.Notes.STAFF_ID,jsonObject.getString("staff_id"));
                        contentValues.put(Note.Notes.PROCESS_COMPLETED, jsonObject.getString("process_completed"));
                        contentValues.put(Note.Notes.TIME_COMPLETED,jsonObject.getString("time_completed"));
                        contentValues.put(Note.Notes.TIME_CREATED,jsonObject.getString("time_created"));
                        contentValues.put(Note.Notes.TIME_UPDATED, jsonObject.getString("time_updated"));

                        database.insert(NOTES_TABLE_SUCCESSFUL_STATS,null,contentValues);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            return "";
        }

        //this method saves the payment structure into the SQLite database based on the logged on user
        public String insertPaymentStructure(JSONArray jsonArray){

            SQLiteDatabase database = getWritableDatabase();

            JSONObject jsonObject = null;

            for(int i = 0; i<jsonArray.length(); i++){

                try {
                    jsonObject = jsonArray.getJSONObject(i);

                    int Check  = 0;

                    String check = "select count("+Note.Notes.STAFFID_ACTIVITYID+") from "+NOTES_TABLE_PAYMENT_STRUCTURE+" where "+Note.Notes.STAFFID_ACTIVITYID+" = \""+jsonObject.getString("staffid_activityid")+"\"";


                    Cursor cursor = database.rawQuery(check,null);
                    cursor.moveToFirst();

                    if(!cursor.isAfterLast()) {
                        Check = cursor.getInt(0);
                    }

                    if(Check == 0) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Note.Notes.STAFFID_ACTIVITYID, jsonObject.getString("staffid_activityid"));
                        contentValues.put(Note.Notes.STAFF_ID, jsonObject.getString("staff_id"));
                        contentValues.put(Note.Notes.ACTIVITY_ID, jsonObject.getString("activity_id"));
                        contentValues.put(Note.Notes.BASE_PAY, jsonObject.getString("base_pay"));
                        contentValues.put(Note.Notes.SUCCESSFUL_PAY, jsonObject.getString("successful_pay"));
                        contentValues.put(Note.Notes.MISCELLANEOUS_BAL, jsonObject.getString("miscellaneous_bal"));
                        contentValues.put(Note.Notes.OPENING_BAL, jsonObject.getString("opening_balance"));


                        database.insert(NOTES_TABLE_PAYMENT_STRUCTURE, null, contentValues);
                    }else{

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Note.Notes.STAFF_ID, jsonObject.getString("staff_id"));
                        contentValues.put(Note.Notes.ACTIVITY_ID, jsonObject.getString("activity_id"));
                        contentValues.put(Note.Notes.BASE_PAY, jsonObject.getString("base_pay"));
                        contentValues.put(Note.Notes.SUCCESSFUL_PAY, jsonObject.getString("successful_pay"));
                        contentValues.put(Note.Notes.MISCELLANEOUS_BAL, jsonObject.getString("miscellaneous_bal"));
                        contentValues.put(Note.Notes.OPENING_BAL, jsonObject.getString("opening_balance"));




                        String where = Note.Notes.STAFFID_ACTIVITYID+"=?";
                        String[] whereArgs = new String[] {String.valueOf(jsonObject.getString("staffid_activityid"))};

                        database.update(NOTES_TABLE_PAYMENT_STRUCTURE, contentValues, where, whereArgs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }




            return "";

        }

        // This method retrieves the payment structure from the sqlite database
        public Map<String,String> getPaymentStructure(String staff_id,String activity_id){

            SQLiteDatabase database = getWritableDatabase();
            Map<String, String> map = null;
            ArrayList<Map<String, String>>wordlist;
            wordlist =new ArrayList<Map<String, String>>();

            Cursor cursor = database.rawQuery("select "+Note.Notes.BASE_PAY+","+Note.Notes.SUCCESSFUL_PAY+" from "+NOTES_TABLE_PAYMENT_STRUCTURE+" where "+Note.Notes.STAFF_ID+"  = \""+staff_id+"\" and "+Note.Notes.ACTIVITY_ID+" = '"+activity_id+"' ",null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {

                map = new HashMap<>();
                map.put("base_pay", cursor.getString(0));
                map.put("successful_pay", cursor.getString(1));




                cursor.moveToNext();
            }
            return map;



        }

        // This method retrieves the payment structure from the sqlite database for the payment dashboard
        public ArrayList<Map<String,String>> getPaymentStructureFullDashboard(String staff_id){

            SQLiteDatabase database = getWritableDatabase();
            Map<String, String> map = null;
            ArrayList<Map<String, String>>wordlist;
            wordlist =new ArrayList<Map<String, String>>();

            Cursor cursor = database.rawQuery("select "+Note.Notes.BASE_PAY+","+Note.Notes.SUCCESSFUL_PAY+" from "+NOTES_TABLE_PAYMENT_STRUCTURE+" where "+Note.Notes.STAFF_ID+"  = \""+staff_id+"\" ",null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()) {

                // this is to move the cursor to the last
                map = new HashMap<>();
                map.put("base_pay", cursor.getString(0));
                map.put("successful_pay", cursor.getString(1));


                wordlist.add(map);

                cursor.moveToNext(); // i'm editing this app
            }
            return wordlist;



        }

        // This method retrieves the first date an action was performed  in the week and payment period boundaries
        public String getActivityStartDate(String module,String activity_id, String weekStart, String weekEnd, String periodStart, String periodEnd){
            SQLiteDatabase database = getWritableDatabase();

            String startDate = "";
            Cursor cursor = null;

            if(module.trim().equalsIgnoreCase("week")){

                cursor = database.rawQuery("select min("+Note.Notes.TIME_COMPLETED+") from "+NOTES_TABLE_WORKING_STATS+" where  "+Note.Notes.ACTIVITY_ID+" = '"+activity_id+"' and "+Note.Notes.TIME_COMPLETED+" between '"+weekStart+"' and '"+weekEnd+"' ",null);
                cursor.moveToFirst();

                if(!cursor.isAfterLast()){
                    startDate=cursor.getString(0);
                }
                return startDate;

            }else if(module.trim().equalsIgnoreCase("period")){

                cursor = database.rawQuery("select min("+Note.Notes.TIME_COMPLETED+") from "+NOTES_TABLE_WORKING_STATS+" where   "+Note.Notes.ACTIVITY_ID+" = '"+activity_id+"' and "+Note.Notes.TIME_COMPLETED+" between '"+periodStart+"' and '"+periodEnd+"' ",null);
                cursor.moveToFirst();

                if(!cursor.isAfterLast()){
                    startDate=cursor.getString(0);
                }
                return startDate;

            }else if (module.trim().equalsIgnoreCase("year")){

                cursor = database.rawQuery("select min("+Note.Notes.TIME_COMPLETED+") from "+NOTES_TABLE_WORKING_STATS+" where  "+Note.Notes.ACTIVITY_ID+" = '"+activity_id+"' and "+Note.Notes.TIME_COMPLETED+" >= '2019-01-01' ",null);
                cursor.moveToFirst();

                if(!cursor.isAfterLast()){
                    startDate=cursor.getString(0);
                }

                return startDate;

            }




            return startDate;
        }

        //This method saves the payment history data in the SQLite databases
        public String paymentHistoryController(JSONArray jsonArray){
            SQLiteDatabase database = getWritableDatabase();
            JSONObject jsonObject = null;
            for(int i = 0; i< jsonArray.length(); i++){
                try {
                    int Check  = 0;
                    jsonObject = jsonArray.getJSONObject(i);
                    String check = "select count("+Note.Notes.PAYMENT_ID+") from "+NOTES_TABLE_PAYMENT_HISTORY+" where "+Note.Notes.PAYMENT_ID+" = \""+jsonObject.getString("payment_id")+"\"";
                    Cursor cursor = database.rawQuery(check,null);
                    cursor.moveToFirst();

                    if(!cursor.isAfterLast()){
                        Check = cursor.getInt(0);

                    }

                    if (Check > 0){


                    }else if(Check == 0){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Note.Notes.PAYMENT_ID,jsonObject.getString("payment_id"));
                        contentValues.put(Note.Notes.STAFF_ID,jsonObject.getString("staff_id"));
                        contentValues.put(Note.Notes.ACTIVITY_ID, jsonObject.getString("activity_id"));
                        contentValues.put(Note.Notes.ACTIVITY_NAME,jsonObject.getString("activity_name"));
                        contentValues.put(Note.Notes.MONEY_EARNED, jsonObject.getString("money_earned"));
                        contentValues.put(Note.Notes.MONEY_PAID,jsonObject.getString("money_paid"));
                        contentValues.put(Note.Notes.PAYMENT_PERIOD,jsonObject.getString("payment_period"));
                        contentValues.put(Note.Notes.PAYMENT_DATE, jsonObject.getString("payment_date"));
                        contentValues.put(Note.Notes.BALANCE_BROUGHT_FORWARD, jsonObject.getString("balance"));
                        contentValues.put(Note.Notes.MISCELLANEOUS_BAL, jsonObject.getString("misc_bal"));

                        database.insert(NOTES_TABLE_PAYMENT_HISTORY,null,contentValues);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("LLLLL",e+"");
                }

            }


            return "";
        }

        //This method retrieves the payment history
        public ArrayList<Map<String,String>> getPaymentHistory(String staff_id){
            ArrayList<Map<String,String>> paymentHistory  = new ArrayList<>();
            Map<String,String> map = null;
            SQLiteDatabase database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select sum("+Note.Notes.BALANCE_BROUGHT_FORWARD+") from "+NOTES_TABLE_PAYMENT_HISTORY+" where "+Note.Notes.STAFF_ID+" = \""+staff_id+"\" group by "+Note.Notes.PAYMENT_DATE+"  order by "+Note.Notes.PAYMENT_DATE+" desc limit 1 ",null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                map = new HashMap<>();
                map.put("date", "Prv Bal");
                map.put("frequency", cursor.getString(0));
                paymentHistory.add(map);
                cursor.moveToNext();

            }
            Log.d("LLLLL",paymentHistory+"");


            Cursor cursor1 = database.rawQuery("select sum("+Note.Notes.MISCELLANEOUS_BAL+") from "+NOTES_TABLE_PAYMENT_HISTORY+" where "+Note.Notes.STAFF_ID+" = \""+staff_id+"\" group by "+Note.Notes.PAYMENT_DATE+"  order by "+Note.Notes.PAYMENT_DATE+" desc limit 1 ",null);
            cursor1.moveToFirst();
            while(!cursor1.isAfterLast()){
                map = new HashMap<>();
                map.put("date", "Misc Bal");
                map.put("frequency", cursor1.getString(0));
                paymentHistory.add(map);
                cursor1.moveToNext();
            }
            Log.d("LLLLL",paymentHistory+"");
            Cursor cursor2 = database.rawQuery("select "+Note.Notes.ACTIVITY_NAME+","+Note.Notes.MONEY_EARNED+",max("+Note.Notes.PAYMENT_DATE+") from "+NOTES_TABLE_PAYMENT_HISTORY+" where "+Note.Notes.STAFF_ID+" = \""+staff_id+"\" GROUP BY "+Note.Notes.ACTIVITY_NAME+" order by "+Note.Notes.MONEY_EARNED+" asc ",null);
            cursor2.moveToFirst();
            while(!cursor2.isAfterLast()){
                map = new HashMap<>();
                map.put("date", cursor2.getString(0));
                map.put("frequency", cursor2.getString(1));
                paymentHistory.add(map);
                cursor2.moveToNext();
            }
            Log.d("LLLLL",paymentHistory+"");
            Cursor cursor3 = database.rawQuery("select sum("+Note.Notes.MONEY_PAID+") from "+NOTES_TABLE_PAYMENT_HISTORY+" where "+Note.Notes.STAFF_ID+" = \""+staff_id+"\" group by "+Note.Notes.PAYMENT_DATE+"  order by "+Note.Notes.PAYMENT_DATE+" desc limit 1 ",null);
            cursor3.moveToFirst();
            while(!cursor3.isAfterLast()){
                map = new HashMap<>();
                map.put("date", "Amt Paid");
                map.put("frequency", cursor3.getString(0));
                paymentHistory.add(map);
                cursor3.moveToNext();
            }

            Cursor cursor4 = database.rawQuery("select ( sum("+Note.Notes.MONEY_EARNED+") - sum("+Note.Notes.MONEY_PAID+")) from "+NOTES_TABLE_PAYMENT_HISTORY+" where "+Note.Notes.STAFF_ID+" = \""+staff_id+"\" group by "+Note.Notes.PAYMENT_DATE+"  order by "+Note.Notes.PAYMENT_DATE+" desc limit 1 ",null);
            cursor4.moveToFirst();
            while(!cursor4.isAfterLast()){
                map = new HashMap<>();
                map.put("date", "Cl Bal");
                map.put("frequency", cursor4.getString(0));
                paymentHistory.add(map);
                cursor4.moveToNext();
            }
            Log.d("LLLLL",paymentHistory+"");
            cursor.close();
            cursor1.close();
            cursor2.close();
            cursor3.close();
            cursor4.close();
            return paymentHistory;
        }

        //This method returns the date of the last payment period
        public String getLastPeriodDate(){
            SQLiteDatabase database = getWritableDatabase();
            String x = "";
            Cursor cursor = database.rawQuery("select "+Note.Notes.PAYMENT_PERIOD+" from "+NOTES_TABLE_PAYMENT_HISTORY+" order by "+Note.Notes.PAYMENT_ID+" desc limit 1 ",null);
            cursor.moveToFirst();

            if(!cursor.isAfterLast()){
                x = cursor.getString(0);
            }
            return  x;
        }

    }

    private DatabaseHelper dbHelper;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                break;
            case NOTES_ID:
                where = where + "_id = "+ uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int count = db.delete(NOTES_TABLE_NAME, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                return Note.Notes.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }
// The insert method is called when other apps push data into the incentive app
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != NOTES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(NOTES_TABLE_NAME, Note.Notes.ACTIVITY_NAME, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Note.Notes.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }
// This query method is used to pull records from the incentive database based on some criteria

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(NOTES_TABLE_NAME);
        qb.setProjectionMap(notesProjectionMap);

        switch (sUriMatcher.match(uri)) {
            case NOTES:
                break;
            case NOTES_ID:
                selection = selection + "_id = "+ uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                count = db.update(NOTES_TABLE_NAME, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE_NAME, NOTES);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE_NAME + "/#", NOTES_ID);

        notesProjectionMap = new HashMap<String, String>();
        notesProjectionMap.put(Note.Notes.OPERATION_ID, Note.Notes.OPERATION_ID);
        notesProjectionMap.put(Note.Notes.ACTIVITY_NAME, Note.Notes.ACTIVITY_NAME);
        notesProjectionMap.put(Note.Notes.STAFF_ID, Note.Notes.STAFF_ID);
        notesProjectionMap.put(Note.Notes.COMPLETION_DATE, Note.Notes.COMPLETION_DATE);
        notesProjectionMap.put(Note.Notes.UPLOAD_STATUS, Note.Notes.UPLOAD_STATUS);

    }
}