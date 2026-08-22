# Kiến Thức Quan Trọng Của Spring Framework

> Tài liệu này đào sâu hơn vào **Spring Framework** (nền tảng bên dưới Spring Boot) — những khái niệm mà 1 dev Spring cần nắm chắc, không chỉ dừng ở annotation. Đọc kèm với file `Spring-Boot-Kien-Truc-Tong-Hop.md` đã tạo trước đó.

---

## 1. Spring Framework vs Spring Boot – phân biệt

| | Spring Framework | Spring Boot |
|---|---|---|
| Bản chất | Framework lõi (IoC, AOP, MVC...) | Bộ "đóng gói" giúp dùng Spring Framework nhanh hơn |
| Cấu hình | Thường phải config thủ công (XML/Java Config) | Auto-configuration, ít config |
| Server | Phải tự deploy WAR ra Tomcat/Jetty | Embedded server, chạy `java -jar` |
| Mục đích | Cung cấp nền tảng (IoC, DI, AOP, MVC, Data, Security...) | Giúp khởi động dự án Spring nhanh, convention over configuration |

Nói ngắn gọn: **Spring Boot = Spring Framework + auto-config + embedded server + starter deps**.

---

## 2. IoC Container & Bean Lifecycle

### 2.1. ApplicationContext là gì?
Là "cái hộp" quản lý toàn bộ Bean trong ứng dụng — tạo, cấu hình, inject, hủy.

```java
ApplicationContext context = SpringApplication.run(DemoApplication.class, args);
UserService service = context.getBean(UserService.class);
```

### 2.2. Vòng đời của Bean

```
Instantiate → Populate properties (DI) → @PostConstruct → Bean sẵn sàng dùng → @PreDestroy → Destroy
```

```java
@Component
public class CacheManager {

    @PostConstruct
    public void init() {
        System.out.println("Bean đã tạo, load cache...");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("App tắt, dọn cache...");
    }
}
```

### 2.3. Bean Scope

| Scope | Ý nghĩa |
|---|---|
| `singleton` (mặc định) | 1 instance duy nhất dùng chung toàn app |
| `prototype` | Mỗi lần inject/getBean tạo instance mới |
| `request` | 1 instance / mỗi HTTP request (web app) |
| `session` | 1 instance / mỗi HTTP session |

```java
@Component
@Scope("prototype")
public class ReportGenerator {
    // mỗi lần cần sẽ tạo 1 object mới, không share state
}
```

---

## 3. Các kiểu Dependency Injection

```java
// 1. Constructor Injection – khuyến nghị (bất biến, dễ test)
@Service
public class OrderService {
    private final PaymentService paymentService;
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}

// 2. Setter Injection – dùng khi dependency là optional
@Service
public class OrderService {
    private PaymentService paymentService;
    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}

// 3. Field Injection – ngắn gọn nhưng khó test, không nên dùng cho code production
@Service
public class OrderService {
    @Autowired
    private PaymentService paymentService;
}
```

---

## 4. AOP (Aspect Oriented Programming)

Dùng để xử lý các "cross-cutting concern" (logging, security, transaction...) tách biệt khỏi business logic.

```java
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.demo.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Gọi hàm: " + joinPoint.getSignature().getName());
    }

    @Around("execution(* com.example.demo.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        System.out.println("Thời gian chạy: " + (System.currentTimeMillis() - start) + "ms");
        return result;
    }
}
```

**Ứng dụng thực tế**: logging, đo performance, kiểm tra quyền, audit log, retry logic.

---

## 5. Spring MVC – cơ chế xử lý request

```
Client → DispatcherServlet → HandlerMapping (tìm Controller phù hợp)
       → Controller xử lý → trả về Model/ResponseBody
       → (nếu trả view) ViewResolver → render HTML
       → Response về Client
```

`DispatcherServlet` là "Front Controller" trung tâm — mọi request đều đi qua nó trước khi đến Controller cụ thể.

---

## 6. Spring Data JPA – Query nâng cao

### 6.1. Query method tự sinh theo tên hàm

```java
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameContaining(String keyword);
    List<User> findByAgeGreaterThan(int age);
    Optional<User> findByEmailAndActiveTrue(String email);
}
```

### 6.2. Custom query với `@Query`

