package com.diploma;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //youtube section
    private YouTubePlayerView youTubePlayerView;
    //end youtube section

    //spotify section
    private TextView userView;

    //end spotify section

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runYoutube();
        runSpotify();
    }

    //todo rename
    private void runYoutube() {
        final EditText editTextId = findViewById(R.id.editTextId);
        Button buttonPlay = findViewById(R.id.buttonPlay);

        youTubePlayerView = findViewById(R.id.activity_main_youtubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);


        buttonPlay.setOnClickListener(view -> {
            String videoId = editTextId.getText().toString();
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> youTubePlayer.cueVideo(videoId, 0));
        });
    }

    private void runSpotify() {
        userView = findViewById(R.id.user);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));
    }
}
