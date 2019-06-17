package com.unified_tm.petdetective.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.theartofdev.edmodo.cropper.CropImage;
import com.unified_tm.petdetective.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity implements Validator.ValidationListener {
    public static String postType = "";

    TextView txtPostType,btnSubmit;
    Spinner spinPetType,spinnerStatus;
    @NotEmpty
    @Length(min = 3,max = 20)
    EditText editBreed;

    @NotEmpty
    @Length(min = 10,max = 300)
    EditText editDesc;

    @NotEmpty
    @Length(min = 5,max = 20)
    EditText editTitle;
    @NotEmpty
    @Email
    EditText
    editEmail;
    @NotEmpty
    @Length(min = 4,max = 12)
    EditText editContact,editZipCode;
    ImageView imagePet;

    String petType,status,image64 = "";


    String[] petCategories = new String[]{"Dog","Cat","Other"};
    String[] statusCategories = new String[]{"Missing","In Search of owner","Reunited","Surrenderd to shelter"};

    Validator validator;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        validator = new Validator(this);
        validator.setValidationListener(this);


        inIT();
        setListeners();
        populateSpinners();

        txtPostType.setText(postType);
    }

    private void populateSpinners() {

        ArrayAdapter<String> petAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,petCategories );
        petAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinPetType.setAdapter(petAdapter);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,statusCategories );
        statusAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerStatus.setAdapter(statusAdapter);


        spinPetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                petType = petCategories[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = statusCategories[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    void inIT(){

        txtPostType = findViewById(R.id.txtPostType);
        spinPetType = findViewById(R.id.spinPetSelection);
        spinnerStatus = findViewById(R.id.spinStatus);
        editBreed     = findViewById(R.id.editBreed);
        editDesc      = findViewById(R.id.editDesc);
        editEmail     = findViewById(R.id.editEmailPost);
        editContact   = findViewById(R.id.editContactPost);

        editTitle    = findViewById(R.id.editTitle);
        editZipCode  = findViewById(R.id.editZip);

        imagePet      = findViewById(R.id.imagePet);
        btnSubmit     = findViewById(R.id.btnSubmitPost);
        progressBar   = findViewById(R.id.progress);

    }

    void setListeners(){

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    validator.validate();
            }
        });

        imagePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity().start(CreatePostActivity.this);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postType = "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imagePet.setImageURI(resultUri);
                try {
                    image64 = GeneralHelper.getImage64String(MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String abc = "";

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        submitPost();
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


    void submitPost(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String postUrl = "https://www.unifiedtnc.com/pet_detective_api/submit_post.php";
        StringRequest request = new StringRequest(Request.Method.POST, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responce",response);
                progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");

                    if(code.equals("200")){
                        Toast toast = Toast.makeText(CreatePostActivity.this,"Posted Successfully",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                      //  finish();
                    }
                    else
                        if(code.equals("403")){
                            Toast toast = Toast.makeText(CreatePostActivity.this,"Ooops! some problem occured while uploading.",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            finish();

                        }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("responce",error.toString());
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                SharedPreferences sharedPreferences = getSharedPreferences(Config.myPreference, Context.MODE_PRIVATE);
                String user_id = sharedPreferences.getString(Config.userID,"");


                Map<String,String> map = new HashMap();
                map.put("title",editTitle.getText().toString());
                map.put("pet_type",petType);
                map.put("breed",editBreed.getText().toString());
                map.put("desc",editDesc.getText().toString());
                map.put("image",image64);
                map.put("status",status);
                map.put("lat","");
                map.put("lng","");
                map.put("zip_code",editZipCode.getText().toString());
                map.put("email",editEmail.getText().toString());
                map.put("contact",editContact.getText().toString());
                map.put("post_type",postType);
                map.put("api_key",Config.apiKey);
                map.put("user_id",user_id);
                return map;
            }





        };

        request.setShouldCache(false);
        requestQueue.add(request);
        progressBar.setVisibility(View.VISIBLE);
    }
}
