package com.unified_tm.petdetective.Activites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unified_tm.petdetective.R;

public class RequestPermissionsActivity extends AppCompatActivity {

    TextView btnEnable,btnSHowAll;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions);

        inIT();
        setListeners();



        if(!hasPermissions(RequestPermissionsActivity.this, PERMISSIONS)){

        }
        else{
            startActivity(new Intent(RequestPermissionsActivity.this,ActivityMakeChoice.class));
            finish();
        }



    }

    void inIT(){
        btnEnable = findViewById(R.id.btnEnable);
        btnSHowAll = findViewById(R.id.btn_showAll);
    }
    void setListeners(){

        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(RequestPermissionsActivity.this, PERMISSIONS, PERMISSION_ALL);

            }
        });

        btnSHowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestPermissionsActivity.this,ActivityMakeChoice.class));
                finish();


            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                     startActivity(new Intent(RequestPermissionsActivity.this,ActivityMakeChoice.class));
                     finish();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }}







        }

    ////////
    ////////////////
    ///////////////////////////////// Permission Work
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }
}
