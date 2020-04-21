package com.kakaopay.preexam.exception;

import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccountException extends BaseException {
    private static final long serialVersionUID = -9020381900631604005L;

    public AccountException(RESPONSE_STATUS responseStatus) {
        super(responseStatus.getMessage(), responseStatus.getCode());
    }

    public AccountException(int errorCode, String message) {
        super(message, errorCode);
    }

    public static final AccountException ACCOUNT_ALREADY_EXIST = new AccountException(
            RESPONSE_STATUS.ACCOUNT_ALREADY_EXIST.getCode(),
            RESPONSE_STATUS.ACCOUNT_ALREADY_EXIST.getMessage()
    );
    public static final AccountException ACCOUNT_NOT_EXIST = new AccountException(
            RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getCode(),
            RESPONSE_STATUS.ACCOUNT_NOT_EXIST.getMessage()
    );
    public static final AccountException ACCOUNT_WRONG_PASSWORD = new AccountException(
            RESPONSE_STATUS.ACCOUNT_WRONG_PASSWORD.getCode(),
            RESPONSE_STATUS.ACCOUNT_WRONG_PASSWORD.getMessage()
    );
}
