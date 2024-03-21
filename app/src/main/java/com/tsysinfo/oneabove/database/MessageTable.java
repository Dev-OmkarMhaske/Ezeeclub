package com.tsysinfo.oneabove.database;

public class MessageTable {

    public static final String DATABASE_TABLE = "MsgTable";
    public static final String KEY_KEYID = "id";
    public static final String KEY_TIME = "time";
    public static final String KEY_MSG_TEXT = "msgtext";
    public static final String KEY_STATUS = "status";
    public static final String KEY_DIETETIAN = "dietetian";
    public static final String KEY_USER_TYPE = "user_type";



    static final String CREATE_TABLE = " CREATE TABLE " + DATABASE_TABLE + "("+KEY_KEYID+ " INTEGER PRIMARY KEY Autoincrement ,"
            + KEY_TIME + " TEXT NOT NULL," + KEY_MSG_TEXT
            + " TEXT," + KEY_STATUS + " TEXT," + KEY_DIETETIAN + " TEXT ," + KEY_USER_TYPE+ " TEXT);";




}
