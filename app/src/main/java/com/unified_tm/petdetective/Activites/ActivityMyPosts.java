package com.unified_tm.petdetective.Activites;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unified_tm.petdetective.Adapters.AdapterPosts;
import com.unified_tm.petdetective.Models.ModelITemMain;
import com.unified_tm.petdetective.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityMyPosts extends AppCompatActivity {
    EditText editSearch;
    ImageView btnWritePost;
    RecyclerView recyclerView;

    List<ModelITemMain> iTemMainList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
       // getSupportActionBar().setTitle("My Posts");

        inIT();
        setListeneres();
      //  getMyPosts();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        getMyPosts();
    }

    void inIT(){

        editSearch   = findViewById(R.id.txtSearch);
        btnWritePost = findViewById(R.id.btnWritePost);
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progress);

    }
    void setListeneres(){

        btnWritePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ActivityMyPosts.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_post,popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if(id == R.id.lost){
                            CreatePostActivity.postType = "lost";
                            startActivity(new Intent(ActivityMyPosts.this,CreatePostActivity.class));
                        }
                        else
                            if(id== R.id.found){
                                CreatePostActivity.postType = "found";
                                startActivity(new Intent(ActivityMyPosts.this,CreatePostActivity.class));


                            }

                        return true;
                    }
                });
            }
        });

    }

    void getMyPosts(){

        SharedPreferences sharedPreferences = getSharedPreferences(Config.myPreference, Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString(Config.userID,"");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String postUrl = "https://www.unifiedtnc.com/pet_detective_api/get_my_posts.php?api_key="+Config.apiKey+"&user_id="+user_id;
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

                        AdapterPosts adapterPosts = new AdapterPosts(ActivityMyPosts.this,iTemMainList);
                        recyclerView.setAdapter(adapterPosts);
                    }
                    else
                    if(code.equals("403")){
                        Toast toast = Toast.makeText(ActivityMyPosts.this,"Ooops! some problem occured while uploading.",Toast.LENGTH_SHORT);
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
}
