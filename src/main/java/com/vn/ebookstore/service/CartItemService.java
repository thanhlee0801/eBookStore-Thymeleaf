package com.vn.ebookstore.service;

import com.vn.ebookstore.model.CartItem;
import java.util.List;

public interface CartItemService {
    CartItem createCartItem(CartItem cartItem);
    CartItem updateCartItem(int id, CartItem cartItem);
    void deleteCartItem(int id);
    CartItem getCartItemById(int id);
    List<CartItem> getAllCartItems();
}