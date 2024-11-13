package com.ecosort.ecosortkiddo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecosort.ecosortkiddo.MyDBHelper;
import com.ecosort.ecosortkiddo.model.Profile;
import com.ecosort.ecosortkiddo.model.StarRating;
import com.ecosort.ecosortkiddo.utils.DateUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StarRatingDao {

    private MyDBHelper dbHelper;

    public StarRatingDao(Context context) {
        dbHelper = new MyDBHelper(context);
    }

    public void testInsert(){

        double julianDayNumber = DateUtil.toJulianDayNumber(LocalDateTime.now());

        StarRating sr = new StarRating();
        sr.setProfileId(1);
        sr.setLevelId(3);
        sr.setStars(3);
        sr.setDateCreated(julianDayNumber);
        insertStarRating(sr);
    }
    public StarRating getStarRatingByProfileIdLocationIdAndLevelId(int profileId, int locationId, int levelId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "star_rating",
                new String[]{"star_rating_id", "date_created", "profile_id", "location_id", "level_id", "stars"},
                "profile_id = ? and location_id = ? and level_id = ?",
                new String[]{String.valueOf(profileId),  String.valueOf(locationId), String.valueOf(levelId)},
                null,
                null,
                null,
                null
        );

        StarRating sr = null;
        if (cursor != null && cursor.moveToFirst()) {
            sr = new StarRating();
            sr.setStarRatingId(cursor.getInt(cursor.getColumnIndexOrThrow("star_rating_id")));
            sr.setDateCreated(cursor.getDouble(cursor.getColumnIndexOrThrow("date_created")));
            sr.setProfileId(cursor.getInt(cursor.getColumnIndexOrThrow("profile_id")));
            sr.setLevelId(cursor.getInt(cursor.getColumnIndexOrThrow("level_id")));
            sr.setStars(cursor.getInt(cursor.getColumnIndexOrThrow("stars")));
            sr.setLocationId(cursor.getInt(cursor.getColumnIndexOrThrow("location_id")));
            cursor.close();
        }
        return sr;
    }

    public  List<StarRating>  getStarRatingByProfileIdAndLocationId(int profileId, int locationId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "star_rating",
                new String[]{"star_rating_id", "date_created", "profile_id", "level_id", "stars", "location_id"},
                "profile_id = ? and location_id = ?",
                new String[]{String.valueOf(profileId), String.valueOf(locationId)},
                null,
                null,
                "level_id",
                null
        );

        List<StarRating> starRatingList = new LinkedList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                StarRating sr = null;
                sr = new StarRating();
                sr.setStarRatingId(cursor.getInt(cursor.getColumnIndexOrThrow("star_rating_id")));
                sr.setDateCreated(cursor.getDouble(cursor.getColumnIndexOrThrow("date_created")));
                sr.setProfileId(cursor.getInt(cursor.getColumnIndexOrThrow("profile_id")));
                sr.setLevelId(cursor.getInt(cursor.getColumnIndexOrThrow("level_id")));
                sr.setStars(cursor.getInt(cursor.getColumnIndexOrThrow("stars")));
                sr.setLocationId(cursor.getInt(cursor.getColumnIndexOrThrow("location_id")));
                starRatingList.add(sr);
            }while (cursor.moveToNext());

            cursor.close();
        }

        return starRatingList;
    }

    // Insert a new star rating entry
    public long insertStarRating(StarRating starRating) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date_created", starRating.getDateCreated());
        values.put("profile_id", starRating.getProfileId());
        values.put("level_id", starRating.getLevelId());
        values.put("stars", starRating.getStars());
        values.put("location_id", starRating.getLocationId());

        long id = db.insert("star_rating", null, values);
        db.close(); // Close the database connection
        return id;
    }

    // Retrieve a single star rating entry by id
    public StarRating getStarRating(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "star_rating",
                new String[]{"star_rating_id", "date_created", "profile_id", "level_id", "stars"},
                "star_rating_id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );

        StarRating starRating = null;
        if (cursor != null && cursor.moveToFirst()) {
            starRating = new StarRating(
                    cursor.getInt(cursor.getColumnIndexOrThrow("star_rating_id")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("date_created")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("profile_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("level_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("stars")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("location_id"))
            );
            cursor.close();
        }
        db.close(); // Close the database connection
        return starRating;
    }

    // Retrieve all star rating entries
    public List<StarRating> getAllStarRatings() {
        List<StarRating> starRatingList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM star_rating", null);

        if (cursor.moveToFirst()) {
            do {
                StarRating starRating = new StarRating(
                        cursor.getInt(cursor.getColumnIndexOrThrow("star_rating_id")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("date_created")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("profile_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("level_id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("stars")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("location_id"))
                );
                starRatingList.add(starRating);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close(); // Close the database connection
        return starRatingList;
    }

    // Update a star rating entry
    public int updateStarRating(StarRating starRating) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stars", starRating.getStars());

        int rowsAffected = db.update("star_rating", values, "star_rating_id = ?", new String[]{String.valueOf(starRating.getStarRatingId())});
        db.close(); // Close the database connection
        return rowsAffected;
    }

    // Delete a star rating entry
    public void deleteStarRating(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("star_rating", "star_rating_id = ?", new String[]{String.valueOf(id)});
        db.close(); // Close the database connection
    }

    // Close the database connection
    public void close() {
        dbHelper.close();
    }

    public int getTotalStarsByProfileIdAndLocationId(int profileId, int locationId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "star_rating",
                new String[]{"stars"},  // Only fetch stars column since we are just summing stars
                "profile_id = ? and location_id = ?",
                new String[]{String.valueOf(profileId), String.valueOf(locationId)},
                null,
                null,
                null,
                null
        );

        int totalStars = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                totalStars += cursor.getInt(cursor.getColumnIndexOrThrow("stars"));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return totalStars;
    }

}