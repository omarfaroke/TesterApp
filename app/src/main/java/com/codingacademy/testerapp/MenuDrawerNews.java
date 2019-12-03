package com.codingacademy.testerapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codingacademy.testerapp.model.TopTalent;
import com.codingacademy.testerapp.model.UserProfile;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuDrawerNews extends AppCompatActivity implements CategoryFragment.CategoryFragmentActionListener, ExamFragment.ExamFragmentActionListener {


    private static final String TAG = "MenuDrawerNews";
    private ActionBar actionBar;
    private Toolbar toolbar;
    private NavigationView nav_view;
    private FragmentManager fragmentManager;

    private FragmentTransaction mFragmentTransaction;

    private RecyclerView recyclerPro;
    private List<TopTalent> proArr;
    private ProAdapter mProAdapter;
    private TopTalent[] allProArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer_news);

        //setup cookies
        Constants.getCookiesFromUrl(MenuDrawerNews.this);


        fragmentManager = getSupportFragmentManager();
        initTopTalent();
        initToolbar();
        initNavigationMenu();
        showCategory();

    }

    void initTopTalent() {
        recyclerPro = findViewById(R.id.recyclerPro);
        recyclerPro.setLayoutManager(new LinearLayoutManager(MenuDrawerNews.this, LinearLayoutManager.HORIZONTAL, false));
        getTalent(new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONArray examJsonArray = result.getJSONArray("JA");
                Gson gson = new GsonBuilder().create();
                allProArray = gson.fromJson(examJsonArray.toString(), TopTalent[].class);
                proArr = Arrays.asList(allProArray);
                upDateTop();
            }

            @Override
            public void onSuccess(JSONArray result) throws JSONException {

            }

            @Override
            public void onError(String result) throws Exception {
                Toast.makeText(MenuDrawerNews.this, result, Toast.LENGTH_SHORT).show();
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
                while (Constants.COOKIES == null) ;
                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };



        VolleyController.getInstance(MenuDrawerNews.this).addToRequestQueue(stringRequest);
    }

    void filterTalent(int cat) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (LoginSharedPreferences.checkIsLogin(this)) {
            nav_view.getMenu().findItem(R.id.nav_logout).setVisible(true);
            nav_view.getMenu().findItem(R.id.nav_login).setVisible(false);

        }

    }

    private void upDateTop() {
        if (mProAdapter == null) {
            mProAdapter = new ProAdapter(MenuDrawerNews.this, proArr);
            recyclerPro.setAdapter(mProAdapter);
        }
        mProAdapter.notifyDataSetChanged();
    }

    public void showCategory() {

        CategoryFragment categoryFragment = new CategoryFragment();


        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.main_fram, categoryFragment, CategoryFragment.TAG);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }


    public static void animateFadeIn(View view, int position) {
        boolean not_first_item = position == -1;
        position = position + 1;
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 0.5f, 1.f);
        ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorAlpha.setStartDelay(not_first_item ? 300 / 2 : (position * 300 / 3));
        animatorAlpha.setDuration(500);
        animatorSet.play(animatorAlpha);
        animatorSet.start();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Drawer News");
    }

    private void initNavigationMenu() {
//        recyclerPro = findViewById(R.id.recyclerPro);
//        recyclerPro.setLayoutManager(new LinearLayoutManager(MenuDrawerNews.this, LinearLayoutManager.HORIZONTAL, false));

        nav_view = findViewById(R.id.nav_view);
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
                Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                actionBar.setTitle(item.getTitle());


                drawer.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.nav_login:
                        showLoginActivity();
                        break;

                    case R.id.nav_logout:
                        LoginSharedPreferences.userLogOut(MenuDrawerNews.this);

                        Toast.makeText(MenuDrawerNews.this, "Logout ...", Toast.LENGTH_SHORT).show();

                        nav_view.getMenu().findItem(R.id.nav_logout).setVisible(false);
                        nav_view.getMenu().findItem(R.id.nav_login).setVisible(true);
                        break;
                }

                return true;
            }
        });


    }

    private void showLoginActivity() {
        Intent intent = new Intent(MenuDrawerNews.this, LoginActivity.class);
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
                    nav_view.getMenu().findItem(R.id.nav_logout).setVisible(true);
                    nav_view.getMenu().findItem(R.id.nav_login).setVisible(false);

                    Toast.makeText(this, "test no h", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
                animateFadeIn(view.itemView, position);
            }
        }

        @Override
        public int getItemCount() {
            return proArr.size();//items.size();
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

            }

        }

    }

    //
    @Override
    public void restartFocusView() {
        Fragment fragment = fragmentManager.findFragmentByTag(CategoryFragment.TAG);
        if (fragment != null) {
            if (fragment instanceof CategoryFragment) {
                ((CategoryFragment) fragment).restartFocusView();
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
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

            return;
        } else {
            backToast = Toast.makeText(MenuDrawerNews.this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }


}
