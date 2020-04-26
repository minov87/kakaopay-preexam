package com.kakaopay.preexam.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Year;
import java.util.Random;

@Slf4j
public final class CouponUtil {
    private static final Random rnd = new Random(System.currentTimeMillis());
    private static int year = Year.now().getValue();

    /**
     * 쿠폰 생성시 사용될 Charset 유형 정의
     */
    private static class Charset {
        private static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String ALPHANUMBER_UPPER = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String ALPAHNUMBER_COMBINE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String NUMBER = "0123456789";
    }

    /**
     * 쿠폰 생성에 필요한 환경설정 정의
     */
    private static class Config {
        private static final int length = 16;
        private static final String pattern = "####-KP$$-####-####";
        private static final String charset = Charset.ALPAHNUMBER_COMBINE;
        private static final char patternPlaceholder = '#';
    }

    /**
     * Coupon 생성
     *
     * @return
     */
    public static String generateCoupon(){
        StringBuilder sb = new StringBuilder();
        char[] charset = Config.charset.toCharArray();
        String modPattern = Config.pattern.replace("$$", String.valueOf(year).substring(2, 4));
        char[] pattern = modPattern.toCharArray();

        for(char ch : pattern){
            if(ch == Config.patternPlaceholder) {
                sb.append(charset[rnd.nextInt(charset.length)]);
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }
}
