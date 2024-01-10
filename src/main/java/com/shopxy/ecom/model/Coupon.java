package com.shopxy.ecom.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.shopxy.ecom.Util.CouponStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Coupon code is required")
    private String code;

    @Min(value = 0, message = "Discount percentage must be greater than or equal to 0")
    @Max(value = 100, message = "Discount percentage must be less than or equal to 100")
    private double discountPercentage;

    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @Column(name = "coupon_status")
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE)
    private List<CouponUsage> couponUsages = new ArrayList<>();

    @Override
    public String toString() {
        return "Coupon{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", discountPercentage=" + discountPercentage +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", status=" + status +
            ", couponUsages=" + couponUsages +
            '}';
    }
}
