package com.codingacademy.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.model.Choice;
import com.codingacademy.testerapp.model.Exam;
import com.codingacademy.testerapp.model.Question;
import com.codingacademy.testerapp.model.Sample;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuesSampleActivity extends AppCompatActivity implements QuesEntryFragment.QuesFragmentActionListener {
    ProgressBar progressBar;

    private int questionCounter;
    private int questionCountTotal;
    Question[] quesArray;
    List<Choice> choices;
    Sample sampleArray[];
    private ViewPager pager;
    TextView tvTimer;
    EditText etSampleName;

    public static final long COUNTDOWN_TIME = 30000;
    private ColorStateList textColorDefault;
    private CountDownTimer countDownTimer;
    private Integer timeLeftInSeconds = 60;
    private int scorePerQues, totalScore = 0;

    QuesIndex mIndex;


    ImageView mNext, mPre;

    interface QuesIndex {
        int check();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        //    Exam exam = (Exam) getIntent().getSerializableExtra(ExamFragment.EXAM_OBJECT);
        Exam exam = new Exam(null, null, null, null, 20, 7, null, null, null, null, null);

        //   int examId = exam.getExamId();
        timeLeftInSeconds = exam.getExamTime();
        init();
        Fragment fragment;
        if (false) ;

            // getSamples(examId);
        else enterNewSample(exam);
    }

    private void enterNewSample(Exam exam) {
        quesArray = new Question[exam.getQuestionNumber()];
        findViewById(R.id.et_sample_name).setVisibility(View.VISIBLE);
        upDateSample();
    }

    private void upDateSample() {
        int count = quesArray.length;
        progressBar.setMax(count);

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {


                return QuesEntryFragment.getInstence(position, quesArray[position]);
            }

            @Override
            public int getCount() {
                return 7;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                    if (position > mCurrentSelectedPage &&
                            ((QuesEntryFragment) pager.getAdapter().instantiateItem(pager, mCurrentSelectedPage)).notValidate()) {
                        pager.setCurrentItem(mCurrentSelectedPage, true);
                    }else
                        mCurrentSelectedPage = position;

            }

            private int mCurrentSelectedPage = pager.getCurrentItem();

            @Override
            public void onPageScrollStateChanged(int state) {
                // Toast.makeText(QuesSampleActivity.this, "state :"+state, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getSamples(int examId, final VolleyCallback mCallback) {
        String url = Constants.GET_SAMPLE;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response ->

                {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("JA", jsonArray);
                        mCallback.onSuccess(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },


                error -> {
                    try {
                        mCallback.onError(error.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<>();
                par.put("exam_id", "" + examId);
                return par;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                while (Constants.COOKIES == null) ;
                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };
        VolleyController.getInstance(QuesSampleActivity.this).addToRequestQueue(stringRequest);

    }


    public void init() {
        tvTimer = findViewById(R.id.time_text);
        pager = findViewById(R.id.pager);
        progressBar = findViewById(R.id.progress);
        mNext = findViewById(R.id.next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pager.setCurrentItem(pager.getCurrentItem() + 1);

            }
        });
        mPre = findViewById(R.id.pre);
        mPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);

            }
        });
        if (!(timeLeftInSeconds == null || timeLeftInSeconds == 0))
            ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();

        }
    }

    @Override
    public void addQues(Question question, int index) {
        quesArray[index] = question;
        pager.getAdapter().notifyDataSetChanged();
        progressBar.setProgress(index);
    }

    @Override
    public void setAnswer(boolean isRight,int index) {
        quesArray[index].gotCorrect = isRight;
        Toast.makeText(this, index + " is " + isRight, Toast.LENGTH_SHORT).show();
    }
}