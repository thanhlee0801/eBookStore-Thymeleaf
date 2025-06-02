# Quy Chuẩn Dự Án eBookStore (Java Spring Boot + Thymeleaf)

## 1. Cấu trúc thư mục

- `src/main/java/com/vn/ebookstore/`: Chứa toàn bộ mã nguồn Java.
  - `controller/`: Controller cho từng nhóm chức năng (User, Admin, Auth...).
  - `service/`: Business logic, tách biệt với controller.
  - `model/`: Entity/model class, mapping với DB.
  - `repository/`: Interface cho JPA/Hibernate.
  - `security/`: Cấu hình bảo mật, custom UserDetails, v.v.
- `src/main/resources/`
  - `templates/`: Giao diện Thymeleaf, chia theo module (page/user, page/admin, page/auth...).
  - `static/`: Tài nguyên tĩnh (css, js, image).
    - `css/`: File style riêng cho từng module/page.
    - `js/`: Script riêng cho từng chức năng.
    - `image/`: Ảnh tĩnh, chia nhỏ theo loại (avatar, covers, ...).
- `database/`: Quản lý migration, seed, script SQL.
  - `migration/`: Các file migration (Flyway/Liquibase hoặc script .sql).
  - `seed/`: Script dữ liệu mẫu (insert, update...).
  - `backup/`: Backup dữ liệu định kỳ (nếu có).
  - Đặt tên file migration theo chuẩn: `V{version}__{description}.sql` (VD: `V1__init_schema.sql`).

## 2. Quy tắc đặt tên

### Java
- Tên class: PascalCase, rõ nghĩa (UserController, BookService, OrderDetailRepository...).
- Tên biến: camelCase, ngắn gọn, rõ ràng.
- Tên method: camelCase, động từ đầu (getUserByEmail, saveReview...).
- Tên package: chữ thường, phân tách theo chức năng.
- Không viết tắt khó hiểu, không dùng tiếng Việt.

### Thymeleaf/HTML
- Tên file: tiếng Anh, snake_case hoặc kebab-case (product_detail.html, user_sidebar.html).
- Tên biến model: tiếng Anh, rõ nghĩa (categories, wishlists, cart...).
- Sử dụng block `th:replace`, `th:if`, `th:each` đúng mục đích, tránh lồng ghép phức tạp.
- Fragment đặt tên rõ ràng, có thể tái sử dụng.

### CSS/JS
- Tên file: snake_case, rõ chức năng (cart.js, wishlist_style.css).
- Class CSS: dùng BEM hoặc tên rõ nghĩa, tránh trùng lặp với Bootstrap.
- JS: Biến và hàm camelCase, tránh pollute global scope, ưu tiên module pattern.

### Database
- Tên bảng: snake_case, số nhiều (users, order_details...).
- Tên cột: snake_case, tiếng Anh, rõ nghĩa.
- Tên file migration: `V{version}__{description}.sql`.

## 3. Chuẩn code Java

- Mỗi controller chỉ xử lý đúng nghiệp vụ của mình, không trộn lẫn logic.
- Service không gọi trực tiếp view/template.
- Sử dụng annotation Spring đúng chuẩn: `@Controller`, `@Service`, `@Repository`, `@Autowired`, `@RequestMapping`, ...
- Validate dữ liệu đầu vào ở cả phía client (JS) và server (Java).
- Xử lý exception rõ ràng, trả về thông báo lỗi thân thiện.
- Không hard-code đường dẫn, dùng biến hoặc cấu hình.
- Không để lộ thông tin nhạy cảm/log lỗi ra ngoài.
- Đặt comment cho method phức tạp, sử dụng Javadoc cho public API/service.
- Không để code chết, code thừa.
- Đặt TODO rõ ràng cho phần chưa hoàn thiện.
- Đảm bảo pass build/test trước khi merge.

## 4. Chuẩn code Thymeleaf/HTML

- Sử dụng layout fragment (`th:replace`, `th:fragment`) cho header, footer, sidebar.
- Không lặp lại code HTML, tách thành fragment nếu dùng lại nhiều nơi.
- Sử dụng `th:each`, `th:if`, `th:text`, `th:href` đúng mục đích.
- Không để logic phức tạp trong template, chỉ hiển thị dữ liệu.
- Đặt biến model rõ ràng, tránh trùng lặp.
- Đảm bảo responsive, ưu tiên dùng Bootstrap class.
- Không viết inline style, tách ra file CSS.
- Đặt meta charset, viewport, title đầy đủ.

