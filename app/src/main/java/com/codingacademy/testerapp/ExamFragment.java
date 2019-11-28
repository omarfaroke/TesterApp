package com.codingacademy.testerapp;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.model.Exam;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExamFragment extends Fragment {
    public static final String TAG = "ExamFragment";
    public static final String EXAM_OBJECT = "EXAM_OBJECT";

    private RecyclerView recyclerExam;
    private Exam[] examsArr;
    private ExamAdapter examAdapter;
    private int currentCategory;
    private ExamFragmentActionListener mListener;




    interface ExamFragmentActionListener {
        void restartFocusView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExamFragment.ExamFragmentActionListener) {
            mListener = (ExamFragment.ExamFragmentActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(CategoryFragment.CATEGORY_ID)) {
                currentCategory = getArguments().getInt(CategoryFragment.CATEGORY_ID);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exam, container, false);
        initView(v);

        getExam(currentCategory);

        return v;
    }

    void getExam(int catID) {
        getExam(catID, new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                JSONArray examJsonArray = result.getJSONArray("JA");

                //  examsArr = new Exam[examJsonArray.length()];
                Gson gson = new GsonBuilder().create();
                examsArr = gson.fromJson(examJsonArray.toString(), Exam[].class);

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
                par.put(CategoryFragment.CATEGORY_ID, "" + cat_id);
                return par;
            }
        };
        VolleyController.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }


//    void fillExam() {
//        examsArr = new Exam[4];
//        examsArr[0] = (new Exam(null, null, " Java Exam name", null, null, null, null, null, null));
//        examsArr[1] = (new Exam(null, null, " Java Exam name", null, null, null, null, null, null));
//        examsArr[2] = (new Exam(null, null, " Java Exam name", null, null, null, null, null, null));
//        examsArr[3] = (new Exam(null, null, " Java Exam name", null, null, null, null, null, null));
//        upDateExam();
//    }

    private void upDateExam() {
        if (examAdapter == null) {
            examAdapter = new ExamAdapter();
            recyclerExam.setAdapter(examAdapter);
        }
        examAdapter.notifyDataSetChanged();
    }


    private void initView(View v) {

        recyclerExam = v.findViewById(R.id.recyclerExam);
        recyclerExam.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerExam.addItemDecoration(new SpacingItemDecoration(1, dpToPx(getActivity(), 8), true));

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
            Exam exam = examsArr[position];
            holder.examName.setText(exam.getExamName());
            holder.examDesc.setText(exam.getExamDescription());
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), QuesExamActvity.class);
                intent.putExtra(EXAM_OBJECT, exam);
                startActivity(intent);
            });
            MenuDrawerNews.animateFadeIn(holder.itemView, position);

            holder.btExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!exam.expanded, v, holder.lyt_expand);
                    exam.expanded = show;
                }
            });
            if(exam.expanded){
                holder.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                holder.lyt_expand.setVisibility(View.GONE);
            }
            toggleArrow(exam.expanded, holder.btExpand, false);

        }
        private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
            toggleArrow(show, view,true);
            if (show)
                lyt_expand.startAnimation(expand(lyt_expand));
            else
               collapse(lyt_expand);

            return show;
        }
        public  void collapse(final View v) {
            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if (interpolatedTime == 1) {
                        v.setVisibility(View.GONE);
                    } else {
                        v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
        }

        public boolean toggleArrow(boolean show, View view, boolean delay) {
            if (show) {
                view.animate().setDuration(delay ? 200 : 0).rotation(180);
                return true;
            } else {
                view.animate().setDuration(delay ? 200 : 0).rotation(0);
                return false;
            }
        }
        private Animation expand(final View v) {
            v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int targtetHeight = v.getMeasuredHeight();

            v.getLayoutParams().height = 0;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1
                            ? ViewGroup.LayoutParams.WRAP_CONTENT
                            : (int) (targtetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
            return a;
        }

        @Override
        public int getItemCount() {
            return examsArr.length;
        }

        private class ExamVH extends RecyclerView.ViewHolder {
            TextView examName,examDesc;
            public ImageButton btExpand;
            public View lyt_expand;

            public ExamVH(@NonNull View itemView) {
                super(itemView);
                examName = itemView.findViewById(R.id.exam_name);
                examDesc = itemView.findViewById(R.id.exam_desc);
                btExpand = itemView.findViewById(R.id.bt_expand);
                lyt_expand = itemView.findViewById(R.id.lyt_expand);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

    }


    @Override
    public void onDestroyView() {
        getView().setFocusableInTouchMode(false);
        getView().clearFocus();

        if (mListener != null) {
            mListener.restartFocusView();
        }

        super.onDestroyView();
    }
}
