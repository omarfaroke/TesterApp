package com.codingacademy.testerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codingacademy.testerapp.model.Ques;

public class QuesFragment extends Fragment {
    TextView textView;


    RadioButton radioButton1, radioButton2, radioButton3;

    public static Fragment getInstence(Ques ques) {
        QuesFragment pagerFragment1 = new QuesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Ques", ques);
        pagerFragment1.setArguments(bundle);
        return pagerFragment1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.viewpager1, container, false);
        textView = v.findViewById(R.id.text1);
        radioButton1 = v.findViewById(R.id.radio1);
        radioButton2 = v.findViewById(R.id.radio2);
        radioButton3 = v.findViewById(R.id.radio3);


        Ques ques = (Ques) getArguments().getSerializable("Ques");
        textView.setText(ques.getQues());
        radioButton1.setText(ques.getAnswers().get(0));
        radioButton2.setText(ques.getAnswers().get(1));
        radioButton3.setText(ques.getAnswers().get(2));



        return v;


    }

}







