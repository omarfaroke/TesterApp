package com.codingacademy.testerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.model.Category;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class CategoryFragment extends Fragment {
    public static final String TAG = "CategoryFragment";
    public static final String CATEGORY_ID = "category_id";
    public static final int DIALOG_ADD_CATEGORY_REQUEST_CODE = 22;


    private RecyclerView recyclerCategory;
    private TextView textNoNet;
    private CategoryAdapter mAdapter;
    private FloatingActionButton addCatBtn;
    private List<Category> arrCategory, subCategory;
    private List<Integer> parents = new ArrayList<>();
    private int currentCategory = 0;
    ProgressDialog progressDialog;

    private CategoryFragmentActionListener mListener;


    public interface CategoryFragmentActionListener {
        void showExams(int currentCategory);

        void updateTopTalent(List<Integer> childrenCategory);
    }


    public CategoryFragment() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryFragment.CategoryFragmentActionListener) {
            mListener = (CategoryFragment.CategoryFragmentActionListener) context;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey(Constants.CATEGORY_ID)) {
                currentCategory = getArguments().getInt(Constants.CATEGORY_ID);

            }
        }

        parents.add(currentCategory);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);

        intViews(v);
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        updateUiAfterLoginOrLogout();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == DIALOG_ADD_CATEGORY_REQUEST_CODE) {
                Category category = (Category) data
                        .getSerializableExtra(AddCategoryDialog.NEW_CATEGORY);

                updateCategory(category);
            }

        }
    }


    private void intViews(View v) {

        setUpProgressDialog();

        addCatBtn = v.findViewById(R.id.add_cat);
        addCatBtn.setOnClickListener(view -> {

            FragmentManager manager = getFragmentManager();
            AddCategoryDialog dialog = new AddCategoryDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("parentID", parents.get(parents.size() - 1));
            dialog.setArguments(bundle);
            dialog.setTargetFragment(CategoryFragment.this, DIALOG_ADD_CATEGORY_REQUEST_CODE);

            dialog.show(manager, AddCategoryDialog.TAG);


        });
        textNoNet = v.findViewById(R.id.no_net);
        recyclerCategory = v.findViewById(R.id.recyclerCat);
        recyclerCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerCategory.addItemDecoration(new SpacingItemDecoration(2, dpToPx(getActivity(), 8), true));
        recyclerCategory.setHasFixedSize(true);
        recyclerCategory.setNestedScrollingEnabled(false);

        recyclerCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (recyclerView.canScrollVertically(-7)) {
                }
                // updateCategory();
            }
        });

        updateCategory();

    }


    void setUpProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Category ...");
        progressDialog.setCancelable(false);

    }


    void upDateList() {
        if (mAdapter == null) {
            mAdapter = new CategoryAdapter(getActivity(), subCategory);
            recyclerCategory.setAdapter(mAdapter);
        }

        mAdapter.notifyDataSetChanged();


    }


    void getSupCat(int catId) {
        subCategory.clear();
        for (Category c : arrCategory)
            if (c.getParentId() == parents.get(parents.size() - 1))
                subCategory.add(c);

        if (subCategory.isEmpty()) {
            if (mListener != null) {
                parents.remove(parents.size() - 1);
                mListener.showExams(catId);
            }

        } else
            upDateList();
    }


    private void getCategory(VolleyCallback mCallback) {

        StringRequest request = new StringRequest(Request.Method.GET,
                Constants.CATEGORY,
                response -> {

                    if (response.equals("null"))
                        Toast.makeText(getActivity(), "There is no category", Toast.LENGTH_SHORT).show();
                    else
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            mCallback.onSuccess(jsonArray);


                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), e.getMessage() + "  jason", Toast.LENGTH_LONG).show();

                            e.printStackTrace();


                            // Toast.makeText(getContext(),"catch  "+ e.getMessage(), Toast.LENGTH_LONG).show();

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
//        	    while (Constants.COOKIES == null);
//                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };

        VolleyController.getInstance(getActivity()).addToRequestQueue(request);


    }


    private void updateCategory() {

        progressDialog.show();

        getCategory(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {

            }

            @Override
            public void onSuccess(JSONArray result) throws JSONException {
                int size = result.length();
                arrCategory = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    JSONObject jsonCat = result.getJSONObject(i);
                    arrCategory.add(new Category(jsonCat));
                }

                subCategory = new ArrayList<>();
                textNoNet.setVisibility(View.INVISIBLE);

                getSupCat(parents.get(parents.size() - 1));

                progressDialog.dismiss();
            }

            @Override
            public void onError(String result) throws Exception {
                textNoNet.setVisibility(View.VISIBLE);

                //progressDialog.setCancelable(true);

               // progressDialog.setContentView(R.layout.dialog_problem);

                progressDialog.dismiss();

                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void updateCategory(Category category) {
        arrCategory.add(category);
        subCategory.add(category);
        mAdapter.notifyItemInserted(subCategory.size());
        //getSupCat(category.getParentId());

    }


    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.OriginalViewHolder> {

        private List<Category> items;

        private Context ctx;

        public CategoryAdapter(Context context, List<Category> items) {
            this.items = items;
            ctx = context;
        }


        @Override
        public CategoryAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CategoryAdapter.OriginalViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.temp_category, parent, false);
            vh = new CategoryAdapter.OriginalViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(CategoryAdapter.OriginalViewHolder view, final int position) {

            Category p = items.get(position);
            view.title.setText(p.name);
            view.itemView.setOnClickListener(view1 -> {


//                    if (listChildrenCategory.size() == 1){
//                        Toast.makeText(ctx, "لا يوجد اختبارات حالياً ضمن هذه الفئة ..", Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                parents.add(p.getCatId());

                getSupCat(p.getCatId());

                List<Integer> listChildrenCategory = getChildrenOfCurrentCategory(p.getCatId());
                mListener.updateTopTalent(listChildrenCategory);

            });

            ViewAnimation.animateFadeIn(view.itemView, position);


        }


        @Override
        public int getItemCount() {
            return items.size();
        }


        class OriginalViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title;


            OriginalViewHolder(View v) {
                super(v);
                image = v.findViewById(R.id.mImageCat);
                title = v.findViewById(R.id.mTextCat);

            }


        }

    }

    public boolean backCategory() {
        if (parents.size() > 1) {
            parents.remove(parents.size() - 1);
            getSupCat(parents.get(parents.size() - 1));

            int currentCategory = parents.get(parents.size() - 1);

            mListener.updateTopTalent(getChildrenOfCurrentCategory(currentCategory));
            return true;
        }

        return false;
    }


    //----------------------------

    private List<Integer> getChildrenOfCurrentCategory(int categoryId) {
        List<Integer> list = new ArrayList<>();

        for (Category category : arrCategory) {
            if (categoryId == category.parentId || list.contains(category.parentId)) {

                list.addAll(getChildrenOfCurrentCategory(category.catId));
            }
        }

        if (list.size() == 0) {
            list.add(categoryId);
        }

        return list;

    }


    public void updateUiAfterLoginOrLogout() {

        if (LoginSharedPreferences.userAsAdmin(getActivity())) {
            addCatBtn.setVisibility(View.VISIBLE);

        } else {
            addCatBtn.setVisibility(View.GONE);
        }

    }


}
