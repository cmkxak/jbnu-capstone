package com.example.firsttest;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.firsttest.databinding.ActivityUserListBinding;
import com.example.firsttest.ui.emergencylive.EmergencyLiveActivity;
import com.example.firsttest.ui.setting.MySettingActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private ListView listView;
    private UserListAdapter adapter;
    private List<User> userList;
    private ActivityUserListBinding binding;
    private SharedPreferences sharedPreferences;

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
                String userPhoneNumber = user.getUserPhoneNumber();
                intent.putExtra("userIP",userIP);
                intent.putExtra("userPhoneNumber", userPhoneNumber);
                startActivity(intent);
            }
        });

        String id = intent.getStringExtra("id");
        binding.userListTextView.setText(id);


        try{
            //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
//            String result = intent.getStringExtra("UserList");
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String result = sharedPreferences.getString("userList", " ");
            JSONObject jsonObject = new JSONObject(result);
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
                    this.userList.add(user);//리스트뷰에 값을 추가해줍니다
                    count++;
                }
                else count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserListActivity.this, MySettingActivity.class);
                startActivity(i);
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        Log.d("userList", id + "유저리스트에서 아이디");
        editor.commit();
    }


}