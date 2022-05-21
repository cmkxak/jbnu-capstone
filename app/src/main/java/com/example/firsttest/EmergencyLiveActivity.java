package com.example.firsttest;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.firsttest.databinding.ActivityEmergencyLiveBinding;

public class EmergencyLiveActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityEmergencyLiveBinding binding;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyLiveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        webSettings = binding.liveStreaming.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
//        webSettings.setSupportZoom(false); // 화면 줌 허용 여부
//        webSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기

        binding.liveStreaming.loadUrl("http://211.117.125.107:12485/");

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


//    public static String getLocalIpAddress() {
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                        return inetAddress.getHostAddress();
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }




