package com.diploma.timer;

import com.diploma.database.DatabaseHandler;
import com.diploma.spotify.MusicSettingEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class SessionsSettingsService {
    private static SessionsSettingsService instance;

    private List<SessionSettingEntity> sessionSettingDataSet;

    private SessionSettingEntity activeSetting;

    private SessionsSettingsService() {
        sessionSettingDataSet = new ArrayList<>();
        setSettingsDataSet(DatabaseHandler.getInstance(null).getAllSessionSettings());
        if (!sessionSettingDataSet.isEmpty()) {
            activeSetting = sessionSettingDataSet.get(0);
        }
    }

    public static SessionsSettingsService getInstance() {
        if (instance == null) {
            instance = new SessionsSettingsService();
        }
        return instance;
    }

    public List<SessionSettingEntity> getSessionSettingDataSet() {
        return sessionSettingDataSet;
    }

    public void setSettingsDataSet(List<SessionSettingEntity> sessionSettingDataSet) {
        this.sessionSettingDataSet = sessionSettingDataSet;
    }

    public void addSetting(SessionSettingEntity... sessionSetting) {
        sessionSettingDataSet.addAll(Arrays.asList(sessionSetting));
    }

    public SessionSettingEntity getSessionSetting(int position) {
        sessionSettingDataSet.sort(Comparator.comparing(SessionSettingEntity::getDistractionRate));
        return sessionSettingDataSet.get(position);
    }

    public void removeSetting(int position) {
        DatabaseHandler.getInstance(null)
                .deleteSessionSetting(sessionSettingDataSet.get(position).getId());
        sessionSettingDataSet.remove(position);
    }

    public void createAndAddSetting(SessionSettingEntity sessionSetting) {
        DatabaseHandler.getInstance(null).addSessionSetting(sessionSetting);
        sessionSettingDataSet.add(sessionSetting);
    }

    public int getDataSetSize() {
        if (sessionSettingDataSet == null) {
            return 0;
        }
        return sessionSettingDataSet.size();
    }

    public SessionSettingEntity getActiveSetting() {
        return activeSetting;
    }

    public void setActiveSetting(SessionSettingEntity activeSetting) {
        this.activeSetting = activeSetting;
    }

    public Boolean isActiveSettingPresent() {
        return activeSetting != null;
    }
}
