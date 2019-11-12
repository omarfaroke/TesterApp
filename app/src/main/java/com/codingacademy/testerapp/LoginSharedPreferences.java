package com.codingacademy.testerapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class LoginSharedPreferences {

    //check if user login
    public static boolean checkIsLogin(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        String loginState = mSharedPreferences.getString(Constants.LOGIN_STATE_PREFERENCES, Constants.LOGIN_STATE_NOT_LOGGED_IN);

        if (loginState.equals(Constants.LOGIN_STATE_LOGGED_IN)) {
            return true;
        }
        return false;
    }


    //save state login and user id
    public static void commitLogin(Context mContext, int userId, int userType) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        mEditor.putString(Constants.LOGIN_STATE_PREFERENCES, Constants.LOGIN_STATE_LOGGED_IN);
        mEditor.putInt(Constants.USER_ID_PREFERENCES, userId);
        mEditor.putInt(Constants.USER_TYPE_PREFERENCES, userType);

        mEditor.apply();
        mEditor.commit();

    }


    public static int getUserId(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        int userId = mSharedPreferences.getInt(Constants.USER_ID_PREFERENCES, -1);

        return userId;
    }


    public static int getUserType(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        int userType = mSharedPreferences.getInt(Constants.USER_TYPE_PREFERENCES, -1);

        return userType;
    }


    public static void UserLogOut(Activity mActivity){
        SharedPreferences mSharedPreferences = mActivity.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        mEditor.putString(Constants.LOGIN_STATE_PREFERENCES, Constants.LOGIN_STATE_NOT_LOGGED_IN);
        mEditor.remove(Constants.USER_ID_PREFERENCES);
        mEditor.remove(Constants.USER_ID_PREFERENCES);
        mEditor.apply();
        mEditor.commit();

        mActivity.finish();
    }

}
