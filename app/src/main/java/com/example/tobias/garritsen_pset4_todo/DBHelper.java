package com.example.tobias.garritsen_pset4_todo;

/**
 * Created by Tobias on 8-5-2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ToDoDB.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE = "todo";

    private String KEY_TODO = "todoTitle";
    private String KEY_CHECK = "status";

    // From mprog videos
    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // From mprog videos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE +  " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CHECK + " TEXT, "  + KEY_TODO + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    // From mprog videos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // From mprog videos
    public void create(ToDoClass task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        // put task and status in database
        values.put(KEY_TODO, task.todoPub);
        values.put(KEY_CHECK, task.checkPub);
        db.insert(TABLE, null, values);
        db.close();
    }

    // From mprog videos
    public ArrayList<String> read(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id , " + KEY_TODO + " FROM " + TABLE;
        ArrayList<String> toDoList = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                toDoList.add(cursor.getString(cursor.getColumnIndex(KEY_TODO)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return toDoList;
    }

    // From mprog videos
    public void update(int id, String statusTask) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHECK, statusTask);
        db.update(TABLE, values, "_id = ? ", new String[] {String.valueOf(id)});
        db.close();
    }

    // From mprog videos
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, " _id = ? ", new String[] {String.valueOf(id)});
        db.close();
    }

    public ArrayList<HashMap<String, String>> getData(int todoVScheck){
        SQLiteDatabase db = getReadableDatabase();
        String query;
        if(todoVScheck==1)
            query = "SELECT _id , " + KEY_TODO + " FROM " + TABLE;
        else
            query = "SELECT _id , " + KEY_TODO + ", " + KEY_CHECK + " FROM " + TABLE; //check
        ArrayList<HashMap<String, String>> toDoList = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> taskData = new HashMap<>();
                if (todoVScheck==1)
                    taskData.put("id", cursor.getString(cursor.getColumnIndex("_id")));
                else
                    taskData.put("status", cursor.getString(cursor.getColumnIndex(KEY_CHECK)));
                taskData.put("todoTitle", cursor.getString(cursor.getColumnIndex(KEY_TODO)));
                toDoList.add(taskData);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return toDoList;
    }
}