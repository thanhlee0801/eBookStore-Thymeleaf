package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.CartItem;
import com.vn.ebookstore.repository.CartItemRepository;
import com.vn.ebookstore.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(int id, CartItem cartItem) {
        CartItem existingItem = cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("CartItem not found"));
        existingItem.setQuantity(cartItem.getQuantity());
        existingItem.setPrice(cartItem.getPrice());
        return cartItemRepository.save(existingItem);
    }

    @Override
    public void deleteCartItem(int id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public CartItem getCartItemById(int id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("CartItem not found"));
    }

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }
}