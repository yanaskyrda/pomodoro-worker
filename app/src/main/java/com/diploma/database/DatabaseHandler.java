package com.diploma.database;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.diploma.spotify.MusicSettingEntity;
import com.diploma.timer.SessionSettingEntity;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler databaseHandlerInstance;

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

    public static DatabaseHandler getInstance(@Nullable Context context) {
        if (databaseHandlerInstance == null) {
            databaseHandlerInstance = new DatabaseHandler(context);
        }
        return databaseHandlerInstance;
    }

    private DatabaseHandler(@Nullable Context context) {
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
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ROUND_COUNT + " INTEGER,"
                + FOCUS_TIME + " INTEGER,"
                + BREAK_TIME + " INTEGER,"
                + BIG_BREAK_FREQUENCY + " INTEGER,"
                + BIG_BREAK_TIME + " INTEGER,"
                + DISTRACTIONS_RATE + " REAL"
                + ")";

        sqLiteDatabase.execSQL(CREATE_SESSIONS_TABLE_QUERY);

        insertInitialSessionsData(sqLiteDatabase);
    }

    private void insertInitialSessionsData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_SESSIONS + " (" +
                KEY_ROUND_COUNT + ", " +
                FOCUS_TIME + ", " +
                BREAK_TIME + ", " +
                BIG_BREAK_FREQUENCY + ", " +
                BIG_BREAK_TIME + ", " +
                DISTRACTIONS_RATE +
                ") VALUES(4, 25, 5, 2, 10, 0)");

        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_SESSIONS + " (" +
                KEY_ROUND_COUNT + ", " +
                FOCUS_TIME + ", " +
                BREAK_TIME + ", " +
                BIG_BREAK_FREQUENCY + ", " +
                BIG_BREAK_TIME + ", " +
                DISTRACTIONS_RATE +
                ") VALUES(4, 25, 5, 2, 10, 0)");
    }

    private void createVideosTable(SQLiteDatabase sqLiteDatabase) {
        String CREATE_VIDEOS_TABLE_QUERY = "CREATE TABLE " + TABLE_VIDEOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VIDEO_ID + " TEXT,"
                + DISTRACTIONS_RATE + " REAL"
                + ")";

        sqLiteDatabase.execSQL(CREATE_VIDEOS_TABLE_QUERY);
    }

    private void createMusicsTable(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MUSICS_TABLE_QUERY = "CREATE TABLE " + TABLE_MUSICS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
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

    public void addSessionSetting(SessionSettingEntity sessionSetting) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ROUND_COUNT, sessionSetting.getRoundsCount());
            values.put(FOCUS_TIME, sessionSetting.getFocusTime());
            values.put(BREAK_TIME, sessionSetting.getBreakTime());
            values.put(BIG_BREAK_FREQUENCY, sessionSetting.getBigBreakFrequency());
            values.put(BIG_BREAK_TIME, sessionSetting.getBigBreakTime());
            values.put(DISTRACTIONS_RATE, 0);

            db.insertOrThrow(TABLE_SESSIONS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Exception while saving session setting to db");
        } finally {
            db.endTransaction();
        }
    }

    public void addVideoSetting(String videoId) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(VIDEO_ID, videoId);
            values.put(DISTRACTIONS_RATE, 0);

            db.insertOrThrow(TABLE_VIDEOS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Exception while saving video setting to db");
        } finally {
            db.endTransaction();
        }
    }

    public void addMusicSetting(String playlistId) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(PLAYLIST_ID, playlistId);
            values.put(DISTRACTIONS_RATE, 0);

            db.insertOrThrow(TABLE_MUSICS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Exception while saving video setting to db");
        } finally {
            db.endTransaction();
        }
    }

    public int updateMusicDistractionRate(MusicSettingEntity musicSettingEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DISTRACTIONS_RATE, musicSettingEntity.getDistractionRate());

        return db.update(TABLE_MUSICS, values, PLAYLIST_ID + " = ?",
                new String[] { String.valueOf(musicSettingEntity.getPlaylistId()) });
    }

    public int updateVideoDistractionRate(com.diploma.youtube.VideoSettingEntity videoSettingEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DISTRACTIONS_RATE, videoSettingEntity.getDistractionRate());

        return db.update(TABLE_MUSICS, values, VIDEO_ID + " = ?",
                new String[] { String.valueOf(videoSettingEntity.getVideoId()) });
    }

    public int updateSessionDistractionRate(SessionSettingEntity sessionSettingEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DISTRACTIONS_RATE, sessionSettingEntity.getDistractionRate());

        return db.update(TABLE_MUSICS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sessionSettingEntity.getId()) });
    }
}
