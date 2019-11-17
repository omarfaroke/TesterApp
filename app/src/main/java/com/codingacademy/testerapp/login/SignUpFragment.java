package com.codingacademy.testerapp.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.Constants;
import com.codingacademy.testerapp.LoginActivity;
import com.codingacademy.testerapp.R;
import com.codingacademy.testerapp.model.UserProfile;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment implements OnSignUpListener, View.OnClickListener {
    private static final String TAG = "SignUpFragment";
    EditText fristName,
                email,
                password,
                con_password,
                lastName,
                address,
                phone;
          //  type;
    Button nextButton, prevButton,regButton;
    LinearLayout[] linearLayout=new LinearLayout[3];
int currentFrame=0;
    UserProfile userProfile;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_signup , container, false);
    setupUI(inflate);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        regButton.setOnClickListener(this);


        return inflate;
    }

private void setupUI(View inflate){
    fristName =inflate.findViewById(R.id.first_name);
    email=inflate.findViewById(R.id.email);
    password=inflate.findViewById(R.id.pass);
    con_password=inflate.findViewById(R.id.conf_pass);
    lastName=inflate.findViewById(R.id.last_name);
    address=inflate.findViewById(R.id.adress);
    phone=inflate.findViewById(R.id.phone);
    nextButton =inflate.findViewById(R.id.next_button);
    prevButton =inflate.findViewById(R.id.prev_button);
    regButton=inflate.findViewById(R.id.reg_button);
    linearLayout[0]=inflate.findViewById(R.id.frame1);
    linearLayout[1]=inflate.findViewById(R.id.frame2);
    linearLayout[2]=inflate.findViewById(R.id.frame3);
    }

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

                    hashPass = LoginActivity.getSHA256(user.getPassword());

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


        VolleyController.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }



    @Override
    public void signUp() {
        Toast.makeText(getContext(), "Sign up", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                switch (currentFrame){
                    case 0: if (!validdate())break;
                            prevButton.setVisibility(View.VISIBLE);
                            moveFrame(1);
                            break;
                    case 1: nextButton.setVisibility(View.GONE);
                            regButton.setVisibility(View.VISIBLE);
                            moveFrame(1);
                }break;

            case R.id.prev_button:
                switch (currentFrame){
                    case 1:prevButton.setVisibility(View.INVISIBLE);break;
                    case 2:regButton.setVisibility(View.GONE);
                           nextButton.setVisibility(View.VISIBLE);
                } moveFrame(-1); break;
            case R.id.reg_button: register();
        }
    }
    private void moveFrame(int dir){
        linearLayout[currentFrame].animate().translationX(-dir*1000);
        currentFrame+=dir;
        linearLayout[currentFrame].animate().translationX(0);
    }
private void register(){
        UserProfile user=new UserProfile(null,fristName.getText().toString(),lastName.getText().toString(),email.getText().toString(),
                password.getText().toString(),address.getText().toString(),phone.getText().toString());
        regester_new_account(user, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String result) throws Exception {
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();

            }
        });
}


     int vEmail(){
         String s= email.getText().toString();
         if(Patterns.EMAIL_ADDRESS.matcher(s).matches()){
             email.setError(null);
             return 1;}
         else if(s.isEmpty())
             email.setError("Enter Email");
         else email.setError("not valid");
         return 0;
    }
    int vPassword(){
         String s= password.getText().toString();
         if(s.isEmpty()){
             password.setError("Enter Password");
         return 0;}
 if(s.equals(con_password.getText().toString()))
    return 1;
else con_password.setError("Password not match");
         return 0;
    }

    boolean validdate(){
        int i=2;
        if(fristName.getText().toString().isEmpty()) {
            fristName.setError("Enter First Name");
            i--;
        }
        if(lastName.getText().toString().isEmpty()){
            lastName.setError("Enter Last Name");
            i--;}

        i+=vEmail();
        i+=vPassword();
        return i==4;

    }
}
