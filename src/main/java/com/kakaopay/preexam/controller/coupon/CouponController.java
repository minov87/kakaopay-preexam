package com.kakaopay.preexam.controller.coupon;

import com.kakaopay.preexam.exception.BaseException;
import com.kakaopay.preexam.model.coupon.CouponInventoryResult;
import com.kakaopay.preexam.model.coupon.CouponParams;
import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import com.kakaopay.preexam.model.response.Response;
import com.kakaopay.preexam.service.coupon.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    @Autowired
    CouponService couponService;

    /**
     * 랜덤 쿠폰 N개 생성 API
     *
     * @param param 셍성 갯수
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/make", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response makeCoupon(
            @RequestBody(required = true) CouponParams param) throws Exception {
        if(param.getCount() < 1) {
            throw new BaseException(RESPONSE_STATUS.BAD_REQUEST.getMessage(), RESPONSE_STATUS.BAD_REQUEST.getCode());
        }

        couponService.makeCoupon(param.getCount());
        return Response.builder().code(RESPONSE_STATUS.SUCCESS.getCode()).msg(RESPONSE_STATUS.SUCCESS.getMessage()).build();
    }

    /**
     * 사용자에게 쿠폰 지급 API
     *
     * @param param 사용자 ID
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/give", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response couponGive(
            @RequestBody(required = true) CouponParams param) throws Exception {
        if(param.getAccountId() == null) {
            throw new BaseException(RESPONSE_STATUS.BAD_REQUEST.getMessage(), RESPONSE_STATUS.BAD_REQUEST.getCode());
        }

        CouponInventoryResult result = couponService.couponGive(param);
        return Response.builder().code(RESPONSE_STATUS.SUCCESS.getCode()).data(result).build();
    }

    /**
     * 사용자 지급 쿠폰 조회 API
     *
     * @param param 사용자 ID
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/give/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getGiveCouponList(
            @RequestBody(required = true) CouponParams param) throws Exception {
        if(param.getAccountId() == null) {
            throw new BaseException(RESPONSE_STATUS.BAD_REQUEST.getMessage(), RESPONSE_STATUS.BAD_REQUEST.getCode());
        }

        List<CouponInventoryResult> result = couponService.getGiveCouponList(param);
        return Response.builder().code(RESPONSE_STATUS.SUCCESS.getCode()).data(result).build();
    }

    /**
     * 지급된 쿠폰 사용 API
     *
     * @param param 사용자 ID, 쿠폰 번호
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/redeem", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response couponRedeem(
            @RequestBody(required = true) CouponParams param) throws Exception {
        if(param.getAccountId() == null || param.getCouponCode() == null) {
            throw new BaseException(RESPONSE_STATUS.BAD_REQUEST.getMessage(), RESPONSE_STATUS.BAD_REQUEST.getCode());
        }

        couponService.couponRedeem(param);
        return Response.builder().code(RESPONSE_STATUS.SUCCESS.getCode()).msg(RESPONSE_STATUS.SUCCESS.getMessage()).build();
    }

    /**
     * 지급된 쿠폰 사용 취소 API
     *
     * @param param 사용자 ID, 쿠폰 번호
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/redeem/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response couponRedeemCancel(
            @RequestBody(required = true) CouponParams param) throws Exception {
        if (param.getAccountId() == null || param.getCouponCode() == null) {
            throw new BaseException(RESPONSE_STATUS.BAD_REQUEST.getMessage(), RESPONSE_STATUS.BAD_REQUEST.getCode());
        }

        couponService.couponRedeemCancel(param);
        return Response.builder().code(RESPONSE_STATUS.SUCCESS.getCode()).msg(RESPONSE_STATUS.SUCCESS.getMessage()).build();
    }

    /**
     * 발급된 쿠폰 중 당일 만료된 쿠폰 목록 조회 API
     *
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/expired/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getExpiredCouponList() throws Exception {
        List<CouponInventoryResult> couponInventoryResultList = couponService.getExpiredCouponList();
        return Response.builder().code(RESPONSE_STATUS.SUCCESS.getCode()).data(couponInventoryResultList).build();
    }
}