## 5. Chuẩn code CSS

- Không override Bootstrap trừ khi cần thiết.
- Đặt biến màu sắc, font, ... ở đầu file (dùng :root).
- Tách style riêng cho từng page/module.
- Không dùng !important tràn lan.
- Ưu tiên dùng class thay vì id.
- Đặt tên class theo BEM hoặc rõ nghĩa.
- Không để style thừa, style chết.
- Đảm bảo tương thích đa trình duyệt.
- Sử dụng comment cho nhóm style lớn.

### Bảng màu chuẩn toàn bộ project (khai báo ở :root trong index_style.css):

```css
:root {
    --primary-color: #EA6C80;
    --primary-dark: #D85D71;
    --primary-light: #FCB4B4;
    --accent-color: #FF8FA3;
    --text-primary: #333333;
    --text-light: #666666;
    --white: #FFFFFF;
    --success: #22c55e;
    --warning: #f59e42;
    --danger: #ef4444;
    --info: #3b82f6;
    --gray-bg: #f8f9fa;
}
```

- Sử dụng các biến màu này cho toàn bộ giao diện, không hard-code mã màu trong file CSS khác.

## 6. Chuẩn code JavaScript

- Tách script riêng cho từng chức năng.
- Không viết inline JS trong HTML trừ trường hợp đơn giản.
- Sử dụng event delegation khi cần.
- Validate dữ liệu phía client trước khi submit form.
- Đặt biến, hàm rõ ràng, tránh trùng lặp.
- Không để lộ thông tin nhạy cảm trong JS.
- Sử dụng strict mode (`'use strict';`) nếu có thể.
- Đặt comment cho hàm phức tạp.
- Không để code chết, code thừa.
- Ưu tiên dùng fetch API, async/await thay vì callback hell.

## 7. Chuẩn code Database

- Migration phải có version, mô tả rõ ràng.
- Không sửa trực tiếp migration đã chạy ở môi trường production.
- Seed data phải tách riêng, không trộn với migration schema.
- Đặt index cho các cột truy vấn nhiều.
- Đảm bảo khóa ngoại, ràng buộc dữ liệu.
- Không dùng kiểu dữ liệu không chuẩn (VD: varchar(255) cho mọi trường).
- Backup định kỳ, lưu vào `database/backup/`.

## 8. Quy tắc bảo mật

- Không truyền thông tin nhạy cảm qua URL.
- Validate dữ liệu đầu vào ở cả client và server.
- Sử dụng CSRF token cho form POST.
- Không để lộ stacktrace, thông báo lỗi chi tiết ra ngoài.
- Mã hóa mật khẩu, không lưu plain text.
- Kiểm tra quyền truy cập ở controller/service.
- Không để lộ thông tin cấu hình (DB, secret key) lên repo.

## 9. Quy tắc kiểm thử

- Viết unit test cho service, repository.
- Test controller với MockMvc.
- Test giao diện với Selenium hoặc Cypress nếu có thể.
- Kiểm thử các trường hợp lỗi, edge case.
- Đặt tên test rõ ràng, phân nhóm theo chức năng.
- Không để test phụ thuộc vào dữ liệu thật.

## 10. Quản lý tài nguyên

- Ảnh upload lưu vào static/image, đặt tên random tránh trùng.
- Xóa ảnh cũ khi upload ảnh mới (avatar, cover...).
- Không upload file quá lớn, kiểm tra định dạng file.
- Đặt watermark cho ảnh nếu cần bảo vệ bản quyền.
- Tối ưu dung lượng ảnh trước khi upload.

## 11. Quy tắc tài liệu & comment

- Mỗi module/service/controller phải có mô tả ngắn đầu file.
- Sử dụng Javadoc cho public API/service.
- Comment cho đoạn code phức tạp, giải thích lý do nếu workaround.
- Không để comment thừa, comment code cũ.
- Tài liệu hướng dẫn cài đặt, build, deploy để ở README.md.

## 12. Quy tắc versioning & release

- Đặt version cho từng release, ghi rõ thay đổi ở CHANGELOG.md.
- Không commit trực tiếp vào main/master, dùng branch feature/bugfix.
- Review code trước khi merge.
- Đảm bảo mọi commit đều pass build/test.

