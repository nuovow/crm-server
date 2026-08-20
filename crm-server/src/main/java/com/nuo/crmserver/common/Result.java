package com.nuo.crmserver.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    // 去掉 static，改为实例成员变量
    private Integer code;
    private String message;
    private T data; // 用泛型 T，不用 Object

    // 静态方法必须自己声明泛型 <T>
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    // 改为静态方法，和 success 统一
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    // 补充：带状态码的 error，方便扩展
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}