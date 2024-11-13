package com.ecosort.ecosortkiddo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ecosort.ecosortkiddo.MyDBHelper;
import com.ecosort.ecosortkiddo.model.Garbage;
import com.ecosort.ecosortkiddo.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class GarbageDao {
    private MyDBHelper dbHelper;

    public GarbageDao(Context context) {
        dbHelper = new MyDBHelper(context);
    }

//    public List<Garbage> getGarbageByLocationIdAndLevelId2(int locationId, int levelId) {
//        List<Garbage> garbageList = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String sql = "SELECT * FROM garbage";
//        Cursor cursor = db.rawQuery(sql, null);
//
//
//        // Loop through all rows and add to list
//        if (cursor.moveToFirst()) {
//            do {
//                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
//                float layoutConstraintHorizontalBias = cursor.getFloat(cursor.getColumnIndexOrThrow("layout_constraint_horizontal_bias"));
//                float layoutConstraintVerticalBias = cursor.getFloat(cursor.getColumnIndexOrThrow("layout_constraint_vertical_bias"));
//                boolean active = cursor.getInt(cursor.getColumnIndexOrThrow("active")) == 1;
//                int levelIdValue = cursor.getInt(cursor.getColumnIndexOrThrow("level_id"));
//                int locationIdValue = cursor.getInt(cursor.getColumnIndexOrThrow("location_id"));
//
//                // Create a new Garbage object and add it to the list
//                Garbage garbage = new Garbage();
//                garbage.setId(id);
//                garbage.setLayoutConstraintHorizontalBias(layoutConstraintHorizontalBias);
//                garbage.setLayoutConstraintVerticalBias(layoutConstraintVerticalBias);
//                garbage.setActive(active);
//                garbage.setLevelId(levelIdValue);
//                garbage.setLocationId(locationIdValue);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return garbageList;
//    }

    public List<Garbage> getGarbageByLocationIdAndLevelId(int locationId, int levelId) {
        List<Garbage> garbageList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "garbage",
                new String[]{"id", "layout_constraint_horizontal_bais", "layout_constraint_vertical_bais", "active", "level_id", "location_id", "garbage_category_id"},
                "location_id = ? AND level_id = ?",
                new String[]{String.valueOf(locationId), String.valueOf(levelId)},
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                Double layoutConstraintHorizontalBais = cursor.getDouble(cursor.getColumnIndexOrThrow("layout_constraint_horizontal_bais"));
                Double layoutConstraintVerticalBais = cursor.getDouble(cursor.getColumnIndexOrThrow("layout_constraint_vertical_bais"));
                boolean active = cursor.getInt(cursor.getColumnIndexOrThrow("active")) == 1;
                int levelIdValue = cursor.getInt(cursor.getColumnIndexOrThrow("level_id"));
                int locationIdValue = cursor.getInt(cursor.getColumnIndexOrThrow("location_id"));
                int garbageCategoryIdValue = cursor.getInt(cursor.getColumnIndexOrThrow("garbage_category_id"));

                // Create a new Garbage object and add it to the list
                Garbage garbage = new Garbage();
                garbage.setId(id);
                garbage.setLayoutConstraintHorizontalBias(layoutConstraintHorizontalBais);
                garbage.setLayoutConstraintVerticalBias(layoutConstraintVerticalBais);
                garbage.setActive(active);
                garbage.setLevelId(levelIdValue);
                garbage.setLocationId(locationIdValue);
                garbage.setGarbagecategoryId(garbageCategoryIdValue);
                garbageList.add(garbage);
            } while (cursor.moveToNext());

            cursor.close();
        }

        //db.close(); // Close the database connection
        return garbageList;

    }

    // Insert a new garbage entry
    public long insertGarbage(Garbage garbage) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date_created", garbage.getDateCreated()); // Assuming it's a string
        values.put("layout_constraint_horizontal_bias", garbage.getLayoutConstraintHorizontalBias());
        values.put("layout_constraint_vertical_bias", garbage.getLayoutConstraintVerticalBias());
        values.put("active", garbage.isActive() ? 1 : 0); // Convert boolean to int
        values.put("level_id", garbage.getLevelId());
        values.put("garbage_category_id", garbage.getGarbagecategoryId());

        long id = db.insert("garbage", null, values);
        db.close(); // Close the database connection
        return id;
    }

    // Update a garbage entry
    public int updateGarbage(Garbage garbage) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date_created", garbage.getDateCreated());
        values.put("layout_constraint_horizontal_bias", garbage.getLayoutConstraintHorizontalBias());
        values.put("layout_constraint_vertical_bias", garbage.getLayoutConstraintVerticalBias());
        values.put("active", garbage.isActive() ? 1 : 0);
        values.put("level_id", garbage.getLevelId());
        values.put("garbage_category_id", garbage.getGarbagecategoryId());

        int rowsAffected = db.update("garbage", values, "id = ?", new String[]{String.valueOf(garbage.getId())});
        db.close(); // Close the database connection
        return rowsAffected;
    }

    // Delete a garbage entry
    public void deleteGarbage(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("garbage", "id = ?", new String[]{String.valueOf(id)});
        db.close(); // Close the database connection
    }

    // Close the database connection
    public void close() {
        dbHelper.close();
    }
}