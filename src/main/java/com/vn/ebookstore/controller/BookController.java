package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/books")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchBooks(
            @RequestParam String query,
            Authentication authentication) {
        List<Book> results = bookService.searchBooks(query);
        
        boolean isAuthenticated = (authentication != null && authentication.isAuthenticated());
        String baseUrl = isAuthenticated ? "/user/book/" : "/book/";
        
        List<Map<String, Object>> simplifiedResults = results.stream()
            .map(book -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", book.getId());
                map.put("title", book.getTitle());
                map.put("cover", book.getCover());
                map.put("price", book.getPrice());
                map.put("url", baseUrl + book.getId()); // Thêm đường dẫn phù hợp
                return map;
            })
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(simplifiedResults);
    }
}
