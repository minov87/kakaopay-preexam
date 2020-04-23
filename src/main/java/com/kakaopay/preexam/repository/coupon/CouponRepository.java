package com.kakaopay.preexam.repository.coupon;

import com.kakaopay.preexam.model.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findTop1ByIsvalidEqualsAndExpireTimeGreaterThanEqualOrderByCreateTimeAsc(int isvalid, LocalDateTime expireTime);

    boolean existsByCouponCode(String couponCode);
}
