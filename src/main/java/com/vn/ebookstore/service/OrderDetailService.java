package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Coupon;
import com.vn.ebookstore.model.OrderDetail;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.*;
@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    public Optional<OrderDetail> getOrderDetailById(Integer id) {
        return orderDetailRepository.findById(id);
    }

    public OrderDetail updateOrderStatus(Integer orderId, String newStatus) {
        Optional<OrderDetail> optionalOrder = orderDetailRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderDetail order = optionalOrder.get();
            order.setStatus(newStatus);
            return orderDetailRepository.save(order);
        }
        return null; // Hoặc throw exception
    }

public interface OrderDetailService {
    OrderDetail createOrder(Integer userId, Integer addressId, String paymentMethod, String note, Coupon coupon);
    OrderDetail getOrderById(Integer id);
    List<OrderDetail> getOrdersByUserId(Integer userId);
    void cancelOrder(Integer orderId);
    OrderDetail updateOrderStatus(Integer orderId, String status);
    List<OrderDetail> getAllOrders();
    OrderDetail save(OrderDetail order);
    long getTotalOrders();
    BigDecimal getTotalRevenue();
    long countOrdersByStatus(String status);
    List<OrderDetail> getRecentOrders(PageRequest pageRequest);
}
