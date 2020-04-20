package com.kakaopay.preexam.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {
    private int code;
    private String msg;
    private T data;

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
