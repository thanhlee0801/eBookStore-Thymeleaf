package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Wishlist;
import com.vn.ebookstore.model.User;
import java.util.List;

public interface WishlistService {
    Wishlist createWishlist(Wishlist wishlist);
    Wishlist updateWishlist(int id, Wishlist wishlist);
    void deleteWishlist(int id);
    Wishlist getWishlistById(int id);
    List<Wishlist> getAllWishlists();
    List<Wishlist> getWishlistByUserId(int userId);
    boolean isBookInWishlist(int userId, int bookId);
    void toggleWishlist(int userId, int bookId);
    List<Wishlist> getWishlistsByUser(User user);
    void clearWishlist(int userId);
}