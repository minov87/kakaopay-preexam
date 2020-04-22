package com.kakaopay.preexam.exception;

import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseException extends Exception {
    private static final long serialVersionUID = -8933137214871364182L;

    private int errorCode = 0;

    public BaseException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(RESPONSE_STATUS responseStatus) {
        super(responseStatus.getMessage());
        this.errorCode = responseStatus.getCode();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
