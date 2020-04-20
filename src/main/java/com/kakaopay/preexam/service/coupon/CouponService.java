package com.kakaopay.preexam.service.coupon;

import com.kakaopay.preexam.model.coupon.Coupon;
import com.kakaopay.preexam.model.coupon.CouponInventory;
import com.kakaopay.preexam.util.CouponUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
public class CouponService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void makeCoupon(int count) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Date expireDate = cal.getTime();

        if(count < -1){
            // TO-DO 익셉션 별도로 생성해서 처리하기
            throw new Exception("out of range");
        } else {
            for(int i = 0; i < count; i++) {
                Coupon coupon = new Coupon();
                coupon.setType("CREATE");
                coupon.setIsvalid(0);
                coupon.setExpireTime(expireDate);
                coupon.setCouponCode(CouponUtil.generateCoupon());
                em.persist(coupon);

                // 메모리 오류 방지 및 통신비용 감소를 위한 영속화 import java.util.List;
                if(i % 500 == 0) {
                    em.flush();
                    em.clear();
                }
            }

            em.flush();
            em.clear();
        }
    }
}
