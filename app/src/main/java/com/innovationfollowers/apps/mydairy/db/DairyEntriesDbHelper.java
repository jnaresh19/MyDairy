/*
 * Copyright  2015  InnovationFollowers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.innovationfollowers.apps.mydairy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.innovationfollowers.apps.mydairy.model.DairyEntry;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Naresh on 21-11-2015.
 */
public class DairyEntriesDbHelper extends SQLiteOpenHelper
{

    public static final String DATABASE_NAME = "DairyEntries.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_QUERY =
            "CREATE TABLE " +
                    DairyEntriesContract.DairyEntries.TABLE_NAME + "(" +
                    DairyEntriesContract.DairyEntries._ID + " INTEGER PRIMARY KEY," +
                    DairyEntriesContract.DairyEntries.COLUMN_NAME_TITLE + " TEXT," +
                    DairyEntriesContract.DairyEntries.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE + " TEXT," +
                    DairyEntriesContract.DairyEntries.COLUMN_NAME_IMAGEPATHS + " TEXT);";
    public static final String DATABASE_OPERATIONS = "DATABASE OPERATIONS";

    public DairyEntriesDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(DATABASE_OPERATIONS, "Db " + DATABASE_NAME + " created successfully..");
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_QUERY);
    }


    public long insertDairyEntry(DairyEntry dairyEntry, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DairyEntriesContract.DairyEntries.COLUMN_NAME_TITLE, dairyEntry.getTitle());
        contentValues.put(DairyEntriesContract.DairyEntries.COLUMN_NAME_DESCRIPTION, dairyEntry.getDescription());
        contentValues.put(DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE, dairyEntry.getDate());
        contentValues.put(DairyEntriesContract.DairyEntries.COLUMN_NAME_IMAGEPATHS, convertToCommaDelimited(dairyEntry.getImagePaths()));
        long id = db.insert(DairyEntriesContract.DairyEntries.TABLE_NAME, null, contentValues);
        Log.i(DATABASE_OPERATIONS, "dairy entry inserted successfully..");
        return id;
    }

    public int deleteDairyEntry(long id, SQLiteDatabase db)
    {
        String selection = DairyEntriesContract.DairyEntries._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return db.delete(DairyEntriesContract.DairyEntries.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getAllDairyEntries(SQLiteDatabase db)
    {
        String[] projection = {
                DairyEntriesContract.DairyEntries._ID,
                DairyEntriesContract.DairyEntries.COLUMN_NAME_TITLE,
                DairyEntriesContract.DairyEntries.COLUMN_NAME_DESCRIPTION,
                DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE,
                DairyEntriesContract.DairyEntries.COLUMN_NAME_IMAGEPATHS
        };

        String sortOrder =
                DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE + " DESC";

        Cursor c = db.query(
                DairyEntriesContract.DairyEntries.TABLE_NAME, projection, null, null, null, null, sortOrder);

        return c;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(DairyEntriesDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DairyEntriesContract.DairyEntries.TABLE_NAME);
        onCreate(db);
    }

    public static String convertToCommaDelimited(String[] list) {
        StringBuilder ret = new StringBuilder("");
        for (int i = 0; list != null && i < list.length; i++) {
            ret.append(list[i]);
            if (i < list.length - 1) {
                ret.append(',');
            }
        }
        return ret.toString();
    }
}
