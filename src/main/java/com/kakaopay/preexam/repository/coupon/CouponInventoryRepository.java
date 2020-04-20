package com.kakaopay.preexam.repository.coupon;

import com.kakaopay.preexam.model.coupon.CouponInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponInventoryRepository extends JpaRepository<CouponInventory, Long> {
}
