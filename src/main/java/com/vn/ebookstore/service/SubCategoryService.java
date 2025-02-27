package com.vn.ebookstore.service;

import com.vn.ebookstore.model.SubCategory;
import java.util.List;

public interface SubCategoryService {
    SubCategory createSubCategory(SubCategory subCategory);
    SubCategory updateSubCategory(int id, SubCategory subCategory);
    void deleteSubCategory(int id);
    SubCategory getSubCategoryById(int id);
    List<SubCategory> getAllSubCategories();
}