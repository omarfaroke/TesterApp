package com.codingacademy.testerapp;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mLoginButton;



    private void initView() {
        mLoginButton.setOnClickListener(this);
    }




    private void checkLoginInfo(final String mEmail, final String mPass, final VolleyCallback mCallback) {
        String url = Constants.BASE_URL + "/" + Constants.LOGIN;

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

                    hashPass = getSHA256(mPass);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                parameter.put(Constants.USER_EMAIL, mEmail);
                parameter.put(Constants.USER_PASS, hashPass);

                return parameter;
            }
        };


        VolleyController.getInstance(this).addToRequestQueue(stringRequest);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case 6: //R.id.login_button:
                String email = null;
                String pass = null;

                checkLoginInfo(email, pass, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {

                    }

                    @Override
                    public void onError(String result) throws Exception {

                    }
                });

                break;
        }
    }




    // convert string password to hash SHA256
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
