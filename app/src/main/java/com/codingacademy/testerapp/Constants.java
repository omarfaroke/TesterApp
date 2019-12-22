package com.codingacademy.testerapp;


import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codingacademy.testerapp.requests.StatusCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

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
    public static final String ADD_SAMPLE = BASE_URL + "/add_questions.php";
    public static final String ADD_EXAM = BASE_URL + "/add_exam.php";
    public static final String UPDATE_EXAM_STATE = BASE_URL + "/update_exam_state.php";
    public static final String GET_EXAMINER = BASE_URL + "/get_all_examinar.php";
    public static final String UPDATE_USER_STATE = BASE_URL + "/update_user_state.php";
    public static final String UPDATE_QUTETION = BASE_URL + "/update_question.php";



    public static String COOKIES;


    public static final int USER_TYPE_ADMIN = 0;
    public static final int USER_TYPE_EXAMINER = 1;
    public static final int USER_TYPE_RECRUITER = 2;
    public static final int USER_TYPE_TALENT = 3;
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
    public static void upDateState(int userId, int status,Context ctx, StatusCallback statusCallback) {

        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.UPDATE_USER_STATE,
                response -> {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String s=jsonObject.getString("message");
                        statusCallback.response(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    statusCallback.response("erorr");

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();

                parameter.put("user_id", userId+"");
                parameter.put("status", status+"");
                return parameter;

            }


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                while (Constants.COOKIES == null) ;
                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };
        Volley.newRequestQueue(ctx).add(request);

    }



}
