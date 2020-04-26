package com.kakaopay.preexam.service.coupon;

import com.kakaopay.preexam.exception.AccountException;
import com.kakaopay.preexam.exception.BaseException;
import com.kakaopay.preexam.exception.CouponException;
import com.kakaopay.preexam.model.account.Account;
import com.kakaopay.preexam.model.coupon.Coupon;
import com.kakaopay.preexam.model.coupon.CouponInventory;
import com.kakaopay.preexam.model.coupon.CouponInventoryResult;
import com.kakaopay.preexam.model.coupon.CouponParams;
import com.kakaopay.preexam.model.response.RESPONSE_STATUS;
import com.kakaopay.preexam.repository.account.AccountRepository;
import com.kakaopay.preexam.repository.coupon.CouponInventoryRepository;
import com.kakaopay.preexam.repository.coupon.CouponRepository;
import com.kakaopay.preexam.util.CouponUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    private final int couponLimitCount = 100000;

    //@PersistenceContext
    //private EntityManager em;

    private LocalDateTime nowDateTime = LocalDateTime.now();
    private LocalDateTime nowDateTimeMod = LocalDate.now().atTime(LocalTime.now().plusHours(1).getHour(), (LocalDateTime.now().getMinute() / 10) * 10);
    private LocalDateTime expireDateTime = LocalDateTime.now().with(LocalTime.MAX).plusYears(1);

    /**
     * 랜덤 쿠폰 N개 생성
     *
     * @param params
     * @throws Exception
     */
    @Transactional
    public void makeCoupon(CouponParams params) throws Exception {
        int count = params.getCount();
        if(count < 1){
            throw new BaseException(RESPONSE_STATUS.BAD_REQUEST);
        }

        // 1회 요청시 100만건 이상 요청하지 못하도록 제한
        if(count > couponLimitCount) {
            throw CouponException.COUPON_MAKE_COUNT_OVER;
        }

        try {
            if(params.getExpireTime() != null) expireDateTime = LocalDateTime.parse(params.getExpireTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            List<Coupon> couponList = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                Coupon coupon = Coupon.builder()
                        .couponCode(CouponUtil.generateCoupon())
                        .type("CREATE")
                        .isvalid(0)
                        .expireTime(expireDateTime)
                        .build();
                couponList.add(coupon);

                // 메모리 오류 방지 및 통신비용 감소를 위한 영속화 - unit test 문제로 기본 방식 사용.
                //em.persist(coupon);
                if (i % 1000 == 0) {
                //    em.flush();
                //    em.clear();
                    couponRepository.saveAll(couponList);
                    couponList.clear();
                }
            }

            couponRepository.saveAll(couponList);

            //em.flush();
            //em.clear();
        } catch (Exception e) {
            throw CouponException.COUPON_MAKE_FAIL;
        }
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
                .orElseThrow(() -> AccountException.ACCOUNT_NOT_EXIST);
        Coupon coupon = couponRepository.findTop1ByIsvalidEqualsAndExpireTimeGreaterThanEqualOrderByCreateTimeAsc(0, nowDateTimeMod)
                .orElseThrow(() -> CouponException.COUPON_NOT_ENOUGH);

        // 지급할 미사용 쿠폰 정보를 지급 쿠폰함에 등록
        CouponInventory couponInventory = CouponInventory.builder()
                .account(account)
                .coupon(coupon)
                .status("NOT_USED")
                .expireTime(coupon.getExpireTime())
                .build();
        couponInventoryRepository.save(couponInventory);

        // 쿠폰 테이블의 사용 가능 여부 변경
        coupon.setIsvalid(1);
        couponRepository.save(coupon);

        // 반환 데이터 정의
        CouponInventoryResult couponInventoryResult = CouponInventoryResult.builder()
                .couponCode(coupon.getCouponCode())
                .status(couponInventory.getStatus())
                .expireTime(couponInventory.getExpireTime())
                .build();

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
                .orElseThrow(() -> AccountException.ACCOUNT_NOT_EXIST);

        return couponInventoryRepository.findUserCouponList(params.getAccountId(), params.getPagePerCount());
    }

    /**
     * 지급된 쿠폰 사용
     *
     * @param params
     * @throws Exception
     */
    @Transactional
    public void couponRedeem(CouponParams params) throws Exception {
        // 지급할 사용자 조회
        Account account = accountRepository.findById(params.getAccountId())
                .orElseThrow(() -> AccountException.ACCOUNT_NOT_EXIST);

        // 쿠폰 코드와 사용자 정보가 매칭되는 쿠폰 보관함 정보 조회
        CouponInventory couponInventory = couponInventoryRepository.findMatchedCouponInventoryDetail(params.getAccountId(), params.getCouponCode())
                .orElseThrow(() -> CouponException.COUPON_NOT_EXIST);

        // 쿠폰 사용 처리
        // 만약 이미 사용 처리된 쿠폰인 경우 불가 처리
        // 만약 쿠폰 기간이 만료된 경우 취소 불가 처리
        if(!couponInventory.getStatus().equals("USED")) {
            if(!nowDateTime.isBefore(couponInventory.getExpireTime())) {
                throw CouponException.COUPON_EXPIRED;
            }

            couponInventory.setUseTime(nowDateTime);
            couponInventory.setStatus("USED");

            couponInventoryRepository.save(couponInventory);
        } else {
            throw CouponException.COUPON_ALREADY_USED;
        }
    }

    /**
     * 지급된 쿠폰 사용 취소
     *
     * @param params
     * @throws Exception
     */
    @Transactional
    public void couponRedeemCancel(CouponParams params) throws Exception {
        // 지급할 사용자 조회
        Account account = accountRepository.findById(params.getAccountId())
                .orElseThrow(() -> AccountException.ACCOUNT_NOT_EXIST);

        // 쿠폰 코드와 사용자 정보가 매칭되는 쿠폰 보관함 정보 조회
        CouponInventory userCouponInventory = couponInventoryRepository.findMatchedCouponInventoryDetail(params.getAccountId(), params.getCouponCode())
                .orElseThrow(() -> CouponException.COUPON_NOT_EXIST);

        // 쿠폰 사용 취소 처리
        // 만약 쿠폰 기간이 만료된 경우 취소 불가 처리
        if(userCouponInventory.getStatus().equals("USED")){
            if(!nowDateTime.isBefore(userCouponInventory.getExpireTime())) {
                throw CouponException.COUPON_EXPIRED;
            }

            CouponInventory couponInventory = userCouponInventory;
            couponInventory.setUseTime(null);
            couponInventory.setStatus("NOT_USED");

            couponInventoryRepository.save(couponInventory);
        } else {
            throw CouponException.COUPON_NOT_EXIST;
        }
    }

    /**
     * 발급된 쿠폰 중 당일 만료된 쿠폰 목록 조회
     *
     * @param params
     * @return
     * @throws Exception
     */
    public List<CouponInventoryResult> getExpiredCouponList(CouponParams params) throws Exception {
        LocalDateTime starTime = nowDateTime.with(LocalTime.MIN);

        // 사용자에게 발급된 쿠폰 중 당일 만료 쿠폰 조회
        return couponInventoryRepository.findExpiredCouponList(starTime, nowDateTimeMod, params.getPagePerCount());
    }
}