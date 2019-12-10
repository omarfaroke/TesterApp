package com.codingacademy.testerapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.codingacademy.testerapp.model.Choice;
import com.codingacademy.testerapp.model.Question;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class QuesEntryFragment extends Fragment implements View.OnClickListener {
    private EditText etQues, etChoice;
    private Button btChoice;
    private Context context;
    private Question ques;
    private RadioGroup radioGroup;
    private LinearLayout layAddChoice;
    private ArrayList<RadioButton> radioButtons;
    private CardView cardView;


    private int index;
    private QuesFragmentActionListener mListener;
    private static final String INDEX = "INDEX";
    private static final String QUES = "QUES";
    private static final String IS_ENTER = "ENTER";
    boolean editState = false;
    private boolean isRight = false;

    @Override
    public void onClick(View view) {

    }


    interface QuesFragmentActionListener {
        void addQues(Question question, int index);

        void setAnswer(boolean isRight, int index);
    }


    public static Fragment getInstence(int position, Question ques, boolean isEnter) {
        QuesEntryFragment quesEntryFragment = new QuesEntryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, position);
        bundle.putSerializable(QUES, ques);
        bundle.putBoolean(IS_ENTER, isEnter);
        quesEntryFragment.setArguments(bundle);
        return quesEntryFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ques_entry, container, false);
        etQues = v.findViewById(R.id.et_ques);
        etChoice = v.findViewById(R.id.et_choice);
        btChoice = v.findViewById(R.id.bt_choice);

        layAddChoice = v.findViewById(R.id.add_choice);
        radioButtons = new ArrayList<>();
        cardView = v.findViewById(R.id.radio_cards);
        index = getArguments().getInt(INDEX);
        ques = (Question) getArguments().getSerializable(QUES);
        editState = getArguments().getBoolean(IS_ENTER);
        radioGroup = new RadioGroup(context);
        cardView.addView(radioGroup);
        if (ques != null)
            setQuestion();


        if (editState) {
            editQuestion();

        }

        return v;
    }

    boolean gotAnswer = false;


    private void fillChoices() {

    }

    private void editQuestion() {
        {

            layAddChoice.setVisibility(View.VISIBLE);
            etQues.setEnabled(true);
            btChoice.setOnClickListener(view -> {
                String s = etChoice.getText().toString();
                if (s.isEmpty())
                    Snackbar.make(getView(), "Please chose category", Snackbar.LENGTH_SHORT).show();
                else
                    addChoice(s);
            });
        }
    }


    void setQuestion() {

        etQues.setText(ques.getQuesText());
        Choice[] choices = ques.getChoices();
        int radioNumber = choices.length;
        for (int choiceIndex = 0; choiceIndex < radioNumber; choiceIndex++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(choices[choiceIndex].getChoiceText());
            int finalChoiceIndex = choiceIndex;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isRight = choices[finalChoiceIndex].getAnswer() == 1;
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
            });
            radioButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.dialog_delet_choice)
                            .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    radioGroup.removeView(radioButton);
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create().show();

                    return false;
                }
            });
            radioButtons.add(radioButton);

            radioGroup.addView(radioButton);

        }

    }


    void addChoice(String s) {
        RadioButton radioButton = new RadioButton(getActivity());
        radioButton.setText(s);
        radioButtons.add(radioButton);
        radioGroup.addView(radioButton);
        etChoice.setText("");
    }

    boolean notValidate() {
        if (!editState) {
            mListener.setAnswer(isRight, index);
            return false;
        }
        String s = etQues.getText().toString();
        int c = radioButtons.size();
        if (c < 2 || s.isEmpty()) {
            Toast.makeText(getActivity(), "Qutetion and two choices at least must be entered", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Choice[] choices = new Choice[c];
            int countAnswer = 0;
            for (int i = 0; i < c; i++) {
                RadioButton radioButton = radioButtons.get(i);
                int answer = radioButton.isChecked() ? 1 : 0;
                countAnswer += answer;
                choices[i] = new Choice(null, radioButton.getText().toString(), null, answer);
            }
            if (countAnswer != 1) {
                Toast.makeText(getActivity(), "Choose right answer", Toast.LENGTH_SHORT).show();
                return true;
            }
            Question question = new Question(s, choices);
            mListener.addQues(question, index);
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        mListener = (QuesFragmentActionListener) context;
    }
}
