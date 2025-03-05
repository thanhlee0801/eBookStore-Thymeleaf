package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.model.Category;
import com.vn.ebookstore.service.BookService;
import com.vn.ebookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GuestController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String home(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Book> premiumBooks = bookService.getPremiumBooks();
        List<Book> latestBooks = bookService.getLatestBooks();
        
        model.addAttribute("categories", categories);
        model.addAttribute("premiumBooks", premiumBooks);
        model.addAttribute("latestBooks", latestBooks);
        return "index";
    }

    @GetMapping("/about_us")
    public String aboutUs() {
        return "page/user/about_us";
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("categories", categories);
        model.addAttribute("books", books);
        return "page/user/product"; // src/main/resources/templates/page/user/product.html
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") int id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "page/user/product_detail";
    }

    @GetMapping("/faq")
    public String faq() {
        return "page/user/faq";
    }
}