package com.codingacademy.testerapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codingacademy.testerapp.model.Exam;
import com.codingacademy.testerapp.requests.CacheRequest;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExamFragment extends Fragment {
    public static final String TAG = "ExamFragment";
    public static final String EXAM_OBJECT = "EXAM_OBJECT";
    public static final int ALL_EXAM = 0;
    public static final int MY_EXAM = -101;


    private RecyclerView recyclerExam;
    private TextView textNoNet;
    private Exam[] examsArr;
    private ExamAdapter examAdapter;
    private int currentCategory;
    private ExamFragmentActionListener mListener;
    private FloatingActionButton btnAddExam;

    ProgressDialog progressDialog;




    interface ExamFragmentActionListener {
        void showSample(Exam exam);
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

        progressDialog.show();

        getExam(catID, new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                JSONArray examJsonArray = result.getJSONArray("JA");

                //  examsArr = new Exam[examJsonArray.length()];
                Gson gson = new GsonBuilder().create();
                examsArr = gson.fromJson(examJsonArray.toString(), Exam[].class);
                textNoNet.setVisibility(View.INVISIBLE);

                upDateExam();
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(JSONArray result) throws JSONException {

            }

            @Override
            public void onError(String result) throws Exception {
                textNoNet.setVisibility(View.VISIBLE);
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
                if (cat_id > 0)
                    par.put("category_id", "" + cat_id);
                else if (cat_id == MY_EXAM)
                    par.put("examiner_id", "" + LoginSharedPreferences.getUserId(getActivity()));

                return par;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
//                while (Constants.COOKIES == null) ;
//                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };
        VolleyController.getInstance(getActivity()).addToRequestQueue(stringRequest);


//        CacheRequest cacheRequest = new CacheRequest(Request.Method.POST,url,
//
//                response ->                 {
//                    try {
//                        final String data = new String(response.data,
//                                HttpHeaderParser.parseCharset(response.headers));
//
//                        JSONArray jsonArray = new JSONArray(data);
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("JA", jsonArray);
//                        mCallback.onSuccess(jsonObject);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//
//                },
//
//
//                error -> {
//                    try {
//                        mCallback.onError(error.getMessage());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }   ){
//
//            Map<String, String> params = new HashMap<>();
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                if (cat_id > 0)
//                    params.put("category_id", "" + cat_id);
//                else if (cat_id == MY_EXAM)
//                    params.put("examiner_id", "" + LoginSharedPreferences.getUserId(getActivity()));
//
//                return params;
//            }
//
////            @Override
////            public Map<String, String> getHeaders() {
////                Map<String, String> map = new HashMap<>();
//////                while (Constants.COOKIES == null) ;
//////                map.put("Cookie", Constants.COOKIES);
////                return map;
////            }
//
//            @Override
//            public String getCacheKey() {
//                return generateCacheKeyWithParam(super.getCacheKey(), params);
//            }
//        };
//
//
//
//
//        VolleyController.getInstance(getActivity()).addToRequestQueue(cacheRequest);


    }

    private void upDateExam() {

        if (examsArr.length == 0 && getContext() != null) {
               Toast.makeText(getContext(), "لا يوجد اختبارات حالياً ضمن هذه الفئة ..", Toast.LENGTH_SHORT).show();
               //getFragmentManager().popBackStack();
               //getActivity().onBackPressed();

            return;
        }



        if (examAdapter == null) {
            examAdapter = new ExamAdapter();
            recyclerExam.setAdapter(examAdapter);
        }
        examAdapter.notifyDataSetChanged();
    }


    private void initView(View v) {

        setUpProgressDialog();


        textNoNet = v.findViewById(R.id.no_net);
        recyclerExam = v.findViewById(R.id.recyclerExam);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        if (currentCategory == ALL_EXAM || currentCategory == MY_EXAM) {
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        }
        recyclerExam.setLayoutManager(mLayoutManager);
        recyclerExam.addItemDecoration(new SpacingItemDecoration(1, dpToPx(getActivity(), 8), true));
        btnAddExam = v.findViewById(R.id.add_exam);
        if (LoginSharedPreferences.getUserType(getActivity()) == Constants.USER_TYPE_EXAMINER &&
                LoginSharedPreferences.getStatus(getActivity()) == 1) {

            btnAddExam.setVisibility(View.VISIBLE);
            btnAddExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ExamEntryActivity.class);
                    intent.putExtra(CategoryFragment.CATEGORY_ID, currentCategory);
                    startActivity(intent);
                }
            });

        } else {
            btnAddExam.setVisibility(View.GONE);
        }

    }


    void setUpProgressDialog(){
        Context context;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Exams ...");
        progressDialog.setCancelable(false);

    }


    public int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamVH> {
        Exam exam;

        final int EMPTY_RECYCLERVIEW=-1;


        @NonNull
        @Override
        public ExamVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



            return new ExamVH(LayoutInflater.from(getActivity()).inflate(R.layout.temp_exam, parent, false));
        }

        @Override
        public int getItemViewType(int position) {


            if (getItemCount() == 0){
                return  EMPTY_RECYCLERVIEW;
            }

            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(@NonNull ExamVH holder, int position) {


            exam = examsArr[position];
            if (LoginSharedPreferences.getUserId(getActivity()) == exam.getExaminerID()) {
                holder.btnShowSample.setVisibility(View.VISIBLE);
                holder.btnShowTalent.setVisibility(View.VISIBLE);
            }
            if (exam.getStatus() == 0) {
                if (LoginSharedPreferences.getUserType(getActivity()) == Constants.USER_TYPE_ADMIN ||
                        LoginSharedPreferences.getUserId(getActivity()) == exam.getExaminerID())
                    holder.examName.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                else {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }


            }


            holder.examName.setText(exam.getExamName());
            holder.examDesc.setText(exam.getExamDescription());

            ViewAnimation.animateFadeIn(holder.itemView, position);


            if (exam.expanded) {
                holder.lyt_expand.setVisibility(View.VISIBLE);
            } else {
                holder.lyt_expand.setVisibility(View.GONE);
            }
            toggleArrow(exam.expanded, holder.btExpand, false);

        }


        private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
            toggleArrow(show, view, true);
            if (show)
                ViewAnimation.expand(lyt_expand);
            else
                ViewAnimation.collapse(lyt_expand);

            return show;
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


        @Override
        public int getItemCount() {
            return examsArr.length;
        }


        private class ExamVH extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView examName, examDesc;
            public ImageButton btExpand;
            public View lyt_expand;
            private Button btnShowSample, btnShowTalent, btnDetilsExam;

            public ExamVH(@NonNull View itemView) {
                super(itemView);



                examName = itemView.findViewById(R.id.exam_name);
                examDesc = itemView.findViewById(R.id.exam_desc);
                btExpand = itemView.findViewById(R.id.bt_expand);
                lyt_expand = itemView.findViewById(R.id.lyt_expand);
                btnShowSample = itemView.findViewById(R.id.show_sample);
                btnShowTalent = itemView.findViewById(R.id.show_talent);
                btnDetilsExam = itemView.findViewById(R.id.detils_exam);
                btnShowSample.setOnClickListener(this);
                examName.setOnClickListener(this);
                btExpand.setOnClickListener(this);
                btnDetilsExam.setOnClickListener(this);
                btnShowTalent.setOnClickListener(this);
                checkPrivlige();
            }


            private void checkPrivlige() {

                switch (LoginSharedPreferences.getUserType(getActivity())) {
                    case Constants.USER_TYPE_ADMIN:
                        btnShowSample.setVisibility(View.VISIBLE);
                        btnShowTalent.setVisibility(View.VISIBLE);
                        btnDetilsExam.setVisibility(View.VISIBLE);
                        break;
                    case Constants.USER_TYPE_EXAMINER:

                        btnDetilsExam.setVisibility(View.VISIBLE);
                        break;
                    case Constants.USER_TYPE_RECRUITER:
                        btnShowTalent.setVisibility(View.VISIBLE);
                        break;
                    case Constants.USER_TYPE_TALENT:

                        break;
                }
            }


            @Override
            public void onClick(View view) {
                exam = examsArr[getAdapterPosition()];

                switch (view.getId()) {
                    case R.id.show_sample:
                        mListener.showSample(exam);
                        Toast.makeText(getActivity(), "" + exam.getSamples().length, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.exam_name:
                        showQuestions();
                        break;
                    case R.id.bt_expand:
                        boolean show = toggleLayoutExpand(!exam.expanded, btExpand, lyt_expand);
                        exam.expanded = show;
                        break;
                    case R.id.detils_exam:
                        Intent intent = new Intent(getActivity(), ExamModifyActivity.class);
                        intent.putExtra(ExamFragment.EXAM_OBJECT, exam);
                        startActivity(intent);


                        break;

                }
            }

            private void showQuestions() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String mBodyMessage;
                if (!LoginSharedPreferences.checkIsLogin(getActivity()))
                    mBodyMessage = "You must login ";
                else if (exam.getSamples().length == 0)
                    mBodyMessage = "No Question yet";
                else {
                    String mNumberOfQuestions = exam.getQuestionNumber() + "";
                    String mExamTime = exam.getExamTime() + "";
                    mBodyMessage = getString(R.string.dialog_info_exam, mNumberOfQuestions, mExamTime);
                    builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            startActivity(QuesExamActvity.getInstance(getActivity(), exam, QuesExamActvity.TAKE_EXAM));

                        }
                    }).setNegativeButton("stop", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(), "you clicked no", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                builder.setTitle("Welcome to Exam?");
                builder.setMessage(mBodyMessage);
                Dialog dialog = builder.create();

                dialog.show();
            }
        }
    }


}
