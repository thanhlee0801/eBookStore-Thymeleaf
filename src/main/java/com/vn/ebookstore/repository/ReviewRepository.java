package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, JpaSpecificationExecutor<Review> {
    List<Review> findByBookId(Integer bookId);
    List<Review> findByUserId(Integer userId);
    boolean existsByUserIdAndBookId(Integer userId, Integer bookId);

    // Thêm phân trang và lọc theo rating
    Page<Review> findByRating(Integer rating, Pageable pageable);

    // Thêm sắp xếp theo created_at
    Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Review> findAllByOrderByCreatedAtAsc(Pageable pageable);
    Page<Review> findAllByOrderByRatingDesc(Pageable pageable);
    Page<Review> findAllByOrderByRatingAsc(Pageable pageable);
}