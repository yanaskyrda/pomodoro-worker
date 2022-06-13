package com.diploma.database;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.diploma.spotify.MusicSettingEntity;
import com.diploma.timer.SessionSettingEntity;
import com.diploma.youtube.VideoSettingEntity;

import java.util.ArrayList;
import java.util.List;

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
                ") VALUES(2, 50, 10, 0, 0, 0)");
    }

    private void createVideosTable(SQLiteDatabase sqLiteDatabase) {
        String CREATE_VIDEOS_TABLE_QUERY = "CREATE TABLE " + TABLE_VIDEOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VIDEO_ID + " TEXT,"
                + DISTRACTIONS_RATE + " REAL"
                + ")";

        sqLiteDatabase.execSQL(CREATE_VIDEOS_TABLE_QUERY);

        insertInitialVideosData(sqLiteDatabase);
    }

    private void insertInitialVideosData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_VIDEOS + " (" +
                VIDEO_ID + ", " +
                DISTRACTIONS_RATE +
                ") VALUES('9jRGR8n0a68', 0)");
    }

    private void createMusicsTable(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MUSICS_TABLE_QUERY = "CREATE TABLE " + TABLE_MUSICS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PLAYLIST_ID + " TEXT,"
                + DISTRACTIONS_RATE + " REAL"
                + ")";

        sqLiteDatabase.execSQL(CREATE_MUSICS_TABLE_QUERY);

        insertInitialMusicsData(sqLiteDatabase);
    }

    private void insertInitialMusicsData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_MUSICS + " (" +
                PLAYLIST_ID + ", " +
                DISTRACTIONS_RATE +
                ") VALUES('37i9dQZF1DX9sIqqvKsjG8', 0)");
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
                new String[]{String.valueOf(musicSettingEntity.getPlaylistId())});
    }

    public int updateVideoDistractionRate(VideoSettingEntity videoSettingEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DISTRACTIONS_RATE, videoSettingEntity.getDistractionRate());

        return db.update(TABLE_VIDEOS, values, VIDEO_ID + " = ?",
                new String[]{String.valueOf(videoSettingEntity.getVideoId())});
    }

    public int updateSessionDistractionRate(SessionSettingEntity sessionSettingEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DISTRACTIONS_RATE, sessionSettingEntity.getDistractionRate());

        return db.update(TABLE_MUSICS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(sessionSettingEntity.getId())});
    }

    public List<SessionSettingEntity> getAllSessionSettings() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<SessionSettingEntity> result = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_SESSIONS +
                " ORDER BY " + DISTRACTIONS_RATE, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    SessionSettingEntity sessionSetting = new SessionSettingEntity(
                            cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getInt(cursor.getColumnIndex(KEY_ROUND_COUNT)),
                            cursor.getInt(cursor.getColumnIndex(FOCUS_TIME)),
                            cursor.getInt(cursor.getColumnIndex(BREAK_TIME)),
                            cursor.getInt(cursor.getColumnIndex(BIG_BREAK_FREQUENCY)),
                            cursor.getInt(cursor.getColumnIndex(BIG_BREAK_TIME)),
                            cursor.getFloat(cursor.getColumnIndex(DISTRACTIONS_RATE))
                    );
                    result.add(sessionSetting);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception while trying to get session settings from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public List<MusicSettingEntity> getAllMusicsSettings() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<MusicSettingEntity> result = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_MUSICS +
                " ORDER BY " + DISTRACTIONS_RATE, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    MusicSettingEntity musicSetting = new MusicSettingEntity(
                            cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(PLAYLIST_ID)),
                            cursor.getFloat(cursor.getColumnIndex(DISTRACTIONS_RATE))
                    );
                    result.add(musicSetting);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception while trying to get musics settings from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public List<VideoSettingEntity> getAllVideosSettings() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<VideoSettingEntity> result = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_VIDEOS +
                " ORDER BY " + DISTRACTIONS_RATE, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    VideoSettingEntity videoSetting = new VideoSettingEntity(
                            cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                            cursor.getString(cursor.getColumnIndex(VIDEO_ID)),
                            cursor.getFloat(cursor.getColumnIndex(DISTRACTIONS_RATE))
                    );
                    result.add(videoSetting);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception while trying to get videos settings from db");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public int deleteSessionSetting(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_SESSIONS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public int deleteVideoSetting(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_VIDEOS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public int deleteMusicsSetting(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_MUSICS, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
}
