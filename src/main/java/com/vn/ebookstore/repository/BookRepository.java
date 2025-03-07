package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBySubCategory_Category_IdAndDeletedAtIsNull(Integer categoryId);
    List<Book> findBySubCategoryIdAndDeletedAtIsNull(Integer subCategoryId);
    
    @Query("SELECT b FROM Book b WHERE b.deletedAt IS NULL ORDER BY b.createdAt DESC")
    List<Book> findLatestBooks();
    
    @Query("SELECT b FROM Book b WHERE b.deletedAt IS NULL ORDER BY b.price DESC")
    List<Book> findPremiumBooks();
    
    List<Book> findByTitleContainingIgnoreCase(String query); // Method tìm kiếm theo title
}