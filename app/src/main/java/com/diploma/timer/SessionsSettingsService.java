package com.diploma.timer;

import com.diploma.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class SessionsSettingsService {
    private static SessionsSettingsService instance;

    private List<SessionSettingEntity> sessionSettingDataSet;

    private SessionSettingEntity activeSetting;

    private SessionsSettingsService() {
        sessionSettingDataSet = new ArrayList<>();
        setSessionSettingDataSet(DatabaseHandler.getInstance(null).getAllSessionSettings());
        activeSetting = sessionSettingDataSet.get(0);
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

    public void setSessionSettingDataSet(List<SessionSettingEntity> sessionSettingDataSet) {
        this.sessionSettingDataSet = sessionSettingDataSet;
    }

    public void addSessionSetting(SessionSettingEntity... sessionSetting) {
        sessionSettingDataSet.addAll(Arrays.asList(sessionSetting));
    }

    public SessionSettingEntity getSessionSetting(int position) {
        sessionSettingDataSet.sort(Comparator.comparing(SessionSettingEntity::getDistractionRate));
        return sessionSettingDataSet.get(position);
    }

    public void removeSessionSetting(int position) {
        sessionSettingDataSet.remove(position);
    }

    public int getDataSetSize() {
        return sessionSettingDataSet.size();
    }

    public SessionSettingEntity getActiveSetting() {
        return activeSetting;
    }

    public void setActiveSetting(SessionSettingEntity activeSetting) {
        this.activeSetting = activeSetting;
    }
}
