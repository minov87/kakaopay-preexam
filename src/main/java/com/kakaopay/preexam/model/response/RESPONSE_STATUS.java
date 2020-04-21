package com.kakaopay.preexam.model.response;

import lombok.Getter;

@Getter
public enum RESPONSE_STATUS {
    // Basic
    SUCCESS(200, "Success"),
    UNKNOWN(201, "Unknown error"),
    NOT_FOUND(202, "Not found"),
    BAD_REQUEST(203, "Wrong Parameter"),
    INTERNAL_SERVER_ERROR(204, "Internal server error"),
    HTTP_IO_ERROR(205, "Internal server error"),

    // Token
    TOKEN_VERIFY_FAIL(206, "Token verify fail"),
    TOKEN_EXPIRED(207, "Token expired"),
    TOKEN_EMPTY(208, "Token Empty"),

    // Account
    ACCOUNT_ALREADY_EXIST(209, "Account already exist"),
    ACCOUNT_NOT_EXIST(210, "Account not exist"),
    ACCOUNT_WRONG_PASSWORD(211, "Account password is wrong"),

    // Coupon
    COUPON_MAKE_FAIL(212, "Coupon make fail"),
    COUPON_NOT_ENOUGH(213, "Coupon not enough"),
    COUPON_NOT_EXIST(214, "Coupon not exist"),
    COUPON_ALREADY_USED(215, "Coupon already used"),
    COUPON_EXPIRED(216, "Coupon expired");

    private int code;
    private String message;

    RESPONSE_STATUS(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
