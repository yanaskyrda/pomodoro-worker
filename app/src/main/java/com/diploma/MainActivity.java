package com.diploma;

import static com.diploma.spotify.SpotifyLoginActivity.CLIENT_ID;
import static com.diploma.spotify.SpotifyLoginActivity.REDIRECT_URI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.diploma.databinding.ActivityMainBinding;
import com.diploma.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

public class MainActivity extends AppCompatActivity {
    //youtube section
    private YouTubePlayerView youTubePlayerView;
    //end youtube section

    //spotify section
    private TextView userView;

    private SpotifyAppRemote spotifyAppRemote;

    //end spotify section

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
//        ViewPager2 viewPager = binding.viewPager;
//        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = binding.tabs;
//        tabs.setupWithViewPager(viewPager);

        runYoutube();
        runSpotify();
    }

    //todo rename
    private void runYoutube() {
        final EditText editTextId = findViewById(R.id.editTextId);
//        Button buttonPlay = findViewById(R.id.buttonPlay);

        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);


        //buttonPlay.setOnClickListener(view -> {
            String videoId = editTextId.getText().toString();
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.cueVideo(videoId, 0));
        //});
    }

    private void runSpotify() {
        userView = findViewById(R.id.user);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        startWidget();
    }

    protected void startWidget() {

        //todo move to method
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();


        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote receivedSpotifyAppRemote) {
                        spotifyAppRemote = receivedSpotifyAppRemote;
                        Log.d("Spotify remote app", "Connected successfully.");

                        playPlaylist();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("Spotify remote app", throwable.getMessage(), throwable);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(spotifyAppRemote);
    }

    private void playPlaylist() {
        spotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1EQqkOPvHGajmW");

        // Subscribe to PlayerState
        spotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }
}
