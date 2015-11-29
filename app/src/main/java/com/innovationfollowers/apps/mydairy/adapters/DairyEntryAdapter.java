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

package com.innovationfollowers.apps.mydairy.adapters;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.innovationfollowers.apps.mydairy.R;
import com.innovationfollowers.apps.mydairy.model.DairyEntry;
import com.innovationfollowers.apps.mydairy.util.AsyncDrawable;
import com.innovationfollowers.apps.mydairy.util.BitmapWorkerTask;

public class DairyEntryAdapter extends ArrayAdapter<DairyEntry>
{
    private Context context;
    private int layoutResourceId;
    private List<DairyEntry> data = new ArrayList<DairyEntry>();

    public DairyEntryAdapter(Context context, int layoutResourceId, List<DairyEntry> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        DairyEntryViewHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DairyEntryViewHolder();
            holder.title = (TextView) row.findViewById(R.id.dairyEntryTitleText);
            holder.desc = (TextView) row.findViewById(R.id.dairyEntryDescText);
            holder.date = (TextView) row.findViewById(R.id.dairyEntryDateText);
            holder.image = (ImageView) row.findViewById(R.id.dairyEntryImage);
            row.setTag(holder);
        } else
        {
            holder = (DairyEntryViewHolder) row.getTag();
        }

        DairyEntry dairyEntry = data.get(position);
        holder.title.setText(dairyEntry.getTitle());
        holder.desc.setText(dairyEntry.getDescription());
        holder.date.setText(dairyEntry.getDate());
        //convert byte to bitmap take from contact class

        String[] imagePaths = dairyEntry.getImagePaths();
        if (imagePaths.length > 0)
        {
            String imageLocation = imagePaths[0];
            String fname = new File(context.getFilesDir() + "/Pictures", imageLocation).getAbsolutePath();
            //Bitmap theImage = BitmapFactory.decodeFile(fname);
            // Bitmap theImage = decodeFile(new File(context.getFilesDir() + "/Pictures", imageLocation),157,105);
            //holder.image.setImageBitmap(theImage);
            loadBitMap(holder.image, fname);
        }

        return row;

    }

    private void loadBitMap(ImageView imageView, String imageFilePath)
    {
        if (cancelPotentialWork(imageFilePath, imageView)) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView,157,105);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(context.getResources(),null,task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(imageFilePath);
        }
    }

    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.getData();
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == null  || !bitmapData.equals(data)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    static class DairyEntryViewHolder
    {
        ImageView image;
        TextView title;
        TextView desc;
        TextView date;
    }
}
