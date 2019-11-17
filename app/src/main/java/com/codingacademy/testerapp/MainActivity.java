package com.codingacademy.testerapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.codingacademy.testerapp.login.LoginFragment;
import com.codingacademy.testerapp.login.SignUpFragment;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    FrameLayout frameLayout1, frameLayout2;
    LinearLayout linearLayout;
    private boolean isLogin = true;
    int w;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout1 = findViewById(R.id.login_fragment);
        frameLayout2 = findViewById(R.id.sign_up_fragment);
        button = findViewById(R.id.button);

        LoginFragment topLoginFragment = new LoginFragment();
        SignUpFragment topSignUpFragment = new SignUpFragment();
        w = frameLayout1.getWidth();
        w = 1000;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sign_up_fragment, topSignUpFragment)
                .replace(R.id.login_fragment, topLoginFragment)

                .commit();

        frame();

    }


    void frame() {
        frameLayout1.animate().translationY(0);
        frameLayout2.setVisibility(INVISIBLE);
        frameLayout2.setTranslationY(-w);
        button.setBackground(getResources().getDrawable(R.drawable.bubble));
        button.setText("or Sign up");
    }

    public void switchFragment(View v) {


        Button button = (Button) v;
        if (isLogin) {
            frameLayout1.setVisibility(VISIBLE);
            frame();
        } else {
            frameLayout2.setVisibility(VISIBLE);
            frameLayout2.animate().translationY(0);
            frameLayout1.setVisibility(INVISIBLE);
            frameLayout1.setTranslationY(-w);
            v.setBackground(getResources().getDrawable(R.drawable.bubble2));
            button.setText("or Sign in");

        }
        isLogin = !isLogin;

    }

}