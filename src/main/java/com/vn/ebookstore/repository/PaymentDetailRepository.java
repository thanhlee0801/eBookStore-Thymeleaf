package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {
    List<PaymentDetail> findByOrderId(Integer orderId);
    Optional<PaymentDetail> findByOrderIdAndStatus(Integer orderId, String status);
}