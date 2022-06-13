package com.diploma.spotify;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;

public class SpotifyAPIExecutor extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... playlistId) {
        SpotifyApi spotifyApi = SpotifyApi.builder()
                .setAccessToken(SpotifyLoginActivity.getToken())
                .build();

        GetPlaylistRequest playlistRequest = spotifyApi.getPlaylist(playlistId[0])
                .build();

        try {
            Playlist playlist = playlistRequest.execute();
            return playlist.getName();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            Log.e(TAG, "Couldn't get spotify playlist, error: " + e.getMessage());
        }
        return playlistId[0];
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
