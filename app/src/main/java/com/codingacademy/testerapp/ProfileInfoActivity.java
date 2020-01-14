package com.codingacademy.testerapp;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codingacademy.testerapp.model.UserProfile;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileInfoActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * Julianna Carter
     */
    private EditText mProfileFullNameEditText;
    private CircleImageView mProfilePhotoCircleImageView;
    /**
     * Email
     */
    private EditText mProfileEmailEditText;
    /**
     * 775295623
     */
    private EditText mProfilePhoneEditText;
    /**
     * Yemen
     */
    private EditText mProfileLocationEditText;
    /**
     * 160th St, Fresh Meadows, NY, 11365
     */
    private EditText mProfileAddressEditText;
    private ImageView mEditImageView;
    private LinearLayoutCompat mRootView;

    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
        initView();

        getInfoProfile();

    }


    void getInfoProfile() {
        if (getIntent().getSerializableExtra("UserProfile") == null) {
            finish();
            return;
        }


        userProfile = (UserProfile) getIntent().getSerializableExtra("UserProfile");
        int mStatus = getIntent().getIntExtra(Constants.USER_STATUS, -1);


        String mFullName = userProfile.getFullName();
        mProfileFullNameEditText.setText(mFullName);

        mProfileEmailEditText.setText(userProfile.getEmail());
        mProfileAddressEditText.setText(userProfile.getAddress());
        mProfilePhoneEditText.setText(userProfile.getPhone());


        String url = Constants.BASE_URL + "/" + userProfile.getImageUrl();

        Glide.with(ProfileInfoActivity.this).applyDefaultRequestOptions(new RequestOptions()
                .placeholder(R.drawable.userphoto)
                .error(R.drawable.userphoto))
                .load(url)
                .into(mProfilePhotoCircleImageView);




        if (LoginSharedPreferences.getUserId(ProfileInfoActivity.this) != userProfile.getUserId()) {
            mEditImageView.setVisibility(View.GONE);
        }

        disableAllEditText();

    }

    void disableAllEditText() {

        mRootView.setEnabled(false);

    }


    void enableAllEditText() {

        if (LoginSharedPreferences.getUserId(ProfileInfoActivity.this) == userProfile.getUserId()) {
            mRootView.setEnabled(true);
        }

    }


    private void initView() {
        mProfileFullNameEditText = findViewById(R.id.profile_full_name_editText);
        mProfilePhotoCircleImageView = findViewById(R.id.profile_photo_circleImageView);
        mProfilePhotoCircleImageView.setOnClickListener(this);
        mProfileEmailEditText = findViewById(R.id.profile_email_editText);
        mProfilePhoneEditText = findViewById(R.id.profile_phone_editText);
        mProfileLocationEditText = findViewById(R.id.profile_location_editText);
        mProfileAddressEditText = findViewById(R.id.profile_address_editText);
        mEditImageView = findViewById(R.id.edit_imageView);
        mEditImageView.setOnClickListener(this);
        mRootView = findViewById(R.id.root_view);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.profile_photo_circleImageView:

                break;
            case R.id.edit_imageView:

                enableAllEditText();
                break;
        }
    }



//    void clearCache(){
//        Runnable target;
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Glide.get(ProfileInfoActivity.this).clearDiskCache();
//            }
//        });
//
//        thread.start();
//
//    }

}
