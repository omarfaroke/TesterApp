package com.codingacademy.testerapp.requests;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codingacademy.testerapp.Constants;

import java.util.HashMap;
import java.util.Map;


public class VolleyController {
    private static VolleyController mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private VolleyController(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyController getInstance(Context context) {
        // If instance is not available, create it. If available, reuse and return the object.
        if (mInstance == null) {
            mInstance = new VolleyController(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key. It should not be activity context,
            // or else RequestQueue won't last for the lifetime of your app
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }



}