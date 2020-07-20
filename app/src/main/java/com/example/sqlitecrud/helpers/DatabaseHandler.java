package com.example.sqlitecrud.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.sqlitecrud.models.Note;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String TAG = "app_log";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "notes_db";
    public static final String TABLE_NAME = "notes";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "created_at";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TITLE + " TEXT, " + KEY_DESCRIPTION + " TEXT, " + KEY_DATE + " INTEGER" + ")";
        Log.d(TAG, "onCreate: " + query);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    public void updateNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, note.getId());
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_DESCRIPTION, note.getDescription());
        values.put(KEY_DATE, note.getDate().getTime());
        int result = database.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});
        Log.d(TAG, "updateNote: UPDATE RESULT: " + result);
    }

    public Note getNoteById(int id) {
        Note note = new Note();
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + id;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            note.setId(Integer.parseInt(cursor.getString(0)));
            note.setTitle(cursor.getString(1));
            note.setDescription(cursor.getString(2));
            note.setDate(new Date(Long.parseLong(cursor.getString(3))));
            cursor.close();
        }
        database.close();
        return note;
    }

    public void addNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_DESCRIPTION, note.getDescription());
        values.put(KEY_DATE, note.getDate().getTime());
        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(noteId)});
        /*String query = "DELETE FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + noteId;
        database.execSQL(query);*/
        database.close();
    }

    public List<Note> getAllNote() {
        SQLiteDatabase database = this.getReadableDatabase();
        List<Note> tempList = new ArrayList<Note>();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_DATE + " DESC";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToNext()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setDescription(cursor.getString(2));
                note.setDate(new Date(Long.parseLong(cursor.getString(3))));
                //Log.d(TAG, "getAllNote: ID:" + note.getId() + " || Title: " + note.getTitle() + " || Description: " + note.getDescription() + " || Time: " + note.getDate());
                tempList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return tempList;
    }
}
