package com.diploma.timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SessionsSettingsService {
    private static SessionsSettingsService instance;

    private List<SessionSetting> sessionSettingDataSet;

    private SessionSetting activeSetting;

    private SessionsSettingsService() {
        sessionSettingDataSet = new ArrayList<>();
        sessionSettingDataSet.add(new SessionSetting(3, 2, 1, 2, 3));
        sessionSettingDataSet.add(new SessionSetting(4, 25, 5));
        sessionSettingDataSet.add(new SessionSetting(2, 50, 10));
        activeSetting = sessionSettingDataSet.get(0);
    }

    public static SessionsSettingsService getInstance() {
        if (instance == null) {
            instance = new SessionsSettingsService();
        }
        return instance;
    }

    public List<SessionSetting> getSessionSettingDataSet() {
        return sessionSettingDataSet;
    }

    public void setSessionSettingDataSet(List<SessionSetting> sessionSettingDataSet) {
        this.sessionSettingDataSet = sessionSettingDataSet;
    }

    public void addSessionSetting(SessionSetting... sessionSetting) {
        sessionSettingDataSet.addAll(Arrays.asList(sessionSetting));
    }

    public SessionSetting getSessionSetting(int position) {
        return sessionSettingDataSet.get(position);
    }

    public void removeSessionSetting(int position) {
        sessionSettingDataSet.remove(position);
    }

    public int getDataSetSize() {
        return sessionSettingDataSet.size();
    }

    public SessionSetting getActiveSetting() {
        return activeSetting;
    }

    public void setActiveSetting(SessionSetting activeSetting) {
        this.activeSetting = activeSetting;
    }
}
