package com.vn.ebookstore.service;

import com.vn.ebookstore.model.OrderItem;
import java.util.List;

public interface OrderItemService {
    OrderItem createOrderItem(OrderItem orderItem);
    OrderItem updateOrderItem(int id, OrderItem orderItem);
    void deleteOrderItem(int id);
    OrderItem getOrderItemById(int id);
    List<OrderItem> getAllOrderItems();
}