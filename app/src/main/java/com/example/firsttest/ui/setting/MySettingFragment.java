package com.example.firsttest.ui.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.firsttest.DeleteRequest;
import com.example.firsttest.LoginActivity;
import com.example.firsttest.SettingUserListActivity;
import com.example.firsttest.R;
import com.example.firsttest.service.MyFirebaseMessagingService;
import com.example.firsttest.ui.emergencylive.EmergencyLiveActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MySettingFragment extends PreferenceFragmentCompat {
    SharedPreferences prefs;
    Preference pref_correctSeniorInfo;
    Preference pref_deleteMember;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.user_preference, rootKey);

        pref_correctSeniorInfo = findPreference("correctSeniorInfo");
        pref_correctSeniorInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            //관리 대상 정보 수정 클릭 시
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String userId = prefs.getString("id", " ");
                Intent intent = new Intent(getActivity(), SettingUserListActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            }
        });

        pref_deleteMember = findPreference("deleteMember");
        pref_deleteMember.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            //회원 탈퇴 클릭 시
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("회원 탈퇴");
                builder.setMessage("정말 탈퇴하시겠습니까?");
                builder.setPositiveButton("네", (dialogInterface, i) -> {
                    //회원 탈퇴 처리 후 로그인 화면으로 이동
//                    queue = Volley.newRequestQueue(getActivity());
//                    DeleteRequest request = new DeleteRequest(id);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                });
                builder.setNegativeButton("아니오", (dialogInterface, i) -> {
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }
}
