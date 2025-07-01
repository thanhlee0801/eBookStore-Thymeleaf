package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.model.Review;
import com.vn.ebookstore.repository.BookRepository;
import com.vn.ebookstore.repository.ReviewRepository;
import com.vn.ebookstore.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    @Transactional
    public Review saveReview(Review review) {
        Review savedReview = reviewRepository.save(review);

        // Refresh book object to update review calculations
        Book book = review.getBook();
        book.getReviews().add(savedReview);
        bookRepository.save(book);

        return savedReview;
    }

    @Override
    public Optional<Review> findById(Integer id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> findByBookId(Integer bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    @Override
    public List<Review> findByUserId(Integer userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            Book book = review.getBook();
            book.getReviews().remove(review);
            reviewRepository.deleteById(reviewId);
            bookRepository.save(book);
        }
    }

    @Override
    public boolean hasUserReviewedBook(Integer userId, Integer bookId) {
        return reviewRepository.existsByUserIdAndBookId(userId, bookId);
    }

    // Triển khai phương thức mới
    @Override
    public Page<Review> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Page<Review> getReviewsByRatingAndSort(Integer rating, String sort, Pageable pageable) {
        if (rating != null) {
            return reviewRepository.findByRating(rating, pageable);
        }
        if ("newest".equals(sort)) {
            return reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
        } else if ("oldest".equals(sort)) {
            return reviewRepository.findAllByOrderByCreatedAtAsc(pageable);
        } else if ("highest".equals(sort)) {
            return reviewRepository.findAllByOrderByRatingDesc(pageable);
        } else if ("lowest".equals(sort)) {
            return reviewRepository.findAllByOrderByRatingAsc(pageable);
        }
        return getAllReviews(pageable);
    }
}