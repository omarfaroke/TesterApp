package com.codingacademy.testerapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlertDialogAcativitiy extends AppCompatActivity {
   Button mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notificatiom_exam);
        mDialog=findViewById(R.id.text_dialog);
      mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new  AlertDialog.Builder(AlertDialogAcativitiy.this);
                builder.setTitle("Welcome to Exam?");
                builder.setMessage("in this Exam you have a 12 Question you must answer ");
                builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AlertDialogAcativitiy.this, "you clicked Yes", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("stop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AlertDialogAcativitiy.this, "you clicked no", Toast.LENGTH_SHORT).show();
                    }
                });
                Dialog dialog=builder.create();
                dialog.show();
            }
        });


    }
}
