package com.diploma.spotify;

import java.util.List;

public class MusicsSettingsService {
    private static MusicsSettingsService instance;

    private List<MusicSettingEntity> musicSettingDataSet;

    private MusicSettingEntity activeSetting;

    public static MusicsSettingsService getInstance() {
        if (instance == null) {
            instance = new MusicsSettingsService();
        }
        return instance;
    }

    public MusicSettingEntity getActiveSetting() {
        return activeSetting;
    }

    public void setActiveSetting(MusicSettingEntity activeSetting) {
        this.activeSetting = activeSetting;
    }
}
