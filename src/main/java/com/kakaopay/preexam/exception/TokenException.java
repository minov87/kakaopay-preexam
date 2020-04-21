package com.kakaopay.preexam.exception;

import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TokenException extends BaseException {
    private static final long serialVersionUID = 6189808550325116018L;

    public TokenException(RESPONSE_STATUS responseStatus) {
        super(responseStatus.getMessage(), responseStatus.getCode());
    }

    public TokenException(int errorCode, String message) {
        super(message, errorCode);
    }

    public static final TokenException TOKEN_VERIFY_FAIL = new TokenException(
            RESPONSE_STATUS.TOKEN_VERIFY_FAIL.getCode(),
            RESPONSE_STATUS.TOKEN_VERIFY_FAIL.getMessage()
    );
    public static final TokenException TOKEN_EXPIRED = new TokenException(
            RESPONSE_STATUS.TOKEN_EXPIRED.getCode(),
            RESPONSE_STATUS.TOKEN_VERIFY_FAIL.getMessage()
    );
    public static final TokenException TOKEN_EMPTY = new TokenException(
            RESPONSE_STATUS.TOKEN_EMPTY.getCode(),
            RESPONSE_STATUS.TOKEN_EMPTY.getMessage()
    );
}
