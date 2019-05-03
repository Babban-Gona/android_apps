package com.babbangona.evoucherapp;

import android.content.Intent;
import android.se.omapi.Session;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandAuthActivity;
import com.babbangona.bg_face.LuxandInfo;

import java.util.HashMap;

public class ValidateMember extends AppCompatActivity {


    Button btnLeader,btnSecretary;
    Button btnValidateMember;
    DBOpenHelper dbOpenHelper;

    String role="";

    SessionManagement sessionManagement;

    String[] arr = {"--Select Member Role-- ","Leader","Secretary"};
    String tknIknumber = "";

    public static final String TAG = "ValidateMember";

    String DBTemplate,DBMemberID,Response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_member);

        btnLeader = findViewById(R.id.btn_leader);
        btnSecretary = findViewById(R.id.btn_secretary);
        btnValidateMember = findViewById(R.id.btnValidateMember);

        sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String, String> token = sessionManagement.getTokenDetails();
        tknIknumber = token.get(SessionManagement.TKN_IKNUMBER);

        btnLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLeader.setBackgroundColor(getResources().getColor(R.color.view_green));
                btnLeader.setTextColor(getResources().getColor(R.color.view_white));

                btnSecretary.setBackgroundColor(getResources().getColor(R.color.view_grey));
                btnSecretary.setTextColor(getResources().getColor(R.color.view_black));
                role = "";
                role = "Leader";
            }
        });

        btnSecretary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLeader.setBackgroundColor(getResources().getColor(R.color.view_grey));
                btnLeader.setTextColor(getResources().getColor(R.color.view_black));

                btnSecretary.setBackgroundColor(getResources().getColor(R.color.view_green));
                btnSecretary.setTextColor(getResources().getColor(R.color.view_white));
                role = "";
                role = "Secretary";
            }
        });


        btnValidateMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (role.equalsIgnoreCase("Leader")) {
                    DBMemberID = DBTemplate = "";
                    dbOpenHelper = new DBOpenHelper(getApplicationContext());
                    Response = dbOpenHelper.getMemberTemplate(tknIknumber, role);
                    DBMemberID = Response.split("##--##")[0];
                    DBTemplate = Response.split("##--##")[1];

                    DBTemplate = DBTemplate.replace(" ", "");

                    new LuxandInfo(getApplicationContext()).putTemplate(DBTemplate);
                    Intent intent = new Intent(getApplicationContext(), LuxandAuthActivity.class);
                    startActivityForResult(intent, 419);
                } else if (role.equalsIgnoreCase("Secretary")) {
                    DBMemberID = DBTemplate = "";

                    dbOpenHelper = new DBOpenHelper(getApplicationContext());
                    Response = dbOpenHelper.getMemberTemplate(tknIknumber, "Secretary");
                    DBMemberID = Response.split("##--##")[0];
                    DBTemplate = Response.split("##--##")[1];

                    Log.d(TAG, DBTemplate);
                    DBTemplate = DBTemplate.replace(" ", "");
                    Log.d(TAG, DBTemplate);


                    new LuxandInfo(getApplicationContext()).putTemplate(DBTemplate);
                    Intent intent = new Intent(getApplicationContext(), LuxandAuthActivity.class);
                    startActivityForResult(intent, 419);

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 419 && data != null){
            if(data.getIntExtra("RESULT",0) == 1)
            {
                //TODO ON SUCCESS
                sessionManagement  = new SessionManagement(getApplicationContext());
                sessionManagement.saveMemberID(DBMemberID);

                startActivity(new Intent(getApplicationContext(),ValidateIDCard.class));

            }

            else
            {
                //TODO ON FAILURE
                Toast.makeText(this, "This is not the "+role+" in "+tknIknumber, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
