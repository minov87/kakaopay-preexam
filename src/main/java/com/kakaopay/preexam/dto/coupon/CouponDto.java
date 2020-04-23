package com.kakaopay.preexam.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class CouponDto {
    private String couponCode;
    private String type;
    private int isvalid;
    private LocalDateTime createTime;
    private LocalDateTime expireTime;
}
