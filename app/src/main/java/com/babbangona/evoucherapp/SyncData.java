package com.babbangona.evoucherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SyncData {

    private static final String TAG = "--SyncData--";

    public static final String URL = "http://apps.babbangona.com/evoucher/mobile/";

    public static class DownloadFieldT extends AsyncTask<String, String, String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;



        public DownloadFieldT(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            URL url = null;
            String staff_id = null;

            sessionManagement = new SessionManagement(context);
            HashMap<String, String> user = sessionManagement.getUserDetails();
            staff_id = user.get(SessionManagement.KEY_STAFF_ID);


            try {
                url = new URL(URL + "/download_fieldt.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("lmd_id", staff_id);

                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e1) {
                e1.printStackTrace();
                return "Failed! Kindly check your network connection ";
            }

            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    try {
                        Log.d(TAG,result.toString());
                        JSONArray jsonArray = new JSONArray(result.toString());
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
                        dbOpenHelper.fieldTInsertController(jsonArray);
                        dbOpenHelper.lastSyncDown(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return "done";
                } else {
                    return ("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }


    }

    public static class DownloadProductQuantityT extends AsyncTask<String, String, String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;



        public DownloadProductQuantityT(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            URL url = null;
            String staff_id = null;



            try {
                url = new URL(URL + "/download_product_quantity_t.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("lmd_id", staff_id);

                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e1) {
                e1.printStackTrace();
                return "Failed! Kindly check your network connection ";
            }

            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    try {
                        Log.d(TAG,result.toString());
                        JSONArray jsonArray = new JSONArray(result.toString());
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
                        dbOpenHelper.ProductTInsertController(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return "done";
                } else {
                    return ("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }


    }

    public static class DownloadMemberDetails extends AsyncTask<String, String, String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;



        public DownloadMemberDetails(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            URL url = null;
            String staff_id = null;

            sessionManagement = new SessionManagement(context);
            HashMap<String, String> user = sessionManagement.getUserDetails();
            staff_id = user.get(SessionManagement.KEY_STAFF_ID);
            try {
                url = new URL(URL + "/download_member_details.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("lmd_id", staff_id);

                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e1) {
                e1.printStackTrace();
                return "Failed! Kindly check your network connection ";
            }

            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    try {
                        Log.d(TAG,result.toString());
                        JSONArray jsonArray = new JSONArray(result.toString());
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
                        dbOpenHelper.MemberDetailsController(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return "done";
                } else {
                    return ("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }


    }

    public static class DownloadLmdData extends AsyncTask<String, String, String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;



        public DownloadLmdData(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            URL url = null;
            String staff_id = null;

            sessionManagement = new SessionManagement(context);
            HashMap<String, String> user = sessionManagement.getUserDetails();
            staff_id = user.get(SessionManagement.KEY_STAFF_ID);
            try {
                url = new URL(URL + "/download_lmd_details.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("lmd_id", staff_id);

                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e1) {
                e1.printStackTrace();
                return "Failed! Kindly check your network connection ";
            }

            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    try {
                        Log.d(TAG,result.toString());
                        JSONArray jsonArray = new JSONArray(result.toString());
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
                        dbOpenHelper.LMDDetailsController(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return "done";
                } else {
                    return ("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }


    }

    public static class DownloadAccoData extends AsyncTask<String, String, String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;



        public DownloadAccoData(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            URL url = null;
            String staff_id = null;

            sessionManagement = new SessionManagement(context);
            HashMap<String, String> user = sessionManagement.getUserDetails();
            staff_id = user.get(SessionManagement.KEY_STAFF_ID);
            try {
                url = new URL(URL + "/download_acco_details.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("lmd_id", staff_id);

                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e1) {
                e1.printStackTrace();
                return "Failed! Kindly check your network connection ";
            }

            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    try {
                        Log.d(TAG,result.toString());
                        JSONArray jsonArray = new JSONArray(result.toString());
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
                        dbOpenHelper.ACCOInsertController(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return "done";
                } else {
                    return ("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }


    }

    public static class DownloadUsedTokens extends AsyncTask<String, String, String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;



        public DownloadUsedTokens(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            URL url = null;
            String staff_id = null;

            sessionManagement = new SessionManagement(context);
            HashMap<String, String> user = sessionManagement.getUserDetails();
            staff_id = user.get(SessionManagement.KEY_STAFF_ID);
            try {
                url = new URL(URL + "/download_used_tokens.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder().appendQueryParameter("lmd_id", staff_id);

                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e1) {
                e1.printStackTrace();
                return "Failed! Kindly check your network connection ";
            }

            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    try {
                        Log.d(TAG,result.toString());
                        JSONArray jsonArray = new JSONArray(result.toString());
                        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
                        dbOpenHelper.insertUsedTokens(jsonArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return "done";
                } else {
                    return ("Connection error");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }


    }

    public static class UploadCompleteTansactions extends AsyncTask<Void,Void,String> {

        Context context;
        ArrayList wordlist;
        DBOpenHelper dbOpenHelper;

        SessionManagement sessionManagement;
        String staff_id;

        public UploadCompleteTansactions(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(Void... voids) {

            sessionManagement = new SessionManagement(context);
            HashMap<String, String> user = sessionManagement.getUserDetails();

            staff_id= user.get(SessionManagement.KEY_STAFF_ID);
            dbOpenHelper = new DBOpenHelper(context);
            wordlist = dbOpenHelper.uploadCompleteTransactions(staff_id);
            Log.d(TAG,wordlist+""+wordlist.size());
            if(wordlist.size()<1) return "All your transaction records have been synced";

            Gson gson = new GsonBuilder().create();
            String WordList = gson.toJson(wordlist);

            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(URL+"/upload_complete_transactions.php");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("wordlist", "UTF-8") + "=" + URLEncoder.encode(WordList, "UTF-8");

                bufferedWriter.write(data_string); // writing information to Database
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                httpURLConnection.connect();

            } catch (IOException e) {e.printStackTrace();}

            try{
                int response_code = httpURLConnection.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK){
                    InputStream input = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {result.append(line);}
                    Log.d(TAG,result.toString());
                    try {

                        JSONArray arr1 = new JSONArray(String.valueOf(result));
                        dbOpenHelper = new DBOpenHelper(context);
                        dbOpenHelper.processServerResponse("complete",arr1);
                        dbOpenHelper.lastSyncUp(arr1);

                        return "Transaction records synced successfully";
                    } catch (JSONException e) {

                        Log.d(TAG,e+"");
                        return "Operation failed, kindly check your internet connection";
                    }


                } else{return("Sync failed due to internal error. Most likely a network error");}
            }

            catch (IOException e){
                e.printStackTrace(); return "Sync failed due to internal error. Most likely a network error";
            }
            finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }

    }

    public static class UploadInCompleteTansactions extends AsyncTask<Void,Void,String> {

        Context context;
        ArrayList wordlist;
        DBOpenHelper dbOpenHelper;

        SessionManagement sessionManagement;
        String staff_id;

        public UploadInCompleteTansactions(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(Void... voids) {

            sessionManagement = new SessionManagement(context);
            HashMap<String, String> user = sessionManagement.getUserDetails();

            staff_id= user.get(SessionManagement.KEY_STAFF_ID);
            dbOpenHelper = new DBOpenHelper(context);
            wordlist = dbOpenHelper.uploadInCompleteTransactions(staff_id);
            Log.d(TAG,wordlist+""+wordlist.size());
            if(wordlist.size()<1) return "All your Issues records have been synced";

            Gson gson = new GsonBuilder().create();
            String WordList = gson.toJson(wordlist);

            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(URL+"/upload_incomplete_transactions.php");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data_string = URLEncoder.encode("wordlist", "UTF-8") + "=" + URLEncoder.encode(WordList, "UTF-8");

                bufferedWriter.write(data_string); // writing information to Database
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                httpURLConnection.connect();

            } catch (IOException e) {e.printStackTrace();}

            try{
                int response_code = httpURLConnection.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK){
                    InputStream input = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {result.append(line);}
                    Log.d(TAG,result.toString());
                    try {

                        JSONArray arr1 = new JSONArray(String.valueOf(result));
                        dbOpenHelper = new DBOpenHelper(context);
                        dbOpenHelper.processServerResponse("incomplete",arr1);
                        dbOpenHelper.lastSyncUp(arr1);
                        return "Complaint records synced successfully";
                    } catch (JSONException e) {

                        Log.d(TAG,e+"");
                        return "Operation failed, kindly check your internet connection";
                    }


                } else{return("Sync failed due to internal error. Most likely a network error");}
            }

            catch (IOException e){
                e.printStackTrace(); return "Sync failed due to internal error. Most likely a network error";
            }
            finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }

    }

}
