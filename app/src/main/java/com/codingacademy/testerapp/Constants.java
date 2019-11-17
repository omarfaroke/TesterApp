package com.codingacademy.testerapp;

public class Constants {

    public static final String APP_NAME="testerapp";
    public static final String PACKAGE_NAME = "com.codingacademy.testerapp";

    public static final String APP_PREFERENCES  = PACKAGE_NAME + "_preferences";
    public static final String LOGIN_STATE_PREFERENCES = "LOGIN_STATE";
    public static final String LOGIN_STATE_LOGGED_IN="LOGGED_IN";
    public static final String LOGIN_STATE_NOT_LOGGED_IN="NOT_LOGGED_IN";
    public static final String USER_ID_PREFERENCES = "USER_ID";
    public static final String USER_TYPE_PREFERENCES="USER_TYPE";
    public static final String USER_EMAIL="email";
    public static final String USER_PASS="password";
    public static final String USER_F_NAME="first_name";
    public static final String USER_L_NAME="last_name";
    public static final String ADDRESS="address";
    public static final String PHONE="phone";
    public static final String IMAGE_URI="image";

    public static final String CATEGORY_ID="Category_id";
    public static final String CATEGORY_NAME="Category_name";
    public static final String CATEGORY_PARENT="parent";



    //web
    public static final String BASE_URL = "http://192.168.1.103/testerapp";
    public static final String LOGIN=BASE_URL + "/login.php";
    public static final String REGISTER =BASE_URL + "/register.php";
    public static final String CATEGORY =BASE_URL + "/get_category.php";



}
