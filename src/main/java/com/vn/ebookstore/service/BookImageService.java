package com.vn.ebookstore.service;

import com.vn.ebookstore.model.BookImage;
import java.util.List;

public interface BookImageService {
    BookImage createBookImage(BookImage bookImage);
    BookImage updateBookImage(int id, BookImage bookImage);
    void deleteBookImage(int id);
    BookImage getBookImageById(int id);
    List<BookImage> getAllBookImages();
}