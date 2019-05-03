package com.babbangona.shpincentiveapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;



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

import java.util.HashMap;


public class SyncData {

    static String URL = "http://apps.babbangona.com/incentive/";

    //static String URL = "http://fpf.babbangona.com/tgl_test/tgl_sync/";
    @SuppressLint("StaticFieldLeak")

    // This async task downloads the data that feeds into incentive_working_stats table
    public static class DownloadApplication extends AsyncTask<String,String,String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;


        public DownloadApplication(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            java.net.URL url = null;
            String staff_id = null;

            try {

                sessionManagement = new SessionManagement(context);
                HashMap<String, String> user = sessionManagement.getUserDetails();
                staff_id = user.get(SessionManagement.KEY_STAFF_ID);


            } catch (Exception e) {
                System.out.println("Exception caught: Session Management didnt work");
            }

            if (staff_id == null) {
                return "No active session";
            }


                try {
                    url = new URL(URL + "/fetch_working_stats.php");

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return "exception 1";
                }

                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(40000);
                    conn.setConnectTimeout(60000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    Uri.Builder builder = new Uri.Builder().appendQueryParameter("staff_id", staff_id);
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
                    return "Operation failed, kindly check your internet connection 1";
                }

                String output = "Operation failed, kindly check your internet connection 2";
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
                            JSONArray jsonArray = new JSONArray(result.toString());
                            NotesContentProvider.DatabaseHelper databaseHelper = new NotesContentProvider.DatabaseHelper(context);
                            databaseHelper.WorkingInsertController(jsonArray);

                            Log.d("REGISTER_NOW",jsonArray.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        output = "Records downloaded successfully";

                        return output;
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

    // This async task downloads the data that feeds into incentive_period_week_binding  table
    public static class DownloadPeriodWeek extends AsyncTask<String,String,String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;


        public DownloadPeriodWeek(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            java.net.URL url = null;
            String staff_id = null;

            try {

                sessionManagement = new SessionManagement(context);
                HashMap<String, String> user = sessionManagement.getUserDetails();
                staff_id = user.get(SessionManagement.KEY_STAFF_ID);


            } catch (Exception e) {
                System.out.println("Exception caught: Session Management didnt work");
            }

            if (staff_id == null) {
                return "No active session";
            }


            try {
                url = new URL(URL + "/fetch_period_week.php");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(40000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("staff_id", staff_id);
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
                return "Operation failed, kindly check your internet connection 1";
            }

            String output = "Operation failed, kindly check your internet connection 2";
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
                        JSONArray jsonArray = new JSONArray(result.toString());
                        NotesContentProvider.DatabaseHelper databaseHelper = new NotesContentProvider.DatabaseHelper(context);
                        databaseHelper.insertPeriodWeekBinding(jsonArray);

                        Log.d("REGISTER_NOW",jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    return output;
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

    // This async task downloads the data that feeds into incentive_successful_stats table
    public static class DownloadSuccessfulStats extends AsyncTask<String,String,String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;

        public DownloadSuccessfulStats(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            java.net.URL url = null;
            String staff_id = null;

            try {

                sessionManagement = new SessionManagement(context);
                HashMap<String, String> user = sessionManagement.getUserDetails();
                staff_id = user.get(SessionManagement.KEY_STAFF_ID);


            } catch (Exception e) {
                System.out.println("Exception caught: Session Management didnt work");
            }

            if (staff_id == null) {
                return "No active session";
            }


            try {
                url = new URL(URL + "/fetch_successful_stats.php");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(40000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("staff_id", staff_id);
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
                return "Operation failed, kindly check your internet connection 1";
            }

            String output = "Operation failed, kindly check your internet connection 2";
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
                        JSONArray jsonArray = new JSONArray(result.toString());
                        NotesContentProvider.DatabaseHelper databaseHelper = new NotesContentProvider.DatabaseHelper(context);
                        databaseHelper.SuccessfulInsertController(jsonArray);

                        Log.d("REGISTER_NOW",jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    return output;
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

    // This async task downloads the data that feeds into incentive_payment_structure table
    public static class DownloadPaymentStructure extends AsyncTask<String,String,String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;

        public DownloadPaymentStructure(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            java.net.URL url = null;
            String staff_id = null;

            try {

                sessionManagement = new SessionManagement(context);
                HashMap<String, String> user = sessionManagement.getUserDetails();
                staff_id = user.get(SessionManagement.KEY_STAFF_ID);


            } catch (Exception e) {
                System.out.println("Exception caught: Session Management didnt work");
            }

            if (staff_id == null) {
                return "No active session";
            }


            try {
                url = new URL(URL + "/fetch_payment_structure.php");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(40000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("staff_id", staff_id);
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
                return "Operation failed, kindly check your internet connection 1";
            }

            String output = "Operation failed, kindly check your internet connection 2";
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
                        JSONArray jsonArray = new JSONArray(result.toString());
                        NotesContentProvider.DatabaseHelper databaseHelper = new NotesContentProvider.DatabaseHelper(context);
                        databaseHelper.insertPaymentStructure(jsonArray);
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(0);

                        SessionManagement sessionManagement  = new SessionManagement(context);
                        sessionManagement.lastSyncedTime(jsonObject.getString("last_synced"));

                        Log.d("REGISTER_NOW",jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    return output;
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

    // This async task downloads the data that feeds into incentive_payment_history table
    public static class DownloadPaymentHistory extends AsyncTask<String,String,String> {

        @SuppressLint("StaticFieldLeak")
        Context context;
        StringBuilder result = null;
        SessionManagement sessionManagement;

        public DownloadPaymentHistory(Context mCtx) {
            this.context = mCtx;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            java.net.URL url = null;
            String staff_id = null;

            try {

                sessionManagement = new SessionManagement(context);
                HashMap<String, String> user = sessionManagement.getUserDetails();
                staff_id = user.get(SessionManagement.KEY_STAFF_ID);

            } catch (Exception e) {
                System.out.println("Exception caught: Session Management didnt work");
            }

            if (staff_id == null) {
                return "No active session";
            }


            try {
                url = new URL(URL + "/fetch_payment_history.php");

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception 1";
            }

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(40000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("staff_id", staff_id);
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
                return "Operation failed, kindly check your internet connection 1";
            }

            String output = "Operation failed, kindly check your internet connection 2";
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
                        JSONArray jsonArray = new JSONArray(result.toString());
                        NotesContentProvider.DatabaseHelper databaseHelper = new NotesContentProvider.DatabaseHelper(context);
                        databaseHelper.paymentHistoryController(jsonArray);

                        Log.d("REGISTER_NOW",jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    return output;
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



}

