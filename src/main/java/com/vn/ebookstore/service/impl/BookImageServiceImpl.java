package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.BookImage;
import com.vn.ebookstore.repository.BookImageRepository;
import com.vn.ebookstore.service.BookImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookImageServiceImpl implements BookImageService {

    @Autowired
    private BookImageRepository bookImageRepository;

    @Override
    public BookImage createBookImage(BookImage bookImage) {
        return bookImageRepository.save(bookImage);
    }

    @Override
    public BookImage updateBookImage(int id, BookImage bookImage) {
        BookImage existingImage = bookImageRepository.findById(id).orElseThrow(() -> new RuntimeException("BookImage not found"));
        existingImage.setImageUrl(bookImage.getImageUrl());
        existingImage.setAltText(bookImage.getAltText());
        return bookImageRepository.save(existingImage);
    }

    @Override
    public void deleteBookImage(int id) {
        bookImageRepository.deleteById(id);
    }

    @Override
    public BookImage getBookImageById(int id) {
        return bookImageRepository.findById(id).orElseThrow(() -> new RuntimeException("BookImage not found"));
    }

    @Override
    public List<BookImage> getAllBookImages() {
        return bookImageRepository.findAll();
    }
}