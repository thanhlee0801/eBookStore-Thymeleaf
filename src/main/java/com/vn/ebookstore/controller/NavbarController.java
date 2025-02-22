package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.model.Category;
import com.vn.ebookstore.service.CategoryService;
import com.vn.ebookstore.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NavbarController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String home(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("categories", categories);
        model.addAttribute("books", books);
        return "index"; // Tương ứng với src/main/resources/templates/index.html
    }

    @GetMapping("/about_us")
    public String aboutUs(Model model) {
        return "page/user/about_us"; // Tương ứng với src/main/resources/templates/page/user/about_us.html
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        return "page/user/cart"; // Tương ứng với src/main/resources/templates/page/user/cart.html
    }

    @GetMapping("/faq")
    public String faq(Model model) {
        return "page/user/faq"; // Tương ứng với src/main/resources/templates/page/user/faq.html
    }

    @GetMapping("/order_tracking")
    public String orderTracking(Model model) {
        return "page/user/order_tracking"; // Tương ứng với src/main/resources/templates/page/user/order_tracking.html
    }

    @GetMapping("/product")
    public String product(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "page/user/product"; // Tương ứng với src/main/resources/templates/page/user/product.html
    }

    @GetMapping("/product_detail")
    public String productDetail(Model model) {
        return "page/user/product_detail"; // Tương ứng với src/main/resources/templates/page/user/product_detail.html
    }

    @GetMapping("/wishlist")
    public String wishlist(Model model) {
        return "page/user/wishlist"; // Tương ứng với src/main/resources/templates/page/user/wishlist.html
    }

    // Thêm các phương thức điều hướng khác nếu cần
}