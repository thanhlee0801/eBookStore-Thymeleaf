package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.*;
import com.vn.ebookstore.service.BookService;
import com.vn.ebookstore.service.CategoryService;
import com.vn.ebookstore.service.UserService;
import com.vn.ebookstore.service.WishlistService;
import com.vn.ebookstore.service.CartService;
import com.vn.ebookstore.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.vn.ebookstore.security.UserDetailsImpl;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    // Dependencies injection
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
    @Autowired
    private ReviewService reviewService;

    // ==================== Page Navigation Mappings ====================
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

    // ==================== Product Related Mappings ====================
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

    @GetMapping("/book/{id}")
    public String viewBookDetail(@PathVariable Integer id, Model model, Principal principal) {
        // Lấy danh sách categories (đồng bộ với các controller khác)
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        // Lấy thông tin sách
        Optional<Book> bookOptional = bookService.findById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            model.addAttribute("book", book);

            // Kiểm tra và xử lý nếu người dùng đã đăng nhập
            if (principal != null) {
                User user = userService.getUserByEmail(principal.getName());
                // Lấy giỏ hàng
                Cart cart = cartService.getCurrentCartByUser(user);
                if (cart == null) {
                    cart = new Cart();
                    cart.setUser(user);
                    cart.setCartItems(new ArrayList<>());
                    cart = cartService.save(cart);
                }
                model.addAttribute("cart", cart);

                // Lấy danh sách wishlist
                List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
                model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
            }

            return "page/user/product_detail";
        } else {
            return "redirect:/user/products";
        }
    }

    // ==================== Cart Related Mappings ====================
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

    @PostMapping("/cart/add/{bookId}")
    @ResponseBody
    public ResponseEntity<?> addToCart(@PathVariable("bookId") int bookId, 
                                     @RequestParam("quantity") int quantity,
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
    public ResponseEntity<?> removeFromCart(@PathVariable("bookId") int bookId, 
                                          Principal principal) {
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

    // ==================== Wishlist Related Mappings ====================
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
    public ResponseEntity<?> toggleWishlist(@PathVariable("bookId") int bookId, 
                                          Principal principal) {
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

    // ==================== Review Related Mappings ====================
    @PostMapping("/review/add")
    public String addReview(
            @RequestParam("bookId") Integer bookId,
            @RequestParam("rating") Integer rating,
            @RequestParam("comment") String comment,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            
            if (reviewService.hasUserReviewedBook(user.getId(), bookId)) {
                redirectAttributes.addFlashAttribute("error", "Bạn đã đánh giá cuốn sách này rồi");
                return "redirect:/user/book/" + bookId;
            }

            Book book = bookService.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));

            Review review = new Review();
            review.setUser(user);
            review.setBook(book);
            review.setRating(rating);
            review.setComment(comment);
            review.setCreatedAt(new Date());
            
            reviewService.saveReview(review);
            redirectAttributes.addFlashAttribute("success", "Đã thêm đánh giá thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/book/" + bookId;
    }

    @PutMapping("/review/update/{reviewId}")
    @ResponseBody
    public ResponseEntity<?> updateReview(
            @PathVariable("reviewId") Integer reviewId,
            @RequestParam("rating") Integer rating,
            @RequestParam("comment") String comment,
            Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            Review review = reviewService.findById(reviewId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));
            
            // Kiểm tra quyền sở hữu review
            if (review.getUser().getId() != user.getId()) {
                return ResponseEntity.status(403).body(Map.of("error", "Không có quyền sửa đánh giá này"));
            }

            // Validate rating
            if (rating < 1 || rating > 5) {
                return ResponseEntity.badRequest().body(Map.of("error", "Đánh giá phải từ 1 đến 5 sao"));
            }
            
            review.setRating(rating);
            review.setComment(comment);
            review.setUpdatedAt(new Date());
            
            Review updatedReview = reviewService.saveReview(review);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "review", updatedReview,
                "message", "Đã cập nhật đánh giá thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/review/delete/{reviewId}")
    @ResponseBody
    public ResponseEntity<?> deleteReview(
            @PathVariable("reviewId") Integer reviewId,
            Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            Review review = reviewService.findById(reviewId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá"));
            
            // Kiểm tra quyền sở hữu review
            if (review.getUser().getId() != user.getId()) {
                return ResponseEntity.status(403).body(Map.of("error", "Không có quyền xóa đánh giá này"));
            }
            
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Đã xóa đánh giá thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/reviews")
    public String showUserReviews(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            
            // Add common attributes
            List<Category> categories = categoryService.getAllCategories();
            Cart cart = cartService.getCurrentCartByUser(user);
            List<Wishlist> wishlists = wishlistService.getWishlistsByUser(user);
            
            // Get user's reviews
            List<Review> reviews = reviewService.findByUserId(user.getId());
            
            model.addAttribute("categories", categories);
            model.addAttribute("cart", cart);
            model.addAttribute("wishlists", wishlists != null ? wishlists : new ArrayList<>());
            model.addAttribute("reviews", reviews);
            
            return "page/user/review";
        }
        return "redirect:/login";
    }

    // ==================== User Profile & Settings Mappings ====================
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

    @PostMapping("/profile/update-username")
    public String updateUsername(@RequestParam("newUsername") String newUsername,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            
            // Verify password
            if (!passwordEncoder.matches(confirmPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu không đúng");
                return "redirect:/user/profile";
            }
            
            // Check if username is taken
            if (userService.existsByUsername(newUsername)) {
                redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại");
                return "redirect:/user/profile";
            }
            
            user.setUsername(newUsername);
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật tên đăng nhập thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/profile";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("birthOfDate") String birthOfDate,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            @RequestParam("addressLine") String addressLine,
            @RequestParam("ward") String ward,
            @RequestParam("district") String district,
            @RequestParam("city") String city,
            @RequestParam("country") String country,
            @RequestParam("postalCode") String postalCode,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userService.getUserByEmail(principal.getName());
            
            // Validate email
            if (!email.equals(currentUser.getEmail()) && userService.checkEmailExists(email)) {
                redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng");
                return "redirect:/user/profile";
            }

            // Update user information
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setEmail(email);
            currentUser.setPhoneNumber(phoneNumber);

            // Parse ngày sinh
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateFormat.setLenient(false);
                Date date = dateFormat.parse(birthOfDate);
                if (date.after(new Date())) {
                    throw new IllegalArgumentException("Ngày sinh không thể là ngày trong tương lai");
                }
                currentUser.setBirthOfDate(date);
            } catch (ParseException e) {
                redirectAttributes.addFlashAttribute("error", "Định dạng ngày sinh không hợp lệ (dd/MM/yyyy)");
                return "redirect:/user/profile";
            }
            
            // Xử lý avatar
            if (avatar != null && !avatar.isEmpty()) {
                String uploadDir = "src/main/resources/static/image/avatar";
                String oldAvatarPath = null;
                
                if (currentUser.getAvatar() != null) {
                    oldAvatarPath = "src/main/resources/static" + currentUser.getAvatar();
                }

                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFilename = avatar.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String filename = UUID.randomUUID().toString() + extension;

                Path filePath = uploadPath.resolve(filename);
                Files.copy(avatar.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                currentUser.setAvatar("/image/avatar/" + filename);

                if (oldAvatarPath != null) {
                    try {
                        Files.deleteIfExists(Paths.get(oldAvatarPath));
                    } catch (IOException e) {
                        logger.warn("Không thể xóa avatar cũ: " + e.getMessage());
                    }
                }
            }

            // Update address
            if (currentUser.getAddresses() == null || currentUser.getAddresses().isEmpty()) {
                Address address = new Address();
                address.setUser(currentUser);
                address.setCreatedAt(new Date());
                currentUser.setAddresses(Arrays.asList(address));
            }
            Address address = currentUser.getAddresses().get(0);
            address.setAddressLine(addressLine);
            address.setWard(ward);
            address.setDistrict(district);
            address.setCity(city);
            address.setCountry(country);
            address.setPostalCode(postalCode);
            address.setCreatedAt(new Date());

            userService.save(currentUser);

            // Cập nhật SecurityContext với thông tin mới
            UserDetailsImpl userDetails = new UserDetailsImpl(currentUser, new ArrayList<>());
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                userDetails, 
                null, 
                userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            redirectAttributes.addFlashAttribute("success", "Thông tin cá nhân đã được cập nhật");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/user/profile"; // Return to profile page on error
        }
        return "redirect:/user/profile"; // Redirect to home page on success
    }
}