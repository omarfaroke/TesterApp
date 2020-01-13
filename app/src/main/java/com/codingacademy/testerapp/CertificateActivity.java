package com.codingacademy.testerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.codingacademy.testerapp.model.TopTalent;

public class CertificateActivity extends AppCompatActivity {
    static final String CERTIFICATE = "Certificate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.certification);
        TopTalent topTalent = (TopTalent) getIntent().getSerializableExtra(CERTIFICATE);
        if (topTalent != null) {
            TextView examiner = findViewById(R.id.examinar_name);
            TextView talent = findViewById(R.id.talent_name);
            TextView note = findViewById(R.id.exam_note);
            TextView date = findViewById(R.id.date);

            examiner.setText(topTalent.getmExam().getExaminerName());


            note.setText(topTalent.getmExam().getExamNote());




            talent.setText(topTalent.getmExam().getExaminerName());


            date.setText(topTalent.getDate());
        }
    }
}