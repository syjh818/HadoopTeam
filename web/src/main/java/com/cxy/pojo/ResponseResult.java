package com.cxy.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回结果类
 *
 * @author zxl
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult {
    private Integer code;
    private String msg;
    private Object data;

    public static ResponseResult ok() {
        return new ResponseResult(200, "操作成功", null);
    }

    public static ResponseResult ok(Object object) {
        return new ResponseResult(200, "操作成功", object);
    }

    public static ResponseResult ok(PageDTO<?> pageDTO) {
        return new ResponseResult(200, "操作成功", pageDTO);
    }

    public static ResponseResult error(Integer code, String msg) {
        return new ResponseResult(code, msg, null);
    }

    public static ResponseResult error(AppHttpCodeEnum enums) {
        return new ResponseResult(enums.getCode(), enums.getMsg(), null);
    }

}
