# Spring Boot – Tổng Hợp Kiến Trúc & Keyword Quan Trọng

> Tài liệu tổng hợp dành cho người mới ôn lại hoặc tra cứu nhanh. Mỗi phần có ví dụ code Java tối thiểu để dễ hình dung.

---

## 1. Spring Boot là gì?

Spring Boot là 1 framework xây dựng trên nền Spring Framework, giúp:
- **Tự động cấu hình (Auto-configuration)**: giảm code cấu hình XML/Java thủ công.
- **Starter dependencies**: gom nhóm thư viện liên quan (vd `spring-boot-starter-web` gồm Spring MVC + Tomcat).
- **Embedded server**: chạy app độc lập (java -jar) không cần deploy WAR ra server ngoài.
- **Production-ready**: có sẵn Actuator để theo dõi health, metrics.

---

## 2. Kiến trúc tổng quan (Layered Architecture)

Một ứng dụng Spring Boot chuẩn thường chia làm các tầng:

```
Client (HTTP request)
      │
      ▼
Controller Layer   ──► Nhận request, trả response (REST API)
      │
      ▼
Service Layer      ──► Xử lý business logic
      │
      ▼
Repository Layer   ──► Giao tiếp với database (JPA/Hibernate)
      │
      ▼
Database
```

Sơ đồ project chuẩn:

```
src/main/java/com/example/demo
 ├── DemoApplication.java        // Entry point
 ├── controller/                 // REST Controllers
 ├── service/                    // Business logic
 ├── repository/                 // Data access (JPA)
 ├── entity/ (model)             // Đối tượng ánh xạ DB
 ├── dto/                        // Data Transfer Object
 ├── config/                     // Các class cấu hình
 └── exception/                  // Xử lý exception toàn cục
src/main/resources
 ├── application.yml / .properties
 └── static/, templates/
```

---

## 3. Các khái niệm cốt lõi

### 3.1. IoC (Inversion of Control) & DI (Dependency Injection)

Spring quản lý vòng đời object (gọi là **Bean**) thay vì bạn tự `new`. Bean được "tiêm" (inject) vào nơi cần dùng.

```java
@Service
public class UserService {
    private final UserRepository userRepository;

    // Constructor Injection (cách khuyến nghị nhất)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### 3.2. Auto-configuration

Khi thêm dependency (vd `spring-boot-starter-data-jpa`), Spring Boot tự động cấu hình DataSource, EntityManager... dựa trên classpath, giảm code boilerplate.

### 3.3. Embedded Server

`spring-boot-starter-web` mặc định nhúng sẵn Tomcat, chỉ cần `mvn spring-boot:run` hoặc `java -jar app.jar` là chạy được, không cần cài server riêng.

---

## 4. Danh sách Keyword / Annotation quan trọng (kèm ví dụ)

### 4.1. `@SpringBootApplication`
Đánh dấu class chính (entry point), gộp 3 annotation: `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`.

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 4.2. `@RestController` / `@Controller`
`@RestController` = `@Controller` + `@ResponseBody`, trả JSON trực tiếp thay vì render view.

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

### 4.3. `@RequestMapping` và các biến thể
`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`.

```java
@PostMapping
public UserDto createUser(@RequestBody @Valid UserDto dto) {
    return userService.create(dto);
}
```

### 4.4. `@Service`
Đánh dấu tầng business logic, cũng là 1 loại Bean.

```java
@Service
public class UserService {
    public UserDto findById(Long id) {
        // business logic
        return new UserDto(id, "Nguyen Van A");
    }
}
```

### 4.5. `@Repository`
Đánh dấu tầng truy cập dữ liệu, Spring tự "dịch" exception của DB thành `DataAccessException`.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

### 4.6. `@Component`
Annotation gốc, dùng cho bean generic không thuộc Controller/Service/Repository.

```java
@Component
public class EmailValidator {
    public boolean isValid(String email) {
        return email != null && email.contains("@");
    }
}
```

### 4.7. `@Autowired`
Tiêm dependency tự động (nên ưu tiên constructor injection thay vì field injection).

```java
@Component
public class NotificationSender {
    @Autowired
    private EmailValidator emailValidator; // field injection (ít khuyến khích)
}
```

### 4.8. `@Configuration` & `@Bean`
Định nghĩa Bean thủ công khi cần custom logic khởi tạo.

```java
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### 4.9. `@Value` & `application.yml`
Lấy giá trị từ file cấu hình.

```yaml
# application.yml
app:
  name: MyDemoApp
  version: "1.0"
```

```java
@Component
public class AppInfo {
    @Value("${app.name}")
    private String appName;
}
```

