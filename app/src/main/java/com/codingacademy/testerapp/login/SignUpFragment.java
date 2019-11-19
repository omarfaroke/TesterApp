package com.codingacademy.testerapp.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.Constants;
import com.codingacademy.testerapp.LoginActivity;
import com.codingacademy.testerapp.R;
import com.codingacademy.testerapp.model.UserProfile;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SignUpFragment{
    
}
/*
public class SignUpFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SignUpFragment";
    EditText firstName,
            email,
            password,
            con_password,
            lastName,
            address,
            phone;
    //  type;
    Button nextButton, prevButton, regButton;
    LinearLayout[] linearLayout = new LinearLayout[3];
    int currentFrame = 0;
    UserProfile userProfile;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_signup, container, false);
        setupUI(inflate);


        return inflate;
    }

    private void setupUI(View inflate) {
        firstName = inflate.findViewById(R.id.first_name);
        email = inflate.findViewById(R.id.email);
        password = inflate.findViewById(R.id.pass);
        con_password = inflate.findViewById(R.id.conf_pass);
        lastName = inflate.findViewById(R.id.last_name);
        address = inflate.findViewById(R.id.adress);
        phone = inflate.findViewById(R.id.phone);
        nextButton = inflate.findViewById(R.id.next_button);
        prevButton = inflate.findViewById(R.id.prev_button);
        regButton = inflate.findViewById(R.id.reg_button);
        linearLayout[0] = inflate.findViewById(R.id.frame1);
        linearLayout[1] = inflate.findViewById(R.id.frame2);
        linearLayout[2] = inflate.findViewById(R.id.frame3);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        regButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                switch (currentFrame) {
                    case 0:
                        if (!validdate()) break;
                        prevButton.setVisibility(View.VISIBLE);
                        moveFrame(1);
                        break;
                    case 1:
                        nextButton.setVisibility(View.GONE);
                        regButton.setVisibility(View.VISIBLE);
                        moveFrame(1);
                }
                break;

            case R.id.prev_button:
                switch (currentFrame) {
                    case 1:
                        prevButton.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        regButton.setVisibility(View.GONE);
                        nextButton.setVisibility(View.VISIBLE);
                }
                moveFrame(-1);
                break;
            case R.id.reg_button:
                register();
        }
    }

    private void register() {
        UserProfile userProfile = new UserProfile(null,1,firstName.getText().toString(),
                "",lastName.getText().toString(),address.getText().toString(),phone.getText().toString(),"");


        regester_new_account(email.getText().toString(), password.getText().toString(), userProfile, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                exitFromRegisterActivity();
            }

            @Override
            public void onError(String result) throws Exception {

                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void regester_new_account(final String mEmail, final String mPass, final UserProfile user, final VolleyCallback mCallback) {
        String url = Constants.REGISTER;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
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

                    hashPass = LoginActivity.getSHA256(mPass);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                String userProfileGson = new Gson().toJson(user);

                parameter.put("user_profile", userProfileGson);

                parameter.put("email", mEmail);
                parameter.put("password", mPass);

                parameter.put("photo_base64" , "");

                return parameter;
            }
        };


        VolleyController.getInstance(getContext()).addToRequestQueue(stringRequest);

    }


    void exitFromRegisterActivity(){

        Toast.makeText(getContext(), "done register .....", Toast.LENGTH_SHORT).show();
    }



    private void moveFrame(int dir) {
        linearLayout[currentFrame].animate().translationX(-dir * 1000);
        currentFrame += dir;
        linearLayout[currentFrame].animate().translationX(0);
    }



    int vEmail() {
        String s = email.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            email.setError(null);
            return 1;
        } else if (s.isEmpty())
            email.setError("Enter Email");
        else email.setError("not valid");
        return 0;
    }

    int vPassword() {
        String s = password.getText().toString();
        if (s.isEmpty()) {
            password.setError("Enter Password");
            return 0;
        }
        if (s.equals(con_password.getText().toString()))
            return 1;
        else con_password.setError("Password not match");
        return 0;
    }

    boolean validdate() {
        int i = 2;
        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("Enter First Name");
            i--;
        }
        if (lastName.getText().toString().isEmpty()) {
            lastName.setError("Enter Last Name");
            i--;
        }

        i += vEmail();
        i += vPassword();
        return i == 4;

    }
}
*/