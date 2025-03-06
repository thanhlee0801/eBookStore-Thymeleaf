package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.*;
import com.vn.ebookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/user/category/{id}")
    public String showCategoryBooks(@PathVariable Integer id, Model model, Principal principal) {
        // Lấy tất cả categories cho navigation
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        // Lấy category hiện tại
        Category currentCategory = categoryService.getCategoryById(id);
        model.addAttribute("currentCategory", currentCategory);
        
        // Lấy danh sách sách
        List<Book> books;
        if (currentCategory.getSubCategories() != null && !currentCategory.getSubCategories().isEmpty()) {
            // Nếu là category cha, lấy tất cả sách từ các subcategory
            books = bookService.getBooksByCategoryId(id);
        } else {
            // Nếu là subcategory, chỉ lấy sách của subcategory đó
            books = bookService.getBooksBySubCategoryId(id);
        }
        model.addAttribute("books", books);

        // Xử lý thông tin user nếu đã đăng nhập
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
        }

        return "page/user/category_books";
    }

    // Endpoint cho người dùng chưa đăng nhập
    @GetMapping("/category/{id}")
    public String showPublicCategoryBooks(@PathVariable Integer id, Model model) {
        List<Category> categories = categoryService.getAllCategories();
        Category currentCategory = categoryService.getCategoryById(id);
        List<Book> books;
        
        if (currentCategory.getSubCategories() != null && !currentCategory.getSubCategories().isEmpty()) {
            books = bookService.getBooksByCategoryId(id);
        } else {
            books = bookService.getBooksBySubCategoryId(id);
        }
        
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", currentCategory);
        model.addAttribute("books", books);
        
        return "page/user/category_books";
    }
}
