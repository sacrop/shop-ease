package com.shop.ease.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.OrderItemRepository;
import com.shop.ease.Repository.OrderRepository;
import com.shop.ease.Util.OrderStatus;
import com.shop.ease.helper.MycustomException;
import com.shop.ease.model.Address;
import com.shop.ease.model.Cart;
import com.shop.ease.model.CartItem;
import com.shop.ease.model.Order;
import com.shop.ease.model.OrderItem;
import com.shop.ease.model.Product;
import com.shop.ease.model.UserDtls;


import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    DecimalFormat df = new DecimalFormat("#.00");

    public Order createOrder(Long id, String payment, Double price, UserDtls user, CartItem cartItem, Cart cart) {
        // Assuming addressService is a valid service for getting the address by ID
        Address address = addressService.getAddressById(id);

        // Create and save an OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());

        orderItemRepository.save(orderItem);

        // Create the Order
        Order order = new Order();
        order.setShippingAddress(address);
        order.setOrderDateTime(LocalDateTime.now());
        order.setPaymentMethod(payment);
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);

        // Add the OrderItem to the Order's list of items
        order.addOrderItem(orderItem);
        order.setTotalAmount(Double.parseDouble(df.format(cart.getTotalamount())));
        // Save the Order (assuming you have a method like saveOrder in your
        // service/repository)
        this.saveOrder(order);
        shoppingCartService.removeCartByCartItem(cartItem);

        return order;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order createSingleorder(Long addressid, double price, UserDtls user, String payment, int quantity,
            Long productid) {

        Address address = addressService.getAddressById(addressid);
        OrderItem orderitem = new OrderItem();
        orderitem.setProduct(productService.getproductbyid(productid));
        orderitem.setQuantity(quantity);

        orderItemRepository.save(orderitem);
        Order order = new Order();

        order.setShippingAddress(address);
        order.setOrderDateTime(LocalDateTime.now());
        order.setPaymentMethod(payment);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setUser(user);
        order.addOrderItem(orderitem);
        order.setTotalAmount(Double.parseDouble(df.format(price)));
        this.saveOrder(order);
        return order;
    }

    @Transactional
    public Order createOrder(UserDtls user, Long addressId, String payment, Double grandTotal) {
        try {
            Address address = addressService.getAddressById(addressId);
            if (address == null) {
                // Handle the case where the address is not found
                throw new MycustomException("Address not found for id: " + addressId);
            }

            Cart cart = shoppingCartService.getCartByUserId(user.getId());
            if (cart == null) {
                // Handle the case where the cart is not found
                throw new MycustomException("Cart not found for user: " + user.getId());
            }

            List<CartItem> cartItems = shoppingCartService.getCartItemByUserDtls(user);

            Order order = new Order();
            order.setShippingAddress(address);
            order.setOrderDateTime(LocalDateTime.now());
            order.setPaymentMethod(payment); // Set the payment method accordingly
            order.setUser(user);

            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItemRepository.save(orderItem);
                order.addOrderItem(orderItem);
            }
            order.setStatus(OrderStatus.CONFIRMED);
            order.setTotalAmount(Double.parseDouble(df.format(grandTotal)));
            this.saveOrder(order);

            // Clear the user's shopping cart after successful order creation
            shoppingCartService.clearCart(user);

            return order;
        } catch (MycustomException e) {
            // Handle exceptions and log appropriately
            // throw new MycustomException("Error creating order for user: " + user.getId(),
            // e);
            return null;
        }

    }

    public List<Order> getOrderByUser(UserDtls user) {
        return orderRepository.findByUser(user);
    }

    public List<OrderItem> getAllOrderItemsByOrder(Order order) {

        return orderItemRepository.findByOrder(order);
    }

    public List<Order> getAllOrderByOrderDateTimeDesc() {
        for(Order orders:orderRepository.findAllByOrderByOrderDateTimeDesc()){
            orders.setTotalAmount(Double.parseDouble(df.format(orders.getTotalAmount())));
            orderRepository.save(orders);
        }
        return orderRepository.findAllByOrderByOrderDateTimeDesc();
    }

    public void deleteOrderByOrderId(int orderId) {
        List<OrderItem> orderitems = orderItemRepository.findByOrder(orderRepository.findById(orderId));
        for (OrderItem orderitem : orderitems) {
            orderItemRepository.deleteById(orderitem.getId());
        }
        orderRepository.deleteById(orderId);
    }

    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId);
    }

    public void cancelOrder(int orderId) {
        Order orders = orderRepository.findById(orderId);
        orders.setStatus(OrderStatus.CANCELED);
        orderRepository.save(orders);

    }

    public List<Order> getAllOrderByDate(LocalDate date1, LocalDate date2) {
        LocalDateTime startDateTime = date1.atStartOfDay();
        LocalDateTime endDateTime = date2.atTime(LocalTime.now());
        return orderRepository.findOrdersBetweenDatesOrderedByDateDesc(startDateTime, endDateTime);
    }

    public static Order getsalesHistory() {
        return null;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public double getTotalSale() {
        double amount = 0.0;
        for (Order orders : orderRepository.findAll()) {

            if (orders.getStatus() == OrderStatus.DELIVERED) {
                amount += orders.getTotalAmount();
            }
        }
        return Double.parseDouble(df.format(amount));
    }

    public boolean userProductDelivered(UserDtls user,Product product){
        List<Order> orderList=orderRepository.findByUser(user);
        for(Order orders:orderList){
            for(OrderItem orderItem:orders.getOrderItems()){
                if(orderItem.getProduct().equals(product))
                {
                    return true;
                }
            }
        }
        return false;
    }

}
