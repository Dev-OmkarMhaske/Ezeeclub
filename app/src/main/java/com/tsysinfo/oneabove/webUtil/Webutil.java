package com.tsysinfo.oneabove.webUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.tsysinfo.oneabove.volley.VolleySingleton;

import java.io.UnsupportedEncodingException;

/**
 * Created by Nayan on 26/5/16.
 */
public class Webutil {

    public static void getResponse(final Context context, String url, final String params, final Handler handler) {
        Log.d("test", "inn thread webutil...");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Message message = new Message();
                message.obj = response;
                handler.sendMessage(message);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("test", "error while getting response...." + error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return params == null ? null : params.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            params, "utf-8");
                    return null;
                }
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(stringRequest);
    }

    public static void getResponseWithoutProgress(final Context context, String url, final String params, final Handler handler) {
        Log.d("test", "inn thread webutil...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     longInfo(response);

                Log.d("test", "response of thread..." + response);

                Message message = new Message();
                message.obj = response;
                handler.sendMessage(message);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test", "error while getting response...." + error);
                //Toast.makeText(context,"error while getting response....", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return params == null ? null : params.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            params, "utf-8");
                    return null;
                }
            }
        };


        VolleySingleton.getInstance(context).getRequestQueue().add(stringRequest);
    }


    public static void longInfo(String str) {
        if (str.length() > 1000) {
            Log.w("...............", str.substring(0, 1000));
            longInfo(str.substring(1000));
        } else
            Log.i("", str);
    }
}
