package com.codingacademy.testerapp.requests;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.Constants;
import org.json.JSONArray;
import org.json.JSONException;


import java.util.HashMap;
import java.util.Map;

public class WebServicesGlobal {


    public static void getProfileInfo(Context mContext ,int mUserID ,VolleyCallback mCallback) {
        String url = Constants.GET_INFO_PROFILE;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response ->
                {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        mCallback.onSuccess(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    try {
                        mCallback.onError(error.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<>();

                    par.put("user_id", "" + mUserID);
                return par;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
//                while (Constants.COOKIES == null) ;
//                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };


        VolleyController.getInstance(mContext).addToRequestQueue(stringRequest);
    }

}
