package com.diploma.timer;

import java.util.Locale;

public class SessionSetting {
    /**
     * Number of rounds of focus-time + break-time.
     */
    Integer roundsCount;
    /**
     * Time of one focus time round in minutes.
     */
    Integer focusTime;

    /**
     * Time of one break time round in minutes.
     */
    Integer breakTime;

    /**
     * How ofter big rest occurs.
     * The number mean that every n round instead of short break would be big break.
     */
    Integer bigBreakFrequency = 0;

    /**
     * Time of one big rest time round in minutes.
     */
    Integer bigBreakTime = 0;

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
}
