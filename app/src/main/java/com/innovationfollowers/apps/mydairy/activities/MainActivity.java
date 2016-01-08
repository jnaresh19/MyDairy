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

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.innovationfollowers.apps.mydairy.R;
import com.innovationfollowers.apps.mydairy.adapters.DairyEntryAdapter;
import com.innovationfollowers.apps.mydairy.dao.DairyEntryDao;
import com.innovationfollowers.apps.mydairy.db.DairyEntriesContract;
import com.innovationfollowers.apps.mydairy.db.DairyEntriesDbHelper;
import com.innovationfollowers.apps.mydairy.model.DairyEntry;

import java.io.File;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, AddDairyEntryActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //if images folder doesn't exist , create it.
        File applicationDir = new File(this.getFilesDir().toString());
        File picturesDir = new File(applicationDir + "/Pictures");

        if (!picturesDir.exists())
        {
            picturesDir.mkdir();
        }

        displayDairyEntires();
    }

    private void displayDairyEntires()
    {
        // get the list view
        final ListView listView = (ListView) findViewById(R.id.dairyEntriesListView);


        DairyEntryDao dairyEntryDao = new DairyEntryDao(getBaseContext());
        List<DairyEntry> dairyEntries = dairyEntryDao.getAllDairyEntries();
        DairyEntryAdapter adapter = new DairyEntryAdapter(this, R.layout.list_view_dairy_entries, dairyEntries);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos,
                                    long arg3)
            {

                DairyEntry dairyEntry = (DairyEntry) (arg0.getAdapter().getItem(pos));

                Intent intent = new Intent(MainActivity.this, EditDairyEntryActivity.class);
                intent.putExtra("entryId", dairyEntry.getId());
                intent.putExtra("entryTitle", dairyEntry.getTitle());
                intent.putExtra("entryDesc", dairyEntry.getDescription());
                intent.putExtra("entryDate", dairyEntry.getDate());
                intent.putExtra("entryImagePaths", dairyEntry.getImagePaths());
                startActivity(intent);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id)
            {
                // TODO Auto-generated method stub
                DairyEntry dairyEntry = (DairyEntry) (arg0.getAdapter().getItem(pos));

                showEditDeleteDialog(dairyEntry,listView);


                return true;
            }
        });

    }

    private void showEditDeleteDialog(final DairyEntry dairyEntry,final ListView listView)
    {
        String names[] = {"Edit", "Delete"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.edit_delete_list_view, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Choose Action");
        ListView lv = (ListView) convertView.findViewById(R.id.edit_delete_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        lv.setAdapter(adapter);
        final AlertDialog ad = alertDialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos,
                                    long arg3)
            {
                if(pos == 1)
                {
                    //delete
                    DairyEntryDao dairyEntryDao = new DairyEntryDao(getBaseContext());
                    int rows = dairyEntryDao.deleteDairyEntry(dairyEntry.getId());
                    if (rows == 1)
                    {
                        DairyEntryAdapter adapter = (DairyEntryAdapter) listView.getAdapter();
                        adapter.remove(dairyEntry);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "entry deleted successfully", Toast.LENGTH_SHORT).show();
                        ad.dismiss();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Failed to delete entry", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        } else if (id == R.id.nav_gallery)
        {

        } else if (id == R.id.nav_slideshow)
        {

        } else if (id == R.id.nav_manage)
        {

        } else if (id == R.id.nav_share)
        {

        } else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
