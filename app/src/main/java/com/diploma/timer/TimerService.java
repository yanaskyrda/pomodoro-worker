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
        IN_PROCESS,
        PAUSED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private Button startStopSessionButton;
    private ImageButton resetButton;
    private CountDownTimer countDownTimer;
    private TextView textSessionStatus;
    private TextView textRoundProgress;

    private long timeCountInMilliSeconds = 60000;
    private final String START_SESSION = "START SESSION";
    private final String STOP_SESSION = "STOP SESSION";
    private final String RESUME_SESSION = "RESUME SESSION";

    private final String FOCUS = "FOCUS";
    private final String BREAK = "BREAK";
    private final String BIG_BREAK = "BIG BREAK";
    private final String ROUND_PROGRESS_TEMPLATE = "%d / %d";

    private final SessionsSettingsService sessionsSettingsService;
    private int currentRound = 0;
    private boolean isBreak = false;

    public void initViews(View view) {
        progressBarCircle = view.findViewById(R.id.progress_bar_circle);
        textViewTime = view.findViewById(R.id.text_session_time);
        startStopSessionButton = view.findViewById(R.id.start_session_button);
        resetButton = view.findViewById(R.id.reset_timer_button);
        textSessionStatus = view.findViewById(R.id.text_session_status);
        textRoundProgress = view.findViewById(R.id.text_round_progress);

        initListeners();
    }

    public void initListeners() {
        resetButton.setOnClickListener(v -> reset());
        startStopSessionButton.setOnClickListener(v -> startStop());
    }

    private void startStop() {
        switch (timerStatus) {
            //pause -> resume
            case PAUSED:
                setProgressBarValues();
                startStopSessionButton.setText(STOP_SESSION);
                timerStatus = TimerStatus.IN_PROCESS;
                startCountDownTimer();
                break;
            //-> new start
            case STOPPED:
                setTimerValues(sessionsSettingsService.getActiveSetting().getFocusTime());
                setProgressBarValues();
                startStopSessionButton.setText(STOP_SESSION);
                textSessionStatus.setText(FOCUS);
                timerStatus = TimerStatus.IN_PROCESS;
                currentRound++;
                startCountDownTimer();
                break;
            //process -> pause
            case IN_PROCESS:
                startStopSessionButton.setText(RESUME_SESSION);
                timerStatus = TimerStatus.PAUSED;
                countDownTimer.cancel();
                break;
        }
    }

    private void setTimerValues(int timeInMinutes) {
        timeCountInMilliSeconds = (long) timeInMinutes * 5 * 1000;
    }

    private void startCountDownTimer() {
        textRoundProgress.setText(String.format(Locale.ROOT, ROUND_PROGRESS_TEMPLATE,
                currentRound, sessionsSettingsService.getActiveSetting().getRoundsCount()));
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (currentRound == sessionsSettingsService.getActiveSetting().getRoundsCount()) {
                    startStopSessionButton.setText(START_SESSION);
                    textSessionStatus.setText("");
                    textRoundProgress.setText("");
                    timerStatus = TimerStatus.STOPPED;
                    currentRound = 0;
                    textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                    setProgressBarValues();
                    return;
                }

                if (!isBreak) {
                    if (currentRound % sessionsSettingsService.getActiveSetting().getBigBreakFrequency() == 0) {
                        textSessionStatus.setText(BIG_BREAK);
                        setTimerValues(sessionsSettingsService.getActiveSetting().getBigBreakTime());
                    } else {
                        textSessionStatus.setText(BREAK);
                        setTimerValues(sessionsSettingsService.getActiveSetting().getBreakTime());
                    }
                    isBreak = true;
                } else {
                    textSessionStatus.setText(FOCUS);
                    setTimerValues(sessionsSettingsService.getActiveSetting().getFocusTime());
                    isBreak = false;
                    currentRound++;
                    textRoundProgress.setText(String.format(Locale.ROOT, ROUND_PROGRESS_TEMPLATE,
                            currentRound, sessionsSettingsService.getActiveSetting().getRoundsCount()));
                }

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                setProgressBarValues();
                startCountDownTimer();
            }
        };
        countDownTimer.start();
    }

    private void reset() {
        if (progressBarCircle.getProgress() != progressBarCircle.getMax()) {
            progressBarCircle.setProgress(progressBarCircle.getMax());
            countDownTimer.cancel();
            textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
            startStopSessionButton.setText(START_SESSION);
            textSessionStatus.setText("");
            textRoundProgress.setText("");
            currentRound = 0;
            timerStatus = TimerStatus.STOPPED;
        }
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
        sessionsSettingsService = SessionsSettingsService.getInstance();
    }

    public static TimerService getInstance(View view) {
        if (instance == null) {
            instance = new TimerService(view);
        }
        return instance;
    }
}
