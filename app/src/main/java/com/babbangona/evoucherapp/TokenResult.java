package com.babbangona.evoucherapp;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TokenResult extends AppCompatActivity {

    TextView tvName,tvPackageType,tvSeed,tvAtrazine,tvUrea,tvDap,tvSeeds;
    SessionManagement sessionManagement;
    String seedType, loanfieldsize,ik_number,leader_name;

    Map<String,String> map;
    DBOpenHelper dbOpenHelper;
    public static final String TAG = "TokenResult1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_result);

        sessionManagement  = new SessionManagement(getApplicationContext());
        HashMap<String,String> token = sessionManagement.getTokenDetails();
        seedType = token.get(SessionManagement.TKN_SEED_TYPE);
        loanfieldsize = token.get(SessionManagement.TKN_LOAN_FIELDSIZE);
        ik_number = token.get(SessionManagement.TKN_IKNUMBER);

        tvName = findViewById(R.id.tvName);
        tvPackageType = findViewById(R.id.tvPackageType);
        tvSeed = findViewById(R.id.tvSeedType);
        tvAtrazine = findViewById(R.id.tvAtrazine);
        tvUrea = findViewById(R.id.tvUrea);
        tvDap = findViewById(R.id.tvDap);
        tvSeeds = findViewById(R.id.tvSeed);

        dbOpenHelper = new DBOpenHelper(getApplicationContext());
        map = new HashMap<>();
        map = dbOpenHelper.getPackage(loanfieldsize+"_seed_maize");
        leader_name = dbOpenHelper.leaderInfo(ik_number);

        try {
            tvName.setText("TG Leader: " + leader_name + " \nIK Number: " + ik_number);
            tvPackageType.setText("ID 1 " + seedType + " Package:");

            tvSeed.setText(seedType+" seeds: "+map.get("seed")+" bag(s)");
            tvPackageType.setText(seedType + " Package");
            tvSeeds.setText("Seed Type: " + seedType);
            tvAtrazine.setText("Atrazine: " + map.get("atrazine") + " bottle(s)");
            tvUrea.setText("Urea: " + map.get("urea") + " bag(s)");
            tvDap.setText("Dap" + map.get("dap") + " bag(s)");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void Cancel(View v){


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.cancel_card, null);
        dialogBuilder.setView(dialogView);

        final LinearLayout linearLayout = dialogView.findViewById(R.id.transitions_container);
        final Spinner spinner = dialogView.findViewById(R.id.spn_1);
        final Spinner spinner2 = dialogView.findViewById(R.id.spn_2);
        final Button submit = dialogView.findViewById(R.id.btn_1);



        String[] cancel_type = new String[]{"-- select  reason --","Stock Out"};
        String[] desc_type = new String[]{"No Seed ","No DAP","No Atrazine","No Urea"};
        //String[] bank2 = new String[]{"Access Bank","Diamond Bank","ECO BANK","Fidelity Bank","First Bank","FCMB", "GT Bank","Heritage Bank","Keystone Bank","Stanbic IBTC","Sterling Bank","UBA","Union Bank","Unity Bank","Wema Bank","Zenith Bank"};

        ArrayAdapter<String> dataAdapterx = new ArrayAdapter<String>(TokenResult.this,
                android.R.layout.simple_spinner_item, cancel_type);
        dataAdapterx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterx);

        ArrayAdapter<String> dataAdaptery = new ArrayAdapter<String>(TokenResult.this,
                android.R.layout.simple_spinner_item, desc_type);
        dataAdaptery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdaptery);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem().toString().trim().equalsIgnoreCase("-- select  reason --")){
                    Toast.makeText(TokenResult.this, "you have not selected a reason", Toast.LENGTH_SHORT).show();
                }else{
                    String complain_type = spinner.getSelectedItem().toString().trim();
                    String complain_desc = spinner2.getSelectedItem().toString().trim();

                    DBOpenHelper dbOpenHelper = new DBOpenHelper(getApplicationContext());
                    long x = dbOpenHelper.complain(getApplicationContext(),ik_number,complain_type,complain_desc);

                    startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                }
            }
        });


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    public void Next(View v){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TokenResult.this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirmation_card, null);
        dialogBuilder.setView(dialogView);

        final LinearLayout linearLayout = dialogView.findViewById(R.id.transitions_container);
        final TextView textView = dialogView.findViewById(R.id.tv_1);
        final EditText editText = dialogView.findViewById(R.id.edt_1);
        final Button submit = dialogView.findViewById(R.id.btn_1);

        String[] chars = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","0","1","2","3","4","5","6","7","8","9"};


        Random rand = new Random();
        String a = chars[rand.nextInt(36)];
        String b = chars[rand.nextInt(36)];
        String c = chars[rand.nextInt(36)];

        textView.setText(a+b+c);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().equalsIgnoreCase("")){
                    editText.setError("This field cannot be left blank");
                }else if(!editText.getText().toString().trim().equalsIgnoreCase(textView.getText().toString().trim())){
                    Toast.makeText(TokenResult.this, "Ensure you enter the characters above", Toast.LENGTH_SHORT).show();

                }
                else{

                    sessionManagement  = new SessionManagement(getApplicationContext());
                    HashMap<String,String> token = sessionManagement.getTokenDetails();
                    String  seedType = token.get(SessionManagement.TKN_SEED_TYPE);
                    String  loanfieldsize = token.get(SessionManagement.TKN_LOAN_FIELDSIZE);
                    String  ik_number = token.get(SessionManagement.TKN_IKNUMBER);
                    String  tokenSeason = token.get(SessionManagement.TKN_SEASON);
                    String tokenEntered = token.get(SessionManagement.TKN_TOKEN_ENTERED);
                    String memberId = token.get(SessionManagement.KEY_MEMBERID);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date1 = new Date();
                    String time = dateFormat1.format(date1);

                    String folder_name = "EVOUCHER";
                    String filename = ik_number+"_"+time+".jpg";

                    sessionManagement = new SessionManagement(getApplicationContext());
                    sessionManagement.savePicParams(folder_name,filename);

                    sessionManagement = new SessionManagement(getApplicationContext());
                    HashMap<String,String> user = sessionManagement.getUserDetails();
                    String staffID = user.get(SessionManagement.KEY_STAFF_ID);
                    String staffRole = user.get(SessionManagement.KEY_STAFF_ROLE);
                    String acco_id = user.get(SessionManagement.KEY_ACCO_ID);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date();
                    String TxnID =  staffID+"_"+dateFormat.format(date)+"_ID";

                    SessionManagement sessionManagement  = new SessionManagement(getApplicationContext());
                    sessionManagement.saveTxnId(TxnID);

                    DBOpenHelper dbOpenHelper = new DBOpenHelper(getApplicationContext());
                    String urea = dbOpenHelper.getInput(loanfieldsize+"_no_seed_maize").split("##")[0];
                    String dap = dbOpenHelper.getInput(loanfieldsize+"_no_seed_maize").split("##")[1];
                    String atrazin = dbOpenHelper.getInput(loanfieldsize+"_no_seed_maize").split("##")[2];
                    String seed = "";


                    insertIntoLMTC(staffID,memberId, TxnID+"_UREA","100000369","Fertiliser:Urea-Indorama",urea);
                    insertIntoLMTC(staffID,memberId, TxnID+"_ATRAZINE","100000018","AgroChem:Atrazine",atrazin);
                    insertIntoLMTC(staffID,memberId, TxnID+"_SEED","100000192","Seed:Maize(SM15) - 12.5KG",seed);
                    insertIntoLMTC(staffID,memberId, TxnID+"_DAP","100000091","Fertiliser:DAP",dap);


                    dbOpenHelper = new DBOpenHelper(getApplicationContext());
                    dbOpenHelper.insertTransactionData(getApplicationContext(),TxnID,ik_number,loanfieldsize+"_no_seed_maize",loanfieldsize,seedType,staffID,staffRole,tokenSeason,tokenEntered,memberId,acco_id);


                    startActivity(new Intent(getApplicationContext(),CameraPage.class));
                }
            }
        });


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();





    }



    public void insertIntoLMTC(String staffID, String memberId,String TxnID,String ItemID,String ItemName,String unit){
        try {
            String URL = "content://com.bgenterprise.bglmtcinventory/Inventory03T";
            Uri inventory = Uri.parse(URL);
            ContentValues contentV = new ContentValues();


            SimpleDateFormat dateFormat11 = new SimpleDateFormat("yyyy-MM-dd");
            Date datex = new Date();




            contentV.put("ItemID", ItemID);
            contentV.put("ItemName", ItemName);
            contentV.put("LMDID", staffID);
            contentV.put("Notes", " input distributed to "+ik_number+", collected by  "+memberId);
            contentV.put("Staff_ID", staffID);
            contentV.put("SyncDate", dateFormat11.format(datex));
            contentV.put("SyncStatus", "No");
            contentV.put("TxnDate", dateFormat11.format(datex));
            contentV.put("Type", "ID");
            contentV.put("UniqueID", TxnID);
            contentV.put("Unit", unit);
            contentV.put("UnitPrice", "0");

            Uri x = getContentResolver().insert(inventory, contentV);
            long result = ContentUris.parseId(x);

            Log.d(TAG, String.valueOf(result));

        }catch(Exception e){
            e.printStackTrace();
        }
    }



}
