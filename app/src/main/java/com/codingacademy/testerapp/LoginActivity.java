package com.codingacademy.testerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_LOGIN = 11;
    /**
     * Email
     */
    private EditText mEtLoginEmail;
    /**
     * Password
     */
    private EditText mEtLoginPassword;
    /**
     * Login
     */
    private Button mBtnLogin;
    /**
     * Register
     */
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();


    }


    private void checkLoginInfo(final String mEmail, final String mPass, final VolleyCallback mCallback) {
        String url = Constants.LOGIN;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        mCallback.onSuccess(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            try {
                mCallback.onError(error.getMessage());

            } catch (Exception e) {
                e.printStackTrace();
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

                @Override
                public Map<String, String> getHeaders(){
                Map<String, String> map = new HashMap<>();
        	while (Constants.COOKIES == null);
                map.put("Cookie", Constants.COOKIES);
                return map;
            }
            };


        VolleyController.getInstance(this).addToRequestQueue(stringRequest);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btnLogin:
                if (validate()) {

                    String email = mEtLoginEmail.getText().toString();
                    String pass = mEtLoginPassword.getText().toString();

                    checkLoginInfo(email, pass, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject result) throws JSONException {
                            if (result.getInt("success") == 0) {
                                JSONObject jsonObjectLogin = result.getJSONArray("login").getJSONObject(0);
                                int userId, userType, status;
                                String userEmail, firstName, lastName, imageUrl;

                                userId = jsonObjectLogin.getInt("user_id");
                                userType = jsonObjectLogin.getInt("type");
                                status = jsonObjectLogin.getInt("status");

                                userEmail = jsonObjectLogin.getString("email");
                                firstName = jsonObjectLogin.getString("first_name");
                                lastName = jsonObjectLogin.getString("last_name");
                                imageUrl = jsonObjectLogin.getString("image_url");

                                LoginSharedPreferences.commitLogin(LoginActivity.this, userId, status, userEmail, userType, firstName, lastName, imageUrl);

                                setResult(Activity.RESULT_OK);

                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "البريد الذي ادخلته او كلمة المرور غير صحيحة !!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onSuccess(JSONArray result) throws JSONException {

                        }

                        @Override
                        public void onError(String result) throws Exception {

                        }
                    });

                }
                break;
            case R.id.btnRegister:
                showRegisterActivity();
                break;
        }
    }

    private void showRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
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


    private void initView() {
        mEtLoginEmail = findViewById(R.id.etLoginEmail);
        mEtLoginPassword = findViewById(R.id.etLoginPassword);
        mBtnLogin = findViewById(R.id.btnLogin);
        mBtnLogin.setOnClickListener(this);
        mBtnRegister = findViewById(R.id.btnRegister);
        mBtnRegister.setOnClickListener(this);
    }


    private boolean validate() {
        int f = 3;
        String s = mEtLoginEmail.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            mEtLoginEmail.setError(null);

        } else if (s.isEmpty())
            mEtLoginEmail.setError("Enter Email");
        else {
            mEtLoginEmail.setError("not valid");
            f--;
        }

        if (mEtLoginPassword.getText().toString().trim().isEmpty()) {
            mEtLoginPassword.setError("Enter Password");
            f--;
        }

        return f == 3;
    }


}
