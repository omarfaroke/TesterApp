package com.codingacademy.testerapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingacademy.testerapp.model.Exam;
import com.codingacademy.testerapp.model.Sample;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SampleFragment extends Fragment {
    public static final String TAG = "SampleFragment" ;
    RecyclerView mRecyclerSample;
    Exam exam;
    Sample[] mSamples;
    private FloatingActionButton btnAddSample;

    SampleRecyclerViewAdapter myAdapterSample;
    private SampleFragmentActionListener mListener;
    static final String EXAM_OPJECT = "EXAM_OPJECT";


    interface SampleFragmentActionListener {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sample, container, false);
        //fillAdapter();
        exam = (Exam) getArguments().getSerializable(SampleFragment.EXAM_OPJECT);
        mSamples = exam.getSamples();
        initView(v);

        return v;
    }

    private void initView(View v) {
        /// Exam exam = new Exam(1, null, null, null, 20, 7, null, null, null, null, null);

        mRecyclerSample = v.findViewById(R.id.recyclersample);
        mRecyclerSample.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerSample.addItemDecoration(new SpacingItemDecoration(1, dpToPx(getActivity(), 8), true));

        myAdapterSample = new SampleRecyclerViewAdapter();
        mRecyclerSample.setAdapter(myAdapterSample);
        if(LoginSharedPreferences.getUserId(getContext())==exam.getExaminerID()) {
            btnAddSample = v.findViewById(R.id.add_sample);
            btnAddSample.setVisibility(View.VISIBLE);
            btnAddSample.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(QuesExamActvity.getInstance(getActivity(), exam, QuesExamActvity.ADD_SAMPLE));
                }
            });
        }

    }

    public int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    public class SampleRecyclerViewAdapter extends RecyclerView.Adapter<SampleRecyclerViewAdapter.ViewHolder> {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_sample, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Sample sample = mSamples[position];
            holder.mSampleName.setText(sample.getSampleName());
            holder.mBtnExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!sample.expanded, v, holder.layoutExpand);
                    sample.expanded = show;
                }
            });
            if (sample.expanded) {
                holder.layoutExpand.setVisibility(View.VISIBLE);
            } else {
                holder.layoutExpand.setVisibility(View.GONE);
            }
            toggleArrow(sample.expanded, holder.mBtnExpand, false);

        }

        private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
            toggleArrow(show, view, true);
            if (show)
                lyt_expand.startAnimation(expand(lyt_expand));
            else
                collapse(lyt_expand);

            return show;
        }

        public void collapse(final View v) {
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
            return mSamples.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView mSampleName;
            Button mBtnTalent;

            ImageView mBtnExpand;
            View layoutExpand;

            public ViewHolder(View view) {
                super(view);
                mSampleName = view.findViewById(R.id.sample_name);
                mSampleName.setOnClickListener(this);
                mBtnExpand = view.findViewById(R.id.btn_expand);
                layoutExpand = view.findViewById(R.id.layoutt_expand);
                mBtnTalent = view.findViewById(R.id.btn_talent);

            }

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sample_name:
                        int whatPos=getAdapterPosition();
                        startActivity(QuesExamActvity.getInstance(getActivity(), exam, whatPos));
                        break;

                }
            }
        }
    }


}