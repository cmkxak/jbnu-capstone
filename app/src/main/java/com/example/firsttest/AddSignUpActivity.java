package com.example.firsttest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.firsttest.databinding.ActivityAddSignUpBinding;


import org.json.JSONObject;

public class AddSignUpActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityAddSignUpBinding binding;
    RequestQueue queue;
    boolean canRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        queue = Volley.newRequestQueue(AddSignUpActivity.this);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        binding.signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = binding.addSignupName.getText().toString();
                String age = binding.addSignupAge.getText().toString();
                String phone_number = binding.addSignupPhoneNumber.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            // String으로 그냥 못 보냄으로 JSON Object 형태로 변형하여 전송
                            // 서버 통신하여 회원가입 성공 여부를 jsonResponse로 받음
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) { // 회원가입이 가능한다면
                                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddSignUpActivity.this, UserListActivity.class);
                                startActivity(intent);
                                finish();//액티비티를 종료시킴(회원등록 창을 닫음)
                            } else {// 회원가입이 안된다면
                                Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다. 다시 한 번 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                SignUpRequest signupRequest = new SignUpRequest(id, name, age, phone_number, responseListener);
                queue.add(signupRequest);
            }
        });
    }
}