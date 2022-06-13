package com.diploma.youtube;

import android.os.AsyncTask;

import com.diploma.Credentials;
import com.diploma.MainActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YoutubeAPIExecutor extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... videoId) {
        try {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            YouTube youtubeService = new YouTube.Builder(
                    transport, jsonFactory, null)
                    .setApplicationName("pomodoro-worker")
                    .build();

            Map<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet");
            parameters.put("id", videoId[0]);
            parameters.put("key", Credentials.YOUTUBE_API_KEY);

            final YouTube.Videos.List request = youtubeService.videos()
                    .list(parameters.get("part"));
            request.setId(parameters.get("id"));
            request.setKey(parameters.get("key"));

            VideoListResponse response = request.execute();
            return response.getItems().get(0).getSnippet().getTitle();
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return videoId[0];
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
