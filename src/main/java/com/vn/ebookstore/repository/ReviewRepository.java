package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByBookId(Integer bookId);
    List<Review> findByUserId(Integer userId);
    boolean existsByUserIdAndBookId(Integer userId, Integer bookId);
}
