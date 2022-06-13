package com.diploma.youtube;

import com.diploma.database.DatabaseHandler;

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

    public void addSetting(String videoId) {
        DatabaseHandler db = DatabaseHandler.getInstance(null);
        db.addVideoSetting(videoId);
        VideoSettingEntity videoSetting = db.getAllVideosSettings().stream()
                .filter(s -> s.getVideoId().equals(videoId))
                .findFirst().get();
        videoSettingDataSet.add(videoSetting);
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
