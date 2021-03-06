package com.kakaopay.preexam.model.coupon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Entity
public class CouponInventoryResult implements Serializable {
    private static final long serialVersionUID = 5112998889731887248L;

    protected CouponInventoryResult() { }

    @Id
    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "expire_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expireTime;

    @Column(name = "status")
    private String status;
}
