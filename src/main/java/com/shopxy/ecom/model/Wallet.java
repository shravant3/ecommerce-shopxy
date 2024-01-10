package com.shopxy.ecom.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "wallet")
public class Wallet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    private Double currentBalance=0.0;

    @OneToMany(mappedBy ="wallet",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WalletHistory> WalletHistory;

    public Wallet() {
        this.currentBalance = 0.0;
    }

    @Override
    public String toString() {
        return "Wallet [id=" + id + ", user=" + user + ", currentBalance=" + currentBalance + "]";
    }
  
}
