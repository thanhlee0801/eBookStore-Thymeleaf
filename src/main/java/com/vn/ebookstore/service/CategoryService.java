package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Category;
import com.vn.ebookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {

    public List<Category> getAllCategories();
}