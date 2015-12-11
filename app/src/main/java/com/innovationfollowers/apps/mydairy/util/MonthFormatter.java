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

package com.innovationfollowers.apps.mydairy.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Naresh on 11-12-2015.
 */
public class MonthFormatter
{
    private static final Map<Integer,String> MONTH_NUMBER_TO_STRING = new HashMap<>(12);
    private static final Map<String,Integer> MONTH_STRING_TO_NUMBER = new HashMap<>(12);

    static
    {
        MONTH_NUMBER_TO_STRING.put(Calendar.JANUARY,"JAN");
        MONTH_NUMBER_TO_STRING.put(Calendar.FEBRUARY,"FEB");
        MONTH_NUMBER_TO_STRING.put(Calendar.MARCH,"MAR");
        MONTH_NUMBER_TO_STRING.put(Calendar.APRIL,"APR");
        MONTH_NUMBER_TO_STRING.put(Calendar.MAY,"MAY");
        MONTH_NUMBER_TO_STRING.put(Calendar.JUNE,"JUN");
        MONTH_NUMBER_TO_STRING.put(Calendar.JULY,"JUL");
        MONTH_NUMBER_TO_STRING.put(Calendar.AUGUST,"AUG");
        MONTH_NUMBER_TO_STRING.put(Calendar.SEPTEMBER,"SEP");
        MONTH_NUMBER_TO_STRING.put(Calendar.OCTOBER,"OCT");
        MONTH_NUMBER_TO_STRING.put(Calendar.NOVEMBER,"NOV");
        MONTH_NUMBER_TO_STRING.put(Calendar.DECEMBER,"DEC");

        MONTH_STRING_TO_NUMBER.put("JAN",Calendar.JANUARY);
        MONTH_STRING_TO_NUMBER.put("FEB",Calendar.FEBRUARY);
        MONTH_STRING_TO_NUMBER.put("MAR",Calendar.MARCH);
        MONTH_STRING_TO_NUMBER.put("APR",Calendar.APRIL);
        MONTH_STRING_TO_NUMBER.put("MAY",Calendar.MAY);
        MONTH_STRING_TO_NUMBER.put("JUN",Calendar.JUNE);
        MONTH_STRING_TO_NUMBER.put("JUL",Calendar.JULY);
        MONTH_STRING_TO_NUMBER.put("AUG",Calendar.AUGUST);
        MONTH_STRING_TO_NUMBER.put("SEP",Calendar.SEPTEMBER);
        MONTH_STRING_TO_NUMBER.put("OCT",Calendar.OCTOBER);
        MONTH_STRING_TO_NUMBER.put("NOV",Calendar.NOVEMBER);
        MONTH_STRING_TO_NUMBER.put("DEC",Calendar.DECEMBER);
    }


    public static String getMonthInText(int month)
    {
        return MONTH_NUMBER_TO_STRING.get(month);
    }


    public static int getMonthInNumber(String month)
    {
        return MONTH_STRING_TO_NUMBER.get(month);
    }


}
