package com.ecosort.ecosortkiddo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecosort.ecosortkiddo.MyDBHelper;
import com.ecosort.ecosortkiddo.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileDao {
    private MyDBHelper dbHelper;

    public ProfileDao(Context context) {
        dbHelper = new MyDBHelper(context);
    }

    // Insert a new profile
    public long insertProfile(Profile profile) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", profile.getName());
        values.put("avatar_id", profile.getAvatarId());

        return db.insert("profile", null, values);
    }

    // Retrieve a single profile by profileId
    public Profile getProfile(int profileId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "profile",
                new String[]{"profile_id", "name", "avatar_id", "achievement", "tutorial_done_location_1", "tutorial_done_location_2", "tutorial_done_location_3", "tutorial_done_location_4, is_location_completed_house, is_location_completed_backyard, is_location_completed_forest, is_location_completed_beach"},
                "profile_id = ?",
                new String[]{String.valueOf(profileId)},
                null,
                null,
                null,
                null
        );

        Profile profile = null;
        if (cursor != null && cursor.moveToFirst()) {
            profile = new Profile(
                    cursor.getInt(cursor.getColumnIndexOrThrow("profile_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("avatar_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("achievement")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_1")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_2")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_3")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_4")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("is_location_completed_house")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("is_location_completed_backyard")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("is_location_completed_forest")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("is_location_completed_beach"))
            );
            cursor.close();
        }
        return profile;
    }

    public int getTotalStarsByLocationId(int locationId) {
        int totalStars = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT SUM(stars) FROM star_rating WHERE location_id = ?", new String[]{String.valueOf(locationId)});

            if (cursor.moveToFirst()) {
                totalStars = cursor.getInt(0); // Get the result from the first column of the first row
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        } finally {
            if (cursor != null) {
                cursor.close(); // Close the cursor to avoid memory leaks
            }
            if (db != null) {
                db.close(); // Close the database to free resources
            }
        }

        return totalStars;
    }


    // Retrieve all profiles
    public List<Profile> getAllProfiles() {
        List<Profile> profiles = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM profile", null);

        // Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                Profile profile = new Profile(
                        cursor.getInt(cursor.getColumnIndexOrThrow("profile_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("avatar_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("achievement")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_1")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_2")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_3")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("tutorial_done_location_4")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_location_completed_house")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_location_completed_backyard")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_location_completed_forest")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_location_completed_beach"))
                );
                profiles.add(profile);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return profiles;
    }

    // Update a profile
    public int updateProfile(Profile profile) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", profile.getName());
        values.put("avatar_id", profile.getAvatarId());
        values.put("tutorial_done_location_1", profile.getTutorialDoneLocation1());
        values.put("tutorial_done_location_2", profile.getTutorialDoneLocation2());
        values.put("tutorial_done_location_3", profile.getTutorialDoneLocation3());
        values.put("tutorial_done_location_4", profile.getTutorialDoneLocation4());
        values.put("is_location_completed_house", profile.getIsLocationCompletedHouse());
        values.put("is_location_completed_backyard", profile.getIsLocationCompletedBackyard());
        values.put("is_location_completed_forest", profile.getIsLocationCompletedForest());
        values.put("is_location_completed_beach", profile.getIsLocationCompletedBeach());

        // Updating row
        return db.update("profile", values, "profile_id = ?",
                new String[]{String.valueOf(profile.getProfileId())});
    }

    // Delete a profile
    public void deleteProfile(Profile profile) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("profile", "profile_id = ?",
                new String[]{String.valueOf(profile.getProfileId())});
        db.close();
    }

    // Close the database connection
    public void close() {
        dbHelper.close();
    }
}
