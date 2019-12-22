package com.codingacademy.testerapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codingacademy.testerapp.model.TopTalent;
import com.codingacademy.testerapp.model.UserProfile;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoExamTalentActivity extends AppCompatActivity {

    public static final String TOP_TALENT_FOR = "TOP_TALENT_FOR";
    public static final String TOP_TALENT_VALUE = "TOP_TALENT_VALUE";


    public static final String EXAM_ID = "exam_id";
    public static final String SAMPLE_ID = "sample_id";
    public static final String TALENT_ID = "talent_id";
    public static final String EXAMINER_ID = "examiner_id";
    public static final String ALL_TALENTS = "all";


    private RecyclerView mTalentInfoExamRecyclerView;
    private List<TopTalent> mTopTalents = new ArrayList<>();
    private AdapterTalentInoExam mAdapterTalentInoExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_exam_talent);
        initView();

        getInfoTopTalent();

    }


    private void getInfoTopTalent() {
        if (getIntent().getSerializableExtra(TOP_TALENT_FOR) == null) {
            finish();
            return;
        }

        String namePrameter = getIntent().getStringExtra(TOP_TALENT_FOR);
        String valuePrameter = getIntent().getStringExtra(TOP_TALENT_VALUE);


        getTalent( namePrameter , valuePrameter ,new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {
            }

            @Override
            public void onSuccess(JSONArray result) throws JSONException {
                Gson gson = new GsonBuilder().create();
                TopTalent allProArray[] = gson.fromJson(result.toString(), TopTalent[].class);
                mTopTalents = Arrays.asList(allProArray);
                updateRecyclerView();
            }

            @Override
            public void onError(String result) throws Exception {
                Toast.makeText(InfoExamTalentActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getTalent(String namePrameter , String valuePrameter ,final VolleyCallback mCallback) {
        String url = Constants.GET_TOP_TALET;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response ->
                {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        mCallback.onSuccess(jsonArray);
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
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
//                while (Constants.COOKIES == null) ;
//                map.put("Cookie", Constants.COOKIES);
                map.put(namePrameter, valuePrameter);
                return map;
            }
        };


        VolleyController.getInstance(InfoExamTalentActivity.this).addToRequestQueue(stringRequest);
    }


    void updateRecyclerView() {
        if (mAdapterTalentInoExam == null) {
            mAdapterTalentInoExam = new AdapterTalentInoExam(InfoExamTalentActivity.this, mTopTalents);
            mTalentInfoExamRecyclerView.setAdapter(mAdapterTalentInoExam);
        }
        mAdapterTalentInoExam.notifyDataSetChanged();
    }


    private void initView() {
        mTalentInfoExamRecyclerView = findViewById(R.id.talent_info_exam_recyclerView);

        mTalentInfoExamRecyclerView.setLayoutManager(new LinearLayoutManager(InfoExamTalentActivity.this, LinearLayoutManager.VERTICAL, false));

    }


    class AdapterTalentInoExam extends RecyclerView.Adapter<AdapterTalentInoExam.ViewHolder> {
        Context context;
        List<TopTalent> mTalents;

        public AdapterTalentInoExam(Context context, List<TopTalent> topTalents) {
            this.context = context;
            mTalents = topTalents;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            LayoutInflater layoutInflater = LayoutInflater.from(context);

            View view = layoutInflater.inflate(R.layout.talent_info_exam_item, parent, false);


            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bindData();
        }


        @Override
        public int getItemCount() {
            return mTalents.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView mFullNameTextView;
            private CircularImageView mTalentPhotoCircularImageView;
            private TextView mExam;
            private Button mMark;
            private TextView mSample;
            private TextView mSampleName;
            private Button mPrint;
            private TextView mDateExam;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                initView(itemView);
                //bindData();


            }


            void initView(View v) {
                mFullNameTextView = v.findViewById(R.id.full_name_textView);
                mTalentPhotoCircularImageView = v.findViewById(R.id.talent_photo_circularImageView);
                mTalentPhotoCircularImageView.setOnClickListener(this);
                mExam = v.findViewById(R.id.exam);
                mMark = v.findViewById(R.id.mark);
                mMark.setOnClickListener(this);
                mSample = v.findViewById(R.id.sample);
                mSampleName = v.findViewById(R.id.sample_name);
                mPrint = v.findViewById(R.id.print);
                mPrint.setOnClickListener(this);
                mDateExam = v.findViewById(R.id.date_exam);
            }


            void bindData(){
                TopTalent currentTopTalent = mTalents.get(getAdapterPosition());

                UserProfile userProfile = currentTopTalent.getUserProfile();

                String mFullName = userProfile.getFirstName() + " " + userProfile.getLastName();

                mFullNameTextView.setText(mFullName);

                Glide.with(InfoExamTalentActivity.this).applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.userphoto)
                        .error(R.drawable.userphoto))
                        .load(Constants.BASE_URL + "/" + userProfile.getImageUrl())
                        .into(mTalentPhotoCircularImageView);


                mMark.setText(currentTopTalent.getScore() + " \n " + currentTopTalent.getExam().getFullMarks());
                mExam.setText(currentTopTalent.getExam().getExamName());
                mSampleName.setText(currentTopTalent.getSample().getSampleName());
                mDateExam.setText(currentTopTalent.getDate());

            }


            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    default:
                        break;
                    case R.id.talent_photo_circularImageView:
                        break;
                    case R.id.mark:
                        break;
                    case R.id.print:
                        break;
                }
            }

        }
    }

}
