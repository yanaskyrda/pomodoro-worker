package com.diploma.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PomodoroAppSettings";
    private static final String TABLE_SESSIONS = "sessions";
    private static final String KEY_ID = "id";
    private static final String KEY_ROUND_COUNT = "round_count";
    private static final String FOCUS_TIME = "focus_time";
    private static final String BREAK_TIME = "break_time";
    private static final String BIG_BREAK_FREQUENCY = "big_break_frequency";
    private static final String BIG_BREAK_TIME = "big_break_time";
    private static final String DISTRACTIONS_RATE = "distractions_rate";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SESSIONS_TABLE_QUERY = "CREATE TABLE " + TABLE_SESSIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ROUND_COUNT + " INTEGER,"
                + FOCUS_TIME + " INTEGER,"
                + BREAK_TIME + " INTEGER,"
                + BIG_BREAK_FREQUENCY + " INTEGER,"
                + BIG_BREAK_TIME + " INTEGER,"
                + DISTRACTIONS_RATE + " REAL"
                + ")";
        sqLiteDatabase.execSQL(CREATE_SESSIONS_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        onCreate(sqLiteDatabase);
    }
}
