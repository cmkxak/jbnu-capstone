package com.example.firsttest.ui.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.firsttest.DeleteRequest;
import com.example.firsttest.GuardianDeleteRequest;
import com.example.firsttest.LoginActivity;
import com.example.firsttest.SettingUserListActivity;
import com.example.firsttest.R;
import com.example.firsttest.SignUpActivity;

import org.json.JSONObject;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MySettingFragment extends PreferenceFragmentCompat {
     SharedPreferences prefs;
     Preference pref_correctSeniorInfo;
     Preference pref_deleteMember;
     RequestQueue queue;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.user_preference, rootKey);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        pref_correctSeniorInfo = findPreference("correctSeniorInfo");
        pref_correctSeniorInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            //관리 대상 정보 수정 클릭 시
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
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
                    String id = prefs.getString("id", "");
                    queue = Volley.newRequestQueue(getActivity());

                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getActivity(), "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                } else {// 회원가입이 안된다면
                                    Toast.makeText(getActivity(), "탈퇴를 실패했습니다. 다시 한 번 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    GuardianDeleteRequest deleteRequest = new GuardianDeleteRequest(id, listener);
                    queue.add(deleteRequest);
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
