package com.kakaopay.preexam.model.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Entity(name = "Coupon")
@Table(name = "coupons")
public class Coupon implements Serializable {
    private static final long serialVersionUID = 899658754065197989L;

    protected Coupon() { }

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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;

    @Column(name = "expire_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expireTime;
}
