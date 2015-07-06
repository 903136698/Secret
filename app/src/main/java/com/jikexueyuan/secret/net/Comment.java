package com.jikexueyuan.secret.net;

/**
 * Created by MiracleWong on 2015/7/6.
 */
public class Comment {

    private String content,phone_md5;

    public Comment(String content,String phone_md5) {
        this.content = content;
        this.phone_md5 = phone_md5;
    }

    public String getContent() {
        return content;
    }

    public String getPhone_md5() {
        return phone_md5;
    }
}
