package com.codingacademy.testerapp;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ExamModifyActivity extends AppCompatActivity {
    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();

    private View parent_view;
    private Switch swAprove;
    Spinner spinnerCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_modify);
        parent_view = findViewById(android.R.id.content);


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

        spinnerCat = findViewById(R.id.sp_cat);
        swAprove = findViewById(R.id.sw_aprove);

        checkPrevlige();


        hideSoftKeyboard();


    }

    private void checkPrevlige() {

        int type = LoginSharedPreferences.getUserType(ExamModifyActivity.this);

        switch (type) {
            case Constants.USER_TYPE_ADMIN:
                break;
            case Constants.USER_TYPE_TALENT:
                break;

            case Constants.USER_TYPE_EXAMINER:
                swAprove.setVisibility(View.VISIBLE);
                break;
            case Constants.USER_TYPE_RECRUITER:
                break;

            default:

        }
    }

    public void clickLabel(View v) {
        Toast.makeText(this, v.getId() + "", Toast.LENGTH_SHORT).show();
    }

    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {


            case R.id.bt_modify_exam:
                // validate input user here
                finish();
                break;
        }
    }


    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}
