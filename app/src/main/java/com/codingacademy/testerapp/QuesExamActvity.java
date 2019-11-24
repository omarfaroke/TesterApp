package com.codingacademy.testerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.codingacademy.testerapp.model.Ques;

import java.util.ArrayList;
import java.util.Locale;

public class QuesExamActvity extends AppCompatActivity {
    ArrayList<Ques> quesArrayList = new ArrayList<>();

    ProgressBar progressBar;

    private int questionCounter;
    private int questionCountTotal;
    private Ques currentQuestion;
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
        setContentView(R.layout.activity_main);
        ArrayList<String> ansers = new ArrayList<>();
        ansers.add("1dd");
        ansers.add("2dd");
        ansers.add("3dd");
        quesArrayList.add(new Ques("1zzzzzzzz", ansers));
        quesArrayList.add(new Ques("2zzzzzzzz", ansers));
        quesArrayList.add(new Ques("3zzzzzzzz", ansers));
        quesArrayList.add(new Ques("4zzzzzzzz", ansers));

        progressBar = findViewById(R.id.progress);
   //     textColorDefault= textView.getTextColors();




        // View v=LayoutInflater.from(this).inflate(R.layout.viewpager1,null);
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
           }
       }.start();


    }
    private  void  updateCountDownText(){

   int minuts= (int) (timeLeftInMills/1000)/60;
   int secound= (int) (timeLeftInMills/1000)%60;
   String timeFormated = String.format(Locale.getDefault(),"%02d:%02d",minuts,secound);
   textView.setText(timeFormated);

    }


    public void init() {
textView=findViewById(R.id.time_text);
        pager = findViewById(R.id.pager);
        //  adapter= new Adapter(getSupportFragmentManager(),quesArrayList);
        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return QuesFragment.getInstence(quesArrayList.get(position));
            }

            @Override
            public int getCount() {
                return quesArrayList.size();
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
