package com.example.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// T是通用范型，可以传入Payment也可以传入Order
public class CommonResult<T> {
    // 404 not_fount ：返回码 + 消息内容
    private Integer code;
    private String message;
    private T data;


    public CommonResult(Integer code , String message){
        this(code, message, null);
    }

}
