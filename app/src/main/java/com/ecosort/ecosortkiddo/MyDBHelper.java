package com.ecosort.ecosortkiddo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecosortKiddo.db";
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_PATH = "/data/data/com.ecosort.ecosortkiddo/databases/";

    private Context context;

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        if (!checkDatabase()) {
            copyDatabase();
        }

//        // Function to get all table names from SQLite database
//        public List<String> getAllTables(SQLiteDatabase db) {
//            List<String> tables = new ArrayList<>();
//
//            // SQL query to get table names
//            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    String tableName = cursor.getString(0);
//                    tables.add(tableName);
//                } while (cursor.moveToNext());
//            }
//
//            cursor.close(); // Always close the cursor to avoid memory leaks
//            return tables;
//        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // No need to create tables here since we're using an existing database
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String path = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            Log.e("MyDBHelper", "Database does not exist yet.");
            return false;
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

//

    public void copyDatabase() {
        try {

            String outFileName = DATABASE_PATH + DATABASE_NAME;

            // Clear the cache by deleting the existing database file (if any)
            File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
            if (dbFile.exists()) {
                dbFile.delete();  // Delete the old database file
                Log.d("MyDBHelper", "Old database deleted.");
            }

            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            Log.d("MyDBHelper", "Database copied successfully.");

            // Open the copied database and display tables
            SQLiteDatabase db = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READONLY);
            List<String> tableNames = getAllTables(db);

            for (String tableName : tableNames) {
                Log.d("TABLE_LIST", "Table: " + tableName);
            }

            db.close();
        } catch (IOException e) {
            Log.e("MyDBHelper", "Error copying database", e);
        }
    }

    // Function to get all table names from the copied SQLite database
    public List<String> getAllTables(SQLiteDatabase db) {
        List<String> tables = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                tables.add(tableName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return tables;
    }


    public SQLiteDatabase getReadableDB() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDB() {
        return this.getWritableDatabase();
    }
}
