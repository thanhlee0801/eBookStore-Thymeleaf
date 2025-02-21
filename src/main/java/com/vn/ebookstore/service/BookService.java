package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Book;
import java.util.List;

public interface BookService {
    Book createBook(Book book);
    Book updateBook(int id, Book book);
    void deleteBook(int id);
    Book getBookById(int id);
    List<Book> getAllBooks();
}