package com.diploma.youtube;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.diploma.MainActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeUtils {
    public static String getVideoTitle(String videoId, int maxLength) {
        if (videoId == null || videoId.isEmpty()) {
            return "unknown video title";
        }
        try {
            String title = new YoutubeAPIExecutor().execute(videoId).get(10, TimeUnit.SECONDS);
            if (!Objects.equals(title, videoId)) {
                if (title.length() > maxLength) {
                    title = title.substring(0, maxLength).concat("...");
                }
            }
            return title;
        } catch (Exception e) {
            return videoId;
        }
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
