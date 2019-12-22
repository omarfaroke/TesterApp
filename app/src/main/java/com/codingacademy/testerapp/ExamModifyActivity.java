package com.codingacademy.testerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codingacademy.testerapp.model.Exam;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamModifyActivity extends AppCompatActivity {


    private List<EditText> editTexts = new ArrayList<>();
    private Exam exam;
    Button btnSave;
    private View parent_view;
    private Switch swAprove;
    boolean abrove;
    Spinner spinnerCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_modify);
        parent_view = findViewById(android.R.id.content);
        exam = (Exam) getIntent().getSerializableExtra(ExamFragment.EXAM_OBJECT);

        initSpinner();


        initComponent();
        checkPrivilige();
    }

    private void checkPrivilige() {
        if (LoginSharedPreferences.userAsAdmin(this)) {
            swAprove.setVisibility(View.VISIBLE);

        } else {
            swAprove.setVisibility(View.GONE);
        }
    }

    void initSpinner() {
        spinnerCat = findViewById(R.id.sp_cat);
        ArrayList<String> arrList = new ArrayList<>();
        arrList.add("OOP");
        arrList.add("Java");
        arrList.add("C++");
        arrList.add("Flutter");
        ArrayAdapter<String> adpS = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrList);
        spinnerCat.setAdapter(adpS);

    }

    private void initComponent() {

        TextView tvExaminerName = findViewById(R.id.tv_examiner);
        tvExaminerName.setText("Examiner: "+exam.getExaminerName());

        editTexts.add(0, (findViewById(R.id.et_title)));
        editTexts.get(0).setText(exam.getExamName());

        editTexts.add(0, (findViewById(R.id.et_num_ques)));
        editTexts.get(0).setText(exam.getQuestionNumber() + "");

        editTexts.add(0, (findViewById(R.id.et_time_ques)));
        editTexts.get(0).setText(exam.getExamTime() + "");

        editTexts.add(0,(findViewById(R.id.et_full_mark)));
        editTexts.get(0).setText(exam.getFullMarks() + "");

        editTexts.add(0, (findViewById(R.id.et_pass_mark)));
        editTexts.get(0).setText(exam.getExamPass() + "");

        editTexts.add(0, (findViewById(R.id.et_note)));
        editTexts.get(0).setText(exam.getExamNote());

        editTexts.add(0, (findViewById(R.id.et_description)));
        editTexts.get(0).setText(exam.getExamDescription());


        // Toast.makeText(this, editTexts.size()+"", Toast.LENGTH_SHORT).show();
        for (EditText e : editTexts)
            e.setEnabled(false);

        btnSave = findViewById(R.id.bt_modify_exam);
        swAprove = findViewById(R.id.sw_aprove);
        if (LoginSharedPreferences.getUserType(ExamModifyActivity.this) == Constants.USER_TYPE_ADMIN) {
            swAprove.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.VISIBLE);
            abrove = exam.getStatus() == 1 ? true : false;
            swAprove.setChecked(abrove);
            swAprove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    exam.setStatus(b ? 1 : 0);
                    btnSave.setEnabled(true);

                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upDateExam();
                }
            });
        }
        hideSoftKeyboard();
    }

    public void clickLabel(View v) {
        Toast.makeText(this, v.getId() + "", Toast.LENGTH_SHORT).show();
    }

    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {


            case R.id.bt_modify_exam:
                upDateExam();
                finish();
                break;
        }
    }

    private void upDateExam() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.UPDATE_EXAM_STATE,
                response -> {
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show();


                },
                error -> {
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();

                parameter.put("exam_id", exam.getExamId() + "");
                parameter.put("status", exam.getStatus() + "");
                return parameter;

            }


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
//                while (Constants.COOKIES == null) ;
//                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };
        Volley.newRequestQueue(ExamModifyActivity.this).add(request);

    }


    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}
