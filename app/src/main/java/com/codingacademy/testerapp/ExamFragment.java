package com.codingacademy.testerapp;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.model.Exam;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExamFragment extends Fragment {
    RecyclerView recyclerExam;
    Exam[] examsArr;
    ExamAdapter examAdapter;
    int cat_id = 3;

    public ExamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exam, container, false);
        intViwes(v);
       fillExam();
    //  getExam();

        return v;
    }
void getExam(){
    getExam(cat_id, new VolleyCallback() {

        @Override
        public void onSuccess(JSONObject result) throws JSONException {

            JSONArray examJsonArray = result.getJSONArray("JA");

            examsArr = new Exam[examJsonArray.length()];
            Gson gson = new GsonBuilder().create();
            examsArr = gson.fromJson(examJsonArray.toString(), Exam[].class);
            int i=examsArr[0].getCategoryId();
            upDateExam();
        }

        @Override
        public void onError(String result) throws Exception {
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        }
    });
}
    private void getExam(int cat_id, final VolleyCallback mCallback) {
        String url = Constants.GET_EXAM;

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
                par.put("category_id", "" + cat_id);
                return par;
            }
        };
        VolleyController.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    void fillExam() {
        examsArr = new Exam[4];
        examsArr[0] = (new Exam(null, null, " Java Exam name", null, null, null, null,null,null));
        examsArr[1] = (new Exam(null, null, " Java Exam name", null, null, null, null,null,null));
        examsArr[2] = (new Exam(null, null, " Java Exam name", null, null, null, null,null,null));
        examsArr[3] = (new Exam(null, null, " Java Exam name", null, null, null, null,null,null));
upDateExam();
    }

    private void intViwes(View v) {
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Toast.makeText(getContext(), "key", Toast.LENGTH_SHORT).show();

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(getContext(), "back", Toast.LENGTH_SHORT).show();
                    ((MenuDrawerNews) getActivity()).showCategory();
                    return true;
                }
                return false;
            }
        });
        recyclerExam = v.findViewById(R.id.recyclerExam);
        recyclerExam.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerExam.addItemDecoration(new SpacingItemDecoration(1, dpToPx(getActivity(), 8), true));
    }

    private void upDateExam() {
        if (examAdapter == null) {
            examAdapter = new ExamAdapter();
            recyclerExam.setAdapter(examAdapter);
        }
        examAdapter.notifyDataSetChanged();
    }


    public int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamVH> {

        @NonNull
        @Override
        public ExamVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ExamVH(LayoutInflater.from(getActivity()).inflate(R.layout.temp_exam, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ExamVH holder, int position) {
            holder.examName.setText(examsArr[position].getExamName());
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), QuesExamActvity.class);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return examsArr.length;
        }

        private class ExamVH extends RecyclerView.ViewHolder {
            TextView examName;

            public ExamVH(@NonNull View itemView) {
                super(itemView);
                examName = itemView.findViewById(R.id.exam_name);
            }
        }
    }
}
