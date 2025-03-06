package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.SubCategory;
import com.vn.ebookstore.repository.SubCategoryRepository;
import com.vn.ebookstore.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Override
    public SubCategory createSubCategory(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public SubCategory updateSubCategory(int id, SubCategory subCategory) {
        SubCategory existingSubCategory = subCategoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("SubCategory not found"));
        existingSubCategory.setName(subCategory.getName());
        existingSubCategory.setDescription(subCategory.getDescription());
        existingSubCategory.setCategory(subCategory.getCategory()); // Changed from setParent to setCategory
        return subCategoryRepository.save(existingSubCategory);
    }

    @Override
    public void deleteSubCategory(int id) {
        subCategoryRepository.deleteById(id);
    }

    @Override
    public SubCategory getSubCategoryById(Integer id) {
        return subCategoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("SubCategory not found"));
    }

    @Override
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }
}