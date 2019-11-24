package com.codingacademy.testerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Locale;

public class TesterActivity extends AppCompatActivity {
CountDownTimer countDownTimer;
long time=10000;
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);
        textView=findViewById(R.id.time_text);
        countDownTimer=new CountDownTimer(time,3000) {
            @Override
            public void onTick(long l) {
time=l;
update();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
    private void update(){
        int m=(int)(time/1000)/60;
        int s=(int)(time/1000)%60;
        String tString=String.format(Locale.getDefault(),"%02d:%02d",m,s);
        textView.setText(tString);
    }
}
