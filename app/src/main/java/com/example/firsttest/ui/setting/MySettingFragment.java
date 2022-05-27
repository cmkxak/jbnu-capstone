package com.example.firsttest.ui.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.firsttest.SettingUserListActivity;
import com.example.firsttest.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MySettingFragment extends PreferenceFragmentCompat {
    SharedPreferences prefs;
    Preference mypref;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

        setPreferencesFromResource(R.xml.user_preference, rootKey);
        mypref = findPreference("correctSeniorInfo");
        mypref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            //돌봄 리스트 정보 수정
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

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("voiceNotifications")) {
                    if (prefs.getBoolean("voiceNotifications", true)) {
                        Toast.makeText(getActivity(), "음성인식 알림이 켜졌습니다.", Toast.LENGTH_SHORT).show();
                        //음성인식 켜지도록 동작
                    } else {
                        Toast.makeText(getActivity(), "음성인식 알림이 꺼졌습니다.", Toast.LENGTH_SHORT).show();
                        //음성인식 꺼지도록 동작
                    }
                }
                if (key.equals("abnormalBehaviorDetection")) {
                    if (prefs.getBoolean("abnormalBehaviorDetection", true)) {
                        Toast.makeText(getActivity(), "이상행동 탐지 알림이 켜졌습니다.", Toast.LENGTH_SHORT).show();
                        //이상행동 탐지가 켜지도록 동작
                        //FirebaseMessaging.getInstance().subscribeToTopic("users");
                    } else {
                        Toast.makeText(getActivity(), "이상행동 탐지 알림이 꺼졌습니다.", Toast.LENGTH_SHORT).show();
                        //이상행동 탐지가 꺼지도록 동작
                        //FirebaseMessaging.getInstance().unsubscribeFromTopic("users");
                    }
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }
}
