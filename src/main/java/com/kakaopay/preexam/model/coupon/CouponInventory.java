package com.kakaopay.preexam.model.coupon;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kakaopay.preexam.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "CouponInventory.findUserCouponList",
                query = "SELECT c.id, c.coupon_code, ci.status, ci.expire_time " +
                        "FROM coupon_inventory AS ci " +
                        "INNER JOIN coupons AS c ON ci.coupon_id = c.id " +
                        "WHERE ci.account_id = :accountId " +
                        "ORDER BY ci.id DESC",
                resultClass = CouponInventoryResult.class),
        @NamedNativeQuery(
                name = "CouponInventory.findExpiredCouponList",
                query = "SELECT c.coupon_code, ci.status, ci.expire_time " +
                        "FROM coupon_inventory AS ci " +
                        "INNER JOIN coupons AS c ON ci.coupon_id = c.id " +
                        "WHERE ci.expire_time BETWEEN :starTime AND :endTime " +
                        "ORDER BY ci.id DESC",
                resultClass = CouponInventoryResult.class)
})
@Data
@Builder
@AllArgsConstructor
@Entity(name = "CouponInventory")
@Table(name = "coupon_inventory")
public class CouponInventory implements Serializable {
    private static final long serialVersionUID = 344358140916304245L;

    protected CouponInventory() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    @JsonBackReference
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @Column(name = "status")
    private String status;

    @Column(name = "use_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime useTime;

    @CreationTimestamp
    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;

    @Column(name = "expire_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expireTime;
}
