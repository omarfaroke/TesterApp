package com.codingacademy.testerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TesterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);
        getSupportFragmentManager().beginTransaction().add(R.id.fram,new ExamFragment()).commit();
    }
}
