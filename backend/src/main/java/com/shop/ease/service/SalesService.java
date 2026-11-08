package com.shop.ease.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.ease.model.Order;
import com.shop.ease.model.OrderItem;

@Service
public class SalesService {
    
    private final OrderService orderService;

    public SalesService(OrderService orderService) {
        this.orderService = orderService;
    }

    public byte[] getSalesReportData(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderService.getAllOrderByDate(startDate, endDate);
        StringBuilder csv = new StringBuilder("Product,Quantity,Revenue\n");

        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                csv.append(orderItem.getProduct().getProductName()).append(',')
                .append(orderItem.getOrderQuantity()).append(',')
                .append(order.getTotalAmount()).append('\n');
            }
        }
        return csv.toString().getBytes();
    }

}
