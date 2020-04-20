package com.kakaopay.preexam.model.coupon;

import com.kakaopay.preexam.model.account.Account;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "coupon_inventory")
public class CouponInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "status")
    private String status;

    @Column(name = "use_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date useTime;

    @CreationTimestamp
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "expire_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;

    public CouponInventory() {}
    public CouponInventory(Coupon coupon, Account account, String status, Date useTime, Date expireTime){
        this.coupon = coupon;
        this.account = account;
        this.status = status;
        this.useTime = useTime;
        this.expireTime = expireTime;
    }
}
