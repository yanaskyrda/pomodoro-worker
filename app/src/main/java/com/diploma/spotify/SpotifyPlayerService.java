package com.diploma.spotify;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.diploma.Credentials;
import com.diploma.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

public class SpotifyPlayerService {
    private static SpotifyPlayerService instance;

    private SpotifyAppRemote spotifyAppRemote;
    private SpotifyPlayerState spotifyPlayerState = SpotifyPlayerState.UNKNOWN;
    private String playlistId;

    private View view;
    ImageButton playSongButton;

    private SpotifyPlayerService(@NonNull Context context, @NonNull View view) {
        this.view = view;
        connectSpotifyRemoteApp(context);
        this.playSongButton = view.findViewById(R.id.play_song_button);
        ImageButton playPreviousSongButton = view.findViewById(R.id.previous_song_button);
        ImageButton playNextSongButton = view.findViewById(R.id.next_song_button);

        playSongButton.setOnClickListener(v -> pauseOrResumeSpotifyPlayer());
        playNextSongButton.setOnClickListener(v -> skipToNextSong());
        playPreviousSongButton.setOnClickListener(v -> skipToPreviousSong());
    }

    public static SpotifyPlayerService getInstance(@Nullable Context context, @Nullable View view) {
        if (instance == null) {
            instance = new SpotifyPlayerService(context, view);
        }
        return instance;
    }

    public SpotifyAppRemote getSpotifyAppRemote() {
        return spotifyAppRemote;
    }

    public SpotifyPlayerState getSpotifyPlayerState() {
        return spotifyPlayerState;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    protected void connectSpotifyRemoteApp(@NonNull Context context) {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(Credentials.SPOTIFY_CLIENT_ID)
                        .setRedirectUri(Credentials.SPOTIFY_REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(context, connectionParams,
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

    public void onStop() {
        spotifyAppRemote.getPlayerApi().pause();
        SpotifyAppRemote.disconnect(spotifyAppRemote);
        spotifyPlayerState = SpotifyPlayerState.DISCONNECTED;
    }

    private void playPlaylist() {
        if (playlistId != null) {
            spotifyAppRemote.getPlayerApi().play(playlistId);
            setTrackTitle();
        }
    }

    private void setTrackTitle() {
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

    public void pauseOrResumeSpotifyPlayer() {

        switch (spotifyPlayerState) {
            case INITIALIZED:
                if (isReadyToPlay()) {
                    playPlaylist();
                    playSongButton.setImageResource(R.drawable.pause_button);
                    spotifyPlayerState = SpotifyPlayerState.PLAYING;
                }
                break;
            case PAUSED:
                if (isReadyToPlay()) {
                    spotifyAppRemote.getPlayerApi().resume();
                    playSongButton.setImageResource(R.drawable.pause_button);
                    spotifyPlayerState = SpotifyPlayerState.PLAYING;
                }
                break;
            case PLAYING:
                spotifyAppRemote.getPlayerApi().pause();
                playSongButton.setImageResource(R.drawable.play_button);
                spotifyPlayerState = SpotifyPlayerState.PAUSED;
                break;
            default:
                Log.d("MainTabFragment", "Unrecognizable spotify player state");
        }
    }

    public void forceStart() {
        if (isReadyToPlay()) {
            if (spotifyPlayerState.equals(SpotifyPlayerState.INITIALIZED)) {
                playPlaylist();
            } else if (spotifyPlayerState.equals(SpotifyPlayerState.PAUSED)) {
                spotifyAppRemote.getPlayerApi().resume();
            }
            playSongButton.setImageResource(R.drawable.pause_button);
            spotifyPlayerState = SpotifyPlayerState.PLAYING;
        }
    }

    public void forceStop() {
        if (spotifyPlayerState.equals(SpotifyPlayerState.PLAYING)) {
            spotifyAppRemote.getPlayerApi().pause();
            playSongButton.setImageResource(R.drawable.play_button);
            spotifyPlayerState = SpotifyPlayerState.PAUSED;
        }
    }

    private void skipToNextSong() {
        if (isReadyToPlay()) {
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
            setTrackTitle();
        }
    }

    private void skipToPreviousSong() {
        if (isReadyToPlay()) {
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
            setTrackTitle();
        }
    }

    public boolean isReadyToPlay() {
        return playlistId != null;
    }
}
