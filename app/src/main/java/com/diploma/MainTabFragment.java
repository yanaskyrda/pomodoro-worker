package com.diploma;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.spotify.MusicOptionsAdapter;
import com.diploma.spotify.MusicsSettingsService;
import com.diploma.spotify.SpotifyPlayerService;
import com.diploma.timer.SessionOptionsAdapter;
import com.diploma.timer.SessionsSettingsService;
import com.diploma.timer.TimerService;
import com.diploma.youtube.VideoOptionsAdapter;
import com.diploma.youtube.VideoSettingsService;
import com.diploma.youtube.YoutubeUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTabFragment extends Fragment {

    private YouTubePlayerView youtubePlayerView;
    private SpotifyPlayerService spotifyPlayerService;
    private TimerService timerService;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog sessionSettingDialog;
    private AlertDialog videosSettingDialog;
    private AlertDialog musicsSettingDialog;

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
        spotifyPlayerService = SpotifyPlayerService.getInstance(this.requireContext(), view);
        timerService = TimerService.getInstance(view);

        sessionsSettingsService = SessionsSettingsService.getInstance();
        videoSettingsService = VideoSettingsService.getInstance();
        musicsSettingsService = MusicsSettingsService.getInstance();

        final Button openVideosChooser = view.findViewById(R.id.video_playlist_button);
        openVideosChooser.setOnClickListener(this::createNewVideosDialog);

        final Button openMusicsChooser = view.findViewById(R.id.music_playlist_button);
        openMusicsChooser.setOnClickListener(this::createNewMusicDialog);

        final ImageButton openSessionSetting = view.findViewById(R.id.sessions_settings_button);
        openSessionSetting.setOnClickListener(this::createNewSessionSettingsDialog);
    }

    private void connectYoutube(@NonNull View view) {
        youtubePlayerView = view.findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youtubePlayerView);
    }

    @Override
    public void onStop() {
        super.onStop();
        spotifyPlayerService.onStop();
    }

    public void createNewVideosDialog(View view) {
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View videoChooserView = getLayoutInflater().inflate(R.layout.video_chooser_popup, null);

//        final EditText editTextId = videoChooserView.findViewById(R.id.youtube_video_input);
        final Button closeButton = videoChooserView.findViewById(R.id.close_video_chooser);
        youtubePlayerView = view.getRootView().findViewById(R.id.activity_main_youtubePlayerView);
        Button videoChooserButton = view.getRootView().findViewById(R.id.video_playlist_button);
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
            videoChooserButton.setText(YoutubeUtils.getVideoTitle(videoId));
            youtubePlayerView.getYouTubePlayerWhenReady(youTubePlayer ->
                    youTubePlayer.cueVideo(videoId, 0));

            videosSettingDialog.dismiss();
        });

        videosSettingDialog.setOnDismissListener(v -> youtubePlayerView
                .getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer
                        .cueVideo(VideoSettingsService.getInstance()
                                .getActiveSetting().getVideoId(), 0)
                ));
    }

    public void createNewMusicDialog(View view) {
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View musicChooserView = getLayoutInflater().inflate(R.layout.music_chooser_popup, null);

        final Button closeButton = musicChooserView.findViewById(R.id.close_music_chooser);
        youtubePlayerView = view.getRootView().findViewById(R.id.activity_main_youtubePlayerView);

        dialogBuilder.setView(musicChooserView);
        musicsSettingDialog = dialogBuilder.create();
        musicsSettingDialog.show();


        RecyclerView recyclerView = musicChooserView.findViewById(R.id.music_option_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        MusicOptionsAdapter adapter = new MusicOptionsAdapter(musicsSettingsService);
        recyclerView.setAdapter(adapter);

        closeButton.setOnClickListener(v -> {
            spotifyPlayerService.setPlaylistId(musicsSettingsService
                    .getActiveSetting().getPlaylistId());
            musicsSettingDialog.dismiss();
        });

        musicsSettingDialog.setOnDismissListener(v ->
                spotifyPlayerService.setPlaylistId(musicsSettingsService
                        .getActiveSetting().getPlaylistId())
        );
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
}