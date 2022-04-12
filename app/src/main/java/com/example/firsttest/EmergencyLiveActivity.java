package com.example.firsttest;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.firsttest.databinding.ActivityEmergencyLiveBinding;

public class EmergencyLiveActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityEmergencyLiveBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyLiveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //convert to 119 dial.
        binding.report.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyLiveActivity.this);
            builder.setTitle("신고");
            builder.setMessage("신고하시겠습니까?");
            builder.setPositiveButton("네", (dialogInterface, i) -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:119"));
                startActivity(intent);
            });
            builder.setNegativeButton("아니오", (dialogInterface, i) -> {
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }
}
