package com.codingacademy.testerapp.login;


import android.os.Bundle;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codingacademy.testerapp.R;


public class LoginFragment extends Fragment  {
    private static final String TAG="LoginFragment";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate=inflater.inflate ( R.layout.fragment_login, container, false );
        inflate.findViewById ( R.id.forgot_password ).setOnClickListener ( v ->
                Toast.makeText ( getContext (), "Forgot password clicked", Toast.LENGTH_SHORT ).show () );
        return inflate;
    }


}
