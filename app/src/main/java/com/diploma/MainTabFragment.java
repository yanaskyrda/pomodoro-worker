package com.diploma;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diploma.spotify.MusicOptionsAdapter;
import com.diploma.spotify.MusicsSettingsService;
import com.diploma.spotify.SpotifyPlayerService;
import com.diploma.spotify.SpotifyUtils;
import com.diploma.timer.SessionOptionsAdapter;
import com.diploma.timer.SessionsSettingsService;
import com.diploma.timer.TimerService;
import com.diploma.youtube.VideoOptionsAdapter;
import com.diploma.youtube.VideoSettingsService;
import com.diploma.youtube.YoutubeUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Objects;

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

        final Button expandYoutubeButton = view.findViewById(R.id.expand_youtube_button);
        expandYoutubeButton.setOnClickListener(v -> {
            Button videoChooserButton = view.getRootView().findViewById(R.id.video_playlist_button);
            ImageButton expandYoutubeIcon = view.findViewById(R.id.expand_youtube_icon);

            videoChooserButton.setVisibility(View.VISIBLE);
            youtubePlayerView.setVisibility(View.VISIBLE);
            expandYoutubeButton.setVisibility(View.INVISIBLE);
            expandYoutubeIcon.setVisibility(View.INVISIBLE);
        });
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

        final TextInputLayout editTextLayout = videoChooserView.findViewById(R.id.input_setting_layout);
        final ImageButton saveSettingButton = videoChooserView.findViewById(R.id.save_streaming_option_button);
        final Button hideYoutubeButton = videoChooserView.findViewById(R.id.hide_youtube_button);
        youtubePlayerView = view.getRootView().findViewById(R.id.activity_main_youtubePlayerView);
        Button videoChooserButton = view.getRootView().findViewById(R.id.video_playlist_button);

        dialogBuilder.setView(videoChooserView);
        videosSettingDialog = dialogBuilder.create();
        videosSettingDialog.show();


        RecyclerView recyclerView = videoChooserView.findViewById(R.id.video_option_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        VideoOptionsAdapter adapter = new VideoOptionsAdapter(videoSettingsService, videosSettingDialog);
        recyclerView.setAdapter(adapter);

        hideYoutubeButton.setOnClickListener(v -> {
            Button expandYoutubeButton = view.getRootView().findViewById(R.id.expand_youtube_button);
            ImageButton expandYoutubeIcon = view.getRootView().findViewById(R.id.expand_youtube_icon);

            videoChooserButton.setVisibility(View.INVISIBLE);
            youtubePlayerView.setVisibility(View.INVISIBLE);
            expandYoutubeButton.setVisibility(View.VISIBLE);
            expandYoutubeIcon.setVisibility(View.VISIBLE);
            videosSettingDialog.dismiss();
        });

        videosSettingDialog.setOnDismissListener(v -> {
            if (videoSettingsService.isActiveSettingPresent()) {
                String videoId = videoSettingsService.getActiveSetting().getVideoId();
                videoChooserButton.setText(YoutubeUtils.getVideoTitle(videoId, 30));
                youtubePlayerView.getYouTubePlayerWhenReady(youTubePlayer ->
                        youTubePlayer.cueVideo(videoId, 0));
            }
        });

        saveSettingButton.setOnClickListener(v -> {
            String videoId = YoutubeUtils.getVideoIdFromUrl(
                    Objects.requireNonNull(editTextLayout.getEditText()).getText().toString());
            videoSettingsService.addSetting(videoId);
            adapter.notifyDataSetChanged();
            editTextLayout.getEditText().setText("");
        });
    }

    public void createNewMusicDialog(View view) {
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View musicChooserView = getLayoutInflater().inflate(R.layout.music_chooser_popup, null);
        final Button musicChooserButton = view.getRootView().findViewById(R.id.music_playlist_button);
        final TextInputLayout editTextLayout = musicChooserView.findViewById(R.id.input_setting_layout);
        final ImageButton saveSettingButton = musicChooserView.findViewById(R.id.save_streaming_option_button);

        dialogBuilder.setView(musicChooserView);
        musicsSettingDialog = dialogBuilder.create();
        musicsSettingDialog.show();


        RecyclerView recyclerView = musicChooserView.findViewById(R.id.music_option_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        MusicOptionsAdapter adapter = new MusicOptionsAdapter(musicsSettingsService, musicsSettingDialog);
        recyclerView.setAdapter(adapter);

        musicsSettingDialog.setOnDismissListener(v -> {
            if (musicsSettingsService.isActiveSettingPresent()) {
                String playlistTitle = SpotifyUtils.getPlaylistTitleFromId(
                        musicsSettingsService.getActiveSetting().getPlaylistId(), 30);
                musicChooserButton.setText(playlistTitle);
            }
        });

        saveSettingButton.setOnClickListener(v -> {
            String playlistId = SpotifyUtils.getPlaylistIdFromUrl(
                    Objects.requireNonNull(editTextLayout.getEditText()).getText().toString());
            musicsSettingsService.addSetting(playlistId);
            adapter.notifyDataSetChanged();
            editTextLayout.getEditText().setText("");
        });
    }

    public void createNewSessionSettingsDialog(View view) {
        dialogBuilder = new AlertDialog.Builder(view.getContext());
        final View sessionSettingsView = getLayoutInflater().inflate(R.layout.session_setting_popup, null);
        final TextView timeText = view.getRootView().findViewById(R.id.text_session_time);

        dialogBuilder.setView(sessionSettingsView);
        sessionSettingDialog = dialogBuilder.create();
        sessionSettingDialog.show();

        RecyclerView recyclerView = sessionSettingsView.findViewById(R.id.session_option_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        SessionOptionsAdapter adapter = new SessionOptionsAdapter(sessionsSettingsService, sessionSettingDialog);
        recyclerView.setAdapter(adapter);

        sessionSettingDialog.setOnDismissListener(v -> {
            if (sessionsSettingsService.isActiveSettingPresent()) {
                timeText.setText(TimerService.getInstance(null)
                        .hmsTimeFormatterFromMinutes(
                                sessionsSettingsService.getActiveSetting().getFocusTime()));
            }
        });
    }
}