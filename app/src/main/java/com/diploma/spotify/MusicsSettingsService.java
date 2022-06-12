package com.diploma.spotify;

import android.view.View;

import androidx.annotation.Nullable;

import com.diploma.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MusicsSettingsService {
    private static MusicsSettingsService instance;

    private List<MusicSettingEntity> musicSettingDataSet;

    private MusicSettingEntity activeSetting;

    private MusicsSettingsService() {
        musicSettingDataSet = new ArrayList<>();
        setSettingsDataSet(DatabaseHandler.getInstance(null).getAllMusicsSettings());
        if (!musicSettingDataSet.isEmpty()) {
            activeSetting = musicSettingDataSet.get(0);
        }
    }

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

    public void setSettingsDataSet(List<MusicSettingEntity> settings) {
        this.musicSettingDataSet = settings;
    }

    public void addSetting(MusicSettingEntity... settings) {
        musicSettingDataSet.addAll(Arrays.asList(settings));
    }

    public MusicSettingEntity getMusicSetting(int position) {
        musicSettingDataSet.sort(Comparator.comparing(MusicSettingEntity::getDistractionRate));
        return musicSettingDataSet.get(position);
    }

    public void removeSetting(int position) {
        DatabaseHandler.getInstance(null)
                .deleteMusicsSetting(musicSettingDataSet.get(position).getId());
        musicSettingDataSet.remove(position);
    }

    public int getDataSetSize() {
        if (musicSettingDataSet == null) {
            return 0;
        }
        return musicSettingDataSet.size();
    }
}
