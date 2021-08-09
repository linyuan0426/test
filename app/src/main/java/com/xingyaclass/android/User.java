package com.xingyaclass.android;
/**
 * Created by littlecurl 2018/6/24
 */
public class User {
    private String name;            //用户名
    private String password;        //密码
    private String sex;  //xingbie
    public User(String name, String password, String sex) {
        this.name = name;
        this.password = password;
        this.sex = sex;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ",sex='"+ sex +'\'' +
                '}';
    }
}

