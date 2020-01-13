package com.codingacademy.testerapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codingacademy.testerapp.model.Exam;
import com.codingacademy.testerapp.model.TopTalent;
import com.codingacademy.testerapp.model.UserProfile;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LauncherActivity extends AppCompatActivity implements CategoryFragment.CategoryFragmentActionListener, ExamFragment.ExamFragmentActionListener {


    private static final String TAG = "LauncherActivity";
    private ActionBar actionBar;
    private Toolbar toolbar;
    private NavigationView nav_view;
    private TextView mEmailHeaderDrawer;
    private TextView mFullNameHeaderDrawer;
    private CircleImageView mPhotoProfileHeaderDrawer;


    private FragmentManager fragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private RecyclerView recyclerPro;
    private List<TopTalent> mCurrentTopTalent;
    private ProAdapter mProAdapter;
    private TopTalent[] mAllTopTalent;
    private final int MAX_TOP_TALENT = 4;
    private List<List<Integer>> mLastChildrenCategory = new ArrayList<>();

    private TextView mEmptyListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //setup cookies
        //Constants.getCookiesFromUrl(LauncherActivity.this);


        setImageSlider();
        fragmentManager = getSupportFragmentManager();
        initTopTalent();
        initToolbar();
        initNavigationMenu();
        showCategory();

    }

    private void  setImageSlider(){

        SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapterExample adapter = new SliderAdapterExample(this);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();


    }

    void initTopTalent() {
        recyclerPro = findViewById(R.id.recyclerPro);
        recyclerPro.setLayoutManager(new LinearLayoutManager(LauncherActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerPro.setHasFixedSize(true);

        getTalent(new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONArray examJsonArray = result.getJSONArray("JA");
                Gson gson = new GsonBuilder().create();
                mAllTopTalent = gson.fromJson(examJsonArray.toString(), TopTalent[].class);
                mCurrentTopTalent = new ArrayList<>();
                //Arrays.asList(mAllTopTalent);
                updateTopTalent(null);

            }

            @Override
            public void onSuccess(JSONArray result) throws JSONException {

            }

            @Override
            public void onError(String result) throws Exception {
                Toast.makeText(LauncherActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getTalent(final VolleyCallback mCallback) {
        String url = Constants.GET_TOP_TALET;
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
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
//                while (Constants.COOKIES == null) ;
//                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };


        VolleyController.getInstance(LauncherActivity.this).addToRequestQueue(stringRequest);
    }

    void filterTalent(int cat) {

    }

    @Override
    protected void onResume() {
        super.onResume();


        setupItemsMenuNavDrawer();
    }

    private void updateRecyclerTopTalent() {
        if (mProAdapter == null) {
            mProAdapter = new ProAdapter(LauncherActivity.this, mCurrentTopTalent);
            recyclerPro.setAdapter(mProAdapter);
        }
        mProAdapter.notifyDataSetChanged();
    }




    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    private void initNavigationMenu() {

        nav_view = findViewById(R.id.nav_view);
        mEmailHeaderDrawer = nav_view.getHeaderView(0).findViewById(R.id.header_email);
        mFullNameHeaderDrawer = nav_view.getHeaderView(0).findViewById(R.id.header_full_name);
        mPhotoProfileHeaderDrawer = nav_view.getHeaderView(0).findViewById(R.id.header_photo_profile_circleImageView);


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);



        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
               // Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
               // actionBar.setTitle(item.getTitle());


                drawer.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.nav_login:
                        showLoginActivity();
                        break;

                    case R.id.nav_profile:


                        UserProfile userProfile = new UserProfile();

                        userProfile.setFirstName(LoginSharedPreferences.getUserFirstName(LauncherActivity.this));
                        userProfile.setLastName(LoginSharedPreferences.getUserLastName(LauncherActivity.this));
                        userProfile.setImageUrl(LoginSharedPreferences.getUserImageUrl(LauncherActivity.this));
                        userProfile.setUserId(LoginSharedPreferences.getUserId(LauncherActivity.this));


                        Intent intent = new Intent(LauncherActivity.this, ProfileInfoActivity.class);

                        intent.putExtra("UserProfile", userProfile);
                        intent.putExtra(Constants.USER_EMAIL, LoginSharedPreferences.getUserEmail(LauncherActivity.this));
                        intent.putExtra(Constants.USER_STATUS, LoginSharedPreferences.getStatus(LauncherActivity.this));

                        startActivity(intent);



                        break;

                    case R.id.nav_exam_talent:

                        Context context = LauncherActivity.this;

                        Intent intent1 = new Intent(context,InfoExamTalentActivity.class);

                        intent1.putExtra(InfoExamTalentActivity.TOP_TALENT_FOR,InfoExamTalentActivity.EXAMINER_ID);
                        intent1.putExtra(InfoExamTalentActivity.TOP_TALENT_VALUE,LoginSharedPreferences.getUserId(context));

                        startActivity(intent1);

                        break;
                    case R.id.nav_logout:
                        LoginSharedPreferences.userLogOut(LauncherActivity.this);

                        Toast.makeText(LauncherActivity.this, "Logout ...", Toast.LENGTH_SHORT).show();

                        nav_view.getMenu().findItem(R.id.nav_logout).setVisible(false);
                        nav_view.getMenu().findItem(R.id.nav_login).setVisible(true);
                        setupItemsMenuNavDrawer();

                        showCategory();
                        break;
                    case R.id.nav_help:
                        LoginSharedPreferences.checkIsLogin(LauncherActivity.this);
                        break;
                    case R.id.nav_settings:
                        LoginSharedPreferences.test(LauncherActivity.this);

                        break;
                    case R.id.nav_approve_test:
                        showExams(0);

                        break;
                    case R.id.nav_approve_examiner:
                        Intent intent2=new Intent(LauncherActivity.this,ExaminarActivity.class);
                        startActivity(intent2);

                        break;

                    case R.id.nav_my_exam:
                        showExams(ExamFragment.MY_EXAM);


                        break;

                    case R.id.nav_exit:
                        exitFromApp();
                        break;
                }

                return true;
            }
        });


    }

    private void showLoginActivity() {
        Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivityForResult(intent, LoginActivity.REQUEST_CODE_LOGIN);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == LoginActivity.REQUEST_CODE_LOGIN) {
                if (LoginSharedPreferences.checkIsLogin(this)) {

                    Toast.makeText(this, "تم تسجيل الدخول بنجاح ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void setupItemsMenuNavDrawer() {
        int type = LoginSharedPreferences.getUserType(LauncherActivity.this);


        switch (type) {
            case Constants.USER_TYPE_ADMIN:
                nav_view.getMenu().setGroupVisible(R.id.group_items_admin, true);

                nav_view.getMenu().setGroupVisible(R.id.group_items_examiner, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_recruiters, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_talent, false);
                break;
            case Constants.USER_TYPE_EXAMINER:
                nav_view.getMenu().setGroupVisible(R.id.group_items_examiner, true);

                nav_view.getMenu().setGroupVisible(R.id.group_items_admin, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_recruiters, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_talent, false);
                break;
            case Constants.USER_TYPE_RECRUITER:
                nav_view.getMenu().setGroupVisible(R.id.group_items_recruiters, true);

                nav_view.getMenu().setGroupVisible(R.id.group_items_examiner, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_admin, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_talent, false);
                break;
            case Constants.USER_TYPE_TALENT:
                nav_view.getMenu().setGroupVisible(R.id.group_items_talent, true);

                nav_view.getMenu().setGroupVisible(R.id.group_items_examiner, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_admin, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_recruiters, false);

            default:
                nav_view.getMenu().setGroupVisible(R.id.group_items_talent, false);

                nav_view.getMenu().setGroupVisible(R.id.group_items_examiner, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_admin, false);
                nav_view.getMenu().setGroupVisible(R.id.group_items_recruiters, false);
                break;
        }


        if (LoginSharedPreferences.checkIsLogin(this)) {
            nav_view.getMenu().findItem(R.id.nav_logout).setVisible(true);
            nav_view.getMenu().findItem(R.id.nav_login).setVisible(false);

            String url = Constants.BASE_URL + "/" + LoginSharedPreferences.getUserImageUrl(this);

            Glide.with(this).applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.drawable.userphoto)
                    .error(R.drawable.userphoto))
                    .load(url)
                    .into(mPhotoProfileHeaderDrawer);

            String mFullName = LoginSharedPreferences.getUserFirstName(this) + " " + LoginSharedPreferences.getUserLastName(this);
            mFullNameHeaderDrawer.setText(mFullName);

            String mEmail = LoginSharedPreferences.getUserEmail(this);
            mEmailHeaderDrawer.setText(mEmail);

        }else {
            mPhotoProfileHeaderDrawer.setImageResource(R.drawable.userphoto);
            mFullNameHeaderDrawer.setText("");
            mEmailHeaderDrawer.setText("");


        }
    }


    public void showCategory() {

        CategoryFragment categoryFragment = new CategoryFragment();


        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.main_fram, categoryFragment, CategoryFragment.TAG);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();


    }


    @Override
    public void showExams(int currentCategory) {

        ExamFragment examFragment = new ExamFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(CategoryFragment.CATEGORY_ID, currentCategory);
        examFragment.setArguments(bundle);

        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.main_fram, examFragment, ExamFragment.TAG);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }


    @Override
    public void updateTopTalent(List<Integer> childrenCategory) {

        mCurrentTopTalent.clear();


        for (int i = 0; i < mAllTopTalent.length && mCurrentTopTalent.size() < MAX_TOP_TALENT; i++) {

            if (childrenCategory == null) {
                if (isThisTalentExists(mAllTopTalent[i].getUserProfile().getUserId()))
                    mCurrentTopTalent.add(mAllTopTalent[i]);
                continue;
            }

            if (childrenCategory.contains(mAllTopTalent[i].getCategoryID()) && isThisTalentExists(mAllTopTalent[i].getUserProfile().getUserId())) {
                mCurrentTopTalent.add(mAllTopTalent[i]);
            }


        }

        updateRecyclerTopTalent();

        if (mLastChildrenCategory.size() ==2) {
            mLastChildrenCategory.remove(0);
        }

        mLastChildrenCategory.add(childrenCategory);
    }


    boolean isThisTalentExists(int talentId) {


        for (int i = 0; i < mCurrentTopTalent.size(); i++) {
            if (talentId == mCurrentTopTalent.get(i).getUserProfile().getUserId())
                return false;
        }

        return true;
    }


    @Override
    public void showSample(Exam exam) {
        SampleFragment sampleFragment = new SampleFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(SampleFragment.EXAM_OPJECT, (Serializable) exam);
        sampleFragment.setArguments(bundle);

        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.main_fram, sampleFragment, SampleFragment.TAG);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    private class ProAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<TopTalent> items = new ArrayList<>();

        private Context ctx;


        public ProAdapter(Context context, List<TopTalent> items) {
            this.items = items;
            ctx = context;
        }

        public class OriginalViewHolder extends RecyclerView.ViewHolder {
            public ImageView image;
            public TextView name;


            public OriginalViewHolder(View v) {
                super(v);
                image = v.findViewById(R.id.top_pro_photo);
                name = v.findViewById(R.id.top_pro_name);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TopTalent topTalent = items.get(getAdapterPosition());

                        Intent intent = new Intent(LauncherActivity.this, ProfileInfoActivity.class);

                        intent.putExtra("UserProfile", topTalent.getUserProfile());
                        intent.putExtra(Constants.USER_STATUS, topTalent.getStatusUser());

                        startActivity(intent);

                    }
                });

            }


        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_top_pro, parent, false);
            vh = new ProAdapter.OriginalViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ProAdapter.OriginalViewHolder) {
                ProAdapter.OriginalViewHolder view = (ProAdapter.OriginalViewHolder) holder;

                UserProfile userProfile = items.get(position).getUserProfile();
                view.name.setText(userProfile.getFirstName());

                Glide.with(ctx).applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.userphoto)
                        .error(R.drawable.userphoto))
                        .load(Constants.BASE_URL + "/" + userProfile.getImageUrl())
                        .into(view.image);

                view.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });
                ViewAnimation.animateFadeIn(view.itemView, position);
            }
        }

        @Override
        public int getItemCount() {
            return mCurrentTopTalent.size();//items.size();
        }

    }


    @Override
    public void onBackPressed() {

        Fragment fragment = fragmentManager.findFragmentById(R.id.main_fram);
        if (fragment != null) {
            if (fragment instanceof CategoryFragment) {

                if (!((CategoryFragment) fragment).backCategory()) {
                    exitFromApp();
                }

            } else if (fragment instanceof ExamFragment) {
                fragmentManager.popBackStack();
                updateTopTalent(mLastChildrenCategory.get(0));


            } else if (fragment instanceof SampleFragment) {
                if (((SampleFragment) fragment).isAdd) {
                    ExamFragment exam = (ExamFragment) fragmentManager.findFragmentByTag(ExamFragment.TAG);
                    exam.getExam();
                }
                fragmentManager.popBackStack();

            }

        }

    }


    private long backPressedTime;
    private Toast backToast;

    private void exitFromApp() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            //super.onBackPressed();

            moveTaskToBack(true);
            Process.killProcess(Process.myPid());
            System.exit(1);

            return;
        } else {
            backToast = Toast.makeText(LauncherActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }


    public void updateUiAfterLoginOrLogout() {
        switch (LoginSharedPreferences.getUserType(LauncherActivity.this)) {

        }
    }

}
