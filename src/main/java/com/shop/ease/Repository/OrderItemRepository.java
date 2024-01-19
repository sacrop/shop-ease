package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Order;
import com.shop.ease.model.OrderItem;
import java.util.List;
@Repository

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer>{
    public List<OrderItem> findByOrder(Order order);
}
