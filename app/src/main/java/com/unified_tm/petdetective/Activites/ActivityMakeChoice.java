package com.unified_tm.petdetective.Activites;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unified_tm.petdetective.MainActivity;
import com.unified_tm.petdetective.R;

public class ActivityMakeChoice extends AppCompatActivity {
    ImageView btnMenu;
    TextView btnLost,btnFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_choice);
        inIT();
        setListenres();


    }
    void inIT(){
        btnLost = findViewById(R.id.btnLost);
        btnFound = findViewById(R.id.btnFound);
    }
    void setListenres(){
        btnLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.contextFrom = "lost";
              //  startActivity(new Intent(ActivityMakeChoice.this, MainActivity.class));
                Intent intent = new Intent(ActivityMakeChoice.this,MainActivity.class);
                startActivityForResult(intent,123);
            }
        });

        btnFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.contextFrom = "found";
              //  startActivity(new Intent(ActivityMakeChoice.this,MainActivity.class));

                Intent intent = new Intent(ActivityMakeChoice.this,MainActivity.class);
                startActivityForResult(intent,123);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String logOut = data.getStringExtra("logout_status");
            if(logOut.equals("1")){
                startActivity(new Intent(ActivityMakeChoice.this,LoginActivity.class));
                finish();
            }
        }
    }
}
