package com.diploma.timer;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.diploma.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class TimerService {
    private static TimerService instance;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private Button startStopSessionButton;
    private ImageButton resetButton;
    private CountDownTimer countDownTimer;

    private long timeCountInMilliSeconds = 60000;
    private final String START_SESSION = "START SESSION";
    private final String STOP_SESSION = "STOP SESSION";

    public void initViews(View view) {
        progressBarCircle = view.findViewById(R.id.progressBarCircle);
        textViewTime = view.findViewById(R.id.textViewTime);
        startStopSessionButton = view.findViewById(R.id.startSessionButton);
        resetButton = view.findViewById(R.id.resetTimerButton);

        initListeners();
    }

    public void initListeners() {
        resetButton.setOnClickListener(v -> stopCountDownTimer());
        startStopSessionButton.setOnClickListener(v -> startStop());
    }

    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
            setTimerValues();
            setProgressBarValues();
            startStopSessionButton.setText(STOP_SESSION);
            timerStatus = TimerStatus.STARTED;
            startCountDownTimer();

        } else {
            startStopSessionButton.setText(START_SESSION);
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }

    }

    private void setTimerValues() {
        int time;
        time = Integer.parseInt("25");
        timeCountInMilliSeconds = (long) time * 60 * 1000;
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                setProgressBarValues();

                startStopSessionButton.setText(START_SESSION);
                timerStatus = TimerStatus.STOPPED;
            }
        };
        countDownTimer.start();
    }

    private void stopCountDownTimer() {
        progressBarCircle.setProgress(progressBarCircle.getMax());
        countDownTimer.cancel();
        textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
        startStopSessionButton.setText(START_SESSION);
        timerStatus = TimerStatus.STOPPED;
    }

    private void setProgressBarValues() {
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * Convert millisecond to HH:mm:ss time format
     *
     * @param milliSeconds time that should be converter
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {
        return String.format(Locale.ROOT, "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));
    }

    private TimerService(View view) {
        initViews(view);
    }

    public static TimerService getInstance(View view) {
        if (instance == null) {
            instance = new TimerService(view);
        }
        return instance;
    }
}
