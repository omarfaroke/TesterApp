package com.codingacademy.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.model.Category;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class QuesExamActvity extends AppCompatActivity  {


    ProgressBar progressBar;

    private int questionCounter;
    private int questionCountTotal;
    List<Question> quesArrayList;
    List<Choice> choices;
    Sample sampleArray[];
    private ViewPager pager;
    TextView textView;

    public static final long COUNTDOWN_TIME = 30000;
    private ColorStateList textColorDefault;
    private CountDownTimer countDownTimer;
    private Integer timeLeftInSeconds = 60;
    private int scorePerQues,totalScore=0;

    // int counter =0;


    ImageView mNext, mPre;
    int position;
   public void setAnswer(boolean isRight){
        quesArrayList.get(position).gotCorrect=isRight;
       Toast.makeText(this, position+" is "+isRight, Toast.LENGTH_SHORT).show();
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        Exam exam = (Exam) getIntent().getSerializableExtra(ExamFragment.EXAM_OBJECT);


        int examId = exam.getExamId();
        timeLeftInSeconds = exam.getExamTime();
        init();
        getSamples(examId);
    }

    private void getSamples(int examId) {
        getSamples(examId, new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                JSONArray sampleJsonArray = result.getJSONArray("JA");

                //  allProArray = new Exam[examJsonArray.length()];
                Gson gson = new GsonBuilder().create();
                sampleArray = gson.fromJson(sampleJsonArray.toString(), Sample[].class);

                //s=random sample from sampleArray

                int sampleNumber = sampleArray.length;
                if(sampleNumber>0){
                quesArrayList = Arrays.asList(sampleArray[0].getQuestions());
                upDateSample();
                }
            }

            @Override
            public void onError(String result) throws Exception {
                Toast.makeText(QuesExamActvity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upDateSample() {
        int count = quesArrayList.size();
        progressBar.setMax(count);
;
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return QuesFragment.getInstence(quesArrayList.get(position));
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

            @Override
            public void onPageSelected(int position) {
                progressBar.setProgress(position);
                setPosition(position);

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
                par.put("exam_id", "" + examId);
                return par;
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
       FinishExamDialog finishExamDialog=new FinishExamDialog();
       Bundle bundle=new Bundle();
       String result="The Exam is finish You got "+getScore();
       bundle.putString("RESULT",result);
       finishExamDialog.setArguments(bundle);
       uploadResult();

       finishExamDialog.show(getSupportFragmentManager(),"Your result");

    }
    private void uploadResult() {

        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.ADD_EXAM_HISTORY,
                response -> {



                },
                error -> {


                }){  @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            Map<String, String> parameter = new HashMap<>();

            parameter.put("user_id","7");
            parameter.put("sample_id","1");
            parameter.put("date","2020");
            parameter.put("score","99");
            parameter.put("status","1");

            return parameter;

        }

        };

        VolleyController.getInstance(QuesExamActvity.this).addToRequestQueue(request);

    }


    private String getScore() {
       int s=0;
       for(Question q:quesArrayList)
           if(q.gotCorrect)
               s++;
           return s+"/"+quesArrayList.size();
    }

    private void updateCountDownText() {

        int minuts = (int) (timeLeftInSeconds / 1000) / 60;
        int secound = (int) (timeLeftInSeconds / 1000) % 60;
        String timeFormated = String.format(Locale.getDefault(), "%02d:%02d", minuts, secound);
        if (secound < 5)
            textView.setTextColor(Color.RED);
        textView.setText(timeFormated);

    }

    void fill() {
//     choices = new ArrayList<>();
//    choices.add(new Choice("Ansers"));
//    choices.add(new Choice("Ansers"));
//    choices.add(new Choice("Ansers"));
//    choices.add(new Choice("Ansers"));
//    choices.add(new Choice("Ansers"));
//    choices.add(new Choice("Ansers"));
//    choices.add(new Choice("Ansers"));
//    choices.add(new Choice("Ansers"));
//    quesArrayList = new ArrayList<>();
//    quesArrayList.add(new Question("1zzzzzzzz", choices));
//    quesArrayList.add(new Question("2zzzzzzzz", choices));
//    quesArrayList.add(new Question("3zzzzzzzz", choices));
//    quesArrayList.add(new Question("4zzzzzzzz", choices));
    }

    public void init() {
        textView = findViewById(R.id.time_text);
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
            startCountDown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();

        }
    }
}
