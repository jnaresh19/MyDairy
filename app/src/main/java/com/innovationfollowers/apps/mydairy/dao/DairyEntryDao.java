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

package com.innovationfollowers.apps.mydairy.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.innovationfollowers.apps.mydairy.db.DairyEntriesContract;
import com.innovationfollowers.apps.mydairy.db.DairyEntriesDbHelper;
import com.innovationfollowers.apps.mydairy.model.DairyEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Naresh on 29-11-2015.
 */
public class DairyEntryDao
{
    private SQLiteDatabase database;

    private DairyEntriesDbHelper dbHelper;

    public DairyEntryDao(Context context)
    {
        dbHelper = new DairyEntriesDbHelper(context);
        open();
    }

    public long insertDairyEntry(DairyEntry dairyEntry)
    {
       return dbHelper.insertDairyEntry(dairyEntry,database);
    }

    public List<DairyEntry> getAllDairyEntries()
    {

        Cursor cursor = dbHelper.getAllDairyEntries(database);

        if(null ==cursor || cursor.getCount() == 0)
        {
            return new ArrayList<>(0);
        }


        int idColumnIndex = cursor.getColumnIndex(DairyEntriesContract.DairyEntries._ID);
        int titleColumnIndex = cursor.getColumnIndex(DairyEntriesContract.DairyEntries.COLUMN_NAME_TITLE);
        int descColumnIndex = cursor.getColumnIndex(DairyEntriesContract.DairyEntries.COLUMN_NAME_DESCRIPTION);
        int dateColumnIndex = cursor.getColumnIndex(DairyEntriesContract.DairyEntries.COLUMN_NAME_DATE);
        int imagesColumnIndex = cursor.getColumnIndex(DairyEntriesContract.DairyEntries.COLUMN_NAME_IMAGEPATHS);

        List<DairyEntry> dairyEntries = new ArrayList<>(4);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            int dbId = cursor.getInt(idColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String desc = cursor.getString(descColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String imagePaths = cursor.getString(imagesColumnIndex);
            DairyEntry dairyEntry = new DairyEntry();
            dairyEntry.setId(dbId);
            dairyEntry.setTitle(title);
            dairyEntry.setDescription(desc);
            dairyEntry.setDate(date);
            dairyEntry.setImagePaths(imagePaths.split(","));
            dairyEntries.add(dairyEntry);
            cursor.moveToNext();
        }
        cursor.close();
        return dairyEntries;
    }




    public void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }


}
