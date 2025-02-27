package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Wishlist;
import com.vn.ebookstore.repository.WishlistRepository;
import com.vn.ebookstore.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Override
    public Wishlist createWishlist(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist updateWishlist(int id, Wishlist wishlist) {
        Wishlist existingWishlist = wishlistRepository.findById(id).orElseThrow(() -> new RuntimeException("Wishlist not found"));
        existingWishlist.setUser(wishlist.getUser());
        existingWishlist.setBook(wishlist.getBook());
        return wishlistRepository.save(existingWishlist);
    }

    @Override
    public void deleteWishlist(int id) {
        wishlistRepository.deleteById(id);
    }

    @Override
    public Wishlist getWishlistById(int id) {
        return wishlistRepository.findById(id).orElseThrow(() -> new RuntimeException("Wishlist not found"));
    }

    @Override
    public List<Wishlist> getAllWishlists() {
        return wishlistRepository.findAll();
    }
}