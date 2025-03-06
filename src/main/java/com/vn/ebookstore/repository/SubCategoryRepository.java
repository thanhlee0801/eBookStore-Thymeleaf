package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
    @Query("SELECT s FROM SubCategory s WHERE s.category.id = :categoryId AND s.deletedAt IS NULL ORDER BY s.id")
    List<SubCategory> findByCategoryIdOrderById(@Param("categoryId") Integer categoryId);
}