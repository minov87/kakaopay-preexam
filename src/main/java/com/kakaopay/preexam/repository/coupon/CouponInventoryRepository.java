package com.kakaopay.preexam.repository.coupon;

import com.kakaopay.preexam.model.coupon.CouponInventory;
import com.kakaopay.preexam.model.coupon.CouponInventoryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponInventoryRepository extends JpaRepository<CouponInventory, Long> {
    @Query("SELECT DISTINCT a FROM CouponInventory a JOIN FETCH a.coupon AS d WHERE a.account.id = :accountId")
    List<CouponInventory> findAllWithCoupon(@Param("accountId") Long accountId);

    @Query("SELECT DISTINCT a FROM CouponInventory a JOIN FETCH a.coupon AS d WHERE a.account.id = :accountId AND d.couponCode = :couponCode")
    Optional<CouponInventory> findMatchedCouponInventoryDetail(
            @Param("accountId") Long accountId,
            @Param("couponCode") String couponCode);

    @Query(nativeQuery = true)
    List<CouponInventoryResult> findUserCouponList(@Param("accountId") Long accountId);
}
