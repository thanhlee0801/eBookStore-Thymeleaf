package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByUserIdOrderByCreatedAtDesc(Integer userId);
    List<OrderDetail> findByStatus(String status);
}