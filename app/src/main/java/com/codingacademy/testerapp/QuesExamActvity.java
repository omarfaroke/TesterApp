package com.codingacademy.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class QuesExamActvity extends AppCompatActivity implements QuesEntryFragment.QuesFragmentActionListener {


    ProgressBar progressBar;

    private int questionCounter;
    private int questionCountTotal;

    List<Choice> choices;
    Question questionArray[];
    private ViewPager pager;
    TextView tvTimer;
    EditText etSampleName;
    Button btnFinish;
    Sample sample;
    Exam exam;


    public static final long COUNTDOWN_TIME = 30000;
    private ColorStateList textColorDefault;
    private CountDownTimer countDownTimer;
    private Integer timeLeftInSeconds = 60;
    private int scorePerQues, totalScore = 0;
    static final int ADD_SAMPLE = -101;
    static final int TAKE_EXAM = -103;
    private static final String WHAT = "WHAT";
    // int counter =0;


    ImageView mNext, mPre;


    public static Intent getInstance(Context context, Exam exam, int what) {
        Intent intent = new Intent(context, QuesExamActvity.class);
        intent.putExtra(ExamFragment.EXAM_OBJECT, exam);
        intent.putExtra(WHAT, what);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
         exam = (Exam) getIntent().getSerializableExtra(ExamFragment.EXAM_OBJECT);
        int what = getIntent().getIntExtra(WHAT, -1);

        timeLeftInSeconds = exam.getExamTime();
        init();

        if (what == TAKE_EXAM) {
            sample = exam.getSamples()[new Random().nextInt(exam.getSamples().length-1)];
            getSamples(sample.getSampleId());
        }

        else if (what == ADD_SAMPLE)
            enterNewSample();
        else if (what >=0) {
            sample = exam.getSamples()[what];
            getSamples(sample.getSampleId());
        }

        else finish();
    }

    private void enterNewSample() {

        questionArray = new Question[exam.getQuestionNumber()];
        findViewById(R.id.et_sample_name).setVisibility(View.VISIBLE);
        showQuesFragment(true);
    }

    private void getSamples(int examId) {
        getSamples(examId, new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                JSONArray sampleJsonArray = result.getJSONArray("JA");

                Gson gson = new GsonBuilder().create();
                questionArray = gson.fromJson(sampleJsonArray.toString(), Question[].class);

                showQuesFragment(false);
                if (!(timeLeftInSeconds == null || timeLeftInSeconds == 0))
                    startCountDown();

            }

            @Override
            public void onSuccess(JSONArray result) throws JSONException {
            }

            @Override
            public void onError(String result) throws Exception {
                Toast.makeText(QuesExamActvity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuesFragment(boolean isEnter) {
        int count = questionArray.length;
        progressBar.setMax(count);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return QuesEntryFragment.getInstence(position, questionArray[position], isEnter);
            }

            @Override
            public int getCount() {
                return count;
            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            private int mCurrentSelectedPage = 0;

            @Override
            public void onPageSelected(int position) {
                if (position > mCurrentSelectedPage &&
                        ((QuesEntryFragment) pager.getAdapter().instantiateItem(pager, mCurrentSelectedPage)).notValidate()) {
                    pager.setCurrentItem(mCurrentSelectedPage, true);
                } else {
                    mCurrentSelectedPage = position;
                    progressBar.setProgress(mCurrentSelectedPage + 1);
                    if (position == questionArray.length - 1)
                        btnFinish.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
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
                par.put("sample_id", "" + examId);
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
        VolleyController.getInstance(QuesExamActvity.this).addToRequestQueue(stringRequest);

    }


    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInSeconds * 1000, 1000) {

            @Override
            public void onTick(long lTime) {
                timeLeftInSeconds = (int) lTime;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInSeconds = 0;
                updateCountDownText();
                finishExam();
            }
        }.start();


    }

    private void finishExam() {
        FinishExamDialog finishExamDialog = new FinishExamDialog();
        Bundle bundle = new Bundle();
        String result = "The Exam is finish You got " + getScore() + "/" + questionArray.length;
        bundle.putString("RESULT", result);
        finishExamDialog.setArguments(bundle);
        uploadResult();
        finishExamDialog.show(getSupportFragmentManager(), "Your result");
           }

    private void uploadResult() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.ADD_EXAM_HISTORY,
                response -> {
                },
                error -> {
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                int score=getScore();

                parameter.put("user_id", LoginSharedPreferences.getUserId(QuesExamActvity.this)+"");
                parameter.put("sample_id", sample.getSample_id()+"");
                parameter.put("date", new Date()+"");
                parameter.put("score", score+"");
                parameter.put("status", "1");
                return parameter;

            }


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                while (Constants.COOKIES == null) ;
                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };

        VolleyController.getInstance(QuesExamActvity.this).addToRequestQueue(request);

    }


    private int getScore() {
       int markPerQues=exam.getFullMarks()/exam.getQuestionNumber();
        int s = 0;
        for (Question q : questionArray)
            if (q.gotCorrect)
                s+=markPerQues;

        return s;
    }

    private void updateCountDownText() {
        int minuts = (int) (timeLeftInSeconds / 1000) / 60;
        int secound = (int) (timeLeftInSeconds / 1000) % 60;
        String timeFormated = String.format(Locale.getDefault(), "%02d:%02d", minuts, secound);
        if (secound < 5)
            tvTimer.setTextColor(Color.RED);
        tvTimer.setText(timeFormated);

    }

    public void init() {
        btnFinish = findViewById(R.id.finish_exam);
        tvTimer = findViewById(R.id.time_text);
        pager = findViewById(R.id.pager);
        progressBar = findViewById(R.id.progress);
        mNext = findViewById(R.id.next);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishExam();
            }
        });
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
        questionArray[index] = question;
        pager.getAdapter().notifyDataSetChanged();
        progressBar.setProgress(index);
    }

    @Override
    public void setAnswer(boolean isRight, int index) {
        questionArray[index].gotCorrect = isRight;
       // Toast.makeText(this, index + " is " + isRight, Toast.LENGTH_SHORT).show();
    }
}
