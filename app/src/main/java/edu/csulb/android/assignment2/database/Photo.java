package edu.csulb.android.assignment2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Photo {

    public static String TABLE_NAME = "photos";

    private static final String _ID = "_id";
    public static String COLUMN_CAPTION = "caption";
    public static String COLUMN_PATH = "path";

    private static SQLiteHelper sqLiteHelper;
    private static Photo singletonInstance;

    private Photo() {
    }

    public static Photo getInstance(Context context) {
        if (singletonInstance == null) {
            singletonInstance = new Photo();
        }
        if (sqLiteHelper == null) {
            sqLiteHelper = new SQLiteHelper(context);
        }
        return singletonInstance;
    }

    public void saveNew(String caption, String path) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CAPTION, caption);
        values.put(COLUMN_PATH, path);

        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        long newRowId = db.insert(TABLE_NAME, null, values);
    }

    public void close() {
        if (sqLiteHelper != null) {
            sqLiteHelper.close();
        }
    }

    public static String getCreateQuery() {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CAPTION + " TEXT," +
                COLUMN_PATH + " TEXT);";
        return query;
    }

    public static String getDeleteTableQuery() {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        return query;
    }

    public List<String> getAll() {
        String[] projection = {_ID, COLUMN_CAPTION, COLUMN_PATH};
        String sortOrder = COLUMN_PATH + " DESC";

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, sortOrder);

        List<String> photos = new ArrayList<>();
        while (cursor.moveToNext()) {
            String caption = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAPTION));
            photos.add(caption);
            System.out.println(caption);
        }
        cursor.close();
        return photos;
    }

    public JSONObject get(String caption) throws JSONException {

        String[] projection = {_ID, COLUMN_CAPTION, COLUMN_PATH};
        String selection = COLUMN_CAPTION + " = ?";
        String[] selectionArgs = {caption};
        String sortOrder = COLUMN_PATH + " DESC";

        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        JSONObject json = new JSONObject();
        while (cursor.moveToNext()) {
            json.put("caption", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAPTION)));
            json.put("path", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATH)));
        }

        cursor.close();
        return json;
    }
}