package com.tsysinfo.oneabove.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Nayan on 5/26/2016.
 */
public class VolleySingleton {

    public static int VOLLEY_TIMEOUT = 350000;

    private static VolleySingleton mInstance;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {

        requestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }
}
