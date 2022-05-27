package com.example.firsttest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.firsttest.databinding.ActivityModifyInformationBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModifyUserInfoActivity extends AppCompatActivity{

        private ListView listView;
        private UserListAdapter adapter;
        private List<User> userList;
        private ActivityModifyInformationBinding binding;
        private SharedPreferences pref;
        private Intent intent;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityModifyInformationBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            intent = getIntent();
            listView = (ListView)findViewById(R.id.listView);
            userList = new ArrayList<User>();
            adapter = new UserListAdapter(getApplicationContext(), userList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userList.get(i);
                Intent intent = new Intent(ModifyUserInfoActivity.this, WriteUserInfoActivity.class);
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

            getuserInfo();

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
            //if(userID.equals(id)) {
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
                    userList.add(user);//리스트뷰에 값을 추가해줍니다
                    count++;
                }
                else count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}