---

## 13. Chuẩn hóa Database & Giải thích các bảng

### 13.1. Quy tắc thiết kế bảng

- Định dạng timestamp: Sử dụng `TIMESTAMP DEFAULT CURRENT_TIMESTAMP` cho created_at
- Định dạng updated_at: `TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`
- Cột deleted_at: Sử dụng `TIMESTAMP NULL` cho soft delete
- Giá tiền: Sử dụng kiểu `BIGINT` cho các cột price, amount, total (đơn vị VNĐ)
- Discount: Sử dụng kiểu `DECIMAL(10,2)` cho các giá trị giảm giá
- Foreign keys: Luôn đặt tên cột kết thúc bằng `_id`

### 13.2. Cấu trúc Database chi tiết

#### Bảng users
```sql
id INT PRIMARY KEY AUTO_INCREMENT
avatar VARCHAR(255)
first_name VARCHAR(255)
last_name VARCHAR(255)
username VARCHAR(255) NOT NULL UNIQUE
email VARCHAR(255) NOT NULL UNIQUE
password VARCHAR(255)
birth_of_date DATE
phone_number VARCHAR(20)
created_at TIMESTAMP
deleted_at TIMESTAMP NULL
```

#### Bảng coupons
```sql
id INT PRIMARY KEY AUTO_INCREMENT
code VARCHAR(50) UNIQUE NOT NULL
description TEXT
discount_type ENUM('PERCENTAGE', 'FIXED_AMOUNT') NOT NULL
discount_value DECIMAL(10,2) NOT NULL
min_purchase DECIMAL(10,2)
max_discount DECIMAL(10,2)
start_date DATETIME NOT NULL
end_date DATETIME NOT NULL
usage_limit INT
times_used INT DEFAULT 0
is_active BOOLEAN DEFAULT TRUE
created_at TIMESTAMP
```

#### Bảng order_details
```sql
id INT PRIMARY KEY AUTO_INCREMENT
user_id INT FOREIGN KEY
total BIGINT
order_address VARCHAR(255)
status VARCHAR(50) DEFAULT 'pending'
coupon_id INT FOREIGN KEY
discount_amount DECIMAL(10,2) DEFAULT 0
sub_total DECIMAL(10,2)
created_at TIMESTAMP
updated_at TIMESTAMP
```

### 13.3. Các quy tắc về Giá & Giảm giá

1. **Quy tắc xử lý giá:**
   - Giá lưu trong DB là VNĐ (không có phần thập phân)
   - Sử dụng BIGINT cho các trường price, total, amount
   - SubTotal: Tổng tiền trước khi áp dụng giảm giá
   - DiscountAmount: Số tiền được giảm
   - Total: Số tiền cuối cùng (sau khi trừ giảm giá)

2. **Quy tắc mã giảm giá:**
   - Hai loại giảm giá: PERCENTAGE (%) và FIXED_AMOUNT (VNĐ)
   - Mã giảm giá có thể có giới hạn:
     - Số lần sử dụng (usage_limit)
     - Giá trị đơn hàng tối thiểu (min_purchase)
     - Giảm tối đa (max_discount) cho loại phần trăm
   - Mã giảm giá có thời hạn (start_date, end_date)
   - Có thể vô hiệu hóa (is_active)
   - Theo dõi số lần đã sử dụng (times_used)

3. **Quy tắc xử lý đơn hàng:**
   - Status mặc định là 'pending'
   - Lưu địa chỉ giao hàng trực tiếp vào order_address
   - Lưu lại thông tin giảm giá (coupon_id, discount_amount)
   - Tracking thanh toán qua bảng payment_details

---

## 14. Ý nghĩa & Quy tắc từng loại file chính trong project

### 14.1. Java

