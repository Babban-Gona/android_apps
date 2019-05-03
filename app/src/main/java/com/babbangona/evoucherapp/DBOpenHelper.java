package com.babbangona.evoucherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public  class DBOpenHelper extends SQLiteOpenHelper {

        DBOpenHelper(Context context) {
            super(context, DBContractClass.DATABASE_NAME, null, DBContractClass.DATABASE_VERSION);
        }

        //OnCreate method is called to create all the important database tables for our android application
        @Override
        public void onCreate(SQLiteDatabase db) {

            //Table creation for fieldT
            db.execSQL("CREATE TABLE " + DBContractClass.TABLE_FIELD_T + " (" + DBContractClass.COL_FIELD_ID
                    + " TEXT PRIMARY KEY,"  +DBContractClass.COL_IKNUMBER+" TEXT,"  +DBContractClass.COL_TGHECTARES+" TEXT,"+DBContractClass.COL_FIELD_SIZE +" TEXT,"+DBContractClass.COL_FIELD_PERCENTAGE_OWNERSHIP +" TEXT, "+ DBContractClass.COL_LOAN_FIELDSIZE +" TEXT,"+DBContractClass.COL_SEED_TYPE+" TEXT,"+DBContractClass.COL_STAFF_ID+" TEXT );");
            //Table creation for  product_quantity_t
            db.execSQL("CREATE TABLE "+DBContractClass.TABLE_PACKAGE_TABLE+" ("+DBContractClass.COL_LOANFIELDSIZE_SEEDTYPE_CROPTYPE +" TEXT PRIMARY KEY,"+DBContractClass.COL_ID1_SEEDTYPE+" TEXT,"+DBContractClass.COL_ID1_FIELDSIZE+" TEXT,"+DBContractClass.COL_ID1_SEED+" TEXT,"+DBContractClass.COL_ID1_ATRAZIN+" TEXT,"+DBContractClass.COL_ID1_UREA+" TEXT,"+
                    DBContractClass.COL_ID1_DAP+" TEXT,"+DBContractClass.COL_ID2_UREA+" TEXT,"+DBContractClass.COL_ID2_ALFASAFE+" TEXT,"+DBContractClass.COL_STAFF_ID+" TEXT ); ");

            //Table controlling last synced
            db.execSQL("CREATE TABLE "+DBContractClass.TABLE_LAST_SYNCED+" ("+DBContractClass.COL_STAFF_ID+" TEXT PRIMARY KEY, "+DBContractClass.COL_LAST_SYNC_UP+" TEXT,"+DBContractClass.COL_LAST_SYNC_DOWN+" TEXT);");

            //Table collecting complete transation data
            db.execSQL("CREATE TABLE "+DBContractClass.TABLE_EVOUCHER+"("+DBContractClass.COL_TXN_ID+" TEXT PRIMARY KEY,"+DBContractClass.COL_IKNUMBER+" TEXT,"+DBContractClass.COL_MEMBER_ID+" TEXT,"+DBContractClass.COL_PACKAGE_DISTRIBUTED+" TEXT,"+DBContractClass.COL_LOAN_FIELDSIZE+" TEXT,"+DBContractClass.COL_SEED_TYPE+" " +
                    " TEXT,"+DBContractClass.COL_STAFF_ID+" TEXT,"+DBContractClass.COL_STAFF_ROLE+" TEXT,"+DBContractClass.COL_TOKEN_SEASON+" TEXT,"+DBContractClass.COL_TOKEN+" TEXT UNIQUE,"+DBContractClass.COL_RECEIPT_ID+" TEXT,"+DBContractClass.COL_COLLECTION_TIME+" TEXT,"+DBContractClass.COL_ACCO_ID+" TEXT,"+DBContractClass.COL_SYNC_FLAG+" TEXT,"+DBContractClass.COL_APP_VERSION+" TEXT);");

            //Table containing LMD data
             db.execSQL("CREATE TABLE "+DBContractClass.TABLE_LMDDATA+"("+DBContractClass.COL_LMD_ID+" TEXT PRIMARY KEY ,"+DBContractClass.COL_LMD_NAME+" TEXT,"+DBContractClass.COL_LMD_VILLAGE+" TEXT,"+DBContractClass.COL_LMD_PHONENUMBER+" TEXT);");


             //Table containing incomplete transactions
            db.execSQL("CREATE TABLE "+DBContractClass.TABLE_INCOMPLETE_TRANSACTIONS+"("+DBContractClass.COL_COMPLAINT_ID+" TEXT PRIMARY KEY, "+DBContractClass.COL_IKNUMBER+" TEXT,"+DBContractClass.COL_TOKEN_ISSUED+" TEXT UNIQUE," +
                    ""+DBContractClass.COL_COMPLAINT_TYPE +" TEXT,"+DBContractClass.COL_COMPLAINT_DESC+" TEXT,"+DBContractClass.COL_DATE_LOGGED+" TEXT,"+DBContractClass.COL_LMD_ID+" TEXT,"+DBContractClass.COL_APP_VERSION+"," +
                    ""+DBContractClass.COL_SYNC_FLAG+" TEXT );");

            //Table containing member details
            db.execSQL("CREATE TABLE "+DBContractClass.TABLE_MEMBER_DETAILS+"("+DBContractClass.COL_MEMBER_ID+" TEXT PRIMARY KEY, "+DBContractClass.COL_IKNUMBER+" TEXT,"+DBContractClass.COL_MEMBER_NAME +" TEXT,"+DBContractClass.COL_MEMBER_ROLE+" TEXT,"+DBContractClass.COL_MEMBER_TEMPLATE+" TEXT);");

            //Table for used tokens
            db.execSQL("CREATE TABLE "+DBContractClass.TABLE_USED_TOKENS+"("+DBContractClass.COL_USED_TOKENS+" TEXT PRIMARY KEY, "+DBContractClass.COL_MEMBER_ID+" TEXT,"+DBContractClass.COL_IKNUMBER+" TEXT);");

            //Table for ACCO binding
            db.execSQL("CREATE TABLE "+DBContractClass.TABLE_ACCO_BINDING+"("+DBContractClass.COL_ACCO_ID+" TEXT PRIMARY KEY, "+DBContractClass.COL_ACCO_NAME+" TEXT, "+DBContractClass.COL_ACCO_TEMPLATE+" TEXT);");

        }

        // OnUpgrade method is called to handle database changes
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          //
        }

        public void fieldTInsertController(JSONArray jsonArray){
            SQLiteDatabase database = getReadableDatabase();
            JSONObject jsonObject = null;
            int count = 0;
            for(int i = 0; i <jsonArray.length(); i++){
                try {
                    jsonObject = jsonArray.getJSONObject(i);


                    Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_FIELD_T+" where "+DBContractClass.COL_FIELD_ID+" = \""+jsonObject.getString("unique_field_id")+"\" ", null);
                    cursor.moveToFirst();
                    if(!cursor.isAfterLast()) count = cursor.getInt(0);

                    if (count > 0){

                        ContentValues contentValues = new ContentValues();

                        contentValues.put(DBContractClass.COL_IKNUMBER, jsonObject.getString("ik_number"));
                        contentValues.put(DBContractClass.COL_TGHECTARES, jsonObject.getString("size"));
                        contentValues.put(DBContractClass.COL_FIELD_SIZE, jsonObject.getString("size"));
                        contentValues.put(DBContractClass.COL_FIELD_PERCENTAGE_OWNERSHIP, jsonObject.getString("size"));
                        contentValues.put(DBContractClass.COL_LOAN_FIELDSIZE, jsonObject.getString("size"));
                        contentValues.put(DBContractClass.COL_SEED_TYPE, jsonObject.getString("crop_type"));

                        String where = DBContractClass.COL_FIELD_ID+"= \""+jsonObject.getString("unique_field_id")+"\"";


                        long x = database.update(DBContractClass.TABLE_FIELD_T, contentValues, where, null);


                        Log.d("HHEELLOONOW", x+" ");

                    }else{
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBContractClass.COL_FIELD_ID, jsonObject.getString("unique_field_id"));
                        contentValues.put(DBContractClass.COL_FIELD_SIZE, jsonObject.getString("size"));
                        contentValues.put(DBContractClass.COL_IKNUMBER, jsonObject.getString("ik_number"));
                        contentValues.put(DBContractClass.COL_TGHECTARES, jsonObject.getString("size"));
                        contentValues.put(DBContractClass.COL_FIELD_PERCENTAGE_OWNERSHIP, jsonObject.getString("size"));
                        contentValues.put(DBContractClass.COL_LOAN_FIELDSIZE, jsonObject.getString("size"));
                        contentValues.put(DBContractClass.COL_SEED_TYPE, jsonObject.getString("crop_type"));


                        long x = database.insert(DBContractClass.TABLE_FIELD_T, null, contentValues);
                        Log.d("HHEELLOONOW", x+" ");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

        public void MemberDetailsController(JSONArray jsonArray) {

            SQLiteDatabase database = getReadableDatabase();
            JSONObject jsonObject = null;
            int count = 0;
            for(int i = 0; i <jsonArray.length(); i++){
                try {
                    jsonObject = jsonArray.getJSONObject(i);

                    Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_MEMBER_DETAILS+" where "+DBContractClass.COL_MEMBER_ID+" = \""+jsonObject.getString("member_id")+"\" ", null);
                    cursor.moveToFirst();
                    if(!cursor.isAfterLast()) count = cursor.getInt(0);

                    if (count > 0){

                        ContentValues contentValues = new ContentValues();

                        contentValues.put(DBContractClass.COL_IKNUMBER, jsonObject.getString("ik_number"));
                        contentValues.put(DBContractClass.COL_MEMBER_ROLE, jsonObject.getString("member_role"));
                        contentValues.put(DBContractClass.COL_MEMBER_TEMPLATE, jsonObject.getString("member_template"));
                        contentValues.put(DBContractClass.COL_MEMBER_NAME, jsonObject.getString("member_name"));


                        String where = DBContractClass.COL_MEMBER_ID+"= \""+jsonObject.getString("member_id")+"\"";


                        long x = database.update(DBContractClass.TABLE_MEMBER_DETAILS, contentValues, where, null);


                        Log.d("HHEELLOONOW", x+" ");

                    }else{
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBContractClass.COL_MEMBER_ID, jsonObject.getString("member_id"));
                        contentValues.put(DBContractClass.COL_IKNUMBER, jsonObject.getString("ik_number"));
                        contentValues.put(DBContractClass.COL_MEMBER_ROLE, jsonObject.getString("member_role"));
                        contentValues.put(DBContractClass.COL_MEMBER_TEMPLATE, jsonObject.getString("member_template"));
                        contentValues.put(DBContractClass.COL_MEMBER_NAME, jsonObject.getString("member_name"));



                        long x = database.insert(DBContractClass.TABLE_MEMBER_DETAILS, null, contentValues);
                        Log.d("HHEELLOONOW", x+" ");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        public void LMDDetailsController(JSONArray jsonArray) {

        SQLiteDatabase database = getReadableDatabase();
        JSONObject jsonObject = null;
        int count = 0;
        for(int i = 0; i <jsonArray.length(); i++){
            try {
                jsonObject = jsonArray.getJSONObject(i);

                Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_LMDDATA+" where "+DBContractClass.COL_LMD_ID+" = \""+jsonObject.getString("lmd_id")+"\" ", null);
                cursor.moveToFirst();
                if(!cursor.isAfterLast()) count = cursor.getInt(0);

                if (count > 0){

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBContractClass.COL_LMD_NAME, jsonObject.getString("lmd_name"));
                    contentValues.put(DBContractClass.COL_LMD_VILLAGE, jsonObject.getString("lmd_village"));
                    contentValues.put(DBContractClass.COL_LMD_PHONENUMBER, jsonObject.getString("lmd_phonenumber"));



                    String where = DBContractClass.COL_LMD_ID+"= \""+jsonObject.getString("lmd_id")+"\"";


                    long x = database.update(DBContractClass.TABLE_LMDDATA, contentValues, where, null);


                    Log.d("HHEELLOONOW", x+" ");

                }else{
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBContractClass.COL_LMD_ID, jsonObject.getString("lmd_id"));
                    contentValues.put(DBContractClass.COL_LMD_NAME, jsonObject.getString("lmd_name"));
                    contentValues.put(DBContractClass.COL_LMD_VILLAGE, jsonObject.getString("lmd_village"));
                    contentValues.put(DBContractClass.COL_LMD_PHONENUMBER, jsonObject.getString("lmd_phonenumber"));


                    long x = database.insert(DBContractClass.TABLE_LMDDATA, null, contentValues);
                    Log.d("HHEELLOONOW", x+" ");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

        public void ProductTInsertController(JSONArray jsonArray){
        SQLiteDatabase database = getReadableDatabase();
        JSONObject jsonObject = null;
        int count = 0;
        for(int i = 0; i <jsonArray.length(); i++){
            try {
                jsonObject = jsonArray.getJSONObject(i);

                Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_PACKAGE_TABLE+" where "+DBContractClass.COL_LOANFIELDSIZE_SEEDTYPE_CROPTYPE +" = \""+jsonObject.getString("loanfieldsize_seedtype_croptype")+"\" ", null);
                cursor.moveToFirst();
                if(!cursor.isAfterLast()) count = cursor.getInt(0);

                if (count > 0){

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBContractClass.COL_LOANFIELDSIZE_SEEDTYPE_CROPTYPE, jsonObject.getString("loanfieldsize_seedtype_croptype"));
                    //contentValues.put(DBContractClass.COL_ID1_SEEDTYPE, jsonObject.getString("seed_type"));
                    //contentValues.put(DBContractClass.COL_ID1_FIELDSIZE, jsonObject.getString("id1_field_size"));
                    //contentValues.put(DBContractClass.COL_ID1_PARAQUART, jsonObject.getString("id1_paraquart"));
                    contentValues.put(DBContractClass.COL_ID1_ATRAZIN, jsonObject.getString("id1_atrazine"));
                    contentValues.put(DBContractClass.COL_ID1_UREA, jsonObject.getString("id1_urea"));
                    contentValues.put(DBContractClass.COL_ID1_DAP, jsonObject.getString("id1_dap"));
                    contentValues.put(DBContractClass.COL_ID2_UREA, jsonObject.getString("id2_urea"));
                    contentValues.put(DBContractClass.COL_ID2_ALFASAFE, jsonObject.getString("id2_alfasafe"));
                    contentValues.put(DBContractClass.COL_ID1_SEED, jsonObject.getString("id1_seed"));
                   // contentValues.put(DBContractClass.COL_ID2_MOP, jsonObject.getString("id2_mop"));

                    String where = DBContractClass.COL_LOANFIELDSIZE_SEEDTYPE_CROPTYPE +"= \""+jsonObject.getString("loanfieldsize_seedtype_croptype")+"\"";


                    long x = database.update(DBContractClass.TABLE_PACKAGE_TABLE, contentValues, where, null);
                    Log.d("HHEELLOONOW", x+" ");

                }else{

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBContractClass.COL_LOANFIELDSIZE_SEEDTYPE_CROPTYPE, jsonObject.getString("loanfieldsize_seedtype_croptype"));
                    //contentValues.put(DBContractClass.COL_ID1_SEEDTYPE, jsonObject.getString("seed_type"));
                    //contentValues.put(DBContractClass.COL_ID1_FIELDSIZE, jsonObject.getString("id1_field_size"));
                    //contentValues.put(DBContractClass.COL_ID1_PARAQUART, jsonObject.getString("id1_paraquart"));
                    contentValues.put(DBContractClass.COL_ID1_ATRAZIN, jsonObject.getString("id1_atrazine"));
                    contentValues.put(DBContractClass.COL_ID1_UREA, jsonObject.getString("id1_urea"));
                    contentValues.put(DBContractClass.COL_ID1_DAP, jsonObject.getString("id1_dap"));
                    contentValues.put(DBContractClass.COL_ID2_UREA, jsonObject.getString("id2_urea"));
                    contentValues.put(DBContractClass.COL_ID2_ALFASAFE, jsonObject.getString("id2_alfasafe"));
                    //contentValues.put(DBContractClass.COL_ID2_MOP, jsonObject.getString("id2_mop"));
                    contentValues.put(DBContractClass.COL_ID1_SEED, jsonObject.getString("id1_seed"));


                    long x = database.insert(DBContractClass.TABLE_PACKAGE_TABLE, null, contentValues);
                    Log.d("HHEELLOONOW", x+" ");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

        public void ACCOInsertController(JSONArray jsonArray){

            SQLiteDatabase database = getReadableDatabase();
            JSONObject jsonObject = null;
            int count = 0;
            for(int i = 0; i <jsonArray.length(); i++){
                try {
                    jsonObject = jsonArray.getJSONObject(i);

                    Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_ACCO_BINDING+" where "+DBContractClass.COL_ACCO_ID+" = \""+jsonObject.getString("acco_staffid")+"\" ", null);
                    cursor.moveToFirst();
                    if(!cursor.isAfterLast()) count = cursor.getInt(0);

                    if (count > 0){

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBContractClass.COL_ACCO_NAME, jsonObject.getString("acco_name"));
                        contentValues.put(DBContractClass.COL_ACCO_TEMPLATE, jsonObject.getString("acco_template"));

                        String where = DBContractClass.COL_ACCO_ID +"= \""+jsonObject.getString("acco_staffid")+"\"";


                        long x = database.update(DBContractClass.TABLE_ACCO_BINDING, contentValues, where, null);


                    }else{

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBContractClass.COL_ACCO_ID, jsonObject.getString("acco_staffid"));
                        contentValues.put(DBContractClass.COL_ACCO_NAME, jsonObject.getString("acco_name"));
                        contentValues.put(DBContractClass.COL_ACCO_TEMPLATE, jsonObject.getString("acco_template"));

                        long x = database.insert(DBContractClass.TABLE_ACCO_BINDING, null, contentValues);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        public ArrayList<Map<String,String>> getIKProperties(String IKNumber){
            ArrayList<Map<String,String>> ikParams = new ArrayList<>();
            Map<String,String> params  = null;
            SQLiteDatabase database = getReadableDatabase();

            Cursor cursor = database.rawQuery("select * from "+DBContractClass.TABLE_FIELD_T+" where "+DBContractClass.COL_IKNUMBER+" = \""+IKNumber+"\" ",null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                params = new HashMap<>();
                params.put("field_size", cursor.getString(cursor.getColumnIndex(DBContractClass.COL_FIELD_SIZE)));
                params.put("tg_hectares", cursor.getString(cursor.getColumnIndex(DBContractClass.COL_TGHECTARES)));
                params.put("percentage_ownership", cursor.getString(cursor.getColumnIndex(DBContractClass.COL_FIELD_PERCENTAGE_OWNERSHIP)));
                params.put("seed_type", cursor.getString(cursor.getColumnIndex(DBContractClass.COL_SEED_TYPE)));

                ikParams.add(params);
                cursor.moveToNext();
            }
            cursor.close();

         return ikParams;
        }

        public long insertTransactionData(Context context,String txnid,String ik_number,String packageDistributed,String loan_fieldsize, String seedType,String staffId,String staffRole,String tokenSeason, String Token,String member_id,String acco){

            GPSValues.initialiseLocationListener(context);
            String time = GPSValues.time;



            SQLiteDatabase database =getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContractClass.COL_TXN_ID, txnid);
            contentValues.put(DBContractClass.COL_IKNUMBER, ik_number);
            contentValues.put(DBContractClass.COL_PACKAGE_DISTRIBUTED, packageDistributed);
            contentValues.put(DBContractClass.COL_SEED_TYPE, seedType);
            contentValues.put(DBContractClass.COL_MEMBER_ID, member_id);
            contentValues.put(DBContractClass.COL_ACCO_ID, acco);

            contentValues.put(DBContractClass.COL_LOAN_FIELDSIZE, loan_fieldsize);
            contentValues.put(DBContractClass.COL_STAFF_ID, staffId);
            contentValues.put(DBContractClass.COL_STAFF_ROLE, staffRole);
            contentValues.put(DBContractClass.COL_TOKEN_SEASON, tokenSeason);
            contentValues.put(DBContractClass.COL_TOKEN, Token);
            contentValues.put(DBContractClass.COL_COLLECTION_TIME,time);
            contentValues.put(DBContractClass.COL_SYNC_FLAG, "0");
            contentValues.put(DBContractClass.COL_APP_VERSION, BuildConfig.VERSION_NAME);


            long x = database.insert(DBContractClass.TABLE_EVOUCHER, null, contentValues);
            Log.d("HHEELLOONOW", x+" ");

            return x;
        }

        public void lastSyncUp(JSONArray jsonArray ){
        SQLiteDatabase database = getReadableDatabase();
        JSONObject jsonObject = null;
        int count = 0;
        for(int i = 0; i <jsonArray.length(); i++){
            try {
                jsonObject = jsonArray.getJSONObject(i);

                Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_LAST_SYNCED+" where "+DBContractClass.COL_STAFF_ID +" = \""+jsonObject.getString("staff_id")+"\" ", null);
                cursor.moveToFirst();
                if(!cursor.isAfterLast()) count = cursor.getInt(0);

                if (count > 0){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBContractClass.COL_LAST_SYNC_UP, jsonObject.getString("last_synced"));
                    String where = DBContractClass.COL_STAFF_ID +"= \""+jsonObject.getString("staff_id")+"\"";

                    long x = database.update(DBContractClass.TABLE_LAST_SYNCED, contentValues, where, null);
                    Log.d("HHEELLOONOW", x+" ");

                }else{

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBContractClass.COL_LAST_SYNC_UP, jsonObject.getString("last_synced"));
                    contentValues.put(DBContractClass.COL_STAFF_ID, jsonObject.getString("staff_id"));

                    long x = database.insert(DBContractClass.TABLE_LAST_SYNCED, null, contentValues);
                    Log.d("HHEELLOONOW", x+" ");

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

        public void lastSyncDown(JSONArray jsonArray ){
            SQLiteDatabase database = getReadableDatabase();
            JSONObject jsonObject = null;
            int count = 0;
            for(int i = 0; i <jsonArray.length(); i++){
                try {
                    jsonObject = jsonArray.getJSONObject(i);

                    Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_LAST_SYNCED+" where "+DBContractClass.COL_STAFF_ID +" = \""+jsonObject.getString("lmd_id")+"\" ", null);
                    cursor.moveToFirst();
                    if(!cursor.isAfterLast()) count = cursor.getInt(0);

                    if (count > 0){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBContractClass.COL_LAST_SYNC_DOWN, jsonObject.getString("last_synced"));
                        String where = DBContractClass.COL_STAFF_ID +"= \""+jsonObject.getString("lmd_id")+"\"";

                        long x = database.update(DBContractClass.TABLE_LAST_SYNCED, contentValues, where, null);
                        Log.d("HHEELLOONOW", x+" ");

                    }else{

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBContractClass.COL_LAST_SYNC_DOWN, jsonObject.getString("last_synced"));
                        contentValues.put(DBContractClass.COL_STAFF_ID, jsonObject.getString("lmd_id"));

                        long x = database.insert(DBContractClass.TABLE_LAST_SYNCED, null, contentValues);
                        Log.d("HHEELLOONOW", x+" ");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

        public Map<String,String> getPackage(String loanfieldsize_seedtype_croptype){
            Map<String,String> map = null;
            SQLiteDatabase database  = getReadableDatabase();
            Cursor cursor = database.rawQuery("select * from "+DBContractClass.TABLE_PACKAGE_TABLE+" where "+DBContractClass.COL_LOANFIELDSIZE_SEEDTYPE_CROPTYPE+" = \""+loanfieldsize_seedtype_croptype+"\"  ",null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                map = new HashMap<>();
                map.put("atrazine",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_ID1_ATRAZIN)));
                map.put("urea",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_ID1_UREA)));
                map.put("dap",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_ID1_DAP)));
                map.put("seed",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_ID1_SEED)));
                cursor.moveToNext();
            }


            return map;

        }

        public String leaderInfo(String ik_number){
            String l_name  = "";
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select "+DBContractClass.COL_MEMBER_NAME +" from "+DBContractClass.TABLE_MEMBER_DETAILS+" where "+
            DBContractClass.COL_IKNUMBER+" = '"+ik_number+"' and "+DBContractClass.COL_MEMBER_ROLE+" = 'Leader'",null);
            cursor.moveToFirst();
            if(!cursor.isAfterLast()){
                l_name=cursor.getString(cursor.getColumnIndex(DBContractClass.COL_MEMBER_NAME));
            }

      return l_name;
        }

        public long complain(Context context,String ik_number, String complain_type,String complain_desc){



            String complain_id,Token,lmd_id;
            GPSValues.initialiseLocationListener(context);
            String time = GPSValues.time;

            SessionManagement sessionManagement  = new SessionManagement(context);
            HashMap<String,String> user  = sessionManagement.getUserDetails();
            HashMap<String,String> token = sessionManagement.getTokenDetails();

            Token  = token.get(SessionManagement.TKN_TOKEN_ENTERED);
            lmd_id  = user.get(SessionManagement.KEY_STAFF_ID);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            complain_id = dateFormat.format(date);

            SQLiteDatabase database =getReadableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(DBContractClass.COL_COMPLAINT_ID, complain_id);
            contentValues.put(DBContractClass.COL_IKNUMBER, ik_number);
            contentValues.put(DBContractClass.COL_TOKEN_ISSUED, Token);
            contentValues.put(DBContractClass.COL_COMPLAINT_TYPE, complain_type);
            contentValues.put(DBContractClass.COL_COMPLAINT_DESC, complain_desc);
            contentValues.put(DBContractClass.COL_DATE_LOGGED,time);
            contentValues.put(DBContractClass.COL_LMD_ID,lmd_id);
            contentValues.put(DBContractClass.COL_SYNC_FLAG, "0");
            contentValues.put(DBContractClass.COL_APP_VERSION, BuildConfig.VERSION_NAME);


            long x = database.insert(DBContractClass.TABLE_INCOMPLETE_TRANSACTIONS, null, contentValues);
            Log.d("HHEELLOONOW", x+" ");

            return x;
        }

        public ArrayList<Map<String,String>> getDBRecords(String staff_id){
            ArrayList<Map<String,String>> arrayList = new ArrayList<>();
            Map<String,String> map = null;

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select b."+DBContractClass.COL_MEMBER_NAME+",a."+DBContractClass.COL_TXN_ID+",a."+DBContractClass.COL_IKNUMBER+"," +
                    "a."+DBContractClass.COL_LOAN_FIELDSIZE+",a."+DBContractClass.COL_SYNC_FLAG+ " from "+DBContractClass.TABLE_EVOUCHER+" a join "+DBContractClass.TABLE_MEMBER_DETAILS+" b " +
                    "on a."+DBContractClass.COL_MEMBER_ID+" = b."+DBContractClass.COL_MEMBER_ID+" order by "+DBContractClass.COL_SYNC_FLAG+" desc ",null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                map = new HashMap<>();
                map.put("txn_id",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_TXN_ID)));
                map.put("mem_name",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_MEMBER_NAME)));
                map.put("ik_number",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_IKNUMBER)));
                map.put("loan_fieldsize",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_LOAN_FIELDSIZE)));
                map.put("sync_flag",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_SYNC_FLAG)));

                arrayList.add(map);
                cursor.moveToNext();
            }
            return arrayList;
        }

        public ArrayList<Map<String,String>> uploadCompleteTransactions(String staff_id){
            ArrayList<Map<String,String>> arrayList = new ArrayList<>();
            Map<String,String> map = null;

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from "+DBContractClass.TABLE_EVOUCHER+" where "+DBContractClass.COL_SYNC_FLAG+" = '0' ",null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                map = new HashMap<>();
                map.put("transaction_id",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_TXN_ID)));
                map.put("ik_number",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_IKNUMBER)));
                map.put("package_distributed",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_PACKAGE_DISTRIBUTED)));
                map.put("loan_fieldsize",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_LOAN_FIELDSIZE)));
                map.put("seed_type",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_SEED_TYPE)));
                map.put("staff_id",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_STAFF_ID)));
                map.put("staff_role",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_STAFF_ROLE)));
                map.put("token_season",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_TOKEN_SEASON)));
                map.put("token",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_TOKEN)));
                map.put("collection_time",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_COLLECTION_TIME)));
                map.put("app_version",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_APP_VERSION)));
                map.put("acco_id",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_ACCO_ID)));
                map.put("member_id",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_MEMBER_ID)));
                map.put("receipt_id",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_RECEIPT_ID)));

                arrayList.add(map);
                cursor.moveToNext();
            }
            return arrayList;
        }

        public ArrayList<Map<String,String>> uploadInCompleteTransactions(String staff_id){
            ArrayList<Map<String,String>> arrayList = new ArrayList<>();
            Map<String,String> map = null;

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from "+DBContractClass.TABLE_INCOMPLETE_TRANSACTIONS+" where "+DBContractClass.COL_SYNC_FLAG+" = '0' ",null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                map = new HashMap<>();
                map.put("complaint_id",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_COMPLAINT_ID)));
                map.put("ik_number",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_IKNUMBER)));
                map.put("token_issued",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_TOKEN_ISSUED)));
                map.put("complaint_type",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_COMPLAINT_TYPE)));
                map.put("complaint_description",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_COMPLAINT_DESC)));
                map.put("date_logged",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_DATE_LOGGED)));
                map.put("staff_id",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_LMD_ID)));
                map.put("app_version",cursor.getString(cursor.getColumnIndex(DBContractClass.COL_APP_VERSION)));

                arrayList.add(map);
                cursor.moveToNext();
            }
            return arrayList;
        }

        public void processServerResponse(String module, JSONArray jsonArray){
            SQLiteDatabase database = getReadableDatabase();
            if(module.trim().matches("complete")){
                JSONObject jsonObject = null;
                for(int i=0; i<jsonArray.length();i++){
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBContractClass.COL_SYNC_FLAG, jsonObject.getString("synced"));
                        String where = DBContractClass.COL_TXN_ID+"= \""+jsonObject.getString("transaction_id")+"\"";


                        int x = database.update(DBContractClass.TABLE_EVOUCHER, contentValues, where, null);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(module.trim().matches("incomplete")){

                JSONObject jsonObject = null;
                for(int i=0; i<jsonArray.length();i++){
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBContractClass.COL_SYNC_FLAG, jsonObject.getString("synced"));
                        String where = DBContractClass.COL_COMPLAINT_ID+"= \""+jsonObject.getString("complaint_id")+"\"";


                        int x = database.update(DBContractClass.TABLE_INCOMPLETE_TRANSACTIONS, contentValues, where, null);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void updateRecieptId(String txnid,String receipt_id){
            SQLiteDatabase database = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContractClass.COL_RECEIPT_ID, receipt_id);


            String where = DBContractClass.COL_TXN_ID+"= \""+txnid+"\"";


            long x = database.update(DBContractClass.TABLE_EVOUCHER, contentValues, where, null);





        }

        public List<String> getAcco(){
            List acco  = new ArrayList();

            SQLiteDatabase database = getReadableDatabase();

            Cursor cursor = database.rawQuery("select "+DBContractClass.COL_ACCO_NAME+" ||'_'|| "+DBContractClass.COL_ACCO_ID+" as username from "+DBContractClass.TABLE_ACCO_BINDING+" order by "+DBContractClass.COL_ACCO_NAME+" asc",null);
            cursor.moveToFirst();
            acco.add("select acco name");
            while(!cursor.isAfterLast()){
                acco.add(cursor.getString(cursor.getColumnIndex("username")));
                cursor.moveToNext();

            }

            return  acco;
        }

        public void insertUsedTokens(JSONArray jsonArray ){
        SQLiteDatabase database = getReadableDatabase();
        JSONObject jsonObject = null;
        int count = 0;
        for(int i = 0; i <jsonArray.length(); i++){
            try {
                jsonObject = jsonArray.getJSONObject(i);

                Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_USED_TOKENS+" where "+DBContractClass.COL_USED_TOKENS +" = \""+jsonObject.getString("token")+"\" ", null);
                cursor.moveToFirst();
                if(!cursor.isAfterLast()) count = cursor.getInt(0);

                if (count == 0){

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBContractClass.COL_USED_TOKENS, jsonObject.getString("token"));
                    contentValues.put(DBContractClass.COL_MEMBER_ID, jsonObject.getString("member_id"));
                    contentValues.put(DBContractClass.COL_IKNUMBER, jsonObject.getString("ik_number"));

                    long x = database.insert(DBContractClass.TABLE_USED_TOKENS, null, contentValues);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

        public boolean validateToken(String Token){
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_USED_TOKENS+" where "+DBContractClass.COL_USED_TOKENS+" = '"+Token+"' ",null);
            cursor.moveToFirst();
            int count = 0;
            while(!cursor.isAfterLast()){
                count = cursor.getInt(cursor.getColumnIndex("count"));
                cursor.moveToNext();
            }

            if(count == 0) return true;
            else return false;
        }

        public boolean validateTokenFrmCompleteTransactions(String Token){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_EVOUCHER+" where "+DBContractClass.COL_TOKEN+" = '"+Token+"' ",null);
        cursor.moveToFirst();
        int count = 0;
        while(!cursor.isAfterLast()){
            count = cursor.getInt(cursor.getColumnIndex("count"));
            cursor.moveToNext();
        }

        if(count == 0) return true;
        else return false;
    }

        public String getMemberTemplate(String ik_number, String role){
            String member_id = "";
            String template = "";
            SQLiteDatabase database  = getReadableDatabase();

            Cursor cursor = database.rawQuery("select "+DBContractClass.COL_MEMBER_ID+","+DBContractClass.COL_MEMBER_TEMPLATE+" from "+DBContractClass.TABLE_MEMBER_DETAILS+" where "+DBContractClass.COL_IKNUMBER+" = '"+ik_number+"' and  "+DBContractClass.COL_MEMBER_ROLE+" = '"+role+"'  ",null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                member_id = cursor.getString(cursor.getColumnIndex(DBContractClass.COL_MEMBER_ID));
                template = cursor.getString(cursor.getColumnIndex(DBContractClass.COL_MEMBER_TEMPLATE));
                cursor.moveToNext();
            }
            return member_id+"##--##"+template;
        }

        public String getAccoTemplate(String AccoID){

            String template = "";
            SQLiteDatabase database  = getReadableDatabase();

            Cursor cursor = database.rawQuery("select "+DBContractClass.COL_ACCO_TEMPLATE+" from "+DBContractClass.TABLE_ACCO_BINDING+" where "+DBContractClass.COL_ACCO_ID+" = '"+AccoID+"' ",null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                template = cursor.getString(cursor.getColumnIndex(DBContractClass.COL_ACCO_TEMPLATE));

                cursor.moveToNext();
            }
            return template;
        }

        public String getDBCount(){
            String x= "0";
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_PACKAGE_TABLE,null);
            cursor.moveToFirst();
            if(!cursor.isAfterLast()) x=cursor.getString(cursor.getColumnIndex("count"));

            return x;
        }

        public Map<String,String> getStatistics(String staff_id){
            Map<String,String> map = new HashMap<>();
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery("select count(*) as count from "+DBContractClass.TABLE_EVOUCHER+" where "+DBContractClass.COL_STAFF_ID+" = \""+staff_id+"\"",null);
            cursor.moveToFirst();
            if(!cursor.isAfterLast()) map.put("total_distributed",cursor.getString(cursor.getColumnIndex("count")));


            Cursor cursor2 = database.rawQuery("select  "+DBContractClass.COL_LAST_SYNC_UP+" from "+DBContractClass.TABLE_LAST_SYNCED+" where "+DBContractClass.COL_STAFF_ID+" = \""+staff_id+"\"",null);
            cursor2.moveToFirst();
            if(!cursor2.isAfterLast()) map.put("sync_up",cursor2.getString(cursor2.getColumnIndex(DBContractClass.COL_LAST_SYNC_UP)));



            Cursor cursor3 = database.rawQuery("select "+DBContractClass.COL_LAST_SYNC_DOWN+" from "+DBContractClass.TABLE_LAST_SYNCED+" where "+DBContractClass.COL_STAFF_ID+" = \""+staff_id+"\"",null);
            cursor3.moveToFirst();
            if(!cursor3.isAfterLast()) map.put("sync_down",cursor3.getString(cursor3.getColumnIndex(DBContractClass.COL_LAST_SYNC_DOWN)));

            return map;
        }

        public String getInput(String packageDistributed){
            String urea="",atrazine ="",dap = "";
            SQLiteDatabase database = getReadableDatabase();

            Cursor cursor = database.rawQuery("select "+DBContractClass.COL_ID1_UREA+","+DBContractClass.COL_ID1_ATRAZIN+","+DBContractClass.COL_ID1_DAP+" from "+DBContractClass.TABLE_PACKAGE_TABLE+"  where "+DBContractClass.COL_LOANFIELDSIZE_SEEDTYPE_CROPTYPE+" = '"+packageDistributed+"' ",null);
            cursor.moveToFirst();
            if(!cursor.isAfterLast()){
                urea = cursor.getString(cursor.getColumnIndex(DBContractClass.COL_ID1_UREA));
                dap = cursor.getString(cursor.getColumnIndex(DBContractClass.COL_ID1_DAP));
                atrazine = cursor.getString(cursor.getColumnIndex(DBContractClass.COL_ID1_ATRAZIN));
            }

            return urea+"##"+dap+"##"+atrazine;
        }





}
