package com.codingacademy.testerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;


public class Dialog extends DialogFragment {

    private EditText catogry;
    String ans;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);

        catogry = v.findViewById(R.id.add_catogry);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("ADD Catogry")
                .setPositiveButton("Add",
                        (dialog, which) -> {

                            if (!catogry.getText().toString().equals("")) {

                                ans = catogry.getText().toString();

                            } else {
                                Toast.makeText(getActivity(), "Enter Comment", Toast.LENGTH_SHORT).show();
                            }

                        })
                .create();

    }


}
