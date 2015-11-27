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

import java.util.List;

/**
 * Created by Naresh on 21-11-2015.
 */
public class CreateTestData extends AndroidTestCase {




    public void testInsertDummyTestData()
    {
        DairyEntriesDbHelper helper = new DairyEntriesDbHelper(mContext);
        SQLiteDatabase database = helper.getWritableDatabase();


        for (int i = 0; i < 50; i++) {
            long id = helper.insertDairyEntry("Diwali"+i, "Enjoyed Diwali"+i, "12-11-15", database);
        }

        assertTrue(helper.getAllDairyEntries(database).getCount() > 49);
    }



}
