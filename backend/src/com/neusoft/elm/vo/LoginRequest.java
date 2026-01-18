package com.neusoft.elm.vo;

/**
 * 登录请求对象
 * 用于接收前端登录表单数据
 *
 * @author Neusoft ELM Team
 * @version 2.0
 */
public class LoginRequest {

    /**
     * 用户名（管理员）或商家编号（商家）
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='***'" +
                '}';
    }
}
