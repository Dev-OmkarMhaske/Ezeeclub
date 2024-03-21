package com.tsysinfo.oneabove.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtil {
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void clearSharedPreference(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.apply();
    }

    public static String getLoginUrl(Context ctx) {
        return getSharedPreferences(ctx).getString("URL-LOGIN", "http://oneabovefit.ezeeclub.net/MobileAppService.svc/");
    }

    public static void setLoginURL(Context ctx, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("URL-LOGIN", value);
        editor.commit();
    }
}