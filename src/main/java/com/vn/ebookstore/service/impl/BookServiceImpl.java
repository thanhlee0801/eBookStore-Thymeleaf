package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.repository.BookRepository;
import com.vn.ebookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(int id, Book book) {
        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setCover(book.getCover());
        existingBook.setSubCategory(book.getSubCategory());
        existingBook.setPrice(book.getPrice());
        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBookById(int id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksByCategory(int categoryId) {
        return bookRepository.findBySubCategory_Parent_IdAndDeletedAtIsNull(categoryId);
    }

    @Override
    public List<Book> getLatestBooks() {
        return bookRepository.findLatestBooks();
    }

    @Override
    public List<Book> getPremiumBooks() {
        return bookRepository.findPremiumBooks();
    }

    @Override
    public void softDeleteBook(int id) {
        Book book = getBookById(id);
        book.setDeletedAt(new Date());
        bookRepository.save(book);
    }
}