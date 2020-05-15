package com.example.m335projekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private Button backBTN;
    private Button aboutBTN;
    private Button submitBTN;
    private EditText multiTextline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        backBTN = (Button) findViewById(R.id.backBTN);
        aboutBTN = (Button) findViewById(R.id.aboutBTN);
        submitBTN = (Button) findViewById(R.id.submitBTN);
        multiTextline = (EditText) findViewById(R.id.editText);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        aboutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutScreen();
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainActivity();
            }
        });
    }


    public void backToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void aboutScreen() {
        Intent intent = new Intent(this, ScreenAbout.class);
        startActivity(intent);
    }

    public void openDialog() {
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "dialog");
        multiTextline.setText("");
    }
}