#### 14.1.1. Controller (`controller/`)
- Định nghĩa các endpoint (route) cho từng nhóm chức năng (user, admin, auth...).
- Chỉ xử lý nhận request, trả response, validate đơn giản, chuyển tiếp sang service.
- Không chứa business logic phức tạp.
- Có thể dùng annotation: `@Controller`, `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, ...
- Đặt tên rõ ràng: `UserController`, `AdminController`, `AuthController`, ...
- Mỗi controller nên có comment đầu file mô tả chức năng.
- Có thể inject service qua `@Autowired` hoặc constructor.

#### 14.1.2. Service (`service/`)
- Chứa toàn bộ business logic, xử lý nghiệp vụ, validate phức tạp.
- Không gọi trực tiếp view/template.
- Được gọi từ controller.
- Đặt tên rõ ràng: `UserService`, `BookService`, `OrderDetailService`, ...
- Có thể chia nhỏ service theo domain nếu lớn.
- Sử dụng annotation: `@Service`.

#### 14.1.3. Model (`model/`)
- Định nghĩa entity mapping với database (JPA/Hibernate).
- Đặt tên class PascalCase, tên bảng snake_case.
- Sử dụng annotation: `@Entity`, `@Table`, `@Id`, `@Column`, `@ManyToOne`, `@OneToMany`, ...
- Có thể chứa các enum, DTO nếu cần.

#### 14.1.4. Repository (`repository/`)
- Interface cho JPA/Hibernate truy vấn dữ liệu.
- Đặt tên: `{Entity}Repository` (UserRepository, BookRepository...).
- Kế thừa `JpaRepository` hoặc `CrudRepository`.
- Không chứa logic nghiệp vụ.

#### 14.1.5. Security (`security/`)
- Cấu hình bảo mật, custom UserDetails, JWT, filter, ...
- Đặt tên: `SecurityConfig`, `UserDetailsImpl`, ...
- Chỉ chứa logic liên quan đến xác thực, phân quyền.

#### 14.1.6. Config (nếu có `config/`)
- Chứa các class cấu hình chung cho project (CORS, Swagger, Mail, ...).
- Đặt tên: `CorsConfig`, `SwaggerConfig`, ...
- Sử dụng annotation: `@Configuration`.

### 14.2. Resource

#### 14.2.1. Template (`templates/`)
- Chứa file Thymeleaf HTML, chia theo module (page/user, page/admin, ...).
- Sử dụng fragment cho header, footer, sidebar.
- Đặt tên file rõ ràng, tiếng Anh, snake_case hoặc kebab-case.
- Không để logic phức tạp trong template.

#### 14.2.2. Static (`static/`)
- Chứa file tĩnh: css, js, image.
- `css/`: Style riêng cho từng page/module, đặt tên rõ ràng.
- `js/`: Script riêng cho từng chức năng, đặt tên rõ ràng.
- `image/`: Ảnh tĩnh, chia nhỏ theo loại (avatar, covers, ...).

### 14.3. Database (`database/`)

#### 14.3.1. Migration (`migration/`)
- Chứa các file migration tạo bảng, alter, index, ...
- Đặt tên: `V{version}__{description}.sql`.
- Chỉ chứa lệnh DDL, không insert dữ liệu mẫu.

#### 14.3.2. Seed (`seed/`)
- Chứa script insert/update dữ liệu mẫu cho dev/test.
- Đặt tên: `seed_{table}.sql`.

#### 14.3.3. Backup (`backup/`)
- Lưu file backup dữ liệu định kỳ.

### 14.4. File cấu hình Spring Boot

#### 14.4.1. `application.properties` hoặc `application.yml`
- Cấu hình DB, port, mail, security, ... cho từng môi trường.
- Không commit thông tin nhạy cảm lên repo.
- Có thể tách file theo profile: `application-dev.properties`, `application-prod.properties`.

### 14.5. File tài liệu

#### 14.5.1. `README.md`
- Hướng dẫn cài đặt, build, deploy, cấu trúc project, contact.

#### 14.5.2. `CHANGELOG.md`
- Ghi lại lịch sử thay đổi, version, tính năng mới, bugfix.

---

## 15. Hướng dẫn chi tiết từng loại file & quy tắc thực thi

### 15.1. Java

#### 15.1.1. Controller (`controller/`)
- **Chức năng:** Định nghĩa các endpoint (route) nhận request từ client, trả response, chuyển tiếp xử lý sang service.
- **Quy tắc:**
  - Mỗi controller chỉ phục vụ 1 domain (User, Admin, Auth...).
  - Đặt annotation rõ ràng: `@Controller` cho trả về view, `@RestController` cho API.
  - Đặt route gốc với `@RequestMapping("/user")` hoặc tương tự.
  - Mỗi method có annotation HTTP rõ ràng: `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`.
  - Validate đầu vào cơ bản, trả lỗi hợp lý, không throw exception ra ngoài.
  - Không chứa business logic, chỉ gọi service.
  - Inject service qua constructor hoặc `@Autowired`.
  - Đặt tên method rõ ràng, mô tả endpoint.
  - Có comment đầu file mô tả controller, comment method nếu phức tạp.
  - Trả về view (template) hoặc JSON (API) đúng chuẩn.
  - Không trả về dữ liệu nhạy cảm (password, token...).

#### 15.1.2. Service (`service/`)
- **Chức năng:** Xử lý nghiệp vụ, validate phức tạp, thao tác dữ liệu, gọi repository.
- **Quy tắc:**
  - Đặt annotation `@Service`.
  - Chỉ nhận dữ liệu từ controller, không gọi trực tiếp view/template.
  - Tách nhỏ service nếu domain lớn (UserService, UserProfileService...).
  - Đặt tên method rõ ràng, mô tả nghiệp vụ.
  - Validate logic nghiệp vụ, throw exception hợp lý.
  - Có thể gọi nhiều repository hoặc service khác nếu cần.
  - Comment đầu file mô tả service, comment method phức tạp.
  - Không để code chết, code test trong service.

#### 15.1.3. Model (`model/`)
- **Chức năng:** Định nghĩa entity mapping với DB, các enum, DTO.
- **Quy tắc:**
  - Đặt annotation: `@Entity`, `@Table(name="...")`.
  - Đặt tên class PascalCase, tên bảng snake_case.
  - Đặt annotation cho field: `@Id`, `@Column`, `@ManyToOne`, ...
  - Đặt trường thời gian: `createdAt`, `updatedAt` (kiểu Date/Timestamp).
  - Không chứa logic nghiệp vụ, chỉ getter/setter, equals/hashCode/toString.
  - Nếu có enum, đặt trong file riêng hoặc trong model liên quan.
  - DTO đặt tên rõ ràng, chỉ dùng cho truyền dữ liệu.

#### 15.1.4. Repository (`repository/`)
- **Chức năng:** Interface cho JPA/Hibernate truy vấn dữ liệu.
- **Quy tắc:**
  - Kế thừa `JpaRepository<Entity, IdType>`.
  - Đặt tên: `{Entity}Repository`.
  - Đặt annotation `@Repository` nếu cần custom.
  - Định nghĩa method query rõ ràng, dùng query method hoặc `@Query`.
  - Không chứa logic nghiệp vụ.

#### 15.1.5. Security (`security/`)
- **Chức năng:** Cấu hình bảo mật, xác thực, phân quyền, custom UserDetails.
- **Quy tắc:**
  - Đặt annotation `@Configuration` cho config.
  - Đặt tên: `SecurityConfig`, `JwtFilter`, `UserDetailsImpl`, ...
  - Không chứa logic ngoài bảo mật.
  - Không để lộ secret key, password trong code.

#### 15.1.6. Config (`config/`)
- **Chức năng:** Cấu hình chung (CORS, Swagger, Mail, ...).
- **Quy tắc:**
  - Đặt annotation `@Configuration`.
  - Đặt tên: `CorsConfig`, `SwaggerConfig`, ...
  - Chỉ cấu hình, không chứa logic nghiệp vụ.

### 15.2. Resource

#### 15.2.1. Template (`templates/`)
- **Chức năng:** Giao diện Thymeleaf, chia module rõ ràng.
- **Quy tắc:**
  - Đặt tên file tiếng Anh, snake_case hoặc kebab-case.
  - Tách fragment cho header, footer, sidebar, dùng `th:replace`, `th:fragment`.
  - Không lặp lại code, dùng fragment tối đa.
  - Không để logic phức tạp, chỉ hiển thị dữ liệu.
  - Đặt biến model rõ ràng, không trùng lặp.
  - Đảm bảo responsive, dùng Bootstrap class.
  - Không viết inline style, tách ra file CSS.
  - Đặt meta charset, viewport, title đầy đủ.
  - Comment đầu file mô tả chức năng template.

#### 15.2.2. Static (`static/`)
- **Chức năng:** Chứa file tĩnh: css, js, image.
- **Quy tắc:**
  - `css/`: Đặt tên theo page/module, ví dụ: `profile_style.css`, `order_style.css`.
  - `js/`: Đặt tên theo chức năng, ví dụ: `cart.js`, `wishlist.js`.
  - `image/`: Chia nhỏ theo loại (avatar, covers, ...), đặt tên file random hoặc theo id.
  - Không để file không dùng tới.
  - Không hard-code màu sắc, dùng biến ở :root.
  - Comment đầu file mô tả chức năng nếu file lớn.

### 15.3. Database (`database/`)

#### 15.3.1. Migration (`migration/`)
- **Chức năng:** Tạo bảng, alter, index, constraint.
- **Quy tắc:**
  - Đặt tên: `V{version}__{description}.sql`.
  - Chỉ chứa DDL, không insert dữ liệu mẫu.
  - Comment rõ từng block lệnh.
  - Không sửa migration đã chạy ở production, chỉ tạo migration mới.

#### 15.3.2. Seed (`seed/`)
- **Chức năng:** Insert/update dữ liệu mẫu cho dev/test.
- **Quy tắc:**
  - Đặt tên: `seed_{table}.sql`.
  - Không insert dữ liệu nhạy cảm.
  - Comment rõ dữ liệu mẫu.

#### 15.3.3. Backup (`backup/`)
- **Chức năng:** Lưu file backup dữ liệu định kỳ.
- **Quy tắc:**
  - Đặt tên file theo ngày/tháng/năm.
  - Không commit backup lên repo.

### 15.4. File cấu hình Spring Boot

#### 15.4.1. `application.properties` hoặc `application.yml`
- **Chức năng:** Cấu hình DB, port, mail, security, ... cho từng môi trường.
- **Quy tắc:**
  - Không commit thông tin nhạy cảm lên repo.
  - Tách file theo profile: `application-dev.properties`, `application-prod.properties`.
  - Comment rõ từng block cấu hình.
  - Đặt biến cấu hình rõ ràng, không hard-code.

### 15.5. File tài liệu

#### 15.5.1. `README.md`
- **Chức năng:** Hướng dẫn cài đặt, build, deploy, cấu trúc project, contact.
- **Quy tắc:**
  - Có mục lục, hướng dẫn từng bước.
  - Giải thích cấu trúc thư mục, quy tắc build/test.
  - Để contact, license nếu có.

#### 15.5.2. `CHANGELOG.md`
- **Chức năng:** Ghi lại lịch sử thay đổi, version, tính năng mới, bugfix.
- **Quy tắc:**
  - Ghi rõ ngày, version, nội dung thay đổi.
  - Không bỏ qua các thay đổi nhỏ.

---

## 16. Quy tắc kiểm tra chức năng trước khi code mới (TRÁNH LẶP CODE, TRÁNH BUG)

### 16.1. Nguyên tắc BẮT BUỘC trước khi code/chỉnh sửa chức năng

- **Luôn kiểm tra kỹ các tầng (controller, service, repository, model) trước khi thêm mới hoặc sửa đổi bất kỳ chức năng nào.**
- **Tuyệt đối không tự ý tạo mới service, repository, controller method nếu chưa kiểm tra kỹ đã có sẵn hay chưa.**
- **Phải đọc lại toàn bộ các class liên quan (controller, service, repository, model) để:**
  - Xác định chức năng đã tồn tại hay chưa.
  - Xác định các method, endpoint, query đã có thể tái sử dụng không.
  - Xác định luồng dữ liệu, quan hệ giữa các class, tránh tạo luồng mới không cần thiết.
- **Nếu phát hiện chức năng trùng lặp, phải refactor/tái sử dụng, không copy-paste hoặc tạo mới.**
- **Nếu cần mở rộng chức năng, nên mở rộng method/service hiện có thay vì tạo mới hoàn toàn.**
- **Mọi thay đổi phải đảm bảo không phá vỡ logic cũ, không gây bug cho các chức năng liên quan.**
- **Luôn hỏi ý kiến leader/mentor nếu không chắc chắn về luồng code hoặc nghi ngờ có trùng lặp.**

### 16.2. Trình tự thực hiện khi nhận yêu cầu code/chức năng mới

1. **Đọc kỹ yêu cầu, xác định domain liên quan (user, order, book, ...).**
2. **Tìm kiếm trong project:**
   - Controller liên quan (UserController, AdminController, ...)
   - Service liên quan (UserService, OrderService, ...)
   - Repository liên quan (UserRepository, ...)
   - Model/entity liên quan
3. **Kiểm tra method, endpoint, query đã có sẵn chưa.**
   - Nếu đã có, cân nhắc tái sử dụng hoặc mở rộng.
   - Nếu chưa có, xác định vị trí hợp lý để thêm mới (không tạo class mới tràn lan).
4. **Kiểm tra các luồng dữ liệu, validate, exception đã xử lý đủ chưa.**
5. **Viết code mới theo đúng chuẩn, có comment rõ ràng.**
6. **Viết/kiểm tra lại test case, đảm bảo không bug, không ảnh hưởng chức năng khác.**
7. **Review lại code trước khi commit, đảm bảo không lặp code, không tạo luồng ảo.**

### 16.3. Lý do phải tuân thủ quy tắc này

- **Tránh lặp code, tránh bug do nhiều luồng xử lý giống nhau nhưng khác logic.**
- **Giảm thiểu code ảo, code chết, code không ai dùng đến.**
- **Dễ bảo trì, dễ mở rộng, dễ review, dễ test.**
- **Đảm bảo chất lượng dự án, tiết kiệm thời gian fix bug về sau.**
- **Giúp mọi thành viên hiểu rõ luồng code, tránh hiểu nhầm, tránh phá vỡ logic cũ.**

---

**NHẤN MẠNH:**  
> **Bất kỳ ai code mới hoặc sửa code đều phải đọc kỹ các tầng liên quan trước, không làm ẩu, không tạo mới tràn lan. Nếu vi phạm, leader có quyền reject code hoặc yêu cầu refactor lại toàn bộ.**

## 17. Quy tắc quan trọng về Security & Role

### 17.1. Chuẩn đặt tên Role trong Database

Role trong database:
- name: VARCHAR(50) NOT NULL UNIQUE
- Không có prefix "ROLE_" trong database (ví dụ: 'admin', 'customer')

### 17.2. Thứ tự bảng Database & Quy tắc Foreign Key

1. users (Bảng gốc chứa thông tin người dùng)
2. addresses (FK: user_id -> users.id)
3. categories (Độc lập)
4. sub_categories (FK: parent_id -> categories.id)
5. books (FK: sub_category_id -> sub_categories.id)
6. book_detail (FK: book_id -> books.id, ONE-TO-ONE)
7. book_images (FK: book_id -> books.id)
8. reviews (FK: user_id -> users.id, book_id -> books.id)
9. wishlist (FK: user_id -> users.id, book_id -> books.id)
10. cart (FK: user_id -> users.id)
11. cart_item (FK: cart_id -> cart.id, book_id -> books.id)
12. order_details (FK: user_id -> users.id, coupon_id -> coupons.id)
13. order_item (FK: order_id -> order_details.id, book_id -> books.id)
14. payment_details (FK: order_id -> order_details.id)
15. roles (Độc lập)
16. user_roles (FK: user_id -> users.id, role_id -> roles.id)
17. coupons (Độc lập)
18. user_coupons (FK: user_id -> users.id, coupon_id -> coupons.id, order_id -> order_details.id)

### 17.3. Quy tắc timestamp và deleted_at

Các bảng sử dụng soft delete:
- users
- addresses
- categories
- sub_categories
- books
- book_detail
- book_images
- wishlist

Các bảng sử dụng created_at & updated_at:
- reviews
- cart
- cart_item
- order_details
- order_item
- payment_details

Các bảng chỉ sử dụng created_at:
- roles
- coupons
- user_coupons

### 17.4. Quy tắc kiểu dữ liệu tiền tệ & đơn vị

- Giá sách (books.price): BIGINT (VNĐ)
- Tổng giỏ hàng (cart.total): BIGINT (VNĐ)
- Giá item trong giỏ (cart_item.price): BIGINT (VNĐ)
- Tổng đơn hàng (order_details.total): BIGINT (VNĐ)
- Giá item trong đơn (order_item.price): BIGINT (VNĐ)
- Số tiền thanh toán (payment_details.amount): BIGINT (VNĐ)
- Giảm giá (discount_value): DECIMAL(10,2)
- Giá trị đơn tối thiểu (min_purchase): DECIMAL(10,2)
- Giảm tối đa (max_discount): DECIMAL(10,2)
