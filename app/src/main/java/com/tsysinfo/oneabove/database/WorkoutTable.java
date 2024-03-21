package com.tsysinfo.oneabove.database;

public class WorkoutTable {

    public static final String DATABASE_TABLE = "Workout";
    public static final String KEY_KEYID = "id";
    public static final String KEY_TIME = "time";
    public static final String KEY_MSG_TEXT = "msgtext";
    public static final String KEY_REPS = "reps";
    public static final String KEY_STATUS = "status";
    public static final String KEY_DIETETIAN = "dietetian";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_EXERCISE = "exercise";
    public static final String KEY_AREA = "area";
    public static final String KEY_BRANCH = "branch";
    public static final String KEY_COOLDOWN = "cooldown";
    public static final String KEY_INSRUCTOR = "instructor";
    public static final String KEY_INSRUCTOR_NO = "instructor_no";
    public static final String KEY_MEMBER_NO = "member_no";
    public static final String KEY_PLAN_END = "plan_end";
    public static final String KEY_PLAN_SELECTION_NO = "plan_selection_no";
    public static final String KEY_PLAN_START = "plan_start";
    public static final String KEY_WARM_UP = "warm_up";
    public static final String KEY_WORKOUT_FROM = "workout_from";
    public static final String KEY_WORKOUT_NO = "workout_no";
    public static final String KEY_WORKOUT_TO = "workout_to";



    static final String CREATE_TABLE = " CREATE TABLE " + DATABASE_TABLE + "("+KEY_KEYID+ " INTEGER PRIMARY KEY Autoincrement ,"
            + KEY_TIME + " TEXT NOT NULL," + KEY_MSG_TEXT + " TEXT," + KEY_STATUS + " TEXT," + KEY_DIETETIAN + " TEXT," + KEY_USER_TYPE+ " TEXT," +KEY_AREA+ " TEXT,"+KEY_BRANCH+ " TEXT,"+KEY_COOLDOWN+ " TEXT,"
            +KEY_INSRUCTOR+ " TEXT,"+KEY_INSRUCTOR_NO+"  TEXT,"+KEY_MEMBER_NO+ " TEXT,"+KEY_PLAN_END+ " TEXT,"+KEY_PLAN_SELECTION_NO+"  TEXT,"+KEY_PLAN_START+"  TEXT,"+KEY_WARM_UP+" TEXT,"+KEY_WORKOUT_FROM+" TEXT,"+KEY_WORKOUT_NO+" TEXT,"+KEY_WORKOUT_TO+" TEXT,"+KEY_REPS+" TEXT,"+KEY_EXERCISE+" TEXT);";
}

