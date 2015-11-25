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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naresh on 21-11-2015.
 */
public class DairyEntriesImagesDbHelper extends SQLiteOpenHelper {

    public static final  String DATABASE_NAME = "DairyEntriesImages.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_QUERY =
            "CREATE TABLE "+
                    DairyEntriesImagesContract.DairyEntriesImages.TABLE_NAME + "(" +
                    DairyEntriesImagesContract.DairyEntriesImages._ID + " INTEGER PRIMARY KEY ASC," +
                    DairyEntriesImagesContract.DairyEntriesImages.COLUMN_NAME_IMAGE_ID + " INTEGER," +
                    DairyEntriesImagesContract.DairyEntriesImages.COLUMN_NAME_IMAGE_DATA + " BLOB," +
                    "FOREIGN KEY("+DairyEntriesImagesContract.DairyEntriesImages.COLUMN_NAME_IMAGE_ID+") " +
                    "REFERENCES "+DairyEntriesContract.DairyEntries.TABLE_NAME + "("+
                    DairyEntriesContract.DairyEntries._ID+"))";

    public static final String DATABASE_OPERATIONS = "DATABASE OPERATIONS";


    public DairyEntriesImagesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(DATABASE_OPERATIONS,"Db " + DATABASE_NAME + " created successfully..");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.i(DATABASE_OPERATIONS, "Db " + DairyEntriesImagesContract.DairyEntriesImages.TABLE_NAME + " created successfully..");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean insertDairyEntryImages(long imageId,String[] imagePaths,SQLiteDatabase db)
    {
        for (int i = 0; i < imagePaths.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DairyEntriesImagesContract.DairyEntriesImages.COLUMN_NAME_IMAGE_ID, imageId);
            byte[] imageData = getImageData(imagePaths[i]);
            if(null == imageData)
            {
                //error while inserting images into db.
                //TODO insertion should be done under transaction
                return false;
            }
            contentValues.put(DairyEntriesImagesContract.DairyEntriesImages.COLUMN_NAME_IMAGE_DATA, imageData);
            long id = db.insert(DairyEntriesImagesContract.DairyEntriesImages.TABLE_NAME, null, contentValues);
        }

        Log.i(DATABASE_OPERATIONS, "dairy image entry inserted successfully..");
        return true;
    }

    private byte[] getImageData(String imagePath) {

        try {
            FileInputStream in = new FileInputStream(new File(imagePath));

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int r = in.read(buffer);
                if (r == -1) break;
                out.write(buffer, 0, r);
            }

            byte[] ret = out.toByteArray();
            return ret;

        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }

    public List<byte[]> getAllImagesOfDairyEntry(long imageId,SQLiteDatabase db)
    {
        List<byte[]> images = new ArrayList<>(2);

        String[] projection = {
                DairyEntriesImagesContract.DairyEntriesImages._ID,
                DairyEntriesImagesContract.DairyEntriesImages.COLUMN_NAME_IMAGE_DATA,
        };


        Cursor c = db.query(
                DairyEntriesImagesContract.DairyEntriesImages.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                DairyEntriesImagesContract.DairyEntriesImages.COLUMN_NAME_IMAGE_ID + "=?",                                // The columns for the WHERE clause
                new String[]{String.valueOf(imageId)},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        int blobColumnIndex = c.getColumnIndex(DairyEntriesImagesContract.DairyEntriesImages.COLUMN_NAME_IMAGE_DATA);
        c.moveToFirst();
        do {
                images.add(c.getBlob(blobColumnIndex));
        } while (c.moveToNext());

        return images;
    }


}
