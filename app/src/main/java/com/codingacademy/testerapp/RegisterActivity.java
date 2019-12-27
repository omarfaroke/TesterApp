package com.codingacademy.testerapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.codingacademy.testerapp.model.UserProfile;
import com.codingacademy.testerapp.requests.VolleyCallback;
import com.codingacademy.testerapp.requests.VolleyController;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private CircleImageView mProfileImage;
    private ImageView mGallary;
    private ImageView mCamera;
    /**
     * First Name
     */
    private EditText mFirstName;
    /**
     * Last Name
     */
    private EditText mLastName;
    /**
     * Email
     */
    private EditText mEmail;
    /**
     * Next
     */
    private Button mNextButton;
    /**
     * password
     */
    private EditText mEtPassword;
    /**
     * confirm password
     */
    private EditText mEtConfirmPassword;
    /**
     * address
     */
    private EditText mAddressEditText;
    /**
     * phone number
     */
    private EditText mPhoneEditText;
    /**
     * REGISTER
     */
    private Button mBtnRegister;
    private ViewFlipper mViewFlipper;
    /**
     * Login
     */
    private Button mLoginButton;

    private int currentSignUpViewNumber = 1;
    private Bitmap imageProfile;
    private Spinner spinnerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();


    }


    private void initView() {
        mProfileImage = findViewById(R.id.profile_image);
        mGallary = findViewById(R.id.gallary);
        mGallary.setOnClickListener(this);
        mCamera = findViewById(R.id.camera);
        mCamera.setOnClickListener(this);
        mFirstName = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);
        mEmail = findViewById(R.id.email);
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(this);
        mEtPassword = findViewById(R.id.etPassword);
        mEtConfirmPassword = findViewById(R.id.etConfirmPassword);
        mAddressEditText = findViewById(R.id.address_editText);
        mPhoneEditText = findViewById(R.id.phone_editText);
        mBtnRegister = findViewById(R.id.btnRegister);
        mBtnRegister.setOnClickListener(this);
        mViewFlipper = findViewById(R.id.viewFlipper);
        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
        spinnerType=findViewById(R.id.spinner_type);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.type_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.gallary:
                pickFromGallery();
                break;
            case R.id.camera:
                dispatchTakePictureIntent();
                break;
            case R.id.next_button:

               // finish();
                if (validate()) {
                    moveToSignUpView2();
                }
                break;
            case R.id.btnRegister:
                if (validate()) {
                    register();
                }
                break;
            case R.id.login_button:
                openActivityLogin();
                break;
        }
    }




    private void openActivityLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (currentSignUpViewNumber == 2) {
            moveToSignUpView1();
        } else {
            finish();
        }
    }

    private void moveToSignUpView1() {
        mViewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        mViewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        mViewFlipper.showPrevious();
        currentSignUpViewNumber--;
    }

    private void moveToSignUpView2() {
        mViewFlipper.setInAnimation(this, R.anim.slide_in_right);
        mViewFlipper.setOutAnimation(this, R.anim.slide_out_left);
        mViewFlipper.showNext();
        currentSignUpViewNumber++;
    }


    int vEmail() {
        String s = mEmail.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            mEmail.setError(null);
            return 1;
        } else if (s.isEmpty())
            mEmail.setError("Enter Email");
        else mEmail.setError("not valid");
        return 0;
    }

    int vPassword() {
        String s = mEtPassword.getText().toString();
        if (s.isEmpty()) {
            mEtPassword.setError("Enter Password");
            return 0;
        }

        if (s.equals(mEtConfirmPassword.getText().toString()))
            return 1;

        else mEtConfirmPassword.setError("Password not match");
        return 0;
    }

    boolean validate() {
        switch (currentSignUpViewNumber) {

            case 1:
                int i = 2;
                if (mFirstName.getText().toString().isEmpty()) {
                    mFirstName.setError("Enter First Name");
                    i--;
                }

                if (mLastName.getText().toString().isEmpty()) {
                    mLastName.setError("Enter Last Name");
                    i--;
                }
                i += vEmail();

                return i == 3;

            case 2:
                return 1 == vPassword();

        }

        return false;
    }


    private void register() {
        int userType = spinnerType.getSelectedItemPosition() + 1;
        int status = 1;

        if (userType  == Constants.USER_TYPE_EXAMINER || userType == Constants.USER_TYPE_RECRUITER){
            status = 0;

            alertMessage();
        }

        UserProfile userProfile = new UserProfile(null,userType, mFirstName.getText().toString(),
                 mLastName.getText().toString(), mAddressEditText.getText().toString(), mPhoneEditText.getText().toString(),status);



        registerNewAccount(mEmail.getText().toString(), mEtPassword.getText().toString(), userProfile, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                if (result.getString("status") == "0") {
                    int userId = result.getInt("user_id");
                    String imageUrl = result.getString("image_url");

                    LoginSharedPreferences.commitLogin(
                            RegisterActivity.this
                            ,userId, 1 , mEmail.getText().toString()
                            ,userId ,mFirstName.getText().toString()
                            ,mLastName.getText().toString(),imageUrl);



                    Toast.makeText(RegisterActivity.this, "تم تسجيل البيانات بنجاح ..", Toast.LENGTH_SHORT).show();

                    setResult(Activity.RESULT_OK);

                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, result.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(JSONArray result) throws JSONException {

            }

            @Override
            public void onError(String result) throws Exception {

                Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void registerNewAccount(final String mEmail, final String mPass, final UserProfile user, final VolleyCallback mCallback) {
        String url = Constants.REGISTER;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                mCallback.onSuccess(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            try {
                mCallback.onError(error.getMessage());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameter = new HashMap<>();

                String hashPass = null;
                try {

                    hashPass = LoginActivity.getSHA256(mPass);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                String userProfileGson = new Gson().toJson(user);

                parameter.put("user_profile", userProfileGson);

                parameter.put("email", mEmail);
                parameter.put("password", hashPass);


                String imageProfileString = "";

                if (imageProfile != null) {
                    imageProfileString = bitmapToString(imageProfile);
                }
                parameter.put("photo_base64", imageProfileString);


                return parameter;
            }

                @Override
                public Map<String, String> getHeaders(){
                Map<String, String> map = new HashMap<>();
//        	while (Constants.COOKIES == null);
//                map.put("Cookie", Constants.COOKIES);
                return map;
            }
            };


        VolleyController.getInstance(this).addToRequestQueue(stringRequest);

    }


    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                imageProfile = (Bitmap) extras.get("data");
                mProfileImage.setImageBitmap(imageProfile);

            } else if (requestCode == GALLERY_REQUEST_CODE) {
                try {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                    imageProfile = BitmapFactory.decodeStream(imageStream);
                    mProfileImage.setImageBitmap(imageProfile);

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

        }
    }


    private void alertMessage(){
        AlertDialog.Builder builder=new  AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Welcome");
        builder.setMessage("Thank you for sending your request to the Admin we wiil take your application and contact with you soon ");
        builder.setIcon(R.drawable.image_1);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(RegisterActivity.this, "you clicked Ok", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog=builder.create();
        builder.setCancelable(false);
        dialog.show();
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text=adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
