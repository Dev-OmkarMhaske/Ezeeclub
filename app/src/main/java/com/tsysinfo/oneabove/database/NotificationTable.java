package com.tsysinfo.oneabove.database;

/**
 * Created by administrator on 24/6/16.
 */
public class NotificationTable {

    public static final String DATABASE_TABLE = "notifi";
    public static final String KEY_KEYID = "id";
    public static final String KEY_Noti= "noti";
    public static final String KEY_STATUS = "status";
    public static final String KEY_Date = "date";


    static final String CREATE_TABLE = " CREATE TABLE " + DATABASE_TABLE + "("+KEY_KEYID+ " INTEGER primary key autoincrement ,"
            + KEY_Noti + " TEXT NOT NULL," + KEY_STATUS
            + " TEXT NOT NULL," + KEY_Date
            + " TEXT NOT NULL);";

}
