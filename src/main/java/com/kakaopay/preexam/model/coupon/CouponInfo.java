package com.kakaopay.preexam.model.coupon;

import lombok.Data;

import java.util.Date;

@Data
public class CouponInfo {
    private String couponCode;
    private Date expireTime;
    private String status;
}
