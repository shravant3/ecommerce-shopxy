package com.shopxy.ecom.model;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@SQLDelete(sql = "UPDATE order_item SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedOrderItemFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedOrderItemFilter", condition = "deleted = :isDeleted")
@Entity
@Table(name = "order_item")
public class OrderItem 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private boolean deleted = Boolean.FALSE;
    
}
