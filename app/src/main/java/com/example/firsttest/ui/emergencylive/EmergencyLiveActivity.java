package com.example.firsttest.ui.emergencylive;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
<<<<<<< HEAD:app/src/main/java/com/example/firsttest/EmergencyLiveActivity.java
=======
import android.util.Log;
import android.view.View;
>>>>>>> c9fb30d265b3e0ce2a4b5c6b88910a8a0174a087:app/src/main/java/com/example/firsttest/ui/emergencylive/EmergencyLiveActivity.java
import android.webkit.WebSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.firsttest.Main2Activity;
import com.example.firsttest.databinding.ActivityEmergencyLiveBinding;
<<<<<<< HEAD:app/src/main/java/com/example/firsttest/EmergencyLiveActivity.java
=======
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
>>>>>>> c9fb30d265b3e0ce2a4b5c6b88910a8a0174a087:app/src/main/java/com/example/firsttest/ui/emergencylive/EmergencyLiveActivity.java

public class EmergencyLiveActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityEmergencyLiveBinding binding;
    private WebSettings webSettings;
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
<<<<<<< HEAD:app/src/main/java/com/example/firsttest/EmergencyLiveActivity.java
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기

        binding.liveStreaming.loadUrl("http://211.117.125.107:12485/");
=======
        binding.liveStreaming.loadUrl("211.117.125.107:12485");
>>>>>>> c9fb30d265b3e0ce2a4b5c6b88910a8a0174a087:app/src/main/java/com/example/firsttest/ui/emergencylive/EmergencyLiveActivity.java

        //convert to 119 dial.
        binding.buttonBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 메인 액티비티로 이동
//                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
//                startActivity(intent);
            }
        });

        //간편 신고 기능
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

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.d(TAG, "token : "+token);
                    }
                });

    }
}



