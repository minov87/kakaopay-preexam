package com.kakaopay.preexam.util;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CouponUtilTest {
    @Test
    @DisplayName("Generate Coupon Test")
    public void generateCouponTest() {
        // 생성된 쿠폰 길이 체크
        String couponStr = CouponUtil.generateCoupon();
        assertEquals(19, couponStr.length());

        // 생성된 쿠폰의 패턴 체크
        String gubunKey = "KP".concat(String.valueOf(Year.now().getValue()).substring(2, 4));
        Pattern p = Pattern.compile("^([A-Z0-9]{4})-"+gubunKey+"-([A-Z0-9]{4})-([A-Z0-9]{4})");
        Matcher m = p.matcher(couponStr);
        assertTrue(m.find());
    }
}
