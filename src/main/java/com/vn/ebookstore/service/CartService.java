package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Cart;
import com.vn.ebookstore.model.User;
import java.util.List;

public interface CartService {
    Cart createCart(Cart cart);
    Cart updateCart(int id, Cart cart);
    void deleteCart(int id);
    Cart getCartById(int id);
    List<Cart> getAllCarts();
    Cart getCurrentCart(int userId);
    void addToCart(int userId, int bookId, int quantity);
    void removeFromCart(int userId, int bookId);
    void updateCartItemQuantity(int userId, int bookId, int quantity);
    Cart getCurrentCartByUser(User user);
    Cart save(Cart cart);
}