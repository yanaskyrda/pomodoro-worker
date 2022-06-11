package com.diploma.database;

public class SessionSettingEntity extends com.diploma.timer.SessionSettingEntity {

    private float distractionsRate;

    private int id;

    public SessionSettingEntity(Integer roundsCount, Integer focusTime, Integer breakTime,
                                Integer bigBreakFrequency, Integer bigBreakTime) {
        super(roundsCount, focusTime, breakTime, bigBreakFrequency, bigBreakTime);
    }

    public float getDistractionsRate() {
        return distractionsRate;
    }

    public void setDistractionsRate(float distractionsRate) {
        this.distractionsRate = distractionsRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
