package com.diploma;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MainActivity extends AppCompatActivity {

    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextId = findViewById(R.id.editTextId);
        Button buttonPlay = findViewById(R.id.buttonPlay);

        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);


        buttonPlay.setOnClickListener(view -> {
            String videoId = editTextId.getText().toString();
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.cueVideo(videoId, 0));
        });
    }
}
