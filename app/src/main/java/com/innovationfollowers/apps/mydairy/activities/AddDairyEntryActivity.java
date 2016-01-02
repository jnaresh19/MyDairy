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

package com.innovationfollowers.apps.mydairy.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.innovationfollowers.apps.mydairy.R;
import com.innovationfollowers.apps.mydairy.dao.DairyEntryDao;
import com.innovationfollowers.apps.mydairy.model.DairyEntry;
import com.innovationfollowers.apps.mydairy.util.MonthFormatter;
import com.innovationfollowers.apps.mydairy.util.Utilities;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class AddDairyEntryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dairy_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        //by default set date and time to current date & time
        TextView date = (TextView) findViewById(R.id.addEntryDate);
        TextView time = (TextView) findViewById(R.id.addEntryTime);

        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int day = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH);

        int hr = today.get(Calendar.HOUR);
        int min = today.get(Calendar.MINUTE);


        date.setText(day + "-" + MonthFormatter.getMonthInText(month) + "-" + year);
        time.setText(hr + ":" + min);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText title = (EditText) findViewById(R.id.addEntryTitle);
                EditText desc = (EditText) findViewById(R.id.addEntryDescription);
                TextView date = (TextView) findViewById(R.id.addEntryDate);
                TextView time = (TextView) findViewById(R.id.addEntryTime);
                ImageView image = (ImageView) findViewById(R.id.addEntryImage);
                String imagePath = image.getContentDescription().toString();
                DairyEntry entry = new DairyEntry();
                entry.setTitle(title.getText().toString());
                entry.setDescription(desc.getText().toString());
                String selectedDate = date.getText().toString();
                String year = getSelectedYear(selectedDate);
                entry.setDate(selectedDate + " " + time.getText().toString());

                //check if the folder name with year exists
                final Context baseContext = getBaseContext();
                File applicationDir = new File(baseContext.getFilesDir().toString());
                File yearDir = new File(applicationDir + "/Pictures/" + year);

                if (!yearDir.exists())
                {
                    yearDir.mkdir();
                }

                //generate a new name for image
                long currentTime = Calendar.getInstance().getTimeInMillis();
                String imageName = currentTime + ".jpg";
                //copy the image to target dir

                File srcFile = new File(imagePath);
                File picturesDir = new File(yearDir.getAbsolutePath() + "/" + imageName);
                try
                {
                    Utilities.copyFolder(srcFile,picturesDir,baseContext);

                } catch (IOException e)
                {
                    e.printStackTrace();
                }

                entry.setImagePaths(new String[]{year+"/"+imageName});

                DairyEntryDao dao = new DairyEntryDao(baseContext);
                long id = dao.insertDairyEntry(entry);
                dao.close();

                if (id > 0)
                {
                    CharSequence text = "Entry Added Successfully";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(baseContext, text, duration);
                    toast.show();
                } else
                {
                    CharSequence text = "Entry Addition failed";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(baseContext, text, duration);
                    toast.show();
                }

                Intent intent = new Intent(AddDairyEntryActivity.this, MainActivity.class);
                startActivity(intent);


            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String getSelectedYear(String selectedDate)
    {
        String contents[] = selectedDate.split("-");
        return contents[2];
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_dairy_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.entryImage)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            // String picturePath contains the path of selected Image

            // Show the Selected Image on ImageView
            ImageView imageView = (ImageView) findViewById(R.id.addEntryImage);
            imageView.setContentDescription(picturePath);

//            BitmapWorkerTask task = new BitmapWorkerTask(imageView, 157, 105);
//            final AsyncDrawable asyncDrawable =
//                    new AsyncDrawable(getBaseContext().getResources(), null, task);
//            imageView.setImageDrawable(asyncDrawable);
//            task.execute(picturePath);

            Picasso.with(this).load("file:"+picturePath).fit().centerCrop().into(imageView);


        }
    }

    public void onDateSet(View view)
    {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                TextView date = (TextView) findViewById(R.id.addEntryDate);
                date.setText(dayOfMonth + "-" + MonthFormatter.getMonthInText(monthOfYear) + "-" + year);

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dpd.show();
    }

    public void onTimeSet(View view)
    {
        Calendar c = Calendar.getInstance();
        TimePickerDialog dpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()
        {

            @Override
            public void onTimeSet(TimePicker view, int hour,
                                  int min)
            {
                TextView time = (TextView) findViewById(R.id.addEntryTime);
                time.setText(hour + ":" + min);

            }
        }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);

        dpd.show();
    }

}
