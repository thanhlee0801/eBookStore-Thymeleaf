package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.model.Category;
import com.vn.ebookstore.service.CategoryService;
import com.vn.ebookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;

    @GetMapping("/home")
    public String home(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("categories", categories);
        model.addAttribute("books", books);
        return "index";
    }

    @GetMapping("/about_us")
    public String aboutUs(Model model) {
        return "page/user/about_us";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        return "page/user/cart";
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        return "page/user/faq";
    }

    @GetMapping("/order_tracking")
    public String orderTracking(Model model) {
        return "page/user/order_tracking";
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("categories", categories);
        model.addAttribute("books", books);
        return "page/user/product";
    }

    @GetMapping("/product_detail")
    public String productDetail(Model model) {
        return "page/user/product_detail";
    }

    @GetMapping("/wishlist")
    public String wishlist(Model model) {
        return "page/user/wishlist";
    }

    @GetMapping("/my-profile")
    public String profile(Model model, Authentication authentication) {
        return "page/user/profile";
    }

    @GetMapping("/settings")
    public String settings() {
        return "page/user/settings";
    }
}