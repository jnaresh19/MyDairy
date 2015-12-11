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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.innovationfollowers.apps.mydairy.dao.DairyEntryDao;
import com.innovationfollowers.apps.mydairy.db.DairyEntriesContract;
import com.innovationfollowers.apps.mydairy.db.DairyEntriesDbHelper;
import com.innovationfollowers.apps.mydairy.model.DairyEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Naresh on 21-11-2015.
 */
public class CreateTestData extends AndroidTestCase {




    public void testInsertDummyTestData()
    {

        DairyEntryDao helper  = new DairyEntryDao(mContext);
        DairyEntry dairyEntry = new DairyEntry();

        for (int i = 1; i <=10; i++) {
            dairyEntry.setTitle("Enjoyed in Goa Enjoyed in Goa "+i);
            dairyEntry.setDescription("Awsome fun in Goa "+i );
            dairyEntry.setDate("12-10-15");
            dairyEntry.setImagePaths(new String[]{"IMG_"+i+".jpg"});
            long id = helper.insertDairyEntry(dairyEntry);
        }

        assertTrue(helper.getAllDairyEntries().size() > 0);

        //copy test images from /sdcard/Pictures to /sdcard/com.innovation..../files/Pictures

//        File srcFolder = new File("/sdcard/Pictures");
//        File applicationDir = new File(mContext.getFilesDir().toString());
//        File picturesDir = new File(applicationDir + "/Pictures/2015");
//
//        if(!picturesDir.exists())
//        {
//            picturesDir.mkdir();
//        }
//        try
//        {
//            copyFolder(srcFolder,picturesDir,mContext);
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }

    }


    public static void copyFolder(File src, File dest,Context mContext)
            throws IOException
    {

        if(src.isDirectory()){

            //if directory not exists, create it
            if(!dest.exists()){
                dest.mkdir();
                System.out.println("Directory copied from "
                        + src + "  to " + dest);
            }

            //list all the directory contents
             String files[] = src.list();

            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                //recursive copy
                copyFolder(srcFile,destFile,mContext);
            }

        }else{
            //if file, then copy it
            //Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            //OutputStream out = new FileOutputStream(dest);
            OutputStream out = mContext.openFileOutput(dest.toString(), Context.MODE_PRIVATE);;

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            System.out.println("File copied from " + src +  " to " + dest);
        }
    }



}
