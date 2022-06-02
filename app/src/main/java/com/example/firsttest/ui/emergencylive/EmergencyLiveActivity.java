package com.example.firsttest.ui.emergencylive;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.preference.PreferenceManager;

import com.example.firsttest.UserListActivity;
import com.example.firsttest.databinding.ActivityEmergencyLiveBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class EmergencyLiveActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityEmergencyLiveBinding binding;
    private WebSettings webSettings;
    SharedPreferences prefs;
    private final String TAG = "MyFirebaseMsgService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyLiveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        webSettings = binding.liveStreaming.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
//        webSettings.setSupportZoom(false); // 화면 줌 허용 여부
//        webSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부

        Intent intent = getIntent();
        String userIP = intent.getStringExtra("userIP");
        String userPhoneNumber = intent.getStringExtra("userPhoneNumber");
        binding.liveStreaming.loadUrl(userIP+":9090/?"+"userPhoneNumber=" + userPhoneNumber);

        //뒤로가기 버튼 클릭
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메인 액티비티로 이동
                Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String userId = prefs.getString("id", " ");
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });

        //간편 신고 기능
        binding.report.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyLiveActivity.this);
            builder.setTitle("신고");
            builder.setMessage("신고하시겠습니까?");
            builder.setPositiveButton("네", (dialogInterface, i) -> {
                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:119"));
                startActivity(intent2);
            });
            builder.setNegativeButton("아니오", (dialogInterface, i) -> {
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }
}



