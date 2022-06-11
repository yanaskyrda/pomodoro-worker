package com.diploma.timer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.diploma.database.DatabaseHandler;
import com.diploma.spotify.MusicSettingEntity;
import com.diploma.spotify.MusicsSettingsService;
import com.diploma.youtube.VideoSettingEntity;
import com.diploma.youtube.VideoSettingsService;

public class DistractionService {
    private static DistractionService instance;

    private final SensorManager sensorManager;

    private final MusicsSettingsService musicsSettingsService;

    private final VideoSettingsService videosSettingsService;

    private final SessionsSettingsService sessionsSettingsService;

    private static final Float DISTRACTION_CHANGE_DELTA = 0.1f;

    private DistractionService(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.musicsSettingsService = MusicsSettingsService.getInstance();
        this.videosSettingsService = VideoSettingsService.getInstance();
        this.sessionsSettingsService = SessionsSettingsService.getInstance();
    }

    public static DistractionService getInstance(SensorManager sensorManager) {
        if (instance == null) {
            instance = new DistractionService(sensorManager);
        }
        return instance;
    }

    public void initializeListener() {
        if (sensorManager != null) {
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void deinitializeListener() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(listener);
        }
    }

    private final SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xChange = Math.abs(event.values[0]);
            float yChange = Math.abs(event.values[1]);
            float zChange = Math.abs(event.values[2]);
            if (xChange > 0 && yChange > 0 && zChange > 0) {
                DatabaseHandler dbHandler = DatabaseHandler.getInstance(null);

                SessionSettingEntity sessionSetting = sessionsSettingsService.getActiveSetting();
                sessionSetting.setDistractionRate(sessionSetting.getDistractionRate()
                        + DISTRACTION_CHANGE_DELTA / sessionSetting.getFocusTime());
                dbHandler.updateSessionDistractionRate(sessionSetting);

                MusicSettingEntity musicSetting = musicsSettingsService.getActiveSetting();
                musicSetting.setDistractionRate(musicSetting.getDistractionRate()
                        + DISTRACTION_CHANGE_DELTA / sessionSetting.getFocusTime());
                dbHandler.updateMusicDistractionRate(musicSetting);

                VideoSettingEntity videoSetting = videosSettingsService.getActiveSetting();;
                videoSetting.setDistractionRate(videoSetting.getDistractionRate()
                        + DISTRACTION_CHANGE_DELTA / sessionSetting.getFocusTime());
                dbHandler.updateVideoDistractionRate(videoSetting);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
