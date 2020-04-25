package com.kakaopay.preexam.exception;

import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CouponException extends BaseException {
    private static final long serialVersionUID = -4258062007646415111L;

    public CouponException(RESPONSE_STATUS responseStatus) {
        super(responseStatus.getMessage(), responseStatus.getCode());
    }

    public CouponException(int errorCode, String message) {
        super(message, errorCode);
    }

    public static final CouponException COUPON_MAKE_FAIL = new CouponException(
            RESPONSE_STATUS.COUPON_MAKE_FAIL.getCode(),
            RESPONSE_STATUS.COUPON_MAKE_FAIL.getMessage()
    );
    public static final CouponException COUPON_NOT_ENOUGH = new CouponException(
            RESPONSE_STATUS.COUPON_NOT_ENOUGH.getCode(),
            RESPONSE_STATUS.COUPON_NOT_ENOUGH.getMessage()
    );
    public static final CouponException COUPON_NOT_EXIST = new CouponException(
            RESPONSE_STATUS.COUPON_NOT_EXIST.getCode(),
            RESPONSE_STATUS.COUPON_NOT_EXIST.getMessage()
    );
    public static final CouponException COUPON_ALREADY_USED = new CouponException(
            RESPONSE_STATUS.COUPON_ALREADY_USED.getCode(),
            RESPONSE_STATUS.COUPON_ALREADY_USED.getMessage()
    );
    public static final CouponException COUPON_EXPIRED = new CouponException(
            RESPONSE_STATUS.COUPON_EXPIRED.getCode(),
            RESPONSE_STATUS.COUPON_EXPIRED.getMessage()
    );
    public static final CouponException COUPON_MAKE_COUNT_OVER = new CouponException(
            RESPONSE_STATUS.COUPON_MAKE_COUNT_OVER.getCode(),
            RESPONSE_STATUS.COUPON_MAKE_COUNT_OVER.getMessage()
    );
}
