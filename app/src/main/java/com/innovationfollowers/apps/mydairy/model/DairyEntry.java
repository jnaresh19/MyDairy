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
package com.innovationfollowers.apps.mydairy.model;

import java.util.Date;

/**
 * Created by Naresh on 29-11-2015.
 */
public class DairyEntry
{
    private long id;

    private String title;

    private String description;

    //TODO : convert to java.util.dates
    private String date;

    private String[] imagePaths;

    public String[] getImagePaths()
    {
        return imagePaths;
    }

    public void setImagePaths(String[] imagePaths)
    {
        this.imagePaths = imagePaths;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DairyEntry that = (DairyEntry) o;

        return id == that.id;

    }

    @Override
    public int hashCode()
    {
        return (int) (id ^ (id >>> 32));
    }
}
