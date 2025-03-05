package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.*;
import com.vn.ebookstore.service.BookService;
import com.vn.ebookstore.service.CategoryService;
import com.vn.ebookstore.service.UserService;
import com.vn.ebookstore.service.WishlistService;
import com.vn.ebookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private CartService cartService;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("categories", categories);
        model.addAttribute("books", books);
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            
            // Initialize cart
            Cart cart = cartService.getCurrentCartByUser(user);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(user);
                cart.setCartItems(new ArrayList<>());
                cart = cartService.save(cart);
            }
            model.addAttribute("cart", cart);
            
            // Initialize wishlist
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
            
            return "index";
        }
        return "redirect:/login";
    }

    @GetMapping("/about_us")
    public String aboutUs(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
        }
        return "page/user/about_us";
    }

    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
        }
        return "page/user/cart";
    }

    @GetMapping("/faq")
    public String faq(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
        }
        return "page/user/faq";
    }

    @GetMapping("/order_tracking")
    public String orderTracking(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
        }
        return "page/user/order_tracking";
    }

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("categories", categories);
        model.addAttribute("books", books);

        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
        }
        return "page/user/product";
    }

    @GetMapping("/product_detail")
    public String productDetail(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
        }
        return "page/user/product_detail";
    }

    @GetMapping("/wishlist")
    public String wishlist(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
        }
        return "page/user/wishlist";
    }

    @PostMapping("/wishlist/toggle/{bookId}")
    @ResponseBody
    public ResponseEntity<?> toggleWishlist(@PathVariable("bookId") int bookId, Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            wishlistService.toggleWishlist(user.getId(), bookId);
            boolean isInWishlist = wishlistService.isBookInWishlist(user.getId(), bookId);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            return ResponseEntity.ok().body(Map.of(
                "inWishlist", isInWishlist,
                "wishlistCount", wishlists != null ? wishlists.size() : 0
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cart/add/{bookId}")
    @ResponseBody
    public ResponseEntity<?> addToCart(@PathVariable("bookId") int bookId, @RequestParam("quantity") int quantity,
            Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            cartService.addToCart(user.getId(), bookId, quantity);
            Cart cart = cartService.getCurrentCart(user.getId());
            return ResponseEntity.ok().body(Map.of(
                    "total", cart.getTotal(),
                    "itemCount", cart.getCartItems().size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cart/remove/{bookId}")
    @ResponseBody
    public ResponseEntity<?> removeFromCart(@PathVariable("bookId") int bookId, Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            cartService.removeFromCart(user.getId(), bookId);
            Cart cart = cartService.getCurrentCart(user.getId());
            return ResponseEntity.ok().body(Map.of(
                    "total", cart.getTotal(),
                    "itemCount", cart.getCartItems().size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cart/update/{bookId}")
    @ResponseBody
    public ResponseEntity<?> updateCartItemQuantity(
            @PathVariable("bookId") int bookId,
            @RequestParam("quantity") int quantity,
            Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            cartService.updateCartItemQuantity(user.getId(), bookId, quantity);
            Cart cart = cartService.getCurrentCart(user.getId());
            return ResponseEntity.ok().body(Map.of(
                    "total", cart.getTotal(),
                    "itemCount", cart.getCartItems().size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/wishlist/clear")
    @ResponseBody
    public ResponseEntity<?> clearWishlist(Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            wishlistService.clearWishlist(user.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cart/clear")
    @ResponseBody
    public ResponseEntity<?> clearCart(Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            if (cart != null) {
                cartService.deleteCart(cart.getId());
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
            model.addAttribute("user", user);
        }
        return "page/user/profile";
    }

    @GetMapping("/settings")
    public String showSettings(Model model, Principal principal) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
        }
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