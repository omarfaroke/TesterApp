package com.codingacademy.testerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class LoginSharedPreferences {

    //check if user login
    public static boolean checkIsLogin(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        String loginState = mSharedPreferences.getString(Constants.LOGIN_STATE_PREFERENCES, Constants.LOGIN_STATE_NOT_LOGGED_IN);

        mSharedPreferences.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
          //      Toast.makeText(mContext, "onSharedPreferenceChanged", Toast.LENGTH_SHORT).show();
           //    Toast.makeText(mContext, key, Toast.LENGTH_SHORT).show();

            }
        });


        if (loginState.equals(Constants.LOGIN_STATE_LOGGED_IN)) {
            return true;
        }
        return false;
    }


    //save state login and user id
    public static void commitLogin(Context mContext, int userId, int status, String userEmail, int userType, String firstName, String lastName, String imageUrl) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        mEditor.putString(Constants.LOGIN_STATE_PREFERENCES, Constants.LOGIN_STATE_LOGGED_IN);
        mEditor.putInt(Constants.USER_ID_PREFERENCES, userId);
        mEditor.putInt(Constants.USER_TYPE_PREFERENCES, userType);
        mEditor.putInt(Constants.USER_STATUS, status);
        mEditor.putString(Constants.USER_EMAIL, userEmail);
        mEditor.putString(Constants.USER_FIRST_NAME, firstName);
        mEditor.putString(Constants.USER_LAST_NAME, lastName);
        mEditor.putString(Constants.IMAGE_URI, imageUrl);








        mEditor.apply();
        mEditor.commit();

    }


    public static int getUserId(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        int userId = mSharedPreferences.getInt(Constants.USER_ID_PREFERENCES, -1);

        return userId;
    }

    public static String getUserEmail(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        String userEmail = mSharedPreferences.getString(Constants.USER_EMAIL, "");

        return userEmail;
    }

    public static String getUserFirstName(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        String userFirstName = mSharedPreferences.getString(Constants.USER_FIRST_NAME, "");

        return userFirstName;
    }

    public static String getUserLastName(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        String userLastName = mSharedPreferences.getString(Constants.USER_LAST_NAME, "");

        return userLastName;
    }

    public static String getUserImageUrl(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        String userLastName = mSharedPreferences.getString(Constants.IMAGE_URI, "");

        return userLastName;
    }


    public static int getUserType(Context mContext) {
        int userImageUrl =-1;

        if (checkIsLogin(mContext)) {

            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
            userImageUrl = mSharedPreferences.getInt(Constants.USER_TYPE_PREFERENCES, -1);
        }

        return userImageUrl;
    }


    public static void userLogOut(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        mEditor.putString(Constants.LOGIN_STATE_PREFERENCES, Constants.LOGIN_STATE_NOT_LOGGED_IN);
        mEditor.remove(Constants.USER_ID_PREFERENCES);
        mEditor.remove(Constants.USER_TYPE_PREFERENCES);




        mEditor.apply();
        mEditor.commit();

    }

    public static boolean userAsAdmin(Context mContext) {
        if (getUserType(mContext) == Constants.USER_TYPE_ADMIN) {
            return true;
        }
        return false;
    }

    public static void test(Context mContext){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        mEditor.putString("12345","12345");

        mEditor.apply();
        mEditor.commit();

    }

}
