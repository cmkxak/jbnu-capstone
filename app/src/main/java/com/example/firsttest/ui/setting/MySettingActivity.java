package com.example.firsttest.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firsttest.R;

public class MySettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, new MySettingFragment())
                    .commit();
        }

    }
}

