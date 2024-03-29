package com.diploma.spotify;

public class MusicSettingEntity {
    private int id;
    private String playlistId;
    private Float distractionRate;

    public MusicSettingEntity(int id, String playlistId, Float distractionRate) {
        this.id = id;
        this.playlistId = playlistId;
        this.distractionRate = distractionRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public Float getDistractionRate() {
        return distractionRate;
    }

    public void setDistractionRate(Float distractionRate) {
        this.distractionRate = distractionRate;
    }
}
