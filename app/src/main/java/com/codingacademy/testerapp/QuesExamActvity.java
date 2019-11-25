package com.codingacademy.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.codingacademy.testerapp.model.Choice;
import com.codingacademy.testerapp.model.Exam;
import com.codingacademy.testerapp.model.Question;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuesExamActvity extends AppCompatActivity {


    ProgressBar progressBar;

    private int questionCounter;
    private int questionCountTotal;
List<Question> quesArrayList;
List<Choice> choices;
    private ViewPager pager;
    TextView textView;

    public static final long COUNTDOWN_TIME = 30000;
    private ColorStateList textColorDefault;
    private CountDownTimer countDownTimer;
    private long timeLeftInMills=10000;
    // int counter =0;

    ImageView mNext, mPre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
       fill();


        progressBar = findViewById(R.id.progress);


        // View v=LayoutInflater.from(this).inflate(R.layout.fragment_ques,null);
        mNext = findViewById(R.id.next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // showNextQuestion();
              //  startCountDown();
                 pager.setCurrentItem(pager.getCurrentItem() + 1);

                Toast.makeText(QuesExamActvity.this, "test on click", Toast.LENGTH_SHORT).show();
            }
        });
        mPre = findViewById(R.id.pre);

        init();
    }




    private void startCountDown() {
       countDownTimer = new CountDownTimer(timeLeftInMills,1000){

           @Override
           public void onTick(long lTime) {
               timeLeftInMills = lTime;
               updateCountDownText();

           }

           @Override
           public void onFinish() {
   timeLeftInMills = 0;
   updateCountDownText();
   finish();
           }
       }.start();


    }
    private  void  updateCountDownText(){

   int minuts= (int) (timeLeftInMills/1000)/60;
   int secound= (int) (timeLeftInMills/1000)%60;
   String timeFormated = String.format(Locale.getDefault(),"%02d:%02d",minuts,secound);
   if(secound<5)
       textView.setTextColor(Color.RED);
   textView.setText(timeFormated);

    }
void fill()
{
     choices = new ArrayList<>();
    choices.add(new Choice("Ansers"));
    choices.add(new Choice("Ansers"));
    choices.add(new Choice("Ansers"));
    choices.add(new Choice("Ansers"));
    choices.add(new Choice("Ansers"));
    choices.add(new Choice("Ansers"));
    choices.add(new Choice("Ansers"));
    choices.add(new Choice("Ansers"));
    quesArrayList = new ArrayList<>();
    quesArrayList.add(new Question("1zzzzzzzz", choices));
    quesArrayList.add(new Question("2zzzzzzzz", choices));
    quesArrayList.add(new Question("3zzzzzzzz", choices));
    quesArrayList.add(new Question("4zzzzzzzz", choices));
}

    public void init() {
textView=findViewById(R.id.time_text);
        pager = findViewById(R.id.pager);
        //  adapter= new Adapter(getSupportFragmentManager(),quesArrayList);
        int count=quesArrayList.size();
        int prssent=100/count;
        progressBar.setMax(count);
        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                progressBar.setProgress(position);
                return QuesFragment.getInstence(quesArrayList.get(position));
            }

            @Override
            public int getCount() {
                return count;
            }
        });



        mPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(pager.getCurrentItem() - 1);

            }
        });

        startCountDown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!= null){
            countDownTimer.cancel();

        }
    }
}
