package com.vn.ebookstore.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.vn.ebookstore.model.Book;
import com.vn.ebookstore.model.BookDetail;
import com.vn.ebookstore.model.Category;
import com.vn.ebookstore.model.SubCategory;
import com.vn.ebookstore.service.BookDetailService;
import com.vn.ebookstore.service.BookService;
import com.vn.ebookstore.service.CategoryService;
import com.vn.ebookstore.service.SubCategoryService;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    private final BookService bookService;
    private final BookDetailService bookDetailService;
    private final SubCategoryService subCategoryService;
    private final CategoryService categoryService;

    public AdminBookController(BookService bookService, BookDetailService bookDetailService, SubCategoryService subCategoryService, CategoryService categoryService) {
        this.bookService = bookService;
        this.bookDetailService = bookDetailService;
        this.subCategoryService = subCategoryService;
        this.categoryService = categoryService;
    }


    @GetMapping
    public ModelAndView listBooks() {
        ModelAndView mav = new ModelAndView("page/admin/books");
        mav.addObject("books", bookService.getAllBooks());
        return mav;
    }

    @GetMapping("/add")
    public ModelAndView showAddForm() {
        ModelAndView mav = new ModelAndView("page/admin/book-form");
        Book book = new Book();
        book.setBookDetail(new BookDetail()); // tạo sẵn để binding form tránh null pointer
        
        mav.addObject("book", book);
        mav.addObject("categories", categoryService.getAllCategories());
        mav.addObject("subCategories", subCategoryService.getAllSubCategories()); // cần có service này
        
        return mav;
    }

@PostMapping("/save")
public String saveBook(@ModelAttribute("book") Book book,
                       @RequestParam("coverFile") MultipartFile coverFile,
                       @RequestParam("bookFile") MultipartFile bookFile) throws IOException {

    // Tạo mới BookDetail nếu chưa có
    if (book.getBookDetail() == null) {
        book.setBookDetail(new BookDetail());
    }

    // Gán quan hệ 2 chiều
    BookDetail detail = book.getBookDetail();
    detail.setBook(book);

    // Kiểm tra xem đã tồn tại Book theo ID chưa
    Book existingBook = null;
    if (book.getId() != null) {
        existingBook = bookService.getBookById(book.getId());
    }

    // Xử lý subCategory: load từ DB nếu có, nếu không thì set null
    if (book.getSubCategory() == null || book.getSubCategory().getId() == null) {
        book.setSubCategory(null);
    } else {
        SubCategory subCategory = subCategoryService.getSubCategoryById(book.getSubCategory().getId());
        book.setSubCategory(subCategory);
    }

    // Upload ảnh bìa
    if (!coverFile.isEmpty()) {
        String coverFileName = coverFile.getOriginalFilename();
        String coverUploadPath = new ClassPathResource("static/image/cover").getFile().getAbsolutePath();
        File coverSaveFile = new File(coverUploadPath, coverFileName);
        coverFile.transferTo(coverSaveFile);
        book.setCover(coverFileName);
    }

    // Upload file PDF
    if (!bookFile.isEmpty()) {
        String bookFileName = bookFile.getOriginalFilename();
        String bookUploadPath = new ClassPathResource("static/image").getFile().getAbsolutePath();
        File bookSaveFile = new File(bookUploadPath, bookFileName);
        bookFile.transferTo(bookSaveFile);
        detail.setFileUrl(bookFileName);
    }

    if (existingBook != null) {
        // Đã có sách trong DB, cập nhật BookDetail
        BookDetail existingDetail = bookDetailService.getBookDetailByBookId(book.getId());
        if (existingDetail != null) {
            detail.setId(existingDetail.getId());
            bookDetailService.updateBookDetail(existingDetail.getId(), detail);
        } else {
            bookDetailService.createBookDetail(detail);
        }
        // Cập nhật sách
        bookService.save(book);
    } else {
        // Sách mới, lưu luôn
        bookService.save(book);
    }

    return "redirect:/admin/books";
}

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id,Model model) {
        try {
            Book book = bookService.getBookById(id);
            model.addAttribute("book", book);
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("subCategories", subCategoryService.getAllSubCategories()); // PHẢI có dòng này
        } catch (Exception e) {
            return "error/404";
        }
        return "page/admin/book-form";
    }
   @PostMapping("/update")
public String updateBook(@ModelAttribute("book") Book book,
                         @RequestParam("coverFile") MultipartFile coverFile,
                         @RequestParam("pdfFile") MultipartFile pdfFile) throws IOException {

    Book existingBook = bookService.getBookById(book.getId());
    if (existingBook == null) {
        return "redirect:/admin/books?error=notfound";
    }

    // Cập nhật thông tin chính
    existingBook.setTitle(book.getTitle());
    existingBook.setAuthor(book.getAuthor());
    existingBook.setPrice(book.getPrice());

    // Xử lý ảnh bìa nếu có
    if (!coverFile.isEmpty()) {
        String coverFileName = coverFile.getOriginalFilename();
        String coverUploadPath = new ClassPathResource("static/image/cover").getFile().getAbsolutePath();
        File coverSaveFile = new File(coverUploadPath, coverFileName);
        coverFile.transferTo(coverSaveFile);
        existingBook.setCover(coverFileName);
    }
        // Xử lý subCategory
    if (book.getSubCategory() == null || book.getSubCategory().getId() == null) {
        existingBook.setSubCategory(null);
    } else {
        SubCategory subCategory = subCategoryService.getSubCategoryById(book.getSubCategory().getId());
        existingBook.setSubCategory(subCategory);
    }

    // Load SubCategory đầy đủ từ DB
    if (book.getSubCategory() != null && book.getSubCategory().getId() != null) {
        SubCategory subCategory = subCategoryService.getSubCategoryById(book.getSubCategory().getId());
        existingBook.setSubCategory(subCategory);
    } else {
        existingBook.setSubCategory(null);
    }

    // Cập nhật BookDetail
    BookDetail existingDetail = existingBook.getBookDetail();
    BookDetail formDetail = book.getBookDetail();

    if (existingDetail == null) {
        existingDetail = new BookDetail();
    }

    existingDetail.setDescription(formDetail.getDescription());
    existingDetail.setSummary(formDetail.getSummary());
    existingDetail.setIsbn(formDetail.getIsbn());
    existingDetail.setPublisher(formDetail.getPublisher());
    existingDetail.setPublicationDate(formDetail.getPublicationDate());
    existingDetail.setPages(formDetail.getPages());

    // Xử lý file PDF nếu có
    if (!pdfFile.isEmpty()) {
        String pdfFileName = pdfFile.getOriginalFilename();
        String pdfUploadPath = new ClassPathResource("static/image").getFile().getAbsolutePath();
        File pdfSaveFile = new File(pdfUploadPath, pdfFileName);
        pdfFile.transferTo(pdfSaveFile);
        existingDetail.setFileUrl(pdfFileName);
    }

    // Liên kết 2 chiều
    existingDetail.setBook(existingBook);
    existingBook.setBookDetail(existingDetail);

    // Lưu book, Hibernate tự cascade lưu BookDetail
    bookService.save(existingBook);

    return "redirect:/admin/books?success=updated";
}

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }
   
}