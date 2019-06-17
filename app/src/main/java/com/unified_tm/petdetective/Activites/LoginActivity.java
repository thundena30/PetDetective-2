package com.unified_tm.petdetective.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.unified_tm.petdetective.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    ProgressBar progressBar ;
    Button btnLogIn;
    Validator validator;

    @NotEmpty
    @Email
    EditText editEmail;

    @NotEmpty
    @Password(min = 4)
    EditText editPassword;

    TextView btnCreateHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        validator = new Validator(this);
        validator.setValidationListener(this);

        inIT();
        setListeners();


    }
    void inIT(){
        progressBar = findViewById(R.id.login_progress);
        btnLogIn = findViewById(R.id.email_sign_in_button);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        btnCreateHere = findViewById(R.id.btnCreateHere);

    }
    void setListeners(){
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        btnCreateHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ActivitySignUp.class));
                finish();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {

        signIN();

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }


    void signIN(){

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        String signInUrl = "https://www.unifiedtnc.com/pet_detective_api/log_in.php";
        StringRequest request = new StringRequest(Request.Method.POST, signInUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if(code.equals("404")){
                        editEmail.setError("User No Found");
                    }
                    else if(code.equals("400")){
                        editPassword.setError("Password Not Correct");
                    }

                    else{
                        Toast toast = Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        SharedPreferences sharedPreferences = getSharedPreferences(Config.myPreference, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Config.logInStatus,"1");
                        editor.putString(Config.userID,jsonObject.getString("user_id"));
                        editor.apply();

                        startActivity(new Intent(LoginActivity.this, RequestPermissionsActivity.class));
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("user_email",editEmail.getText().toString());
                map.put("user_pass",editPassword.getText().toString());
                map.put("api_key",Config.apiKey);

                return map;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
        progressBar.setVisibility(View.VISIBLE);

    }
}
