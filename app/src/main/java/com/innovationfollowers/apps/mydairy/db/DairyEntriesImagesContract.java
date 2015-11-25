/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.innovationfollowers.apps.mydairy.db;

import android.provider.BaseColumns;

/**
 * Created by Naresh on 21-11-2015.
 */
public final class DairyEntriesImagesContract {

     public void DairyEntriesContract()
     {

     }

    public static abstract class DairyEntriesImages implements BaseColumns {
        public static final String TABLE_NAME = "DairyEntriesImages";
        public static final String COLUMN_NAME_IMAGE_ID = "imageid";
        public static final String COLUMN_NAME_IMAGE_DATA = "imagedata";

    }
}