```java
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailCustom(@Param("email") String email);

    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.id = :id")
    void deactivateUser(@Param("id") Long id);
}
```

### 6.3. Phân trang (Pagination)

```java
Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
Page<User> users = userRepository.findAll(pageable);
```

---

## 7. Transaction Management chi tiết

```java
@Service
public class TransferService {

    @Transactional(
        propagation = Propagation.REQUIRED,   // dùng transaction hiện có, nếu chưa có thì tạo mới
        isolation = Isolation.READ_COMMITTED, // mức cô lập dữ liệu
        rollbackFor = Exception.class          // rollback khi gặp exception nào
    )
    public void transfer(Long fromId, Long toId, double amount) {
        accountRepository.decreaseBalance(fromId, amount);
        accountRepository.increaseBalance(toId, amount);
        // nếu dòng dưới lỗi -> cả 2 thao tác trên đều rollback
    }
}
```

**Propagation thường gặp**: `REQUIRED` (mặc định), `REQUIRES_NEW` (luôn tạo transaction mới), `NESTED` (transaction con lồng bên trong).

---

## 8. Spring Security (cơ bản)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

Cơ chế xác thực JWT thường thêm 1 `OncePerRequestFilter` để đọc token trước khi request tới Controller.

---

## 9. Caching

```java
@EnableCaching
@SpringBootApplication
public class DemoApplication { }

@Service
public class ProductService {

    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {
        // chỉ chạy query DB nếu cache chưa có, lần sau lấy từ cache
        return productRepository.findById(id).orElseThrow();
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
```

---

## 10. Scheduling & Async

```java
@EnableScheduling
@SpringBootApplication
public class DemoApplication { }

@Component
public class ReportScheduler {

    @Scheduled(cron = "0 0 8 * * *") // 8h sáng mỗi ngày
    public void sendDailyReport() {
        System.out.println("Gửi báo cáo hàng ngày...");
    }
}
```

```java
@EnableAsync
@SpringBootApplication
public class DemoApplication { }

@Service
public class EmailService {

    @Async
    public void sendEmail(String to) {
        // chạy trên thread riêng, không block request chính
        System.out.println("Đang gửi email tới " + to);
    }
}
```

---

## 11. Spring Testing

```java
// Test toàn bộ context (integration test)
@SpringBootTest
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Test
    void testCreateUser() {
        UserDto user = userService.create(new UserDto("A", "a@test.com"));
        assertNotNull(user.getId());
    }
}

// Test riêng tầng Controller, mock Service
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetUser() throws Exception {
        Mockito.when(userService.findById(1L)).thenReturn(new UserDto(1L, "A"));
        mockMvc.perform(get("/api/users/1"))
               .andExpect(status().isOk());
    }
}
```

---

## 12. Bảng tổng hợp – Nhóm kiến thức theo mục tiêu học

| Mục tiêu | Cần học |
|---|---|
| Hiểu core Spring | IoC, DI, Bean lifecycle, Bean scope |
| Viết REST API | Spring MVC, DispatcherServlet, @RestController |
| Làm việc với DB | Spring Data JPA, Query method, @Transactional |
| Bảo mật | Spring Security, JWT, PasswordEncoder |
| Tối ưu performance | Caching, Async, Scheduling |
| Đảm bảo chất lượng code | Spring Test (@SpringBootTest, @WebMvcTest, @MockBean) |
| Tách logic phụ | AOP (logging, audit, retry) |

---

## 13. Prompt mẫu để đào sâu từng phần với Claude

```
Bạn là chuyên gia Spring Framework với 10 năm kinh nghiệm.

Hãy giải thích sâu về chủ đề: [ĐIỀN TÊN CHỦ ĐỀ, vd: "Spring Security JWT"]

Yêu cầu:
1. Giải thích cơ chế hoạt động bên trong (không chỉ liệt kê annotation)
2. Vẽ sơ đồ luồng xử lý dạng text (step by step)
3. Cho 1 ví dụ code đầy đủ, chạy được thực tế (không phải đoạn code rời rạc)
4. Nêu rõ những lỗi/hiểu lầm phổ biến khi mới học phần này
5. So sánh với cách làm cũ (nếu có) để thấy rõ vì sao Spring làm vậy

Viết bằng tiếng Việt, code bằng Java/Spring Boot, trình bày theo heading rõ ràng.
```