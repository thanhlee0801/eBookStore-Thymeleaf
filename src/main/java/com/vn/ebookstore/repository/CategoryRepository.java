package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.deletedAt IS NULL ORDER BY c.id")
    List<Category> findAllOrderById();
}