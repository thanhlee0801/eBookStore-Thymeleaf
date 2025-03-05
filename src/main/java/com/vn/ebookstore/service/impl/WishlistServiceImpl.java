package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.model.User;
import com.vn.ebookstore.model.Wishlist;
import com.vn.ebookstore.repository.BookRepository;
import com.vn.ebookstore.repository.UserRepository;
import com.vn.ebookstore.repository.WishlistRepository;
import com.vn.ebookstore.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    @Transactional
    public Wishlist createWishlist(Wishlist wishlist) {
        if (wishlist.getCreatedAt() == null) {
            wishlist.setCreatedAt(new Date());
        }
        return wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public Wishlist updateWishlist(int id, Wishlist wishlist) {
        Wishlist existingWishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh sách yêu thích với ID: " + id));
        existingWishlist.setUser(wishlist.getUser());
        existingWishlist.setBook(wishlist.getBook());
        return wishlistRepository.save(existingWishlist);
    }

    @Override
    @Transactional
    public void deleteWishlist(int id) {
        Wishlist wishlist = getWishlistById(id);
        wishlist.setDeletedAt(new Date());
        wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist getWishlistById(int id) {
        return wishlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh sách yêu thích với ID: " + id));
    }

    @Override
    public List<Wishlist> getAllWishlists() {
        return wishlistRepository.findAll();
    }

    @Override
    public List<Wishlist> getWishlistByUserId(int userId) {
        // Kiểm tra xem người dùng có tồn tại không
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Không tìm thấy người dùng với ID: " + userId);
        }
        return wishlistRepository.findByUserIdAndDeletedAtIsNull(userId);
    }

    @Override
    public boolean isBookInWishlist(int userId, int bookId) {
        return wishlistRepository.existsByUserIdAndBookIdAndDeletedAtIsNull(userId, bookId);
    }

    @Override
    @Transactional
    public void toggleWishlist(int userId, int bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + bookId));

        if (isBookInWishlist(userId, bookId)) {
            // Nếu sách đã có trong wishlist, xóa nó đi
            Optional<Wishlist> wishlist = wishlistRepository.findByUserIdAndBookIdAndDeletedAtIsNull(userId, bookId);
            if (wishlist.isPresent()) {
                wishlist.get().setDeletedAt(new Date());
                wishlistRepository.save(wishlist.get());
            }
        } else {
            // Nếu sách chưa có trong wishlist, thêm vào
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(user);
            newWishlist.setBook(book);
            newWishlist.setCreatedAt(new Date());
            wishlistRepository.save(newWishlist);
        }
    }

    @Override
    public List<Wishlist> getWishlistsByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User không thể là null");
        }
        return wishlistRepository.findByUserIdAndDeletedAtIsNull(user.getId());
    }

    @Override
    @Transactional
    public void clearWishlist(int userId) {
        List<Wishlist> userWishlists = wishlistRepository.findByUserIdAndDeletedAtIsNull(userId);
        Date now = new Date();
        for (Wishlist wishlist : userWishlists) {
            wishlist.setDeletedAt(now);
        }
        wishlistRepository.saveAll(userWishlists);
    }
}