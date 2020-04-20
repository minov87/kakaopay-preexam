package com.kakaopay.preexam.model.coupon;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {
    // GenerationType.AUTO 설정시 테이블 기본 시퀀스를 기반 사용 되므로 IDENTITY 정의
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    @Column(name = "type")
    private String type;

    @Column(name = "isvalid")
    private int isvalid = 0;

    @CreationTimestamp
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "expire_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;
}
