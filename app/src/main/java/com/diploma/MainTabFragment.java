package com.diploma;

import static com.diploma.spotify.SpotifyLoginActivity.CLIENT_ID;
import static com.diploma.spotify.SpotifyLoginActivity.REDIRECT_URI;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.diploma.spotify.SpotifyPlayerState;
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

    //start spotify section
    private SpotifyAppRemote spotifyAppRemote;
    private SpotifyPlayerState spotifyPlayerState = SpotifyPlayerState.UNKNOWN;
    //end spotify section

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog videosDialog;

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

        connectYoutube(view);

        ImageButton playSongButton = view.findViewById(R.id.playSongButton);
        ImageButton playPreviousSongButton = view.findViewById(R.id.previousSongButton);
        ImageButton playNextSongButton = view.findViewById(R.id.nextSongButton);

        connectSpotify(view);
        playSongButton.setOnClickListener(v -> pauseOrResumeSpotifyPlayer(view, playSongButton));
        playNextSongButton.setOnClickListener(this::skipToNextSong);
        playPreviousSongButton.setOnClickListener(this::skipToPreviousSong);

        final Button openVideosChooser = view.findViewById(R.id.videoPlaylistButton);
        openVideosChooser.setOnClickListener(this::createNewVideosDialog);
    }

    //todo rename
    private void connectYoutube(@NonNull View view) {
//        final EditText editTextId = view.findViewById(R.id.editTextId);
//        Button buttonPlay = findViewById(R.id.buttonPlay);

        youtubePlayerView = view.findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youtubePlayerView);


        //buttonPlay.setOnClickListener(view -> {
//        String videoId = editTextId.getText().toString();
//        youtubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.cueVideo(videoId, 0));
        //});
    }

    private void connectSpotify(@NonNull View view) {
//        TextView userView = view.findViewById(R.id.user);

        SharedPreferences sharedPreferences = this.requireActivity()
                .getSharedPreferences("SPOTIFY", 0);
//        userView.setText(sharedPreferences.getString("userid", "No User"));

        connectSpotifyRemoteApp();
    }

    protected void connectSpotifyRemoteApp() {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();


        SpotifyAppRemote.connect(this.requireContext(), connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote receivedSpotifyAppRemote) {
                        spotifyAppRemote = receivedSpotifyAppRemote;
                        spotifyPlayerState = SpotifyPlayerState.INITIALIZED;
                        Log.d("Spotify remote app", "Connected successfully.");
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("Spotify remote app", throwable.getMessage(), throwable);
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        spotifyAppRemote.getPlayerApi().pause();
        SpotifyAppRemote.disconnect(spotifyAppRemote);
        spotifyPlayerState = SpotifyPlayerState.DISCONNECTED;
    }

    private void playPlaylist(View view) {
        spotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1EQqkOPvHGajmW");
        setTrackTitle(view);
    }

    private void setTrackTitle(View view) {
        spotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        TextView currentSongTitle = view.findViewById(R.id.currentSong);
                        String songTitlePlaceholder = view.getResources()
                                .getString(R.string.current_song_title);
                        currentSongTitle.setText(String.format(songTitlePlaceholder,
                                track.artist.name, track.name));
                        Log.d("MainTabFragment", track.name + " by " + track.artist.name);
                    }
                });
    }

    private void pauseOrResumeSpotifyPlayer(View view, ImageButton button) {
        switch (spotifyPlayerState) {
            case INITIALIZED:
                playPlaylist(view);
                button.setImageResource(R.drawable.pause_button);
                spotifyPlayerState = SpotifyPlayerState.PLAYING;
                break;
            case PAUSED:
                spotifyAppRemote.getPlayerApi().resume();
                button.setImageResource(R.drawable.pause_button);
                spotifyPlayerState = SpotifyPlayerState.PLAYING;
                break;
            case PLAYING:
                spotifyAppRemote.getPlayerApi().pause();
                button.setImageResource(R.drawable.play_button);
                spotifyPlayerState = SpotifyPlayerState.PAUSED;
                break;
            default:
                Log.d("MainTabFragment", "Unrecognizable spotify player state");
        }
    }

    private void skipToNextSong(View view) {
        switch (spotifyPlayerState) {
            case PAUSED:
                spotifyAppRemote.getPlayerApi().skipNext();
                spotifyAppRemote.getPlayerApi().pause();
                break;
            case PLAYING:
                spotifyAppRemote.getPlayerApi().skipNext();
                break;
            default:
                return;
        }
        setTrackTitle(view);
    }

    private void skipToPreviousSong(View view) {
        switch (spotifyPlayerState) {
            case PAUSED:
                spotifyAppRemote.getPlayerApi().skipPrevious();
                spotifyAppRemote.getPlayerApi().pause();
                break;
            case PLAYING:
                spotifyAppRemote.getPlayerApi().skipPrevious();
                break;
            default:
                return;
        }
        setTrackTitle(view);
    }

    public void createNewVideosDialog(View view) {
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View videoChooserPopup = getLayoutInflater().inflate(R.layout.video_chooser_popup, null);

        final EditText editTextId = videoChooserPopup.findViewById(R.id.youtube_video_input);
        final Button closeButton = videoChooserPopup.findViewById(R.id.close_video_chooser);
        youtubePlayerView = view.getRootView().findViewById(R.id.activity_main_youtubePlayerView);
        //getLifecycle().addObserver(youtubePlayerView);

        dialogBuilder.setView(videoChooserPopup);
        videosDialog = dialogBuilder.create();
        videosDialog.show();

        closeButton.setOnClickListener(v -> {
            String videoId = editTextId.getText().toString();
            youtubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.cueVideo(videoId, 0));
            videosDialog.dismiss();
        });
    }
}