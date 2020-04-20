package com.kakaopay.preexam.controller.coupon;

import com.google.gson.Gson;
import com.kakaopay.preexam.model.coupon.CouponParams;
import com.kakaopay.preexam.model.response.Response;
import com.kakaopay.preexam.service.coupon.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    protected final Gson gson = new Gson();

    @Autowired
    CouponService couponService;

    /**
     * 랜덤 쿠폰 N개 생성 API
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/make", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response makeCoupon(
            @RequestBody(required = true) CouponParams param) throws Exception {
        if(param.getCount() > 0){
            couponService.makeCoupon(param.getCount());
            return new Response(HttpStatus.OK.value(), "success");
        } else {
            return new Response(HttpStatus.BAD_REQUEST.value(), "count param is required.");
        }
    }

    /*
    // 사용자에게 쿠폰 지급 API
    @PostMapping(value = "/give", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response couponGive(
            @RequestBody(required = true) CouponParams param) throws Exception {
        if(param.getCount() > 0){
            // 사용자에게 지급할 쿠폰의 갯수가 넘어올 경우
        } else {
            // 사용자에게 1개의 쿠폰만 지급할 경우

        }
    }


    // 사용자 지급 쿠폰 조회 API
    @GetMapping("/give/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getGiveCouponList(
            @RequestParam(name = "accountId") Long accountId) throws Exception {

    }

    // 지급된 쿠폰 사용 API
    @PostMapping("/redeem", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response couponRedeem(
            @RequestParam(name = "accountId") Long accountId,
            @RequestParam(name = "couponCode") String couponCode) throws Exception {

    }

    // 지급된 쿠폰 사용 취소 API
    @PostMapping("/redeem/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response couponRedeemCancel(
            @RequestParam(name = "accountId") Long accountId,
            @RequestParam(name = "couponCode") String couponCode) throws Exception {
    }

    // 당일 만료 쿠폰 목록 조회 API
    @GetMapping("/expired/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getExpiredCouponList() throws Exception {

    }
    */
}
