# Tạo endpoint trong Spring Boot (Entity → Repository → Service → Controller)

Hướng dẫn ngắn về cách xây dựng một endpoint REST trong ứng dụng Spring Boot sử dụng JPA và Hibernate.

Mục tiêu
- Minh hoạ luồng: `Entity` -> `Repository` -> `Service` -> `Controller`.
- Giải thích vai trò của JPA và Hibernate và một số lưu ý cấu hình thường gặp.

1) Entity (Mô hình dữ liệu)

Ví dụ đơn giản:

```java
package net.codejava.Application.identityservices.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Book {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;
    private String name;
    private String author;
    // getters / setters
}
```

Chú ý:
- `@Entity` đánh dấu lớp là một thực thể JPA — phải import từ `jakarta.persistence`.
- `@Id` xác định khóa chính. `@GeneratedValue` và chiến lược sinh khoá phụ thuộc vào nhà cung cấp (Hibernate hỗ trợ nhiều chiến lược và generator tùy chọn).
- Lỗi phổ biến: `Not a managed type: class ...` xảy ra khi lớp không được đánh dấu `@Entity`, hoặc package của entity không nằm trong phạm vi quét của Spring Boot. Khắc phục bằng cách đặt `Application` ở package gốc hoặc dùng `@EntityScan("...package...")`.

2) Repository (Lớp truy xuất dữ liệu)

Sử dụng Spring Data JPA để giảm boilerplate:

```java
package net.codejava.Application.identityservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.codejava.Application.identityservices.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    // thêm truy vấn tuỳ chỉnh nếu cần
}
```

Vai trò:
- `JpaRepository` cung cấp CRUD, paging, sorting.
- `@Repository` không bắt buộc nhưng giúp phân loại bean và chuyển đổi ngoại lệ thành `DataAccessException`.

3) Service (Lớp nghiệp vụ)

Tách logic nghiệp vụ ra service giúp dễ test và bảo trì:

```java
package net.codejava.Application.identityservices.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookServices {
    private final BookRepository repo;

    public BookServices(BookRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<Book> findAll() { return repo.findAll(); }

    @Transactional
    public Book create(Book book) { return repo.save(book); }
}
```

Chú ý:
- Dùng `@Transactional` để đảm bảo atomic cho các thao tác ghi. Các thao tác chỉ đọc có thể đặt `readOnly=true`.

4) Controller (REST API)

Exposed endpoint:

```java
package net.codejava.Application.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookServices services;

    public BookController(BookServices services) { this.services = services; }

    @GetMapping
    public List<Book> list() { return services.findAll(); }

    @PostMapping
    public Book create(@RequestBody Book book) { return services.create(book); }
}
```

5) Cấu hình JPA / Hibernate (application.properties)

Ví dụ tối thiểu `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Tùy chọn: đặt dialect nếu cần
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

Giải thích:
- JPA (Jakarta Persistence API) là đặc tả (API) cho ORM.
- Hibernate là một implementation phổ biến của JPA — Spring Boot mặc định sử dụng Hibernate nếu có trên classpath.
- `spring.jpa.hibernate.ddl-auto`: `create|update|validate|none` — dùng cẩn trọng trong production.

6) Vấn đề quét entity và package

- Spring Boot tự động quét các component (controllers, services, repositories) bắt đầu từ package chứa lớp `@SpringBootApplication` (thường là `net.codejava.Application`). Đảm bảo package của `@Entity` nằm dưới package gốc này, hoặc cấu hình riêng:

```java
// Nếu entity nằm ngoài package gốc
@SpringBootApplication
@EntityScan("net.codejava.Application.identityservices.entity")
public class Application { ... }
```

7) Chạy và kiểm tra

Build và chạy:

```bash
mvnw package
mvnw spring-boot:run
```

Gọi thử API (ví dụ với `curl`):

```bash
curl -X GET http://localhost:8080/api/books
curl -X POST -H "Content-Type: application/json" -d '{"name":"Tên","author":"Tác giả"}' http://localhost:8080/api/books
```

8) Lời khuyên
- Luôn kiểm tra stack trace nếu gặp lỗi `Not a managed type` — kiểm tra `@Entity`, package scan, và dependency injection của repository.
- Phân tách rõ ràng repository/service/controller giúp unit test dễ dàng (mock repository trong service tests).

---
File hướng dẫn này được thêm vào để nhanh chóng thao tác khi cần tạo endpoint mới trong dự án.
