package com.codingacademy.testerapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codingacademy.testerapp.model.Category;
import com.codingacademy.testerapp.model.Exam;
import com.codingacademy.testerapp.model.Sample;
import com.codingacademy.testerapp.model.UserProfile;
import com.codingacademy.testerapp.requests.StatusCallback;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExaminarFragment extends Fragment {
    RecyclerView mRecyclerExaminar;
    UserProfile[] mUsers;
    ExaminarRecyclerViewAdapter myAdapterExaminar;
    public static final String USER_TYPE = "type";
    private List<UserProfile> arryUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_examinar, container, false);
        //fillAdapter();
        getExaminar();
        initView(v);

        return v;
    }

    private void initView(View v) {

        mRecyclerExaminar = v.findViewById(R.id.recyclerexaminar);
        mRecyclerExaminar.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerExaminar.addItemDecoration(new SpacingItemDecoration(1, dpToPx(getActivity(), 8), true));

    }

    private void upDateExaminar() {
        if (myAdapterExaminar == null) {
            myAdapterExaminar = new ExaminarRecyclerViewAdapter(getContext(), mUsers);
            mRecyclerExaminar.setAdapter(myAdapterExaminar);
        }
        myAdapterExaminar.notifyDataSetChanged();
    }

    public int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    void getExaminar() {
        getExamaminar(new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                JSONArray examJsonArray = result.getJSONArray("JA");

                //  examsArr = new Exam[examJsonArray.length()];
                Gson gson = new GsonBuilder().create();

                mUsers = gson.fromJson(examJsonArray.toString(), UserProfile[].class);

                upDateExaminar();
            }

            @Override
            public void onSuccess(JSONArray result) throws JSONException {

            }

            @Override
            public void onError(String result) throws Exception {
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getExamaminar(final VolleyCallback mCallback) {
        String url = Constants.GET_EXAMINER;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
//                while (Constants.COOKIES == null) ;
//                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };


        VolleyController.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }



    public class ExaminarRecyclerViewAdapter extends RecyclerView.Adapter<ExaminarRecyclerViewAdapter.ViewHolder> {
        private UserProfile[] mUsers;
        private Context mContext;

        public ExaminarRecyclerViewAdapter(Context mContext, UserProfile[] mUsers) {
            this.mContext = mContext;
            this.mUsers = mUsers;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_examinar, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            UserProfile user = mUsers[position];
            holder.mExaminarName.setText(user.getFirstName() + " " + user.getFirstName());
            holder.teEmail.setText(user.getEmail());
            if (user.getStatus() == 0) {
                holder.mExaminarName.setBackgroundColor(Color.GRAY);
                holder.btnAprove.setVisibility(View.VISIBLE);
                holder.btnDisAprove.setVisibility(View.GONE);

            } else if (user.getStatus() == 1) {
                holder.mExaminarName.setBackgroundColor(Color.WHITE);
                holder.btnDisAprove.setVisibility(View.VISIBLE);
                holder.btnAprove.setVisibility(View.GONE);


            }

            holder.btnAprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Constants.upDateState(user.getUserId(), 1, getActivity(), new StatusCallback() {
                        @Override
                        public void response(String s) {
                            if (s.equals("update user status")) {
                                user.setStatus(1);
                                notifyItemChanged(position);
                            }
                            else Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            holder.btnDisAprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Constants.upDateState(user.getUserId(), 0, getActivity(), new StatusCallback() {
                        @Override
                        public void response(String s) {
                            if (s.equals("update user status")) {
                                user.setStatus(0);
                                notifyItemChanged(position);
                            }
                            else Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });


            holder.mBtnExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!user.expanded, v, holder.layoutExpand);
                    user.expanded = show;
                }
            });
            if (user.expanded) {
                holder.layoutExpand.setVisibility(View.VISIBLE);
            } else {
                holder.layoutExpand.setVisibility(View.GONE);
            }
            toggleArrow(user.expanded, holder.mBtnExpand, false);

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
            return mUsers.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mExaminarName;
            ImageView mBtnExpand;
            View layoutExpand;
            TextView teEmail;
            Button btnAprove;
            Button btnDisAprove;

            public ViewHolder(View view) {
                super(view);
                mExaminarName = view.findViewById(R.id.examinar_name);
                mBtnExpand = view.findViewById(R.id.btn_expand);
                layoutExpand = view.findViewById(R.id.layoutt_expand);
                teEmail = view.findViewById(R.id.email);
                btnAprove = view.findViewById(R.id.aprove);
                btnDisAprove = view.findViewById(R.id.dis_aprove);
            }

        }
    }
}