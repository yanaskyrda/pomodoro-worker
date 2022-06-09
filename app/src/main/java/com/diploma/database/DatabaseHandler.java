package com.diploma.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PomodoroAppSettings";
    private static final String KEY_ID = "id";
    private static final String DISTRACTIONS_RATE = "distractions_rate";

    private static final String TABLE_SESSIONS = "sessions";
    private static final String KEY_ROUND_COUNT = "round_count";
    private static final String FOCUS_TIME = "focus_time";
    private static final String BREAK_TIME = "break_time";
    private static final String BIG_BREAK_FREQUENCY = "big_break_frequency";
    private static final String BIG_BREAK_TIME = "big_break_time";

    private static final String TABLE_VIDEOS = "videos";
    private static final String VIDEO_ID = "video_id";

    private static final String TABLE_MUSICS = "musics";
    private static final String PLAYLIST_ID = "playlist_id";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createSessionsTable(sqLiteDatabase);
        createVideosTable(sqLiteDatabase);
        createMusicsTable(sqLiteDatabase);
    }

    private void createSessionsTable(SQLiteDatabase sqLiteDatabase) {
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

    private void createVideosTable(SQLiteDatabase sqLiteDatabase) {
        String CREATE_VIDEOS_TABLE_QUERY = "CREATE TABLE " + TABLE_VIDEOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + VIDEO_ID + " TEXT,"
                + DISTRACTIONS_RATE + " REAL"
                + ")";

        sqLiteDatabase.execSQL(CREATE_VIDEOS_TABLE_QUERY);
    }

    private void createMusicsTable(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MUSICS_TABLE_QUERY = "CREATE TABLE " + TABLE_MUSICS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + PLAYLIST_ID + " TEXT,"
                + DISTRACTIONS_RATE + " REAL"
                + ")";

        sqLiteDatabase.execSQL(CREATE_MUSICS_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSICS);
        onCreate(sqLiteDatabase);
    }
}
