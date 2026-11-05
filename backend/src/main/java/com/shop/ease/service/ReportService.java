package com.shop.ease.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.shop.ease.model.Order;
import com.shop.ease.model.OrderItem;

public class ReportService {

    private final SalesService salesService;

    public ReportService(SalesService salesService) {
        this.salesService = salesService;
    }

    public void generateSalesReport(Order order, List<OrderItem> orderItems) {
        try {
            FileWriter writer = new FileWriter("sales_report.csv");
            writeOrderItemToCSV(writer, orderItems.get(0), order); // Assuming you want to write the first order item for simplicity
            writer.close();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    private void writeOrderItemToCSV(FileWriter writer, OrderItem orderItem, Order order) throws IOException {
        writer.append("Product,Quantity,Revenue\n");
        writer.append(orderItem.getProduct().getProductName()).append(",");
        writer.append(String.valueOf(orderItem.getOrderQuantity())).append(",");
    }
    
}
