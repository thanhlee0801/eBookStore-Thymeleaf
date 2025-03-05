package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    List<Wishlist> findByUserIdAndDeletedAtIsNull(int userId);
    boolean existsByUserIdAndBookIdAndDeletedAtIsNull(int userId, int bookId);
    Optional<Wishlist> findByUserIdAndBookIdAndDeletedAtIsNull(int userId, int bookId);
}