### 4.10. `@Entity`, `@Id`, `@GeneratedValue`
Ánh xạ class Java sang bảng trong Database (JPA/Hibernate).

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
}
```

### 4.11. `@Transactional`
Đảm bảo 1 nhóm thao tác DB chạy trong 1 transaction, rollback nếu lỗi.

```java
@Service
public class OrderService {
    @Transactional
    public void placeOrder(Order order) {
        orderRepository.save(order);
        inventoryService.decreaseStock(order);
        // nếu decreaseStock lỗi -> rollback save ở trên
    }
}
```

### 4.12. `@ControllerAdvice` & `@ExceptionHandler`
Xử lý exception tập trung cho toàn bộ ứng dụng.

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
```

### 4.13. `@Qualifier`
Khi có nhiều bean cùng loại, dùng để chỉ định bean cụ thể cần inject.

```java
public interface PaymentService {}

@Service("momoPayment")
public class MomoPaymentService implements PaymentService {}

@Service("vnpayPayment")
public class VnpayPaymentService implements PaymentService {}

@Component
public class CheckoutHandler {
    public CheckoutHandler(@Qualifier("momoPayment") PaymentService paymentService) {}
}
```

### 4.14. `@Profile`
Cấu hình bean/behavior khác nhau theo môi trường (dev, test, prod).

```java
@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return new H2DataSource();
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return new MysqlDataSource();
    }
}
```

### 4.15. DTO (Data Transfer Object)
Không phải annotation, mà là pattern: tách object trả về API khỏi Entity DB, tránh lộ dữ liệu thừa.

```java
public class UserDto {
    private Long id;
    private String name;
    // không có field password, createdAt... như Entity
}
```

### 4.16. `@Valid` + Bean Validation
Validate dữ liệu đầu vào tự động.

```java
public class UserDto {
    @NotBlank(message = "Name không được để trống")
    private String name;

    @Email(message = "Email không hợp lệ")
    private String email;
}
```

---

## 5. Ví dụ CRUD API hoàn chỉnh (thu nhỏ, ghép các phần trên)

```java
// Entity
@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
}

// Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}

// Service
@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo) { this.repo = repo; }

    public List<Product> getAll() { return repo.findAll(); }
    public Product create(Product p) { return repo.save(p); }
}

// Controller
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @GetMapping
    public List<Product> getAll() { return service.getAll(); }

    @PostMapping
    public Product create(@RequestBody Product p) { return service.create(p); }
}
```

---

## 6. Spring Boot Actuator (theo dõi ứng dụng)

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health, info, metrics
```

Sau khi chạy app, truy cập `http://localhost:8080/actuator/health` để xem trạng thái ứng dụng.

---

## 7. Tổng kết bảng nhanh (cheatsheet)

| Annotation | Vai trò |
|---|---|
| `@SpringBootApplication` | Điểm khởi động app |
| `@RestController` | API trả JSON |
| `@Service` | Business logic |
| `@Repository` | Truy cập DB |
| `@Component` | Bean generic |
| `@Autowired` | Tiêm dependency |
| `@Configuration` / `@Bean` | Cấu hình bean thủ công |
| `@Entity` | Ánh xạ bảng DB |
| `@Transactional` | Quản lý transaction |
| `@ControllerAdvice` | Xử lý lỗi toàn cục |
| `@Qualifier` | Chọn bean cụ thể |
| `@Profile` | Cấu hình theo môi trường |
| `@Valid` | Validate dữ liệu |

---

## 8. Prompt mẫu để dùng lại với Claude

Dưới đây là bản prompt đã được chỉnh gọn, rõ ràng, dùng lại được cho các lần hỏi sau (về Spring Boot hoặc framework khác):

```
Bạn là chuyên gia Spring Boot với 10 năm kinh nghiệm.

Hãy tạo cho tôi 1 tài liệu README (định dạng Markdown) tổng hợp:
1. Kiến trúc tổng quan của Spring Boot (các tầng: Controller - Service - Repository - Entity)
2. Danh sách các annotation/keyword quan trọng nhất, chia theo từng nhóm
   (khởi tạo app, REST API, dependency injection, cấu hình, JPA/DB,
   xử lý exception, validation, môi trường/profile)
3. Với MỖI annotation/keyword, cho tôi:
   - Giải thích ngắn gọn (1-2 câu)
   - 1 ví dụ code Java tối giản, dễ hiểu
4. Cuối tài liệu, thêm 1 ví dụ CRUD API hoàn chỉnh (Controller - Service -
   Repository - Entity) ghép các phần trên lại với nhau
5. Thêm 1 bảng cheatsheet tổng hợp cuối bài

Yêu cầu:
- Viết bằng tiếng Việt, code bằng Java/Spring Boot
- Ngắn gọn, dễ đọc, ưu tiên ví dụ thực tế hơn lý thuyết dài dòng
- Trình bày theo cấu trúc phân cấp (heading rõ ràng) để dễ tra cứu lại sau này
```

Bạn chỉ cần copy đoạn prompt trên và đổi phần "Spring Boot" thành framework/chủ đề khác (vd React, Django, Node.js...) là dùng lại được ngay.