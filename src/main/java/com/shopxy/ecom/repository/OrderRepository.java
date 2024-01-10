package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Order;
import com.shopxy.ecom.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface OrderRepository extends JpaRepository<Order,Integer> {
    
    public List<Order> findByUser(User user);

    // public List<Order> findByUserOrderByOrderDateTimeDesc(UserDtls user);
    public List<Order> findAllByUserOrderByOrderDateTimeDesc(User user);
     public List<Order> findAllByOrderByOrderDateTimeDesc();

    public Order findById(int orderId);

    @Query("SELECT count(*) FROM Order")
    public int countorder();

    @Query("SELECT o FROM Order o WHERE o.orderDateTime >= :startDate AND o.orderDateTime <= :endDate")
    List<Order> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    

    

    
}
