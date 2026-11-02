package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.MyUserDetails;
import com.shop.ease.model.Order;


import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface OrderRepository extends JpaRepository<Order,Integer> {
    
    public List<Order> findByUser(MyUserDetails user);

    // public List<Order> findByUserOrderByOrderDateTimeDesc(MyUserDetails user);
    public List<Order> findAllByUserOrderByOrderDateTimeDesc(MyUserDetails user);
     public List<Order> findAllByOrderByOrderDateTimeDesc();

    public Order findById(int orderId);

    @Query("SELECT o FROM Order o WHERE o.orderDateTime BETWEEN :startDate AND :endDate ORDER BY o.orderDateTime DESC")
    public List<Order> findOrdersBetweenDatesOrderedByDateDesc(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    
}
