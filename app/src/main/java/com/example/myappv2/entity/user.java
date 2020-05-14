package com.example.myappv2.entity;

public class user {
    String userId;          //id
    String password;        //密码
    String age;             //age 故意多的一个和服务器不同的变量

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

}
