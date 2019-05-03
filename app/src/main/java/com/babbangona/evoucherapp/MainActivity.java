package com.babbangona.evoucherapp;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void LaunchAccessControl(View v){

        try{
          /*  Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
            startActivity(intent);
*/

           Intent intent = new Intent(Intent.ACTION_MAIN);

            intent.setComponent(new ComponentName("com.babbangona.accesscontrol","com.babbangona.accesscontrol.MainActivity"));
            startActivity(intent);

        }
        catch(Exception e){ Toast.makeText(this, "Access control is not installed on this device", Toast.LENGTH_SHORT).show(); }
    }

}
