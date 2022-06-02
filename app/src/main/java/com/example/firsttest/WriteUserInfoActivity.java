package com.example.firsttest;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.firsttest.databinding.ActivityWriteUserinfoBinding;

public class WriteUserInfoActivity extends AppCompatActivity implements SendEventListener{
    private ActivityWriteUserinfoBinding binding;
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private final int ENTER_USERNAME_FRAGMENT = 1;
    private final int ENTER_USERAGE_FRAGMENT = 2;
    private final int ENTER_USERPHONENUMBER_FRAGMENT = 3;
    private InputMethodManager inputManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWriteUserinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUserInfo();

        binding.btnChangeUserName.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Fragmentview(ENTER_USERNAME_FRAGMENT);
           }
       });

       binding.btnChangeUserAge.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Fragmentview(ENTER_USERAGE_FRAGMENT);
           }
       });

       binding.btnChangeUserPhoneNumber.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Fragmentview(ENTER_USERPHONENUMBER_FRAGMENT);
           }
       });
    }

    //인텐트로 넘어온 값으로, 이름, 전화번호, 나이 띄워줌
    private void setUserInfo() {
        intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String userPhoneNumber = intent.getStringExtra("userPhoneNumber");
        String userAge = intent.getStringExtra("userAge");

        binding.txtUserName.setText(userName);
        binding.txtUserAge.setText(userAge);
        binding.txtUserPhoneNumber.setText(userPhoneNumber);
    }

    //알맞은 프래그 먼트를 불러옴
    private void Fragmentview(int fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch(fragment){
            case 1:
                EnterUserNameFragment enterUserNameFragment = new EnterUserNameFragment();
                transaction.replace(R.id.fragment_container,enterUserNameFragment);
                transaction.commit();
                break;

            case 2:
                EnterAgeFragment enterAgeFragment = new EnterAgeFragment();
                transaction.replace(R.id.fragment_container,enterAgeFragment);
                transaction.commit();
                break;

            case 3:
                EnterUserPhoneNumberFragment enterUserPhoneNumberFragment = new EnterUserPhoneNumberFragment();
                transaction.replace(R.id.fragment_container,enterUserPhoneNumberFragment);
                transaction.commit();
                break;
        }
    }

    //각 프래그먼트에서 바뀐 값들 update
    @Override
    public void updateName(String userName) {
        binding.txtUserName.setText(userName); //변경된 이름으로 Text 띄워줌
        //디비에 쿼리 쏴줘야 함
    }

    @Override
    public void updateAge(String age) {
        binding.txtUserAge.setText(age);
        //디비에 쿼리 쏴줘야 함
    }

    @Override
    public void updatePhoneNumber(String phoneNumber) {
        binding.txtUserPhoneNumber.setText(phoneNumber);
        //디비에 쿼리 쏴줘야 함
    }
}


