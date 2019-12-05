package com.codingacademy.testerapp;


import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Constants {

    public static final String APP_NAME = "testerapp";
    public static final String PACKAGE_NAME = "com.codingacademy.testerapp";

    public static final String APP_PREFERENCES = PACKAGE_NAME + "_preferences";
    public static final String LOGIN_STATE_PREFERENCES = "LOGIN_STATE";
    public static final String LOGIN_STATE_LOGGED_IN = "LOGGED_IN";
    public static final String LOGIN_STATE_NOT_LOGGED_IN = "NOT_LOGGED_IN";
    public static final String USER_ID_PREFERENCES = "USER_ID";
    public static final String USER_TYPE_PREFERENCES = "USER_TYPE";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASS = "password";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone";
    public static final String IMAGE_URI = "image";

    public static final String CATEGORY_ID = "Category_id";
    public static final String CATEGORY_NAME = "Category_name";
    public static final String CATEGORY_PARENT = "parent";


    //web
    // public static final String BASE_URL = "http://testerapp.aba.cx";
    public static final String BASE_URL = "http://testerapp.rf.gd";
    public static final String LOGIN = BASE_URL + "/login.php";
    public static final String REGISTER = BASE_URL + "/register.php";
    public static final String CATEGORY = BASE_URL + "/get_category.php";
    public static final String GET_EXAM = BASE_URL + "/get_exam.php";
    public static final String GET_SAMPLE = BASE_URL + "/get_question.php";
    public static final String GET_TOP_TALET = BASE_URL + "/get_top_talent.php";
    public static final String ADD_CATEGORY = BASE_URL + "/add_Category.php";
    public static final String ADD_EXAM_HISTORY = BASE_URL + "/add_exams_history.php";


    public static String COOKIES ;


    public static final int USER_TYPE_ADMIN = 0;
    public static final int USER_TYPE_TALENT = 3;
    public static final int USER_TYPE_EXAMINER = 1;
    public static final int USER_TYPE_RECRUITER = 2;
    public static final String USER_STATUS = "status";


//    private static String setUpCookies() {
//        String cookies = CookieManager.getInstance().getCookie(Constants.GET_TOP_TALET);
//        return cookies;
//    }



    static void getCookiesFromUrl(Context context) {

        WebView webViewRequest = new WebView(context);

        webViewRequest.getSettings().setJavaScriptEnabled(true);
        webViewRequest.setWebViewClient(new WebViewClient());
        webViewRequest.getSettings().setLoadWithOverviewMode(true);
        webViewRequest.getSettings().setUseWideViewPort(true);
        webViewRequest.getSettings().setSupportZoom(false);
        webViewRequest.getSettings().setBuiltInZoomControls(false);
        webViewRequest.getSettings().setDisplayZoomControls(false);
        webViewRequest.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webViewRequest.requestFocusFromTouch();
        webViewRequest.getSettings().setAppCacheEnabled(false);
        webViewRequest.setScrollbarFadingEnabled(false);

        webViewRequest.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {
                String cookies = CookieManager.getInstance().getCookie(url);//here you will get cookie
                Log.d("getCookiesFromUrl", "onPageFinished: " + cookies);

                COOKIES = cookies;

            }
        });
        webViewRequest.loadUrl(Constants.GET_TOP_TALET);



    }


}
