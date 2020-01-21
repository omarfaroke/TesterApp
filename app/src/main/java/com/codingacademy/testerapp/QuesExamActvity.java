package com.codingacademy.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class QuesExamActvity extends AppCompatActivity implements QuesEntryFragment.QuesFragmentActionListener {


    ProgressBar progressBar;


    Question questionArray[];
    private ViewPager pager;
    TextView tvTimer;
    TextView tvConter;
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
    private int STATE;
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
         STATE = getIntent().getIntExtra(WHAT, -1);

        timeLeftInSeconds = exam.getExamTime();
        init();

        if (STATE == TAKE_EXAM) {
            int size = exam.getSamples().length;
            int r = new Random().nextInt(size);


            sample = exam.getSamples()[r];


            getSample(sample.getSampleId());
        }

        else if (STATE == ADD_SAMPLE)
            enterNewSample();
        else if (STATE >= 0) {
            sample = exam.getSamples()[STATE];
            getSample(sample.getSampleId());
        } else finish();
    }



    private void enterNewSample() {

        questionArray = new Question[exam.getQuestionNumber()];
        findViewById(R.id.et_sample_name).setVisibility(View.VISIBLE);
        showQuesFragment(STATE);
    }

    private void getSample(int sampleId) {
        getSample(sampleId, new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                JSONArray sampleJsonArray = result.getJSONArray("JA");

                Gson gson = new GsonBuilder().create();
                questionArray = gson.fromJson(sampleJsonArray.toString(), Question[].class);

                showQuesFragment(STATE);
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

    private void showQuesFragment(int state) {
        int count = questionArray.length;
        progressBar.setMax(count);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return QuesEntryFragment.getInstence(position, questionArray[position], state);
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
private int size=questionArray.length;
            private int mCurrentSelectedPage = 0;

            @Override
            public void onPageSelected(int position) {
                if (position > mCurrentSelectedPage &&
                        ((QuesEntryFragment) pager.getAdapter().instantiateItem(pager, mCurrentSelectedPage)).notValidate()) {
                    pager.setCurrentItem(mCurrentSelectedPage, true);
                } else {
                    mCurrentSelectedPage = position;
                    progressBar.setProgress(mCurrentSelectedPage + 1);
                    tvConter.setText(mCurrentSelectedPage+1+"/"+exam.getQuestionNumber());
                    if (position == size - 1) {
                        btnFinish.setVisibility(View.VISIBLE);
                        mNext.setVisibility(View.INVISIBLE);
                    }
                    else if (position < mCurrentSelectedPage && position == size - 2) {
                        btnFinish.setVisibility(View.INVISIBLE);
                        mNext.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void getSample(int sampleId, final VolleyCallback mCallback) {
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
                par.put("sample_id", "" + sampleId);
                return par;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
//                while (Constants.COOKIES == null) ;
//                map.put("Cookie", Constants.COOKIES);
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

        if(STATE==TAKE_EXAM||STATE>=0) {


            if(exam.getExaminerID()==LoginSharedPreferences.getUserId(QuesExamActvity.this))
                exitExam("Right Answers => "+ getScore() + "/" + exam.getFullMarks());
            else
            uploadResult();
        }
        else if(STATE==ADD_SAMPLE){
            uploadSample();



        }

    }

    private void exitExam(String result){
        FinishExamDialog finishExamDialog = new FinishExamDialog();
        Bundle bundle = new Bundle();
        bundle.putString("RESULT", result);
        finishExamDialog.setArguments(bundle);
        finishExamDialog.setCancelable(false);
        finishExamDialog.show(getSupportFragmentManager(), "Your result");

    }
    private void exitExam(String result,Sample sample){
        FinishExamDialog finishExamDialog = new FinishExamDialog();
        Bundle bundle = new Bundle();
        bundle.putString("RESULT", result);
        bundle.putSerializable("Sample", sample);
        finishExamDialog.setArguments(bundle);
        finishExamDialog.setCancelable(false);
        finishExamDialog.show(getSupportFragmentManager(), "Your result");

    }

    private void uploadSample() {

        Sample sample=new Sample(null,etSampleName.getText().toString(),1,"date",exam.getExamId(),questionArray);

        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.ADD_SAMPLE,
                response -> {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        sample.setSampleId(jsonObject.getInt("sample_id"));
                        exitExam("You Added new sample ",sample);
                        Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        btnFinish.setEnabled(true);
                        e.printStackTrace();
                    }


                },
                error -> {
            btnFinish.setEnabled(true);
                    Toast.makeText(QuesExamActvity.this,"Sorry no Connection pless try again",Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                String sampleString = new Gson().toJson(sample);
                parameter.put("sample",sampleString);
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

        VolleyController.getInstance(QuesExamActvity.this).addToRequestQueue(request);
    }

    private void uploadResult() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.ADD_EXAM_HISTORY,
                response -> {
            exitExam("The Exam is finish You got " + getScore() + "/" + exam.getFullMarks());
                },
                error -> {
            btnFinish.setEnabled(true);
            Toast.makeText(QuesExamActvity.this,"Sorry no Connection plese try again",Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                float score = getScore();
                parameter.put("user_id", LoginSharedPreferences.getUserId(QuesExamActvity.this) + "");
                parameter.put("sample_id", sample.getSample_id() + "");
                parameter.put("date",  "2020");
                parameter.put("score", score + "");
                parameter.put("status", "1");
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

        VolleyController.getInstance(QuesExamActvity.this).addToRequestQueue(request);

    }


    private float getScore() {
        float markPerQues = exam.getFullMarks() / exam.getQuestionNumber();
        float s = 0;
        for (Question q : questionArray)
            if (q.gotCorrect)
                s += markPerQues;
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
        etSampleName=findViewById(R.id.et_sample_name);
        btnFinish = findViewById(R.id.finish_exam);
        tvTimer = findViewById(R.id.time_text);
        tvConter = findViewById(R.id.count_text);
        tvConter.setText("1/"+exam.getQuestionNumber());
        pager = findViewById(R.id.pager);
        progressBar = findViewById(R.id.progress);
        mNext = findViewById(R.id.next);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!((QuesEntryFragment) pager.getAdapter().instantiateItem(pager, questionArray.length - 1)).notValidate())
                {   view.setEnabled(false);
                finishExam();
            }
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
    }
}
