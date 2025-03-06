package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.model.Review;
import com.vn.ebookstore.repository.BookRepository;
import com.vn.ebookstore.repository.ReviewRepository;
import com.vn.ebookstore.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
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
        updateBookRating(review.getBook().getId());
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
            Integer bookId = reviewOpt.get().getBook().getId();
            reviewRepository.deleteById(reviewId);
            updateBookRating(bookId);
        }
    }

    @Override
    public boolean hasUserReviewedBook(Integer userId, Integer bookId) {
        return reviewRepository.existsByUserIdAndBookId(userId, bookId);
    }

    private void updateBookRating(Integer bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        Book book = bookRepository.findById(bookId).orElse(null);
        
        if (book != null) {
            if (!reviews.isEmpty()) {
                double averageRating = reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);
                book.setAverageRating(averageRating);
                book.setReviewCount(reviews.size());
            } else {
                book.setAverageRating(0.0);
                book.setReviewCount(0);
            }
            bookRepository.save(book);
        }
    }
}