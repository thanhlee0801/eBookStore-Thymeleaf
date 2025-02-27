package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.OrderItem;
import com.vn.ebookstore.repository.OrderItemRepository;
import com.vn.ebookstore.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem updateOrderItem(int id, OrderItem orderItem) {
        OrderItem existingItem = orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderItem not found"));
        existingItem.setQuantity(orderItem.getQuantity());
        existingItem.setPrice(orderItem.getPrice());
        return orderItemRepository.save(existingItem);
    }

    @Override
    public void deleteOrderItem(int id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public OrderItem getOrderItemById(int id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderItem not found"));
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }
}