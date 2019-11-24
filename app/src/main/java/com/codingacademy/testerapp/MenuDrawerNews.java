package com.codingacademy.testerapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codingacademy.testerapp.model.UserProfile;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MenuDrawerNews extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private CategoryFragment categoryFragment;
    private ExamFragment examFragment;
    private RecyclerView recyclerPro;
    List<UserProfile> proArr;
    private ProAdapter mProAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_drawer_news);
        fragmentManager = getSupportFragmentManager();
        categoryFragment = new CategoryFragment();
        examFragment = new ExamFragment();
        initToolbar();
        initNavigationMenu();
        showCategory();
        upDateTop();
    }

    private void upDateTop() {
        if (mProAdapter == null) {

            mProAdapter = new ProAdapter(MenuDrawerNews.this, proArr);
            recyclerPro.setAdapter(mProAdapter);
        }
        mProAdapter.notifyDataSetChanged();
    }

    public void showCategory() {
        fragmentManager.beginTransaction().replace(R.id.main_fram, categoryFragment).addToBackStack(null).commit();
    }


    public void showExam(int cat) {
        fragmentManager.beginTransaction().replace(R.id.main_fram, examFragment).commit();
    }

    public static void animateFadeIn(View view, int position) {
        boolean not_first_item = position == -1;
        position = position + 1;
        view.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0.f, 0.5f, 1.f);
        ObjectAnimator.ofFloat(view, "alpha", 0.f).start();
        animatorAlpha.setStartDelay(not_first_item ? 500 / 2 : (position * 500 / 3));
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
        recyclerPro = findViewById(R.id.recyclerPro);
        recyclerPro.setLayoutManager(new LinearLayoutManager(MenuDrawerNews.this, LinearLayoutManager.HORIZONTAL, false));

        NavigationView nav_view = findViewById(R.id.nav_view);
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
                return true;
            }
        });


    }

    private class ProAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<UserProfile> items = new ArrayList<>();

        private Context ctx;


        public ProAdapter(Context context, List<UserProfile> items) {
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

                // UserProfile user = items.get(position);
                view.name.setText(" Ziad");
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
            return 5;//items.size();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
