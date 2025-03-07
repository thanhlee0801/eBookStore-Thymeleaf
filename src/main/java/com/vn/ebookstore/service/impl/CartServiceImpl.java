package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.model.Cart;
import com.vn.ebookstore.model.CartItem;
import com.vn.ebookstore.model.Coupon;
import com.vn.ebookstore.model.User;
import com.vn.ebookstore.repository.BookRepository;
import com.vn.ebookstore.repository.CartItemRepository;
import com.vn.ebookstore.repository.CartRepository;
import com.vn.ebookstore.repository.UserRepository;
import com.vn.ebookstore.service.CartService;
import com.vn.ebookstore.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CouponService couponService;

    @Override
    @Transactional
    public Cart createCart(Cart cart) {
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }
        cart.setCreatedAt(new Date());
        cart.setTotal(0L);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart updateCart(int id, Cart cart) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng với ID: " + id));
        existingCart.setTotal(cart.getTotal());
        return cartRepository.save(existingCart);
    }

    @Override
    @Transactional
    public void deleteCart(int id) {
        Cart cart = getCartById(id);
        cart.setUpdatedAt(new Date());
        cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(int id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng với ID: " + id));
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    @Transactional
    public Cart getCurrentCart(int userId) {
        return getCurrentCartByUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId)));
    }

    @Override
    @Transactional
    public void addToCart(int userId, int bookId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        
        Cart cart = getCurrentCart(userId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + bookId));

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndBookIdAndUpdatedAtIsNull(cart.getId(), bookId);
        
        if (existingItem.isPresent()) {
            // Nếu sách đã có trong giỏ hàng, cập nhật số lượng
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // Nếu sách chưa có trong giỏ hàng, thêm mới
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(quantity);
            newItem.setPrice(book.getPrice());
            newItem.setCreatedAt(new Date());
            cartItemRepository.save(newItem);
        }

        // Cập nhật tổng tiền của giỏ hàng
        updateCartTotal(cart);
    }

    @Override
    @Transactional
    public void removeFromCart(int userId, int bookId) {
        Cart cart = getCurrentCart(userId);
        Optional<CartItem> item = cartItemRepository.findByCartIdAndBookIdAndUpdatedAtIsNull(cart.getId(), bookId);
        
        if (item.isPresent()) {
            CartItem cartItem = item.get();
            cartItem.setUpdatedAt(new Date()); // Đánh dấu là đã xóa
            cartItemRepository.save(cartItem);
            updateCartTotal(cart);
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
        }
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(int userId, int bookId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        
        Cart cart = getCurrentCart(userId);
        Optional<CartItem> item = cartItemRepository.findByCartIdAndBookIdAndUpdatedAtIsNull(cart.getId(), bookId);
        
        if (item.isPresent()) {
            CartItem cartItem = item.get();
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            updateCartTotal(cart);
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng");
        }
    }

    @Override
    @Transactional
    public Cart getCurrentCartByUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User không thể là null");
        }
        
        Cart cart = cartRepository.findByUserAndUpdatedAtIsNull(user)  // This line uses the correct method name
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotal(0L);
                    newCart.setCartItems(new ArrayList<>());
                    newCart.setCreatedAt(new Date());
                    return cartRepository.save(newCart);
                });

        // Đảm bảo cartItems không null và chỉ lấy các items chưa bị xóa
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        } else {
            List<CartItem> activeItems = cart.getCartItems().stream()
                    .filter(item -> item.getUpdatedAt() == null)
                    .collect(Collectors.toList());
            cart.setCartItems(activeItems);
        }

        return cart;
    }
    
    @Override
    @Transactional
    public Cart save(Cart cart) {
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }
        
        if (cart.getCreatedAt() == null) {
            cart.setCreatedAt(new Date());
        }
        
        return cartRepository.save(cart);
    }
    
    @Transactional
    private void updateCartTotal(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartIdAndUpdatedAtIsNull(cart.getId());
        long total = 0;
        
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        
        cart.setTotal(total);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void applyCoupon(Integer cartId, String couponCode) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        double subtotal = calculateSubtotal(cart);
        
        Coupon coupon = couponService.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Invalid coupon code"));

        if (!couponService.isValidForUse(coupon, subtotal)) {
            throw new RuntimeException("Coupon cannot be applied");
        }

        Double discount = couponService.calculateDiscount(coupon, subtotal);
        cart.setCoupon(coupon);
        cart.setSubTotal(subtotal);
        cart.setDiscountAmount(discount);
        cart.setTotal((long)(subtotal - discount));
        
        cartRepository.save(cart);
    }

    @Override
    public void removeCoupon(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setCoupon(null);
        cartRepository.save(cart);
    }

    private double calculateSubtotal(Cart cart) {
        return cart.getCartItems().stream()
                .filter(item -> item.getUpdatedAt() == null)
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}