package com.vn.ebookstore.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book_detail_images")
public class BookDetailImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int bookDetailId;
    private String imageUrl;
    private String altText;
    @Column(updatable = false)
    private Date createdAt;
    private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "book_detail_id", insertable = false, updatable = false)
    private BookDetail bookDetail;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookDetailId() {
        return bookDetailId;
    }

    public void setBookDetailId(int bookDetailId) {
        this.bookDetailId = bookDetailId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public BookDetail getBookDetail() {
        return bookDetail;
    }

    public void setBookDetail(BookDetail bookDetail) {
        this.bookDetail = bookDetail;
    }
}