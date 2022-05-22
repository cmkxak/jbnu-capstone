package com.example.firsttest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firsttest.databinding.ActivityUserListBinding;
import com.example.firsttest.ui.emergencylive.EmergencyLiveActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private ListView listView;
    private UserListAdapter adapter;
    private List<User> userList;
    private ActivityUserListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        listView = (ListView)findViewById(R.id.listView);
        userList = new ArrayList<User>();
        adapter = new UserListAdapter(getApplicationContext(), userList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userList.get(i);
                Toast.makeText(UserListActivity.this, user.getUserIP(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserListActivity.this, EmergencyLiveActivity.class);
                String userIP = user.getUserIP();
                intent.putExtra("userIP",userIP);
                intent.putExtra("userPhoneNumber", user.getUserPhoneNumber());
                startActivity(intent);
            }
        });

        String id = intent.getStringExtra("id");
        binding.userListTextView.setText(id);


        try{
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("userList"));

            //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;

            String  userID, userName, userAge, userPhonenumber, userIP;
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

        binding.button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserListActivity.this, AddSignUpActivity.class);
                // 로그인 하면서 사용자 정보 넘기기
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
}