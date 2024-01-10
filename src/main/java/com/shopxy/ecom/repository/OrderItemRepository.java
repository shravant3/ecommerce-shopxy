package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Order;
import com.shopxy.ecom.model.OrderItem;
import java.util.List;
@Repository

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer>{
    public List<OrderItem> findByOrder(Order order);
}
