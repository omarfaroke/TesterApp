package com.codingacademy.testerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.model.Choice;
import com.codingacademy.testerapp.model.Question;
import com.codingacademy.testerapp.requests.StatusCallback;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class QuesEntryFragment extends Fragment {
    private EditText etQues, etChoice;
    private Button btChoice;
    private Context context;
    private Question question;
    private RadioGroup radioGroup;
    private LinearLayout layAddChoice;
    private ArrayList<RadioButton> radioButtons;
    private CardView cardView;


    private int index;
    private QuesFragmentActionListener mListener;
    private static final String INDEX = "INDEX";
    private static final String QUES = "QUES";
    private static final String STATE = "ENTER";
    private boolean isRight = false;
    private int state;
    private boolean editState=false;


    interface QuesFragmentActionListener {
        void addQues(Question question, int index);

        void setAnswer(boolean isRight, int index);
    }


    public static Fragment getInstence(int position, Question ques, int state) {
        QuesEntryFragment quesEntryFragment = new QuesEntryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX, position);
        bundle.putSerializable(QUES, ques);
        bundle.putInt(STATE, state);
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
        radioGroup = v.findViewById(R.id.radio_choices);

        index = getArguments().getInt(INDEX);
        state = getArguments().getInt(STATE);

        if (state == QuesExamActvity.ADD_SAMPLE) {
            editQuestion();
        } else {
            question = (Question) getArguments().getSerializable(QUES);
            setQuestion();
            if (LoginSharedPreferences.getUserType(getActivity()) == Constants.USER_TYPE_EXAMINER && state >= 0)
                canEdit(v);
        }


        return v;
    }

    private void canEdit(View v) {
        Button btnEdit = v.findViewById(R.id.edit_Quiz);
        btnEdit.setVisibility(View.VISIBLE);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnEdit.getText().equals("Edit")) {
                    editQuestion();
                    btnEdit.setText("Save");//Edit
                } else {
                    if (!notValidate()) {
                        upDateQuestion(new VolleyCallback(){

                            @Override
                            public void onSuccess(JSONObject result) throws JSONException {
                                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();

                                for (RadioButton r : radioButtons)
                                    r.setOnLongClickListener(null);
                                layAddChoice.setVisibility(View.GONE);
                                etQues.setEnabled(false);
                                mListener.addQues(question, index);
                                btnEdit.setText("Edit");//Save
                                editState=false;

                            }

                            @Override
                            public void onSuccess(JSONArray result) throws JSONException {

                            }

                            @Override
                            public void onError(String result) throws Exception {
                                Toast.makeText(getActivity(), "Erorr connecting ", Toast.LENGTH_LONG).show();

                            }
                        });

                    }

                }
            }
        });

    }

    private void upDateQuestion(VolleyCallback volleyCallback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.UPDATE_QUTETION,
                response -> {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        volleyCallback.onSuccess(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    try {
                        volleyCallback.onError(error.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                String quesString = new Gson().toJson(question);
                parameter.put("question", quesString);
                return parameter;

            }


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                //while (Constants.COOKIES == null) ;
                //map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };

        VolleyController.getInstance(getActivity()).addToRequestQueue(request);
    }


    private void editQuestion() {
        editState=true;
        View.OnLongClickListener deletChoice = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.dialog_delet_choice)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                radioGroup.removeView(view);
                                radioButtons.remove(view);
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
                return false;
            }
        };
        for (RadioButton r : radioButtons)
            r.setOnLongClickListener(deletChoice);
        layAddChoice.setVisibility(View.VISIBLE);
        etQues.setEnabled(true);
        btChoice.setOnClickListener(view -> {
            String s = etChoice.getText().toString();
            if (s.isEmpty())
                Snackbar.make(getView(), "Please chose category", Snackbar.LENGTH_SHORT).show();
            else {
                RadioButton radioButton = addChoice(s);
                etChoice.setText("");
                radioButton.setOnLongClickListener(deletChoice);
            }
        });

    }


    void setQuestion() {
        editState=false;
        etQues.setText(question.getQuesText());
        Choice[] choices = question.getChoices();
        int radioNumber = choices.length;
        for (int choiceIndex = 0; choiceIndex < radioNumber; choiceIndex++) {
            RadioButton radioButton = addChoice(choices[choiceIndex].getChoiceText());
            int finalChoiceIndex = choiceIndex;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isRight = choices[finalChoiceIndex].getAnswer() == 1;
                }
            });
        }
    }


    RadioButton addChoice(String s) {
        RadioButton radioButton = new RadioButton(getActivity());
        radioButton.setText(s);
        radioButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        radioButtons.add(radioButton);
        radioGroup.addView(radioButton);
        return radioButton;
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
            if (state == QuesExamActvity.ADD_SAMPLE) {
                mListener.addQues(question, index);
                return false;
            }
            else if(editState){
                this.question.setChoices(choices);
                this.question.setQuesText(s);
                return false;
            }
            return true;

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        mListener = (QuesFragmentActionListener) context;
    }
}
