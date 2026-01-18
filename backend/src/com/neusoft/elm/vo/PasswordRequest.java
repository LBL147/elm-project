package com.neusoft.elm.vo;

/**
 * 密码修改请求对象
 * 用于接收前端密码修改表单数据
 *
 * @author Neusoft ELM Team
 * @version 2.0
 */
public class PasswordRequest {

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码（可选，由前端验证）
     */
    private String confirmPassword;

    public PasswordRequest() {
    }

    public PasswordRequest(String newPassword, String confirmPassword) {
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return "PasswordRequest{" +
                "newPassword='***'" +
                ", confirmPassword='***'" +
                '}';
    }
}
