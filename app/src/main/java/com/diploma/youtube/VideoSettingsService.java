package com.diploma.youtube;

import com.diploma.database.DatabaseHandler;
import com.diploma.spotify.MusicSettingEntity;
import com.diploma.timer.SessionSettingEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class VideoSettingsService {
    private static VideoSettingsService instance;

    private List<VideoSettingEntity> videoSettingDataSet;

    private VideoSettingEntity activeSetting;

    private VideoSettingsService() {
        videoSettingDataSet = new ArrayList<>();
        setSettingsDataSet(DatabaseHandler.getInstance(null).getAllVideosSettings());
    }

    public static VideoSettingsService getInstance() {
        if (instance == null) {
            instance = new VideoSettingsService();
        }
        return instance;
    }

    public void setSettingsDataSet(List<VideoSettingEntity> settings) {
        this.videoSettingDataSet = settings;
    }

    public void addSetting(VideoSettingEntity... videoSettings) {
        videoSettingDataSet.addAll(Arrays.asList(videoSettings));
    }

    public VideoSettingEntity getVideoSetting(int position) {
        videoSettingDataSet.sort(Comparator.comparing(VideoSettingEntity::getDistractionRate));
        return videoSettingDataSet.get(position);
    }

    public void removeSetting(int position) {
        DatabaseHandler.getInstance(null)
                .deleteVideoSetting(videoSettingDataSet.get(position).getId());
        videoSettingDataSet.remove(position);
    }

    public VideoSettingEntity getActiveSetting() {
        return activeSetting;
    }

    public Boolean isActiveSettingPresent() {
        return activeSetting != null;
    }

    public void setActiveSetting(VideoSettingEntity activeSetting) {
        this.activeSetting = activeSetting;
    }

    public int getDataSetSize() {
        if (videoSettingDataSet == null) {
            return 0;
        }
        return videoSettingDataSet.size();
    }
}
