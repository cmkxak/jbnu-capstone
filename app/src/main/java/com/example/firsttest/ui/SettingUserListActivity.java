package com.example.firsttest.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firsttest.R;
import com.example.firsttest.adapter.UserAdapter;
import com.example.firsttest.databinding.ActivitySettingUserlistBinding;
import com.example.firsttest.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class SettingUserListActivity extends AppCompatActivity{
        private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private ActivitySettingUserlistBinding binding;
        private SharedPreferences pref;
        private Intent intent;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivitySettingUserlistBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            intent = getIntent();

            recyclerView = findViewById(R.id.recycler_view);

            userAdapter = new UserAdapter();

            recyclerView.setAdapter(userAdapter); //어댑터 연결

            getuserInfo();

            //click Event
            userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                @Override
                public void onItemCLicked(View v, int pos) {
                    User user = userAdapter.getUser(pos);
                    Intent intent = new Intent(SettingUserListActivity.this, WriteUserInfoActivity.class);
                    String userName = user.getUserName();
                    String userAge = user.getUserAge();
                    String userPhoneNumber = user.getUserPhoneNumber();
                    Log.d("ModifyInfo", userPhoneNumber + "정보 수정 클릭 시 폰번호 넘기기");
                    intent.putExtra("userName", userName);
                    intent.putExtra("userAge", userAge);
                    intent.putExtra("userPhoneNumber", userPhoneNumber);
                    startActivity(intent);
                }
            });

        }

    //업데이트 된 유저 값들도 불러오게 설정 해야 함
    private void getuserInfo() {
        String  userID, userName, userAge, userPhonenumber, userIP;
        try{
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            String id = intent.getStringExtra("id");

            pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            JSONObject jsonObject = new JSONObject(pref.getString("userList", " "));

            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;

            //userID = object.getString("userID");
            //JSON 배열 길이만큼 반복문을 실행
            while (count < jsonArray.length()) {
                //count는 배열의 인덱스를 의미
                JSONObject object = jsonArray.getJSONObject(count);
                userID = object.getString("userID");

                if(id.equals(userID)) {
                    userName = object.getString("userName");
                    userAge = object.getString("userAge");
                    userPhonenumber = object.getString("userPhonenumber");
                    userIP = object.getString("userIP");

                    //값들을 User클래스에 묶어줍니다
                    User user = new User(userName, userAge, userPhonenumber, userIP);
                    userAdapter.addUser(user); //어댑터에서 추가되도록 함
                    userAdapter.notifyDataSetChanged(); //데이터 변경을 앙ㄹ림
                    recyclerView.startLayoutAnimation(); //애니메이션 효과
                    count++;
                }
                else count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}

