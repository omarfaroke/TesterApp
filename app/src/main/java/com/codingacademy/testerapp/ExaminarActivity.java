package com.codingacademy.testerapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


public class ExaminarActivity extends AppCompatActivity {
   FragmentManager fm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examinar);
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.examinare_frag, new ExaminarFragment()).commit();

    }

}
