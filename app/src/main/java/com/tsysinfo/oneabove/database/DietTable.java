package com.tsysinfo.oneabove.database;

/**
 * Created by administrator on 24/6/16.
 */
public class DietTable {

    public static final String DATABASE_TABLE = "diettable";
    public static final String KEY_KEYID = "id";
    public static final String KEY_TIME = "time";
    public static final String KEY_DIET = "diet";
    public static final String KEY_Date = "date";
    public static final String KEY_FROMDATE = "fromdate";
    public static final String KEY_TODATE = "todate";


    static final String CREATE_TABLE = " CREATE TABLE " + DATABASE_TABLE + "("+KEY_KEYID+ " INTEGER  ,"
            + KEY_TIME + " TEXT NOT NULL," + KEY_DIET + " TEXT NOT NULL," + KEY_Date + " TEXT NOT NULL," + KEY_FROMDATE + " TEXT NOT NULL," + KEY_TODATE + " TEXT NOT NULL);";

}
