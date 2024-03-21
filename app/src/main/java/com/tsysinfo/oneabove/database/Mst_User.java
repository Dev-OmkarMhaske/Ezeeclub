package com.tsysinfo.oneabove.database;

public class Mst_User {

	public static final String DATABASE_TABLE = "mst_user";
	public static final String KEY_ID = "id";
	public static final String KEY_MEMBER_NO = "memberno";
	public static final String KEY_NAME = "name";
	public static final String KEY_GENDER = "gender";
	public static final String KEY_BDAY = "bday";
	public static final String KEY_STATUS = "status";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_MEMBER_NO_BR = "mnobr";
	public static final String KEY_Mobile = "mobile";
	public static final String KEY_Password = "role";
	public static final String KEY_BRANCH = "br";

	static final String CREATE_TABLE = " CREATE TABLE " + DATABASE_TABLE + "("
			+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT ,"
			+ KEY_MEMBER_NO+ " TEXT," + KEY_GENDER + " TEXT," + KEY_BDAY + " TEXT,"
			+ KEY_ADDRESS + " TEXT," + KEY_EMAIL+ " TEXT," + KEY_MEMBER_NO_BR+ " TEXT,"
			+ KEY_Mobile + " TEXT," + KEY_Password + " TEXT," + KEY_BRANCH + " TEXT,"
			+ KEY_STATUS + " TEXT);";
}
