package com.example.myappv2.entity;

public class UserInfo {
    private Integer uid;
    //用户名
    private String username;
    //姓名
    private String name;
    //密码
    private String password;
    //级别
    private Integer status;
    //手机号
    private String phone;
    //邮箱
    private String email;
//    //盐
//    private String salt;


    public Integer getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Integer getStatus() {
        return status;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
