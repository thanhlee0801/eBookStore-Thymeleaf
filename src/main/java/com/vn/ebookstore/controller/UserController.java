package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.model.Category;
import com.vn.ebookstore.model.User;
import com.vn.ebookstore.service.CategoryService;
import com.vn.ebookstore.service.BookService;
import com.vn.ebookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

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

    @GetMapping("/profile")
    public String showProfile(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        model.addAttribute("user", user);
        return "page/user/profile";
    }

    @GetMapping("/settings")
    public String showSettings() {
        return "page/user/settings";
    }

    @PostMapping("/settings/update")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng");
            return "redirect:/user/settings";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp");
            return "redirect:/user/settings";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");
        return "redirect:/user/settings";
    }
}