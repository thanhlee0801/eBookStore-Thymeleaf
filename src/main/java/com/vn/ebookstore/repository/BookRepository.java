package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBySubCategory_Parent_IdAndDeletedAtIsNull(int categoryId);
    
    @Query(value = "SELECT * FROM books WHERE deleted_at IS NULL ORDER BY created_at DESC LIMIT 8", nativeQuery = true)
    List<Book> findLatestBooks();
    
    @Query(value = "SELECT * FROM books WHERE deleted_at IS NULL ORDER BY price DESC LIMIT 8", nativeQuery = true)
    List<Book> findPremiumBooks();
}