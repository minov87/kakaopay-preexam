package com.kakaopay.preexam.service.coupon;

import com.kakaopay.preexam.model.account.Account;
import com.kakaopay.preexam.model.coupon.Coupon;
import com.kakaopay.preexam.model.coupon.CouponInfo;
import com.kakaopay.preexam.model.coupon.CouponInventory;
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
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.*;

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

    private Calendar cal = Calendar.getInstance();

    /**
     * 랜덤 쿠폰 N개 생성
     *
     * @param count
     * @throws Exception
     */
    @Transactional
    public void makeCoupon(int count) throws Exception {
        cal.add(Calendar.YEAR, 1);
        Date expireDate = cal.getTime();

        for(int i = 0; i < count; i++) {
            Coupon coupon = new Coupon();
            coupon.setType("CREATE");
            coupon.setIsvalid(0);
            coupon.setExpireTime(expireDate);
            coupon.setCouponCode(CouponUtil.generateCoupon());
            em.persist(coupon);

            // 메모리 오류 방지 및 통신비용 감소를 위한 영속화
            if(i % 500 == 0) {
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
    public CouponInfo couponGive(CouponParams params) throws Exception {
        // 지급할 미사용 쿠폰 1개 조회
        Date expireDate = cal.getTime();
        Account account = accountRepository.findById(params.getAccountId())
                .orElseThrow(() -> new Exception("not exist account"));
        Coupon coupon = couponRepository.findTop1ByIsvalidEqualsAndExpireTimeGreaterThanEqualOrderByCreateTimeAsc(0, expireDate)
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
        CouponInfo couponInfo = new CouponInfo();
        couponInfo.setCouponCode(coupon.getCouponCode());
        couponInfo.setExpireTime(couponInventory.getExpireTime());
        couponInfo.setStatus(couponInventory.getStatus());

        // 등록된 쿠폰 정보 반환
        return couponInfo;
    }
}
