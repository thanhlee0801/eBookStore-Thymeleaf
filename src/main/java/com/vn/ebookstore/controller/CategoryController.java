package com.vn.ebookstore.controller;

import com.vn.ebookstore.model.*;
import com.vn.ebookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@Controller
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private SubCategoryService subCategoryService;

    // Guest endpoints only
    @GetMapping("/category/{id}")
    public String showPublicCategoryBooks(@PathVariable Integer id, Model model) {
        List<Category> categories = categoryService.getAllCategories();
        Category currentCategory = categoryService.getCategoryById(id);
        List<Book> books = bookService.getBooksByCategory(id);
        
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", currentCategory);
        model.addAttribute("books", books);
        model.addAttribute("isSubCategory", false);
        
        // Add empty lists for guest users
        model.addAttribute("wishlists", Collections.emptyList());
        model.addAttribute("cart", null);
        
        return "page/user/category_books";
    }

    @GetMapping("/subcategory/{id}")
    public String showPublicSubCategoryBooks(@PathVariable Integer id, Model model) {
        SubCategory subCategory = subCategoryService.getSubCategoryById(id);
        if (subCategory == null) {
            return "redirect:/";
        }

        List<Book> books = bookService.getBooksBySubCategoryId(id);
        List<Category> categories = categoryService.getAllCategories();
        
        model.addAttribute("currentCategory", subCategory);
        model.addAttribute("parentCategory", subCategory.getCategory());
        model.addAttribute("categories", categories);
        model.addAttribute("books", books);
        model.addAttribute("isSubCategory", true);
        
        // Add empty lists for guest users
        model.addAttribute("wishlists", Collections.emptyList());
        model.addAttribute("cart", null);

        return "page/user/category_books";
    }
}
