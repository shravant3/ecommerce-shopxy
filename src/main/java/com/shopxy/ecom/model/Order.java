package com.shopxy.ecom.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;


import com.shopxy.ecom.Util.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@SQLDelete(sql = "UPDATE order_table SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedOrderFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedOrderFilter", condition = "deleted = :isDeleted")
@Table(name ="order_table")
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;

    public String getFormattedOrderDate() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return orderDateTime.format(dateFormatter);
    }
    public String getDelivreyDate() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate orderDate = LocalDate.parse(orderDateTime.format(dateFormatter), dateFormatter);
        LocalDate newDate = orderDate.plusDays(4); // Adding 4 days
        return newDate.format(dateFormatter);
    }

    public String getFormattedOrderTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return orderDateTime.format(timeFormatter);
    }


    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "payment_method")
    private String paymentMethod;

    

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<OrderItem> orderitem=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address shippingAddress;

    // Method to add an OrderItem to the list
    public void addOrderItem(OrderItem orderItem) {
        orderitem.add(orderItem);
        orderItem.setOrder(this); // Set the order reference in the order item
    }

    // Getter for orderItems`
    
    public List<OrderItem> getOrderItems() {
        return orderitem;
    }
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "deleted")
    private boolean deleted = Boolean.FALSE;

    @Override
    public String toString() {
        return "Order [id=" + id + ", orderDateTime=" + orderDateTime + ", totalAmount=" + totalAmount
                + ", paymentMethod=" + paymentMethod + ", shippingAddress="
                + shippingAddress + ", status=" + status + "]";
    }

    
}
