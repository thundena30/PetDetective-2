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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.unified_tm.petdetective.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySignUp extends AppCompatActivity implements Validator.ValidationListener {
    @NotEmpty
    @Email
    EditText editEmail;

    @NotEmpty
    @Length(min = 4,max = 20)
    EditText editFullName;

    @NotEmpty
    @Length(min = 4,max = 20)
    EditText editContact;
    @NotEmpty
    @Password(min = 4)
    EditText editPassword;
    @NotEmpty
    @ConfirmPassword
    EditText editConfrmPassword;

    Validator validator;
    ProgressBar progressBar;
    Button btnSignUp;
    TextView btnLogInhEre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        validator = new Validator(this);
        validator.setValidationListener(this);

        inIT();
        setLIstenrs();
    }


    void inIT(){
        progressBar = findViewById(R.id.login_progress);

        editFullName = findViewById(R.id.fullNameSignUp);
        editContact  = findViewById(R.id.contactSIngUp);
        editEmail    = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        editConfrmPassword = findViewById(R.id.confirm_password);
        btnSignUp = findViewById(R.id.btnSIgnUp);
        btnLogInhEre = findViewById(R.id.btnLogInhEre);



    }
    void setLIstenrs(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        btnLogInhEre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySignUp.this,LoginActivity.class));
                finish();
            }
        });
    }




    @Override
    public void onValidationSucceeded() {

        signUP();

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





    void signUP(){
        RequestQueue requestQueue = Volley.newRequestQueue(ActivitySignUp.this);
        String signUpurl = "https://www.unifiedtnc.com/pet_detective_api/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, signUpurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(ActivitySignUp.this, response, Toast.LENGTH_SHORT).show();

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if(code.equals("409")){
                        editEmail.setError("User Already Exist");
                    }
                    else{
                        Toast toast = Toast.makeText(ActivitySignUp.this,"Register Successfully",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();

                        SharedPreferences sharedPreferences = getSharedPreferences(Config.myPreference, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Config.logInStatus,"1");
                        editor.putString(Config.userID,jsonObject.getString("user_id"));
                        editor.apply();

                        startActivity(new Intent(ActivitySignUp.this, RequestPermissionsActivity.class));
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

                Toast.makeText(ActivitySignUp.this, error.toString(), Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("user_name",editFullName.getText().toString());
                map.put("user_contact",editContact.getText().toString());
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
