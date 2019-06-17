package com.unified_tm.petdetective;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.unified_tm.petdetective.Activites.ActivityMyPosts;
import com.unified_tm.petdetective.Activites.Config;
import com.unified_tm.petdetective.Activites.LoginActivity;
import com.unified_tm.petdetective.Adapters.AdapterCombine;
import com.unified_tm.petdetective.Adapters.AdapterPosts;
import com.unified_tm.petdetective.Adapters.AdpterRecyleMaint;
import com.unified_tm.petdetective.Models.ModelITemMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;


    public static String contextFrom ="";

    RecyclerView recyclerView;

    ProgressBar progressBar;
    List<ModelITemMain> iTemMainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(contextFrom.equals("lost")){
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            getSupportActionBar().setTitle("Lost");
        }
        else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.color_green));
            getSupportActionBar().setTitle("Found");
        }


        inIT();
        setListeners();
        if(contextFrom.equals("found")){
            getPostsFound();
        }
        else{
            getPostsLost();
        }



    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        contextFrom = "";
    }



    void logOutUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(Config.myPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.logInStatus,"0");
        editor.apply();


        Intent returnIntent = new Intent();
        returnIntent.putExtra("logout_status","1");
        setResult(RESULT_OK,returnIntent);
        finish();
    }


    void inIT(){

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        progressBar = findViewById(R.id.progress);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


    }
    void setListeners(){
        navigationView.setNavigationItemSelectedListener(this);

    }







    void getPostsFound(){



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String postUrl = "https://www.unifiedtnc.com/pet_detective_api/get_post_found.php?api_key="+Config.apiKey;
        StringRequest request = new StringRequest(Request.Method.GET, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responce",response);

                progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");

                    if(code.equals("200")){
                        JSONArray jsonArray = jsonObject.getJSONArray("posts");
                        iTemMainList = new ArrayList<>();

                        for(int i=0; i<jsonArray.length();i++){



                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            ModelITemMain modelITemMain = new ModelITemMain();
                            modelITemMain.setTitle(jsonObject1.getString("title"));
                            modelITemMain.setPetType(jsonObject1.getString("pet_type"));
                            modelITemMain.setPetBreed(jsonObject1.getString("pet_breed"));
                            modelITemMain.setDescription(jsonObject1.getString("pet_desc"));
                            modelITemMain.setPetImage(jsonObject1.getString("pet_image"));
                            modelITemMain.setLat(jsonObject1.getString("pet_lat"));
                            modelITemMain.setLng(jsonObject1.getString("pet_lng"));
                            modelITemMain.setStatus(jsonObject1.getString("pet_status"));
                            modelITemMain.setContact(jsonObject1.getString("contact"));
                            modelITemMain.setEmail(jsonObject1.getString("email"));
                            modelITemMain.setZipCode(jsonObject1.getString("zip_code"));
                            modelITemMain.setPostType(jsonObject1.getString("post_type"));
                            modelITemMain.setPostDate(jsonObject1.getString("dated"));

                            iTemMainList.add(modelITemMain);

                        }

                        AdapterCombine adapterPosts = new AdapterCombine(MainActivity.this,iTemMainList);
                        recyclerView.setAdapter(adapterPosts);
                    }
                    else
                    if(code.equals("403")){
                        Toast toast = Toast.makeText(MainActivity.this,"Ooops! some problem occured while uploading.",Toast.LENGTH_SHORT);
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
        });

        request.setShouldCache(false);
        requestQueue.add(request);
        progressBar.setVisibility(View.VISIBLE);
    }

    void getPostsLost(){



        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String postUrl = "https://www.unifiedtnc.com/pet_detective_api/get_post_lost.php?api_key="+Config.apiKey;
        StringRequest request = new StringRequest(Request.Method.GET, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responce",response);

                progressBar.setVisibility(View.GONE);

                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");

                    if(code.equals("200")){
                        JSONArray jsonArray = jsonObject.getJSONArray("posts");
                        iTemMainList = new ArrayList<>();

                        for(int i=0; i<jsonArray.length();i++){



                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            ModelITemMain modelITemMain = new ModelITemMain();
                            modelITemMain.setTitle(jsonObject1.getString("title"));
                            modelITemMain.setPetType(jsonObject1.getString("pet_type"));
                            modelITemMain.setPetBreed(jsonObject1.getString("pet_breed"));
                            modelITemMain.setDescription(jsonObject1.getString("pet_desc"));
                            modelITemMain.setPetImage(jsonObject1.getString("pet_image"));
                            modelITemMain.setLat(jsonObject1.getString("pet_lat"));
                            modelITemMain.setLng(jsonObject1.getString("pet_lng"));
                            modelITemMain.setStatus(jsonObject1.getString("pet_status"));
                            modelITemMain.setContact(jsonObject1.getString("contact"));
                            modelITemMain.setEmail(jsonObject1.getString("email"));
                            modelITemMain.setZipCode(jsonObject1.getString("zip_code"));
                            modelITemMain.setPostType(jsonObject1.getString("post_type"));
                            modelITemMain.setPostDate(jsonObject1.getString("dated"));

                            iTemMainList.add(modelITemMain);

                        }

                        AdapterCombine adapterPosts = new AdapterCombine(MainActivity.this,iTemMainList);
                        recyclerView.setAdapter(adapterPosts);
                    }
                    else
                    if(code.equals("403")){
                        Toast toast = Toast.makeText(MainActivity.this,"Ooops! some problem occured while uploading.",Toast.LENGTH_SHORT);
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
        });

        request.setShouldCache(false);
        requestQueue.add(request);
        progressBar.setVisibility(View.VISIBLE);
    }















    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_my_posts) {

            startActivity(new Intent(MainActivity.this, ActivityMyPosts.class));


        } else if (id == R.id.nav_logOut) {
            logOutUser();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
