package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.OrderDetail;
import com.vn.ebookstore.repository.OrderDetailRepository;
import com.vn.ebookstore.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail updateOrderDetail(int id, OrderDetail orderDetail) {
        OrderDetail existingOrder = orderDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderDetail not found"));
        existingOrder.setTotal(orderDetail.getTotal());
        existingOrder.setStatus(orderDetail.getStatus());
        return orderDetailRepository.save(existingOrder);
    }

    @Override
    public void deleteOrderDetail(int id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public OrderDetail getOrderDetailById(int id) {
        return orderDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderDetail not found"));
    }

    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }
}