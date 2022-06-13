package com.diploma.spotify;

import androidx.annotation.NonNull;

import com.diploma.Credentials;
import com.diploma.youtube.YoutubeAPIExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpotifyUtils {

    public static String getPlaylistTitleFromId(String playlistId, int maxLength) {
        if (playlistId == null || playlistId.isEmpty()) {
            return "unknown playlist title";
        }
        try {
            String title = new SpotifyAPIExecutor().execute(playlistId).get(10, TimeUnit.SECONDS);
            if (!Objects.equals(title, playlistId)) {
                if (title.length() > maxLength) {
                    title = title.substring(0, maxLength).concat("...");
                }
            }
            return title;
        } catch (Exception e) {
            return playlistId;
        }
    }

    public static String getPlaylistIdFromUrl(@NonNull String playlistUrl) {
        String playlistId = "";
        String regex = "^(https:\\/\\/open.spotify.com\\/user\\/spotify\\/playlist\\/|https:\\/\\/open.spotify.com\\/playlist\\/|spotify:user:spotify:playlist:|spotify:playlist:)([a-zA-Z0-9]+)(.*)$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(playlistUrl);
        if (matcher.find()) {
            playlistId = matcher.group(2);
        }
        return playlistId;
    }

}
