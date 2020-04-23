package com.kakaopay.preexam.model.coupon;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CouponParams {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long accountId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String couponCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String expireTime;
}
