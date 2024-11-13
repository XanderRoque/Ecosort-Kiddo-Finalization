package com.ecosort.ecosortkiddo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ecosort.ecosortkiddo.MyDBHelper;
import com.ecosort.ecosortkiddo.model.Settings;

public class SettingsDao {
    private MyDBHelper dbHelper;

    public SettingsDao(Context context) {
        dbHelper = new MyDBHelper(context);
    }

    // Retrieve a single profile by settings
    public Settings getSettings(int profileId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(
                    "app_settings",
                    new String[]{"music", "sound", "language", "profile_id"},
                    "profile_id = ?",
                    new String[]{String.valueOf(profileId)},
                    null,
                    null,
                    null,
                    null
            );
        }catch (Exception e){
            Log.e("DB", "Error getting record: " + e.getMessage());
            // Table does not exist, create the 'settings' table
            //dbHelper.copyDatabase();
            db.execSQL(
                    "CREATE TABLE app_settings (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "music INTEGER, " +
                            "sound INTEGER, " +
                            "language TEXT, " +
                            "profile_id INTEGER)"
            );
            Log.d("DB", "Table app_settings created successfully");
            dbHelper.copyDatabase();
            // Optionally, insert default values or handle further logic
            // Raw SQL insert query
            String insertQuery = "INSERT INTO app_settings (music, sound, language, profile_id) " +
                    "VALUES (1, 1, 'en', 1);";

            try {
                db.execSQL(insertQuery);
                Log.d("DB", "New row inserted successfully");
            } catch (Exception e2) {
                Log.e("DB", "Error inserting new row: " + e2.getMessage());
            }

            cursor = db.query(
                    "app_settings",
                    new String[]{"music", "sound", "language", "profile_id"},
                    "profile_id = ?",
                    new String[]{String.valueOf(profileId)},
                    null,
                    null,
                    null,
                    null
            );

        }

        Settings settings = null;
        if (cursor != null && cursor.moveToFirst()) {
            settings = new Settings(
                    cursor.getInt(cursor.getColumnIndexOrThrow("music")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("sound")),
                    cursor.getString(cursor.getColumnIndexOrThrow("language")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("profile_id"))
            );
            cursor.close();
        }
        return settings;
    }

    // Insert a new settings entry
    public long insertSettings(Settings settings) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("music", settings.getMusic()); // Assuming it's a string
        values.put("sound", settings.getSound());
        values.put("language", settings.getLanguage());
        values.put("profile_id", settings.getProfileId());

        long id = db.insert("settings", null, values);
        db.close(); // Close the database connection
        return id;
    }

    // Update a settings entry
    public int updateSettings(Settings settings) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("music", settings.getMusic());
        values.put("sound", settings.getSound());
        values.put("language", settings.getLanguage());
        values.put("profile_id", settings.getProfileId());

        int rowsAffected = db.update("app_settings", values, "profile_id = ?", new String[]{String.valueOf(settings.getProfileId())});
        db.close(); // Close the database connection
        return rowsAffected;
    }

    // Delete a settings entry
    public void deleteGarbage(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("app_settings", "id = ?", new String[]{String.valueOf(id)});
        db.close(); // Close the database connection
    }

    // Close the database connection
    public void close() {
        dbHelper.close();
    }
}