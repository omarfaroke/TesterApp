package com.codingacademy.testerapp;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codingacademy.testerapp.model.Category;
import com.codingacademy.testerapp.model.Exam;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamEntryActivity extends AppCompatActivity {

    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private int success_step = 0;
    private int current_step = 0;
    private View parent_view;
    private int categoryID;
    Exam exam;

    Spinner spinnerCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_entry);
        parent_view = findViewById(android.R.id.content);
        spinnerCat = findViewById(R.id.sp_cat);
        if (getIntent() != null) {
            categoryID = getIntent().getIntExtra(CategoryFragment.CATEGORY_ID, 0);
           // exam = new Exam(1);
            exam = new Exam(LoginSharedPreferences.getUserId(this));
            exam.setCategoryId(categoryID);
        }
        initSpinner();


        initComponent();
    }

    void initSpinner() {
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
        // populate layout field
        view_list.add(findViewById(R.id.lyt_cat));
        view_list.add(findViewById(R.id.lyt_title));
        view_list.add(findViewById(R.id.lyt_num_ques));
        view_list.add(findViewById(R.id.lyt_mark));
        view_list.add(findViewById(R.id.lyt_note));
        view_list.add(findViewById(R.id.lyt_description));
        view_list.add(findViewById(R.id.lyt_confirmation));

        // populate view step (circle in left)
        step_view_list.add((findViewById(R.id.step_cat)));
        step_view_list.add((findViewById(R.id.step_title)));
        step_view_list.add((findViewById(R.id.step_num_ques)));
        step_view_list.add((findViewById(R.id.step_mark)));
        step_view_list.add((findViewById(R.id.step_note)));
        step_view_list.add((findViewById(R.id.step_description)));
        step_view_list.add((findViewById(R.id.step_confirmation)));

        for (View v : view_list)
            v.setVisibility(View.GONE);


        view_list.get(0).setVisibility(View.VISIBLE);
        hideSoftKeyboard();


    }

    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_continue_cat:
                // validate input user here
                if (false) {
                    spinnerCat.performClick();
                    Snackbar.make(parent_view, "Please chose category", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                collapseAndContinue(0);
                break;
            case R.id.bt_continue_title:
                String s = ((EditText) findViewById(R.id.et_title)).getText().toString();
                // validate input user here
                if (s.trim().equals("")) {
                    Snackbar.make(parent_view, "Title cannot empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                exam.setExamName(s);

                collapseAndContinue(1);
                break;
            case R.id.bt_continue_num_ques:
                // validate input user here
                String q_n_t = null;
                s = ((EditText) findViewById(R.id.et_num_ques)).getText().toString();
                if (s.trim().equals(""))
                    q_n_t = "Please set number of question";
                else
                    exam.setQuestionNumber(Integer.valueOf(s));
                s = ((EditText) findViewById(R.id.et_time_ques)).getText().toString();
                if (s.trim().equals("")) {
                    if (q_n_t == null)
                        q_n_t = "Please set time for exam";
                    else q_n_t += " & time for exam";
                } else exam.setExamTime(Integer.valueOf(s));

                if (q_n_t == null) {
                    collapseAndContinue(2);
                    break;
                }
                Snackbar.make(parent_view, q_n_t, Snackbar.LENGTH_SHORT).show();
                return;
            case R.id.bt_continue_mark:
                // validate input user here
                String mark = null;
                s = ((EditText) findViewById(R.id.et_full_mark)).getText().toString();
                if (s.trim().equals(""))
                    mark = "Please set full mark";
                else exam.setFullMarks(Integer.valueOf(s));
                s = ((EditText) findViewById(R.id.et_pass_mark)).getText().toString();
                if (s.trim().equals("")) {
                    if (mark == null)
                        mark = "Please set pass mark";
                    else mark += " & pass mark";
                } else exam.setExamPass(Integer.valueOf(s));

                if (mark == null) {
                    collapseAndContinue(3);
                    break;
                }
                Snackbar.make(parent_view, mark, Snackbar.LENGTH_SHORT).show();
                return;
            case R.id.bt_continue_note:
                // validate input user here
                s = ((EditText) findViewById(R.id.et_note)).getText().toString();
                if (s.trim().equals("")) {
                    Snackbar.make(parent_view, "Note cannot empty", Snackbar.LENGTH_SHORT).show();
                    return;
                } else exam.setExamNote(s);
                collapseAndContinue(4);
                break;


            case R.id.bt_continue_description:
                // validate input user here
                s = ((EditText) findViewById(R.id.et_description)).getText().toString();
                if (s.trim().equals("")) {
                    Snackbar.make(parent_view, "Description cannot empty", Snackbar.LENGTH_SHORT).show();
                    return;
                } else exam.setExamDescription(s);

                collapseAndContinue(5);
                break;


            case R.id.bt_add_event:
                // validate input user here
                upLoadExam();

                break;
        }
    }

    private void upLoadExam() {
        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.ADD_EXAM,
                response -> {
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                },
                error -> {
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                String examString = new Gson().toJson(exam);
                parameter.put("exam", examString);
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
        Volley.newRequestQueue(ExamEntryActivity.this).add(request);
        // VolleyController.getInstance(ExamEntryActivity.this).addToRequestQueue(request);
    }

    public void clickLabel(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_label_cat:
                if (success_step >= 0 && current_step != 0) {
                    current_step = 0;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(0));
                }
                break;
            case R.id.tv_label_title:
                if (success_step >= 1 && current_step != 1) {
                    current_step = 1;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(1));
                }
                break;
            case R.id.tv_label_num_ques:
                if (success_step >= 2 && current_step != 2) {
                    current_step = 2;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(2));
                }
                break;
            case R.id.tv_label_mark:
                if (success_step >= 3 && current_step != 3) {
                    current_step = 3;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(3));
                }
                break;
            case R.id.tv_label_note:
                if (success_step >= 4 && current_step != 4) {
                    current_step = 4;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(4));
                }
                break;
            case R.id.tv_label_description:
                if (success_step >= 5 && current_step != 5) {
                    current_step = 5;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(5));
                }
                break;
            case R.id.tv_label_confirmation:
                if (success_step >= 6 && current_step != 6) {
                    current_step = 6;
                    collapseAll();
                    ViewAnimation.expand(view_list.get(6));
                }
                break;
        }
    }

    private void collapseAndContinue(int index) {
        ViewAnimation.collapse(view_list.get(index));
        setCheckedStep(index);
        index++;
        current_step = index;
        success_step = index > success_step ? index : success_step;
        ViewAnimation.expand(view_list.get(index));
    }

    private void collapseAll() {
        for (View v : view_list) {
            ViewAnimation.collapse(v);
        }
    }

    private void setCheckedStep(int index) {
        RelativeLayout relative = step_view_list.get(index);
        relative.removeAllViews();
        ImageButton img = new ImageButton(this);
        img.setImageResource(R.drawable.ic_done);
        img.setBackgroundColor(Color.TRANSPARENT);
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        relative.addView(img);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}
