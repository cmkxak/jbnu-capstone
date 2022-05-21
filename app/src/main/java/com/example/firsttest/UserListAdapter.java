package com.example.firsttest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class UserListAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;


    public UserListAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }
    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }
    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.user, null);

        //뷰에 다음 컴포넌트들을 연결시켜줌
        TextView userID = (TextView)v.findViewById(R.id.userID);
        TextView userAge = (TextView)v.findViewById(R.id.userAge);
        TextView userName = (TextView)v.findViewById(R.id.userName);
        TextView userPhonenumber = (TextView)v.findViewById(R.id.userPhonenumber);

        userName.setText(userList.get(i).getUserName());
        userAge.setText(userList.get(i).getUserAge());
        userPhonenumber.setText(userList.get(i).getUserPhonenumber());
        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임

        //만든뷰를 반환함
        return v;
    }
}
