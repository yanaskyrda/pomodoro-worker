package com.diploma.spotify;

import android.view.View;

import androidx.annotation.Nullable;

import com.diploma.database.DatabaseHandler;
import com.diploma.youtube.VideoSettingEntity;

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
        SpotifyPlayerService.getInstance(null, null)
                .setPlaylistId(activeSetting.getPlaylistId());
    }

    public void setSettingsDataSet(List<MusicSettingEntity> settings) {
        this.musicSettingDataSet = settings;
    }

    public void addSetting(MusicSettingEntity... settings) {
        musicSettingDataSet.addAll(Arrays.asList(settings));
    }

    public void addSetting(String playlistId) {
        DatabaseHandler db = DatabaseHandler.getInstance(null);
        db.addMusicSetting(playlistId);
        MusicSettingEntity musicSetting = db.getAllMusicsSettings().stream()
                .filter(s -> s.getPlaylistId().equals(playlistId))
                .findFirst().get();
        musicSettingDataSet.add(musicSetting);
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

    public Boolean isActiveSettingPresent() {
        return activeSetting != null;
    }
}
