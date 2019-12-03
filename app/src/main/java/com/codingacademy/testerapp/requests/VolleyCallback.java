package com.codingacademy.testerapp.requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject result) throws JSONException;
    void onSuccess(JSONArray result) throws JSONException;
    void onError(String result) throws Exception;
}