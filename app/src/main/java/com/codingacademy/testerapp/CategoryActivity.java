package com.codingacademy.testerapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codingacademy.testerapp.model.Category;
import com.codingacademy.testerapp.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private View parent_view;
    int parent = 0;
    private RecyclerView recyclerCategory, recyclerPro;
    private CategoryAdapter mAdapter;
    private ProAdapter mProAdapter;
    List<Category> arrCategory, subCategory;
    List<UserProfile> proArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
       intViews();
        getCategory();




    }

    private void intViews() {
        parent_view = findViewById(R.id.parent_view);
        recyclerCategory = findViewById(R.id.recyclerCat);
        recyclerPro = findViewById(R.id.recyclerPro);
        recyclerPro.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCategory.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerCategory.addItemDecoration(new SpacingItemDecoration(2, dpToPx(this, 8), true));
        recyclerCategory.setHasFixedSize(true);
        recyclerCategory.setNestedScrollingEnabled(false);
    }

    void upDateList() {
//        Toast.makeText(this, subCategory.size()+" "+arrCategory.size()+" ", Toast.LENGTH_SHORT).show();
        if (mAdapter == null) {
            mAdapter = new CategoryAdapter(this, subCategory);
            recyclerCategory.setAdapter(mAdapter);
        }
        if (mProAdapter == null) {

            mProAdapter = new ProAdapter(this, proArr);
            recyclerPro.setAdapter(mProAdapter);
        }

        mAdapter.notifyDataSetChanged();
        mProAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {

        getSupCat(parent);
        mAdapter.getSupCat(0);
    }

    void getSupCat(int parentid) {
        Toast.makeText(this, "" + parentid, Toast.LENGTH_SHORT).show();
        subCategory = new ArrayList<>();
        subCategory.clear();
        for (Category c : arrCategory)
            if (c.getParentId() == parentid)
                subCategory.add(c);
            upDateList();

    }

    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    void getCategory() {
        arrCategory = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET,
                Constants.CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(CategoryActivity.this, response, Toast.LENGTH_SHORT).show();

                        if (response.equals("null"))
                            Toast.makeText(CategoryActivity.this, "There is no category", Toast.LENGTH_SHORT).show();
                        else
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                int size = jsonArray.length();

                                for (int i = 0; i < size; i++) {
                                    JSONObject jsonCat = jsonArray.getJSONObject(i);
                                    arrCategory.add(new Category(jsonCat));

                                }

                                getSupCat(0);
                            } catch (JSONException e) {
                                Toast.makeText(CategoryActivity.this, e.getMessage() + "  jason", Toast.LENGTH_LONG).show();

                                e.printStackTrace();

                                // Toast.makeText(getContext(),"catch  "+ e.getMessage(), Toast.LENGTH_LONG).show();

                            }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CategoryActivity.this, "Can not connect", Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(CategoryActivity.this);
        queue.add(request);


    }

    private void initComponent() {

        subCategory = new ArrayList<>();
        proArr = new ArrayList<>();
        proArr.add(new UserProfile(null, null, "sss", "ddd", null, null, null, null));
        subCategory.add(new Category(1, 2, "Java"));
        subCategory.add(new Category(1, 2, "Java"));
        subCategory.add(new Category(1, 2, "Java"));
        subCategory.add(new Category(1, 2, "Java"));
    }

    private class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Category> items = new ArrayList<>();

        private Context ctx;


        void getSupCat(int parentid) {
            //  Toast.makeText(this, ""+parentid, Toast.LENGTH_SHORT).show();

            items.clear();
            for (Category c : arrCategory)
                if (c.getParentId() == parentid)
                    items.add(c);
            notifyDataSetChanged();
        }


        public CategoryAdapter(Context context, List<Category> items) {
            this.items = items;
            ctx = context;
        }

        public class OriginalViewHolder extends RecyclerView.ViewHolder {
            public ImageView image;
            public TextView title;


            public OriginalViewHolder(View v) {
                super(v);
                image = v.findViewById(R.id.mImageCat);
                title = v.findViewById(R.id.mTextCat);

            }


        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_category, parent, false);
            vh = new OriginalViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof OriginalViewHolder) {
                OriginalViewHolder view = (OriginalViewHolder) holder;

                Category p = items.get(position);
                view.title.setText(p.name);
                view.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getSupCat(p.getCatId());
                        parent = p.getParentId();

                    }
                });
                //   view.image.setImageDrawable(getResources(R.drawable.image_1));

            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

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
            vh = new OriginalViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof OriginalViewHolder) {
                OriginalViewHolder view = (OriginalViewHolder) holder;

                // UserProfile user = items.get(position);
                view.name.setText(" Ziad");
                view.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return 5;//items.size();
        }

    }
}
