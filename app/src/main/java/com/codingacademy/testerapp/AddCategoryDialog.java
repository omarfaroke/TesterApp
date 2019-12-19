package com.codingacademy.testerapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.model.Category;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddCategoryDialog extends AppCompatDialogFragment {
    public static final String TAG = "AddCategoryDialog";
    public static final String NEW_CATEGORY = "info_new_category";
    private ImageView mAddCategoryImage;
    private Button mChangeIconBTN;
    private EditText mAddCategoryEditText;


    private static final int GALLERY_REQUEST_CODE = 101;
    private Bitmap imageCategory;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_category, null);
        mAddCategoryImage = view.findViewById(R.id.image_add_cat);
        mChangeIconBTN = view.findViewById(R.id.change_icon);

        mChangeIconBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });


        mAddCategoryEditText = view.findViewById(R.id.edit_add_cat);

        int parent = getArguments().getInt("parentID");
        builder.setView(view)
                .setTitle("Add Category Dialog")
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_CANCELED, null);
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s = mAddCategoryEditText.getText().toString();
                        if (s.trim().isEmpty())
                            Toast.makeText(getActivity(), "add name category !", Toast.LENGTH_SHORT).show();
                        else {
                            Category category = new Category(null, s, parent, null, 1);
                            addCategory(category);
                        }
                    }
                });


        return builder.create();
    }


    private void addCategory(Category cat) {
        String url = Constants.ADD_CATEGORY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("status") == 0) {
                            int categoryId = jsonObject.getInt("category_id");

                            cat.catId = categoryId;
                            sendResult(RESULT_OK, cat);

                        }else if (jsonObject.getInt("status") == 2){
                            Toast.makeText(getActivity(), "الاسم موجود من قبل , جرب اسماً آخر .", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AddCategoryDialog.this.getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameter = new HashMap<>();
                String category = new Gson().toJson(cat);
                parameter.put("category", category);
                String imageProfileString = "";

                if (imageCategory != null) {
                    imageProfileString = bitmapToString(imageCategory);
                }
                parameter.put("photo_base64", imageProfileString);
                return parameter;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                while (Constants.COOKIES == null) ;
                map.put("Cookie", Constants.COOKIES);
                return map;
            }
        };
        VolleyController.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }


    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }


    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                try {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    imageCategory = BitmapFactory.decodeStream(imageStream);
                    //  mddCategoryImage.setImageURI(selectedImage);

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

        }
    }



    private void sendResult(int resultCode, Category category) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(NEW_CATEGORY, category);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);

    }


}

