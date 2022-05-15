package com.diploma;

import static com.diploma.spotify.SpotifyLoginActivity.CLIENT_ID;
import static com.diploma.spotify.SpotifyLoginActivity.REDIRECT_URI;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTabFragment extends Fragment {

    //youtube section
    private YouTubePlayerView youtubePlayerView;
    //end youtube section

    //spotify section
    private TextView userView;
    private SpotifyAppRemote spotifyAppRemote;
    //end spotify section

    public MainTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainTabFragment.
     */
    public static MainTabFragment newInstance() {
        return new MainTabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        runYoutube(view);
        runSpotify(view);
    }

    //todo rename
    private void runYoutube(@NonNull View view) {
        final EditText editTextId = view.findViewById(R.id.editTextId);
//        Button buttonPlay = findViewById(R.id.buttonPlay);

        youtubePlayerView = view.findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youtubePlayerView);


        //buttonPlay.setOnClickListener(view -> {
        String videoId = editTextId.getText().toString();
        youtubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.cueVideo(videoId, 0));
        //});
    }

    private void runSpotify(@NonNull View view) {
        userView = view.findViewById(R.id.user);

        SharedPreferences sharedPreferences = this.requireActivity()
                .getSharedPreferences("SPOTIFY", 0);
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


        SpotifyAppRemote.connect(this.requireContext(), connectionParams,
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
    public void onStop() {
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