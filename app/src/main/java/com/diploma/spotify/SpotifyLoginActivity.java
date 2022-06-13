package com.diploma.spotify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.diploma.Credentials;
import com.diploma.MainActivity;
import com.diploma.R;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.Objects;

public class SpotifyLoginActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;
    private RequestQueue queue;

    private static final int REQUEST_CODE = 1010;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_spotify_login);

        authenticateSpotify();

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }

    private void authenticateSpotify() {
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                Credentials.SPOTIFY_CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                Credentials.SPOTIFY_REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "playlist-read",
                "playlist-read-private", "streaming"});
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("Auth", "token retrieved successfully");
                    editor.apply();
                    waitForUserInfo();
                    break;

                case ERROR:
                    startMainActivity();
                    Log.e("Auth", response.getError());
                    break;

                default:
                    startMainActivity();
                    Log.e("Auth", "Unexpected response. " +
                            "Probably authentication process was cancelled");
            }
        }
    }

    private void waitForUserInfo() {
        SpotifyUserService userService = new SpotifyUserService(queue, msharedPreferences);
        userService.get(() -> {
            SpotifyUser user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            editor.commit();
            startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent newIntent = new Intent(SpotifyLoginActivity.this, MainActivity.class);
        startActivity(newIntent);
    }
}