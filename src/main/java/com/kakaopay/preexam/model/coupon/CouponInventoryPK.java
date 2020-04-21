package com.kakaopay.preexam.model.coupon;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class CouponInventoryPK implements Serializable {
    private static final long serialVersionUID = 7650081866796850715L;

    @Column(name = "id")
    private Long id;

    public CouponInventoryPK() {
    }
}
