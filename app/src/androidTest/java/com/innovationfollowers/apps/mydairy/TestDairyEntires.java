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

package com.innovationfollowers.apps.mydairy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.innovationfollowers.apps.mydairy.dao.DairyEntryDao;
import com.innovationfollowers.apps.mydairy.db.DairyEntriesContract;
import com.innovationfollowers.apps.mydairy.db.DairyEntriesDbHelper;
import com.innovationfollowers.apps.mydairy.model.DairyEntry;


import java.util.List;

/**
 * Created by Naresh on 21-11-2015.
 */
public class TestDairyEntires extends AndroidTestCase
{

    private boolean isInitialized = false;

    @Override
    protected void setUp() throws Exception
    {
        if (!isInitialized)
        {
            //delete the previous databases if any
            mContext.deleteDatabase(DairyEntriesDbHelper.DATABASE_NAME);
        }
        isInitialized = true;
    }


    public void testInsertAndReadDairyEntry()
    {

        DairyEntryDao dairyEntryDao = new DairyEntryDao(mContext);
        DairyEntry dairyEntry = new DairyEntry();
        dairyEntry.setTitle("Celebrated Dasara");
        dairyEntry.setDescription("Enjoyed Dasara");
        dairyEntry.setDate("12-10-15");
        dairyEntry.setImagePaths(new String[]{"/sdcard/data/data/app/images/img1.jpg",
                "/sdcard/data/data/app/images/img2.jpg"});
        long id = dairyEntryDao.insertDairyEntry(dairyEntry);
        assertTrue(id != -1);

        List<DairyEntry> allDairyEntries = dairyEntryDao.getAllDairyEntries();
        assertTrue(allDairyEntries.size() == 1);
        DairyEntry dairyEntryFromDb = allDairyEntries.get(0);
        assertEquals(id, dairyEntryFromDb.getId());
        assertEquals("Celebrated Dasara", dairyEntryFromDb.getTitle());
        assertEquals("Enjoyed Dasara", dairyEntryFromDb.getDescription());
        assertEquals("12-10-15", dairyEntryFromDb.getDate());
        assertTrue(dairyEntryFromDb.getImagePaths().length == 2);


        dairyEntryDao.close();
    }


}
