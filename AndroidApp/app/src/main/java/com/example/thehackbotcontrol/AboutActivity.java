package com.example.thehackbotcontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImageButton back = (ImageButton) findViewById(R.id.BackAbout);
        back.setOnClickListener(v -> finish());
    }
}