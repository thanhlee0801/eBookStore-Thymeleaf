package com.vn.ebookstore.service;

import com.vn.ebookstore.model.OrderDetail;
import org.springframework.data.domain.PageRequest;
import java.util.List;

public interface OrderDetailService {
    OrderDetail createOrder(Integer userId, Integer addressId, String paymentMethod, String note);
    OrderDetail getOrderById(Integer id);
    List<OrderDetail> getOrdersByUserId(Integer userId);
    void cancelOrder(Integer orderId);
    OrderDetail updateOrderStatus(Integer orderId, String status);
    List<OrderDetail> getAllOrders();
    OrderDetail save(OrderDetail order);
    long getTotalOrders();
    double getTotalRevenue();
    long countOrdersByStatus(String status);
    List<OrderDetail> getRecentOrders(PageRequest pageRequest);
}