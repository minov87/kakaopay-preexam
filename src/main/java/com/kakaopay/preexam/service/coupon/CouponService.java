package com.kakaopay.preexam.service.coupon;

import com.kakaopay.preexam.model.account.Account;
import com.kakaopay.preexam.model.coupon.Coupon;
import com.kakaopay.preexam.model.coupon.CouponInventory;
import com.kakaopay.preexam.model.coupon.CouponInventoryResult;
import com.kakaopay.preexam.model.coupon.CouponParams;
import com.kakaopay.preexam.repository.account.AccountRepository;
import com.kakaopay.preexam.repository.coupon.CouponInventoryRepository;
import com.kakaopay.preexam.repository.coupon.CouponRepository;
import com.kakaopay.preexam.util.CouponUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Slf4j
@Service
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponInventoryRepository couponInventoryRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public CouponService(
            CouponRepository couponRepository,
            CouponInventoryRepository couponInventoryRepository,
            AccountRepository accountRepository) {
        this.couponRepository = couponRepository;
        this.couponInventoryRepository = couponInventoryRepository;
        this.accountRepository = accountRepository;
    }

    @PersistenceContext
    private EntityManager em;

    private final LocalDateTime nowDateTime = LocalDateTime.now();

    /**
     * 랜덤 쿠폰 N개 생성
     *
     * @param count
     * @throws Exception
     */
    @Transactional
    public void makeCoupon(int count) {
        LocalDateTime expireDate = nowDateTime.plus(Period.ofYears(1));

        for (int i = 0; i < count; i++) {
            Coupon coupon = new Coupon();
            coupon.setType("CREATE");
            coupon.setIsvalid(0);
            coupon.setExpireTime(expireDate);
            coupon.setCouponCode(CouponUtil.generateCoupon());
            em.persist(coupon);

            // 메모리 오류 방지 및 통신비용 감소를 위한 영속화
            if (i % 500 == 0) {
                em.flush();
                em.clear();
            }
        }

        em.flush();
        em.clear();
    }

    /**
     * 사용자에게 쿠폰 지급
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Transactional
    public CouponInventoryResult couponGive(CouponParams params) throws Exception {
        // 지급할 사용자 및 지급할 미사용 쿠폰 1개 조회
        Account account = accountRepository.findById(params.getAccountId())
                .orElseThrow(() -> new Exception("not exist account"));
        Coupon coupon = couponRepository.findTop1ByIsvalidEqualsAndExpireTimeGreaterThanEqualOrderByCreateTimeAsc(0, nowDateTime)
                .orElseThrow(() -> new Exception("not enouth coupons"));

        // 지급할 미사용 쿠폰 정보를 지급 쿠폰함에 등록
        CouponInventory couponInventory = new CouponInventory();
        couponInventory.setAccount(account);
        couponInventory.setCoupon(coupon);
        couponInventory.setExpireTime(coupon.getExpireTime());
        couponInventory.setStatus("ACTIVE");

        couponInventoryRepository.save(couponInventory);

        // 쿠폰 테이블의 사용 가능 여부 변경
        coupon.setIsvalid(1);
        couponRepository.save(coupon);

        // 반환 데이터 정의
        CouponInventoryResult couponInventoryResult = new CouponInventoryResult();
        couponInventoryResult.setCouponCode(coupon.getCouponCode());
        couponInventoryResult.setExpireTime(couponInventory.getExpireTime());
        couponInventoryResult.setStatus(couponInventory.getStatus());

        // 등록된 쿠폰 정보 반환
        return couponInventoryResult;
    }

    /**
     * 사용자 지급 쿠폰 조회
     *
     * @param params
     * @return
     */
    public List<CouponInventoryResult> getGiveCouponList(CouponParams params) throws Exception {
        // 지급할 사용자 조회
        Account account = accountRepository.findById(params.getAccountId())
                .orElseThrow(() -> new Exception("not exist account"));

        List<CouponInventoryResult> couponInventoryResultList = couponInventoryRepository.findUserCouponList(params.getAccountId());

        return couponInventoryResultList;
    }

    /**
     * 지급된 쿠폰 사용
     *
     * @param params
     * @throws Exception
     */
    public void couponRedeem(CouponParams params) throws Exception {
        // 지급할 사용자 조회
        Account account = accountRepository.findById(params.getAccountId())
                .orElseThrow(() -> new Exception("not exist account"));

        // 쿠폰 코드와 사용자 정보가 매칭되는 쿠폰 보관함 정보 조회
        CouponInventory userCouponInventory = couponInventoryRepository.findMatchedCouponInventoryDetail(params.getAccountId(), params.getCouponCode())
                .orElseThrow(() -> new Exception("fail to redeem - not exist matched coupon"));

        // 쿠폰 사용 처리
        CouponInventory couponInventory = userCouponInventory;
        couponInventory.setUseTime(nowDateTime);
        couponInventory.setStatus("USED");

        couponInventoryRepository.save(couponInventory);
    }

    /**
     * 지급된 쿠폰 사용 취소
     *
     * @param params
     * @throws Exception
     */
    public void couponRedeemCancel(CouponParams params) throws Exception {
        // 지급할 사용자 조회
        Account account = accountRepository.findById(params.getAccountId())
                .orElseThrow(() -> new Exception("not exist account"));

        // 쿠폰 코드와 사용자 정보가 매칭되는 쿠폰 보관함 정보 조회
        CouponInventory userCouponInventory = couponInventoryRepository.findMatchedCouponInventoryDetail(params.getAccountId(), params.getCouponCode())
                .orElseThrow(() -> new Exception("fail to redeem - not exist matched coupon"));

        // 쿠폰 사용 취소 처리
        // 만약 쿠폰 기간이 만료된 경우 취소 불가 처리
        if(userCouponInventory.getStatus().equals("USED") && nowDateTime.isBefore(userCouponInventory.getExpireTime())){
            CouponInventory couponInventory = userCouponInventory;
            couponInventory.setUseTime(null);
            couponInventory.setStatus("ACTIVE");

            couponInventoryRepository.save(couponInventory);
        } else {
            throw new Exception("fail to redeem cancel - coupon date expired.");
        }
    }
}
