package com.unified_tm.petdetective.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;

import com.unified_tm.petdetective.R;

public class SplacshActiity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splacsh_actiity);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
               checkLogInStatus();

            }
        }.start();
    }


    void checkLogInStatus(){
        SharedPreferences sharedPreferences = getSharedPreferences(Config.myPreference, Context.MODE_PRIVATE);
        String logInStatus = sharedPreferences.getString(Config.logInStatus,"");
        if(logInStatus.equals("0")){
            startActivity(new Intent(SplacshActiity.this,LoginActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(SplacshActiity.this, RequestPermissionsActivity.class));
            finish();
        }

    }






}
