package com.codingacademy.testerapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codingacademy.testerapp.model.Choice;
import com.codingacademy.testerapp.model.Question;

public class QuesFragment extends Fragment {
    TextView textView;
    Context context;

    RadioGroup radioGroup;

    public static Fragment getInstence(Question ques) {
        QuesFragment quesFragment = new QuesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Ques", ques);
        quesFragment.setArguments(bundle);
        return quesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ques, container, false);
        textView = v.findViewById(R.id.text1);
        radioGroup = v.findViewById(R.id.radio_choices);


        Question ques = (Question) getArguments().getSerializable("Ques");
        Choice[] choices = ques.getChoices();

        int radioNumber = choices.length;

        for (int i = 0; i < radioNumber; i++) {

            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(choices[i].getChoiceText());

            boolean isRight = choices[i].getAnswer() == 1;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((QuesExamActvity) context).setAnswer(isRight);

                }
            });
            radioGroup.addView(radioButton);
        }
        textView.setText(ques.getQuesText());


        return v;


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}







