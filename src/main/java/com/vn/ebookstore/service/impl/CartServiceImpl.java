package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Cart;
import com.vn.ebookstore.repository.CartRepository;
import com.vn.ebookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCart(int id, Cart cart) {
        Cart existingCart = cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
        existingCart.setTotal(cart.getTotal());
        return cartRepository.save(existingCart);
    }

    @Override
    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }

    @Override
    public Cart getCartById(int id) {
        return cartRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }
}