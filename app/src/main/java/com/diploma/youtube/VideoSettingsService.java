package com.diploma.youtube;

import com.diploma.spotify.MusicSettingEntity;

import java.util.List;

public class VideoSettingsService {
    private static VideoSettingsService instance;

    private List<VideoSettingEntity> videoSettingDataSet;

    private VideoSettingEntity activeSetting;

    public static VideoSettingsService getInstance() {
        if (instance == null) {
            instance = new VideoSettingsService();
        }
        return instance;
    }

    public VideoSettingEntity getActiveSetting() {
        return activeSetting;
    }

    public void setActiveSetting(VideoSettingEntity activeSetting) {
        this.activeSetting = activeSetting;
    }
}
