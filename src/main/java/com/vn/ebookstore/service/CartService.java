package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Cart;
import java.util.List;

public interface CartService {
    Cart createCart(Cart cart);
    Cart updateCart(int id, Cart cart);
    void deleteCart(int id);
    Cart getCartById(int id);
    List<Cart> getAllCarts();
}