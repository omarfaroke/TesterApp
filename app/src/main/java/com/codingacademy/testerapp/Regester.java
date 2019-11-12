package com.codingacademy.testerapp;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.model.UserProfile;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Regester extends AppCompatActivity   {
UserProfile userProfile;


    private void regester_new_account (final UserProfile user, final VolleyCallback mCallback) {
        String url = Constants.BASE_URL + "/" + Constants.REGESTER;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    mCallback.onSuccess(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    mCallback.onError(error.getMessage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameter = new HashMap<>();

                String hashPass = null;
                try {

                    hashPass = getSHA256(user.getPassword());

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                parameter.put(Constants.USER_EMAIL, user.getEmail());
                parameter.put(Constants.USER_PASS, hashPass);
                parameter.put(Constants.USER_TYPE_PREFERENCES, user.getType()+"");
                parameter.put(Constants.USER_F_NAME, user.getFristName());
                parameter.put(Constants.USER_L_NAME, user.getLastName());
                parameter.put(Constants.ADDRESS, user.getAddress());
                parameter.put(Constants.PHONE, user.getPhone());
                parameter.put(Constants.IMAGE_URI, user.getImageUrl());

                return parameter;
            }
        };


        VolleyController.getInstance(this).addToRequestQueue(stringRequest);

    }

    public static String getSHA256(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte

        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

}
