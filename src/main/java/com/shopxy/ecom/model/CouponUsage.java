package com.shopxy.ecom.model;

import java.time.LocalDateTime;

import com.shopxy.ecom.Util.CouponStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class CouponUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Coupon coupon;

    @Column(name = "couponUsage_status")
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    private LocalDateTime usedAt;

    @Override
    public String toString() {
        return "CouponUsage{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", coupon=" + (coupon != null ? coupon.getId() : null) +
                ", status=" + status +
                ", usedAt=" + usedAt +
                '}';
    }
}
