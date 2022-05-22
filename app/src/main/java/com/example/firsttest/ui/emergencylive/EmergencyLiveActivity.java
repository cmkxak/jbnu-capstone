package com.example.firsttest.ui.emergencylive;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.firsttest.UserListActivity;
import com.example.firsttest.databinding.ActivityEmergencyLiveBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

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
        //1. 일단, 유저별 ip를 알 필요가 있음
        //2. ip를 알고 나의 아이디를 가져옴(id는 지금 위에 띄워놨으니까 그 방식으로 가져오면 되고,
        // ip는 이제 노약자 table에 저장될건데, 전화번호(or 분별가능한 고유한 값)를 키 값으로 해가지 이 전화번호 가진 놈의 ip는 먼지 query해서 가저옴.
        //3. 그 두개의 변수로 ip:port?id=내 아이디 라고 정의
        // ----------------------
        //4. 중범이가 메인서버에 문의를 보냄 -> 얘 아이디 나한테 접근하려는데 이거 맞음?
        //5. ㅇㅇ 맞음 들여보내주셈
        //6. ㅇㅋㅇㅋ 들어오셈
        binding.liveStreaming.loadUrl("211.117.125.107:12485/");

//        String id = getIntent(id);
//        String ip_port = "210.117.128.200:12386/getSeniorIp.php";
//        binding.liveStreaming.loadUrl("$ip_port?id=$id");

        //뒤로가기 버튼 클릭
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메인 액티비티로 이동
                Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                startActivity(intent);
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

    }
}



