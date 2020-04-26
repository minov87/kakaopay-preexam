package com.kakaopay.preexam.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("쿠폰 유틸리티 테스트")
public class CouponUtilTest {
    @Test
    @DisplayName("쿠폰 생성 테스트")
    public void testGenerateCoupon() {
        // 생성된 쿠폰 길이 체크
        String couponStr = CouponUtil.generateCoupon();
        assertEquals(19, couponStr.length());

        // 생성된 쿠폰의 패턴 체크
        String gubunKey = "KP".concat(String.valueOf(Year.now().getValue()).substring(2, 4));
        Pattern p = Pattern.compile("^([A-Za-z0-9]{4})-"+gubunKey+"-([A-Za-z0-9]{4})-([A-Za-z0-9]{4})");
        Matcher m = p.matcher(couponStr);
        assertTrue(m.find());
    }
}
