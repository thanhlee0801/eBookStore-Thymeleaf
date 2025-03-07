package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.*;
import com.vn.ebookstore.repository.*;
import com.vn.ebookstore.service.OrderDetailService;
import com.vn.ebookstore.service.PaymentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PaymentDetailService paymentDetailService;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional
    public OrderDetail createOrder(Integer userId, Integer addressId, String paymentMethod, String note) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUserIdAndUpdatedAtIsNull(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Lấy thông tin địa chỉ giao hàng
        Address shippingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
                
        // Format địa chỉ thành chuỗi
        String formattedAddress = String.format("%s, %s, %s, %s",
            shippingAddress.getAddressLine(),
            shippingAddress.getWard(),
            shippingAddress.getDistrict(),
            shippingAddress.getCity());

        // Tạo đơn hàng mới với địa chỉ
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setUser(user);
        orderDetail.setTotal(cart.getTotal());
        orderDetail.setStatus("PENDING");
        orderDetail.setOrderAddress(formattedAddress); // Lưu địa chỉ giao hàng
        orderDetail.setCreatedAt(new Date());
        orderDetail = orderDetailRepository.save(orderDetail);

        // Chuyển các item từ giỏ hàng sang đơn hàng
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getUpdatedAt() == null) { // Chỉ lấy các item chưa bị xóa
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(orderDetail);
                orderItem.setBook(cartItem.getBook());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getPrice());
                orderItem.setCreatedAt(new Date());
                orderItemRepository.save(orderItem);
            }
        }

        // Tạo payment detail sau khi tạo order
        paymentDetailService.createPayment(orderDetail.getId(), paymentMethod, orderDetail.getTotal());

        return orderDetail;
    }

    @Override
    public OrderDetail getOrderById(Integer id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<OrderDetail> getOrdersByUserId(Integer userId) {
        return orderDetailRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void cancelOrder(Integer orderId) {
        OrderDetail order = getOrderById(orderId);
        // Cho phép hủy đơn khi status là PENDING hoặc CONFIRMED
        if (!("PENDING".equals(order.getStatus()) || "CONFIRMED".equals(order.getStatus()))) {
            throw new RuntimeException("Chỉ có thể hủy đơn hàng ở trạng thái chờ xử lý hoặc đã xác nhận");
        }
        order.setStatus("CANCELLED");
        orderDetailRepository.save(order);
    }

    @Override
    @Transactional
    public OrderDetail updateOrderStatus(Integer orderId, String status) {
        OrderDetail order = getOrderById(orderId);
        order.setStatus(status);
        return orderDetailRepository.save(order);
    }

    @Override
    public List<OrderDetail> getAllOrders() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetail save(OrderDetail order) {
        return orderDetailRepository.save(order);
    }
}