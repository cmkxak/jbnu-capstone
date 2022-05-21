package com.example.firsttest;

public class User {
    String userID;
    String userAge;
    String userName;
    String userPhonenumber;

    public User(String userAge, String userName, String userPhonenumber) {
        this.userAge = userAge;
        this.userName = userName;
        this.userPhonenumber = userPhonenumber;
    }

    public String getUserAge() {
        return userAge;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhonenumber() {
        return userPhonenumber;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPhonenumber(String userPhonenumber) { this.userPhonenumber = userPhonenumber; }

}


