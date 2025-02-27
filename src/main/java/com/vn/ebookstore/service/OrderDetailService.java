package com.vn.ebookstore.service;

import com.vn.ebookstore.model.OrderDetail;
import java.util.List;

public interface OrderDetailService {
    OrderDetail createOrderDetail(OrderDetail orderDetail);
    OrderDetail updateOrderDetail(int id, OrderDetail orderDetail);
    void deleteOrderDetail(int id);
    OrderDetail getOrderDetailById(int id);
    List<OrderDetail> getAllOrderDetails();
}