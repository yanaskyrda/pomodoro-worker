package com.diploma.youtube;

public class VideoSettingEntity {
    private int id;
    private String videoId;
    private Float distractionRate;

    public VideoSettingEntity(int id, String videoId, Float distractionRate) {
        this.id = id;
        this.videoId = videoId;
        this.distractionRate = distractionRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Float getDistractionRate() {
        return distractionRate;
    }

    public void setDistractionRate(Float distractionRate) {
        this.distractionRate = distractionRate;
    }
}
