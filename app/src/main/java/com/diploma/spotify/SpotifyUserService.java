package com.diploma.spotify;

import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.diploma.utils.VolleyCallback;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SpotifyUserService {
    private static final String ENDPOINT = "https://api.spotify.com/v1/me";
    private final SharedPreferences msharedPreferences;
    private final RequestQueue mqueue;
    private SpotifyUser user;

    public SpotifyUserService(RequestQueue queue, SharedPreferences sharedPreferences) {
        mqueue = queue;
        msharedPreferences = sharedPreferences;
    }

    public SpotifyUser getUser() {
        return user;
    }

    public void get(final VolleyCallback callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, response -> {
            Gson gson = new Gson();
            user = gson.fromJson(response.toString(), SpotifyUser.class);
            callBack.onSuccess();
        }, error -> get(() -> {

        })) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = msharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        mqueue.add(jsonObjectRequest);
    }

}
