package com.codingacademy.testerapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class FinishExamDialog extends AppCompatDialogFragment {
    TextView mTextQuestion;

    TextView mTextQuestionTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        String result = getArguments().getString("RESULT");
        View view = inflater.inflate(R.layout.finish_exam_dialog, null);
        builder.setView(view)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                });


        mTextQuestion = view.findViewById(R.id.question_number);
        mTextQuestion.setText(result);
        mTextQuestionTime = view.findViewById(R.id.question_time);


        return builder.create();
    }


}
