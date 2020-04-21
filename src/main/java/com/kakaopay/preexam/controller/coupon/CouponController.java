package com.kakaopay.preexam.controller.coupon;

import com.google.gson.Gson;
import com.kakaopay.preexam.model.coupon.CouponInventoryResult;
import com.kakaopay.preexam.model.coupon.CouponParams;
import com.kakaopay.preexam.model.response.Response;
import com.kakaopay.preexam.service.coupon.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestBody(required = true) CouponParams param) {
        if(param.getCount() < 1) {
            return Response.builder().code(HttpStatus.BAD_REQUEST.value()).msg("invalid parameter").build();
        }

        try {
            couponService.makeCoupon(param.getCount());
            return Response.builder().code(HttpStatus.OK.value()).msg("success").build();
        } catch (Exception e) {
            log.error(e.toString());
            return Response.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg("internal server error").build();
        }
    }

    /**
     * 사용자에게 쿠폰 지급 API
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/give", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response couponGive(
            @RequestBody(required = true) CouponParams param) {
        if(param.getAccountId() == null) {
            return Response.builder().code(HttpStatus.BAD_REQUEST.value()).msg("invalid parameter").build();
        }

        try {
            CouponInventoryResult result = couponService.couponGive(param);
            return Response.builder().code(HttpStatus.OK.value()).data(result).build();
        } catch (Exception e) {
            log.error(e.toString());
            return Response.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg(e.getMessage()).build();
        }
    }

    /**
     * 사용자 지급 쿠폰 조회 API
     *
     * @param param
     * @return
     */
    @GetMapping(value = "/give/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getGiveCouponList(
            @RequestBody(required = true) CouponParams param) {
        if(param.getAccountId() == null) {
            return Response.builder().code(HttpStatus.BAD_REQUEST.value()).msg("invalid parameter").build();
        }

        try {
            List<CouponInventoryResult> result = couponService.getGiveCouponList(param);
            return Response.builder().code(HttpStatus.OK.value()).data(result).build();
        } catch (Exception e) {
            log.error(e.toString());
            return Response.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg(e.getMessage()).build();
        }
    }

    /**
     * 지급된 쿠폰 사용 API
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/redeem", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response couponRedeem(
            @RequestBody(required = true) CouponParams param) throws Exception {
        if(param.getAccountId() == null || param.getCouponCode() == null) {
            return Response.builder().code(HttpStatus.BAD_REQUEST.value()).msg("invalid parameter").build();
        }

        try {
            couponService.couponRedeem(param);
            return Response.builder().code(HttpStatus.OK.value()).msg("success").build();
        } catch (Exception e) {
            log.error(e.toString());
            return Response.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg(e.getMessage()).build();
        }
    }

    /**
     * 지급된 쿠폰 사용 취소 API
     *
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/redeem/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response couponRedeemCancel(
            @RequestBody(required = true) CouponParams param) throws Exception {
        if (param.getAccountId() == null || param.getCouponCode() == null) {
            return Response.builder().code(HttpStatus.BAD_REQUEST.value()).msg("invalid parameter").build();
        }

        try {
            couponService.couponRedeemCancel(param);
            return Response.builder().code(HttpStatus.OK.value()).msg("success").build();
        } catch (Exception e) {
            log.error(e.toString());
            return Response.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg(e.getMessage()).build();
        }
    }

    /*
    // 당일 만료 쿠폰 목록 조회 API
    @GetMapping("/expired/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getExpiredCouponList() throws Exception {

    }
    */
}
