package com.yuanxiaoer.yuanxiaoer.admin.common;

import lombok.Data;

/**
 * 统一返回结果
 */
@Data
public class Result<T> {
    /**
     * 状态码：200成功，500失败
     */
    private int code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    // 成功返回（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 成功返回（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败返回
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}