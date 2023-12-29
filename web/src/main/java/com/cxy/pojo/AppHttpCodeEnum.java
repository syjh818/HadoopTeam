package com.cxy.pojo;

/**
 * 返回类型的枚举类
 *
 * @author zxl
 */
public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR_AUTH(403, "无权限操作"),
    SYSTEM_ERROR(500, "出现错误"),

    USERID_NOT_NULL(504, "用户ID不能为空"),
    LOGIN_ERROR(505, "手机号或密码错误"),
    USER_NOT_FIND(506, "用户不存在");

    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
