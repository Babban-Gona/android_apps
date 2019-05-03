package com.babbangona.evoucherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsPage extends AppCompatActivity {
    ArrayList<Map<String,String>> data;
    ArrayList<SyncedContractClass> userlist;
    TextView tvTGsDistributedToday,tvTGsDistributedTotal,tvBagsDistributed,tvSyncUpDate,tvSyncDownDate;
    RecyclerView recyclerView;
    String staff_id;
    Map<String,String>map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_page);
        tvTGsDistributedToday = findViewById(R.id.tvStat1);
        tvTGsDistributedTotal = findViewById(R.id.tvStat2);
        tvBagsDistributed = findViewById(R.id.tvStat3);
        tvSyncUpDate = findViewById(R.id.tvStat4);
        tvSyncDownDate = findViewById(R.id.tvStat5);

        // This is the declaration for the recycler view that adapts data for Today, This Week, This Payment Period, and Since year started card
        recyclerView = findViewById(R.id.recylcerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String,String> users = sessionManagement.getUserDetails();
        staff_id = users.get(SessionManagement.KEY_STAFF_ID);

        data = new ArrayList<>();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getApplicationContext());
        data = dbOpenHelper.getDBRecords(staff_id);

         map = new HashMap<>();
         map = dbOpenHelper.getStatistics(staff_id);

         tvTGsDistributedTotal.setText("# of complete transaction: "+map.get("total_distributed"));
         tvSyncUpDate.setText("Last Sync Up: "+map.get("sync_up"));
         tvSyncDownDate.setText("Last Sync Down: "+map.get("sync_down"));

         Log.d("JJJJJJ",data+"");
        userlist = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject Quiz = jsonArray.getJSONObject(i);
                userlist.add(new SyncedContractClass(
                        Quiz.getString("txn_id"),
                        Quiz.getString("mem_name"),
                        Quiz.getString("ik_number"),
                        Quiz.getString("loan_fieldsize"),
                        Quiz.getString("sync_flag")
                ));
            }
        }

        catch (JSONException e1) {
            Log.e("FAILED", "Json parsing error: " + e1.getMessage());
        }

        SyncedRecyclerAdapter adapter = new SyncedRecyclerAdapter(this, userlist);
        recyclerView.setAdapter(adapter);




    }
}
