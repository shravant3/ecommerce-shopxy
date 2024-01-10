package com.shopxy.ecom.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userDtls;

    private double totalamount;

    public double getTotalamount() {
        totalamount=0.0;
        for(CartItem item:items){
            totalamount+=(item.getProduct().getPrice()*item.getQuantity());
            
        }
        return totalamount;
    }

    @Override
    public String toString() {
        return "Cart [id=" + id + ", totalamount=" + totalamount + "]";
    }

   
}
