package com.vn.ebookstore.service;

import com.vn.ebookstore.model.SubCategory;
import java.util.List;

public interface SubCategoryService {
    SubCategory getSubCategoryById(Integer id);
    List<SubCategory> getAllSubCategories();
    SubCategory createSubCategory(SubCategory subCategory);
    SubCategory updateSubCategory(int id, SubCategory subCategory);
    void deleteSubCategory(int id);
}