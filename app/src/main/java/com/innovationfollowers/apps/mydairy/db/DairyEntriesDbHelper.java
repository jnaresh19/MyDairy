/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.innovationfollowers.apps.mydairy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Naresh on 21-11-2015.
 */
public class DairyEntriesDbHelper extends SQLiteOpenHelper {

    public static final  String DATABASE_NAME = "DairyEntries.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_QUERY =
            "CREATE TABLE "+
                    DairyEntriesContract.DairyEntries.TABLE_NAME + "(" +
                    DairyEntriesContract.DairyEntries._ID + " INTEGER PRIMARY KEY ASC," +
                    DairyEntriesContract.DairyEntries.COLUMN_NAME_TITLE + " TEXT," +
                    DairyEntriesContract.DairyEntries.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE + " TEXT);" ;
    public static final String DATABASE_OPERATIONS = "DATABASE OPERATIONS";


    public DairyEntriesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(DATABASE_OPERATIONS,"Db " + DATABASE_NAME + " created successfully..");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.i(DATABASE_OPERATIONS, "Db " + DairyEntriesContract.DairyEntries.TABLE_NAME + " created successfully..");
    }


    public long insertDairyEntry(String title, String desc, String date,SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DairyEntriesContract.DairyEntries.COLUMN_NAME_TITLE, title);
        contentValues.put(DairyEntriesContract.DairyEntries.COLUMN_NAME_DESCRIPTION, desc);
        contentValues.put(DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE, date);
        long id = db.insert(DairyEntriesContract.DairyEntries.TABLE_NAME, null, contentValues);
        Log.i(DATABASE_OPERATIONS, "dairy entry inserted successfully..");
        return id;
    }

    public int deleteDairyEntry(long id,SQLiteDatabase db)
    {
        String selection = DairyEntriesContract.DairyEntries._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        return db.delete(DairyEntriesContract.DairyEntries.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getAllDairyEntries(SQLiteDatabase db)
    {
        String[] projection = {
                DairyEntriesContract.DairyEntries._ID,
                DairyEntriesContract.DairyEntries.COLUMN_NAME_TITLE,
                DairyEntriesContract.DairyEntries.COLUMN_NAME_DESCRIPTION,
                DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE
        };

        String sortOrder =
                DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE + " DESC";

        Cursor c = db.query(
                DairyEntriesContract.DairyEntries.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if ( c != null) {
            c.moveToFirst();
        }

        return c;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
