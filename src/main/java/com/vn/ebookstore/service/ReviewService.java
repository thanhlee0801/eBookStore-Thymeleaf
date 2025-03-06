package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    Review saveReview(Review review);
    Optional<Review> findById(Integer id);
    List<Review> findByBookId(Integer bookId);
    List<Review> findByUserId(Integer userId);
    void deleteReview(Integer reviewId);
    boolean hasUserReviewedBook(Integer userId, Integer bookId);
}