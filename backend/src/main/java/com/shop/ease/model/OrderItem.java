package com.shop.ease.model;

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

@SQLDelete(sql = "UPDATE order_item SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedOrderItemFilter", parameters = @ParamDef(name = "isDeleted", type = boolean.class))
@Filter(name = "deletedOrderItemFilter", condition = "deleted = :isDeleted")
@Entity
@Table(name = "order_item")
public class OrderItem 
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int orderQuantity;

    private boolean orderItemDeleted = Boolean.FALSE;

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public boolean isOrderItemDeleted() {
        return orderItemDeleted;
    }

    public void setOrderItemDeleted(boolean orderItemDeleted) {
        this.orderItemDeleted = orderItemDeleted;
    }


    
    
    
}
