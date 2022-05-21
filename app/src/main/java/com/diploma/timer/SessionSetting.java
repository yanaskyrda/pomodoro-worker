package com.diploma.timer;

import java.util.Locale;

public class SessionSetting {
    /**
     * Number of rounds of focus-time + break-time.
     */
    private Integer roundsCount;
    /**
     * Time of one focus time round in minutes.
     */
    private Integer focusTime;

    /**
     * Time of one break time round in minutes.
     */
    private Integer breakTime;

    /**
     * How ofter big rest occurs.
     * The number mean that every n round instead of short break would be big break.
     */
    private Integer bigBreakFrequency = 0;

    /**
     * Time of one big rest time round in minutes.
     */
    private Integer bigBreakTime = 0;

    public SessionSetting(Integer roundsCount, Integer focusTime, Integer breakTime) {
        this.roundsCount = roundsCount;
        this.focusTime = focusTime;
        this.breakTime = breakTime;
    }

    public SessionSetting(Integer roundsCount, Integer focusTime, Integer breakTime, Integer bigBreakFrequency, Integer bigBreakTime) {
        this.roundsCount = roundsCount;
        this.focusTime = focusTime;
        this.breakTime = breakTime;
        this.bigBreakFrequency = bigBreakFrequency;
        this.bigBreakTime = bigBreakTime;
    }

    public String getSimpleName() {
        return String.format(Locale.ROOT, "%d - %d (%d times)",
                focusTime, breakTime, roundsCount);
    }

    public Integer getRoundsCount() {
        return roundsCount;
    }

    public void setRoundsCount(Integer roundsCount) {
        this.roundsCount = roundsCount;
    }

    public Integer getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(Integer focusTime) {
        this.focusTime = focusTime;
    }

    public Integer getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(Integer breakTime) {
        this.breakTime = breakTime;
    }

    public Integer getBigBreakFrequency() {
        return bigBreakFrequency;
    }

    public void setBigBreakFrequency(Integer bigBreakFrequency) {
        this.bigBreakFrequency = bigBreakFrequency;
    }

    public Integer getBigBreakTime() {
        return bigBreakTime;
    }

    public void setBigBreakTime(Integer bigBreakTime) {
        this.bigBreakTime = bigBreakTime;
    }
}
