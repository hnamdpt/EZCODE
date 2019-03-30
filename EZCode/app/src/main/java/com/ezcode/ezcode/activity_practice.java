package com.ezcode.ezcode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class activity_practice extends AppCompatActivity {
    Toolbar toolbar;
    public static String API_KEY  = "AIzaSyDG8HJRGKerWNvKOhoN3UXOzaHanp1xAt8";
    String ID_PLAYLIST = "w3WAW_OJpqE&list=PL4C2OaC1jQqR3ICDBf4j1dH1Fk4uIo-Lx";
    String URL_JSON = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL0eyrZgxdwhwNC5ppZo_dYGVjerQY3xYU&key=AIzaSyB4hyLACCt-zaeFXB4InsP51jqUQ9qJKLk&maxResults=20";
    ListView lvVideo;
    ArrayList<VideoYoutube> arrayVideo;
    VideoYoutubeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        lvVideo = (ListView)findViewById(R.id.listVideo);
        arrayVideo = new ArrayList<>();
        adapter = new VideoYoutubeAdapter(this,R.layout.row_video_youtube,arrayVideo);
        lvVideo.setAdapter(adapter) ;
        Toolbar toolbar = findViewById(R.id.toolbar_practice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);// bo title
        GetJsonYoutube(URL_JSON);
        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity_practice.this,PlayVideo.class);
                intent.putExtra("idVideo",arrayVideo.get(position).getIdVideo());
                startActivity(intent);
            }
        });
    }
    private void  GetJsonYoutube(final String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonItems = response.getJSONArray("items");
                    String title ;
                    String url_thumb = "";
                    String videoId = "";
                    for(int i=0;i<jsonItems.length();i++){
                        JSONObject jsonItem = jsonItems.getJSONObject(i);
                        JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");
                        title = jsonSnippet.getString("title");
                        JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");
                        JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");
                        url_thumb = jsonMedium.getString("url");
                        JSONObject jsonResourceID = jsonSnippet.getJSONObject("resourceId");
                        videoId = jsonResourceID.getString("videoId");
//                        Toast.makeText(activity_practice.this,videoId,Toast.LENGTH_LONG).show();
                        arrayVideo.add(new VideoYoutube(title,url_thumb,videoId));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
          },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity_practice.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }


        );
        requestQueue.add(jsonObjectRequest);
    }
}
