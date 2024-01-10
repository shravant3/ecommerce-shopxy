package com.shopxy.ecom.model;
import java.time.LocalDateTime;

import com.shopxy.ecom.Util.WalletTransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "wallet_transaction")
public class WalletHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private LocalDateTime transactionTime;

    private double amountTransferred;

    private String description;

    @Column(name="transaction status")
    @Enumerated(EnumType.STRING)
    private WalletTransactionStatus status;
}
