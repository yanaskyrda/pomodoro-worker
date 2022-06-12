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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.spotify.MusicsSettingsService;
import com.diploma.spotify.SpotifyPlayerState;
import com.diploma.timer.SessionOptionsAdapter;
import com.diploma.timer.SessionsSettingsService;
import com.diploma.timer.TimerService;
import com.diploma.youtube.VideoOptionsAdapter;
import com.diploma.youtube.VideoSettingsService;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.Track;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private AlertDialog sessionSettingDialog;
    private AlertDialog videosSettingDialog;
    private AlertDialog musicsSettingDialog;

    private TimerService timerService;
    private SessionsSettingsService sessionsSettingsService;
    private MusicsSettingsService musicsSettingsService;
    private VideoSettingsService videoSettingsService;

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

        //spotify
        ImageButton playSongButton = view.findViewById(R.id.play_song_button);
        ImageButton playPreviousSongButton = view.findViewById(R.id.previous_song_button);
        ImageButton playNextSongButton = view.findViewById(R.id.next_song_button);

        connectSpotify(view);
        playSongButton.setOnClickListener(v -> pauseOrResumeSpotifyPlayer(view, playSongButton));
        playNextSongButton.setOnClickListener(this::skipToNextSong);
        playPreviousSongButton.setOnClickListener(this::skipToPreviousSong);
        //spotify

        //timer
        timerService = TimerService.getInstance(view);
        //timer

        sessionsSettingsService = SessionsSettingsService.getInstance();
        videoSettingsService = VideoSettingsService.getInstance();
        musicsSettingsService = MusicsSettingsService.getInstance();

        final Button openVideosChooser = view.findViewById(R.id.video_playlist_button);
        openVideosChooser.setOnClickListener(this::createNewVideosDialog);

        final ImageButton openSessionSetting = view.findViewById(R.id.sessions_settings_button);
        openSessionSetting.setOnClickListener(this::createNewSessionSettingsDialog);
    }

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
        spotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX9sIqqvKsjG8");
        setTrackTitle(view);
    }

    private void setTrackTitle(View view) {
        spotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        TextView currentSongTitle = view.findViewById(R.id.current_song);
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
        final View videoChooserView = getLayoutInflater().inflate(R.layout.video_chooser_popup, null);

//        final EditText editTextId = videoChooserView.findViewById(R.id.youtube_video_input);
        final Button closeButton = videoChooserView.findViewById(R.id.close_video_chooser);
        youtubePlayerView = view.getRootView().findViewById(R.id.activity_main_youtubePlayerView);
        //getLifecycle().addObserver(youtubePlayerView);

        dialogBuilder.setView(videoChooserView);
        videosSettingDialog = dialogBuilder.create();
        videosSettingDialog.show();


        RecyclerView recyclerView = videoChooserView.findViewById(R.id.video_option_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        VideoOptionsAdapter adapter = new VideoOptionsAdapter(videoSettingsService);
        recyclerView.setAdapter(adapter);

        closeButton.setOnClickListener(v -> {
            String videoId = VideoSettingsService.getInstance()
                    .getActiveSetting().getVideoId();//"9jRGR8n0a68";
            youtubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.cueVideo(videoId, 0));
            videosSettingDialog.dismiss();
        });

        videosSettingDialog.setOnDismissListener(v -> youtubePlayerView
                .getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer
                        .cueVideo(VideoSettingsService.getInstance()
                                .getActiveSetting().getVideoId(), 0)
                ));
    }

    public void createNewSessionSettingsDialog(View view) {
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View sessionSettingsView = getLayoutInflater().inflate(R.layout.session_setting_popup, null);

        final Button closeButton = sessionSettingsView.findViewById(R.id.close_session_setting);

        dialogBuilder.setView(sessionSettingsView);
        sessionSettingDialog = dialogBuilder.create();
        sessionSettingDialog.show();

        RecyclerView recyclerView = sessionSettingsView.findViewById(R.id.session_option_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        SessionOptionsAdapter adapter = new SessionOptionsAdapter(sessionsSettingsService);
        recyclerView.setAdapter(adapter);

        closeButton.setOnClickListener(v -> sessionSettingDialog.dismiss());
    }

    public static String getVideoIdFromUrl(@NonNull String videoUrl) {
        String videoId = "";
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }
}