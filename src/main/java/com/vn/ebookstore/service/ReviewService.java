package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Review;
import java.util.List;

public interface ReviewService {
    Review createReview(Review review);
    Review updateReview(int id, Review review);
    void deleteReview(int id);
    Review getReviewById(int id);
    List<Review> getAllReviews();
}