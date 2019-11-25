package com.codingacademy.testerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codingacademy.testerapp.model.Question;

public class QuesFragment extends Fragment {
    TextView textView;


    RadioGroup radioGroup;

    public static Fragment getInstence(Question ques) {
        QuesFragment pagerFragment1 = new QuesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Ques", ques);
        pagerFragment1.setArguments(bundle);
        return pagerFragment1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_ques, container, false);
        textView = v.findViewById(R.id.text1);
        radioGroup=v.findViewById(R.id.radio_choices);



        Question ques = (Question) getArguments().getSerializable("Ques");

        int radioNumber=ques.getChoices().size();

        for(int i=0;i<radioNumber;i++)
        {

            RadioButton radioButton=new RadioButton(getActivity());
            radioButton.setText(ques.getChoices().get(i).getChoiceText());
            radioGroup.addView(radioButton);
        }
        textView.setText(ques.getQuesText());




        return v;


    }

}







