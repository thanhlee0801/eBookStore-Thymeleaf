package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCartIdAndUpdatedAtIsNull(int cartId);
    Optional<CartItem> findByCartIdAndBookIdAndUpdatedAtIsNull(int cartId, int bookId);
}