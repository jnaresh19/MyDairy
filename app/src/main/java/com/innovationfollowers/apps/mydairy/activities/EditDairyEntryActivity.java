/*
 * Copyright  2016  InnovationFollowers
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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innovationfollowers.apps.mydairy.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditDairyEntryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dairy_entry);

        Intent intent = getIntent();
        long entryId = intent.getLongExtra("entryId", -1);
        String entryTitle = intent.getStringExtra("entryTitle");
        String entryDesc = intent.getStringExtra("entryDesc");
        String entryDate = intent.getStringExtra("entryDate");
        String[] imagePaths = intent.getStringArrayExtra("entryImagePaths");

        String _date = entryDate.split(" ")[0];
        String _time = entryDate.split(" ")[1];

        EditText title = (EditText) findViewById(R.id.editEntryTitle);
        EditText desc = (EditText) findViewById(R.id.editEntryDescription);
        TextView date = (TextView) findViewById(R.id.editEntryDate);
        TextView time = (TextView) findViewById(R.id.editEntryTime);
        final LinearLayout lm = (LinearLayout) findViewById(R.id.editImageLinearLayout);

        title.setText(entryTitle);
        desc.setText(entryDesc);
        date.setText(_date);
        time.setText(_time);

        for(int i=0;i<imagePaths.length;i++)
        {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(157, 105);
            imageView.setLayoutParams(lp);
            imageView.setVisibility(View.VISIBLE);
            imageView.setContentDescription(imagePaths[i]);
            lm.addView(imageView);
            String fname = new File(getBaseContext().getFilesDir() + "/Pictures/"+ imagePaths[i]).getAbsolutePath();
            Picasso.with(this).load("file:"+fname).fit().centerCrop().into(imageView);
        }

    }
}
