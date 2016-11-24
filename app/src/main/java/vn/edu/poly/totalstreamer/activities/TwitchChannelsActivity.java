package vn.edu.poly.totalstreamer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import vn.edu.poly.totalstreamer.R;
import vn.edu.poly.totalstreamer.customAdapter.ChannelListAdapter;
import vn.edu.poly.totalstreamer.entities.ChannelEntity;
import vn.edu.poly.totalstreamer.extras.ConstantValue;
import vn.edu.poly.totalstreamer.myLog.L;
import vn.edu.poly.totalstreamer.network.VolleySingleton;

import static vn.edu.poly.totalstreamer.extras.Keys.KEY_CHANNEL;
import static vn.edu.poly.totalstreamer.extras.Keys.KEY_DISPLAY;
import static vn.edu.poly.totalstreamer.extras.Keys.KEY_GAME;
import static vn.edu.poly.totalstreamer.extras.Keys.KEY_LARGE;
import static vn.edu.poly.totalstreamer.extras.Keys.KEY_NAME;
import static vn.edu.poly.totalstreamer.extras.Keys.KEY_PREVIEW;
import static vn.edu.poly.totalstreamer.extras.Keys.KEY_STATUS;
import static vn.edu.poly.totalstreamer.extras.Keys.KEY_STREAMS;
import static vn.edu.poly.totalstreamer.extras.Keys.KEY_VIEWERS;
import static vn.edu.poly.totalstreamer.extras.SpecialChars.KEY_AND;
import static vn.edu.poly.totalstreamer.extras.SpecialChars.KEY_QUESTION;
import static vn.edu.poly.totalstreamer.extras.UrlEndPoints.URL_API_KEY;
import static vn.edu.poly.totalstreamer.extras.UrlEndPoints.URL_GAME;
import static vn.edu.poly.totalstreamer.extras.UrlEndPoints.URL_STREAM;
import static vn.edu.poly.totalstreamer.extras.UrlEndPoints.URL_TWITCH;

public class TwitchChannelsActivity extends AppCompatActivity {

    private static String gameName;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private List<ChannelEntity> channels;
    private RecyclerView recyclerView;
    private ChannelListAdapter channelListAdapter;

    public static String requestUrl() {
        String gameEncoded = ConstantValue.NA;
        try {
            gameEncoded = URLEncoder.encode(gameName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL_TWITCH + URL_STREAM + KEY_QUESTION + URL_GAME + gameEncoded + KEY_AND + URL_API_KEY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitch_channels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameName = getIntent().getExtras().getString("gameName");
        channels = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = VolleySingleton.getRequestQueue();

        sendJsonRequest();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        channelListAdapter = new ChannelListAdapter(this);
        recyclerView.setAdapter(channelListAdapter);
    }

    private void sendJsonRequest() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestUrl(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                channels = getJsonResponse(response);
                channelListAdapter.setChannelList(channels);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.m("Error getting channel Information");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private List<ChannelEntity> getJsonResponse(JSONObject response) {
        List<ChannelEntity> list = new ArrayList<>();

        if (response == null && response.length() == 0) {
            return null;
        }

        try {
            JSONArray jsonArray = response.getJSONArray(KEY_STREAMS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject channel = jsonArray.getJSONObject(i);
                int viewers = channel.getInt(KEY_VIEWERS);
                JSONObject preview = channel.getJSONObject(KEY_PREVIEW);
                String urlPreview = preview.getString(KEY_LARGE);
                JSONObject channelJson = channel.getJSONObject(KEY_CHANNEL);
                String displayName = channelJson.getString(KEY_DISPLAY);
                String channelStatus = channelJson.getString(KEY_STATUS);
                String gamePlaying = channelJson.getString(KEY_GAME);
                String channelName = channelJson.getString(KEY_NAME);

                ChannelEntity channelEntity = new ChannelEntity(channelName, displayName, channelStatus, gamePlaying, viewers, urlPreview);
                list.add(channelEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
