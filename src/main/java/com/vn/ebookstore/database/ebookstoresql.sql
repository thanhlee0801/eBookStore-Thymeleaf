CREATE DATABASE ebookstore;
USE ebookstore;

-- Bảng users: Lưu thông tin người dùng
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    avatar VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    birth_of_date DATE,
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng addresses: Lưu địa chỉ của người dùng
CREATE TABLE addresses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    -- Địa chỉ
    address_line VARCHAR(255),
    -- Phường/Xã
    ward VARCHAR(255),
    -- Quận/Huyện
    district VARCHAR(255),
    -- Thành phố
    city VARCHAR(100),
    -- Đất nước VD Vietnam, ...
    country VARCHAR(100),
    postal_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Bảng categories: Lưu danh mục sách
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Bảng sub_categories: Lưu danh mục con
CREATE TABLE sub_categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    parent_id INT,
    name VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- Bảng books: Lưu thông tin cơ bản của sách
CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    author VARCHAR(255),
    cover VARCHAR(255),
    sub_category_id INT,
    price BIGINT,  -- VNĐ, dùng BIGINT để lưu giá trị lớn
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (sub_category_id) REFERENCES sub_categories(id)
);

-- Bảng book_detail: Lưu thông tin chi tiết của sách
CREATE TABLE book_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT UNIQUE,  -- Liên kết 1:1 với books
    description TEXT,
    summary TEXT,
    isbn VARCHAR(20) UNIQUE,
    publisher VARCHAR(255),
    publication_date DATE,
    pages INT,
    file_url VARCHAR(255),  -- Link tải sách điện tử
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Bảng book_images: Lưu nhiều ảnh bổ sung cho sách
CREATE TABLE book_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    image_url VARCHAR(255),
    alt_text VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Bảng reviews: Lưu đánh giá của người dùng về sách
CREATE TABLE reviews (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    book_id INT,
    rating INT CHECK (rating >= 1 AND rating <= 5),  -- Điểm từ 1 đến 5
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Bảng wishlist: Lưu danh sách yêu thích của người dùng
CREATE TABLE wishlist (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    book_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Bảng cart: Lưu giỏ hàng của người dùng
CREATE TABLE cart (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    total BIGINT DEFAULT 0,  -- Tổng giá trị giỏ hàng (VNĐ)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Bảng cart_item: Lưu chi tiết các mục trong giỏ hàng
CREATE TABLE cart_item (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cart_id INT,
    book_id INT,
    quantity INT,
    price BIGINT,  -- Giá tại thời điểm thêm vào giỏ (VNĐ)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES cart(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Bảng order_details: Lưu thông tin đơn hàng
CREATE TABLE order_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    total BIGINT,  -- Tổng giá trị đơn hàng (VNĐ)
    status VARCHAR(50) DEFAULT 'pending',  -- Trạng thái đơn hàng
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Bảng order_item: Lưu chi tiết các mục trong đơn hàng
CREATE TABLE order_item (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    book_id INT,
    quantity INT,
    price BIGINT,  -- Giá tại thời điểm đặt hàng (VNĐ)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES order_details(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Bảng payment_details: Lưu thông tin thanh toán
CREATE TABLE payment_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    amount BIGINT,  -- Số tiền thanh toán (VNĐ)
    provider VARCHAR(255),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES order_details(id)
);

-- Bảng roles: Lưu các vai trò người dùng
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng user_roles: Liên kết người dùng với vai trò
CREATE TABLE user_roles (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);