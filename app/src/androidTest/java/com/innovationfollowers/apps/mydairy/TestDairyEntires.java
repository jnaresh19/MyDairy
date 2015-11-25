/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.innovationfollowers.apps.mydairy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.innovationfollowers.apps.mydairy.db.DairyEntriesContract;
import com.innovationfollowers.apps.mydairy.db.DairyEntriesDbHelper;
import com.innovationfollowers.apps.mydairy.db.DairyEntriesImagesDbHelper;

import java.util.List;

/**
 * Created by Naresh on 21-11-2015.
 */
public class TestDairyEntires extends AndroidTestCase {

    private boolean isInitialized = false;

    @Override
    protected void setUp() throws Exception {

        super.setUp();
        if(!isInitialized)
        {
            //delete the previous databases if any
            mContext.deleteDatabase(DairyEntriesDbHelper.DATABASE_NAME);
            mContext.deleteDatabase(DairyEntriesImagesDbHelper.DATABASE_NAME);
        }
        isInitialized = true;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testDairyEntriesDbCreated() {
        DairyEntriesDbHelper helper = new DairyEntriesDbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        assertTrue(database.isOpen());
        database.close();
    }

    public void testInsertAndReadDairyEntry()
    {
        DairyEntriesDbHelper helper = new DairyEntriesDbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        long id = helper.insertDairyEntry("Dasara", "Enjoyed Dasara", "12-10-15", database);
        assertTrue(id != -1);

        Cursor cursor = helper.getAllDairyEntries(database);
        cursor.moveToLast();


        int idColumnIndex = cursor.getColumnIndex(DairyEntriesContract.DairyEntries._ID);
        int dbId = cursor.getInt(idColumnIndex);

        int titleColumnIndex = cursor.getColumnIndex(DairyEntriesContract.DairyEntries.COLUMN_NAME_TITLE);
        String title = cursor.getString(titleColumnIndex);

        int descColumnIndex = cursor.getColumnIndex(DairyEntriesContract.DairyEntries.COLUMN_NAME_DESCRIPTION);
        String desc = cursor.getString(descColumnIndex);

        int dateColumnIndex = cursor.getColumnIndex(DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE);
        String date = cursor.getString(dateColumnIndex);


        assertEquals(id, dbId);
        assertEquals("Dasara", title);
        assertEquals("Enjoyed Dasara", desc);
        assertEquals("12-10-15", date);


        database.close();
    }

    public void testDeleteDairyEntry()
    {
        DairyEntriesDbHelper helper = new DairyEntriesDbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        long id = helper.insertDairyEntry("Dasara", "Enjoyed Dasara", "12-10-15", database);
        assertTrue(id != -1);

        int deletedId = helper.deleteDairyEntry(id, database);
        database.close();
        assertTrue(deletedId == 1);
    }

    public void testDairyEntriesImagesDbCreated()
    {
        DairyEntriesImagesDbHelper helper = new DairyEntriesImagesDbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        assertTrue(database.isOpen());
        database.close();
    }

    public void testInsertAndReadImagesOfDairyEntry()
    {
        //first add a dairy entry
        DairyEntriesDbHelper helper = new DairyEntriesDbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();
        long id = helper.insertDairyEntry("Goa", "Enjoyed Goa Trip", "12-10-15", database);
        assertTrue(id != -1);


        // now add a image for dairy entry
        DairyEntriesImagesDbHelper imagesDbHelper = new DairyEntriesImagesDbHelper(mContext);
        database = imagesDbHelper.getWritableDatabase();
        boolean imageInsertStatus = imagesDbHelper.insertDairyEntryImages(id,new String[]{"/sdcard/Pictures/Photo note/test.jpg"},database);
        assertTrue(imageInsertStatus);

        //database = helper.getReadableDatabase();
        List<byte[]> allImagesOfDairyEntry = imagesDbHelper.getAllImagesOfDairyEntry(id, database);
        assertTrue(allImagesOfDairyEntry.size() == 1);


    }




    public void _testInsertDummyTestData()
    {
        DairyEntriesDbHelper helper = new DairyEntriesDbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();


        for (int i = 0; i < 1000; i++) {
            long id = helper.insertDairyEntry("Diwali"+i, "Enjoyed Diwali"+i, "12-11-15", database);
        }

        assertTrue(helper.getAllDairyEntries(database).getCount() > 999);
    }



}
