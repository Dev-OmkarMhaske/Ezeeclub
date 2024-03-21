package com.tsysinfo.oneabove.database;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
public class SessionManager {
    // User name (make variable public to access from outside)
    public static final String KEY_AUTH = "auth";
    public static final String KEY_Name= "name";
    public static final String KEY_BRANCH = "br";
    public static final String KEY_PASS = "pass";
    // Sharedpref file name
    private static final String PREF_NAME = "eZeeClubPref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    /**
     * Create login session
     */
    public void createLoginSession(String auth,String Name,String br,String Pass) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_AUTH, auth);
        editor.putString(KEY_Name, Name);
        editor.putString(KEY_BRANCH, br);
        editor.putString(KEY_PASS, Pass);


        // commit changes
        editor.commit();
    }
    public void UpdatePass(String Pass) {

        editor.putString(KEY_PASS, Pass);


        // commit changes
        editor.commit();
    }
    /**
     * Check login method wil check user login status If false it will redirect
     * user to login page Else won't do anything
     *
     * @return
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity

			/*
             * Intent i = new Intent(_context, LoginActivity.class); // Closing
			 * all the Activities
			 * 
			 * 
			 * i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * 
			 * // Add new Flag to start new Activity
			 * i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * 
			 * // Staring Login Activity _context.startActivity(i);
			 */
            return false;
        }
        return true;
    }
    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
    private void startActivity(Intent startMain) {
        // TODO Auto-generated method stub
    }
    /**
     * Get stored session data
     */
    public String getAuthority() {
        // user name
        String auth = pref.getString(KEY_AUTH, "");
        // String pass = pref.getString(KEY_PASSWORD, null);
        // return user
        return auth;
    }
    public String getPass() {
        // user name
        String auth = pref.getString(KEY_PASS, "");
        // String pass = pref.getString(KEY_PASSWORD, null);
        // return user
        return auth;
    }
    public String getName() {
        // user name
        String auth = pref.getString(KEY_Name, "");
        // String pass = pref.getString(KEY_PASSWORD, null);
        // return user
        return auth;
    }
    public String getBr() {
        // user name
        String auth = pref.getString(KEY_BRANCH, "");
        // String pass = pref.getString(KEY_PASSWORD, null);
        // return user
        return auth;
    }
    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
}
