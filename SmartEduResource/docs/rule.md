# Rules for Adding a New Feature

This project is a Spring MVC WAR application using Hibernate, Spring Security, DTO validation, global exception handling, and a shared API response format.

## 1. Package Structure

Follow the existing package layout:

```text
com.paq.controllers.admin    Admin controllers
com.paq.controllers.client   REST controllers
com.paq.service              service interfaces
com.paq.service.impl         service implementations
com.paq.repository           repository interfaces
com.paq.repository.impl      repository implementations
com.paq.pojo                 Hibernate entities
com.paq.pojo.request         request DTOs
com.paq.pojo.response        response DTOs
com.paq.utils                utility classes and mappers
com.paq.utils.error          global exception handling and custom exceptions
com.paq.configs              Spring, Hibernate, Security, Swagger configs
```

Do not put business logic in controllers. Controllers should only receive requests, call services, and return `ResResponse<T>`.

## 2. Feature Flow

When adding a feature, implement in this order:

1. Entity in `com.paq.pojo` if a new database table is needed.
2. Request DTO in `com.paq.pojo.request`.
3. Response DTO in `com.paq.pojo.response`.
4. Repository interface in `com.paq.repository`.
5. Repository implementation in `com.paq.repository.impl`.
6. Service interface in `com.paq.service`.
7. Service implementation in `com.paq.service.impl`.
8. Controller endpoint in `com.paq.controllers.client`.
9. Exception handling in `GlobalException` if a new error type is needed.
10. Security whitelist/protection in `SpringSecurityConfigs` if endpoint access changes.

## 2.1 Feature Boundary and Class Splitting

Prefer one repository, one service, and one controller per main entity/table when the feature manages separate tables or separate domain concepts.

For example, classification data should be split like this:

```text
repository/
  SubjectRepository.java
  TopicRepository.java
  ResourceTypeRepository.java
  ResourceTagRepository.java

repository/impl/
  SubjectRepositoryImpl.java
  TopicRepositoryImpl.java
  ResourceTypeRepositoryImpl.java
  ResourceTagRepositoryImpl.java

service/
  SubjectService.java
  TopicService.java
  ResourceTypeService.java
  ResourceTagService.java

service/impl/
  SubjectServiceImpl.java
  TopicServiceImpl.java
  ResourceTypeServiceImpl.java
  ResourceTagServiceImpl.java

controllers/client/
  ApiSubjectController.java
  ApiTopicController.java
  ApiResourceTypeController.java
  ApiResourceTagController.java
```

Only group multiple entities into one repository/service/controller when they form one true aggregate and always change together. Do not create broad classes such as `ClassificationRepository` just to make files shorter.

For enum-backed values from the ERD, such as resource levels, prefer a read-only enum API instead of fake CRUD tables unless the database schema has a real table for that data.

## 3. Controller Rules

Controllers must:

- Use `@RestController`.
- Use clear base paths, for example `@RequestMapping("/api/courses")`.
- Validate request DTOs with `@Valid`.
- Return `ResponseEntity<ResResponse<T>>`.
- Throw exceptions instead of manually building error responses.
- Avoid database or business logic.

Example:

```java
@PostMapping
public ResponseEntity<ResResponse<ResCourseDTO>> create(@Valid @RequestBody ReqCourseDTO request) {
    ResCourseDTO data = this.courseService.createCourse(request);

    ResResponse<ResCourseDTO> res = new ResResponse<>();
    res.setStatusCode(HttpStatus.CREATED.value());
    res.setMessage("Create course successfully");
    res.setData(data);

    return new ResponseEntity<>(res, HttpStatus.CREATED);
}
```

Do not do this in controllers:

```java
try {
    // business logic
} catch (Exception ex) {
    // build error response
}
```

Let `GlobalException` handle errors.

### 3.1 Common Controller Pattern

For normal CRUD APIs, controllers should follow one consistent shape:

- Public read endpoints use `/api/{resources}`.
- Protected create, update, and delete endpoints use `/api/secure/{resources}`.
- List endpoints receive filters, search, sort, and pagination through `@RequestParam Map<String, String> params`.
- Detail, update, and delete endpoints receive ids through `@PathVariable`.
- Create and update endpoints receive validated request DTOs through `@Valid @RequestBody`.
- Controllers build only successful `ResResponse<T>` objects.
- Controllers do not check database existence, permissions, or business rules directly.
- Services throw exceptions such as `IdInvalidException`, `PermissionException`, or `IllegalArgumentException`; `GlobalException` converts them into error responses.

Common endpoint shape:

```text
GET    /api/{resources}
GET    /api/{resources}/{id}
POST   /api/secure/{resources}
PUT    /api/secure/{resources}/{id}
DELETE /api/secure/{resources}/{id}
```

Common controller method pattern:

```java
@GetMapping("/api/{resources}")
public ResponseEntity<ResResponse<List<ResEntityDTO>>> getEntities(
        @RequestParam Map<String, String> params) {
    ResResponse<List<ResEntityDTO>> res = new ResResponse<>();
    res.setStatusCode(HttpStatus.OK.value());
    res.setMessage("Lay danh sach thanh cong");
    res.setData(this.entityService.getEntities(params));

    return ResponseEntity.ok(res);
}

@GetMapping("/api/{resources}/{id}")
public ResponseEntity<ResResponse<ResEntityDTO>> getEntityById(@PathVariable int id) {
    ResResponse<ResEntityDTO> res = new ResResponse<>();
    res.setStatusCode(HttpStatus.OK.value());
    res.setMessage("Lay thong tin thanh cong");
    res.setData(this.entityService.getEntityById(id));

    return ResponseEntity.ok(res);
}

@PostMapping("/api/secure/{resources}")
public ResponseEntity<ResResponse<ResEntityDTO>> createEntity(
        @Valid @RequestBody ReqEntityDTO request) {
    ResResponse<ResEntityDTO> res = new ResResponse<>();
    res.setStatusCode(HttpStatus.CREATED.value());
    res.setMessage("Tao moi thanh cong");
    res.setData(this.entityService.createEntity(request));

    return new ResponseEntity<>(res, HttpStatus.CREATED);
}

@PutMapping("/api/secure/{resources}/{id}")
public ResponseEntity<ResResponse<ResEntityDTO>> updateEntity(
        @PathVariable int id,
        @Valid @RequestBody ReqEntityDTO request) {
    ResResponse<ResEntityDTO> res = new ResResponse<>();
    res.setStatusCode(HttpStatus.OK.value());
    res.setMessage("Cap nhat thanh cong");
    res.setData(this.entityService.updateEntity(id, request));

    return ResponseEntity.ok(res);
}

@DeleteMapping("/api/secure/{resources}/{id}")
public ResponseEntity<ResResponse<Object>> deleteEntity(@PathVariable int id) {
    this.entityService.deleteEntity(id);

    ResResponse<Object> res = new ResResponse<>();
    res.setStatusCode(HttpStatus.OK.value());
    res.setMessage("Xoa thanh cong");

    return ResponseEntity.ok(res);
}
```

## 4. DTO Validation Rules

Every input API should use a request DTO in `com.paq.pojo.request`.

Use Jakarta validation annotations:

```java
@NotBlank
@NotNull
@Email
@Size
@Pattern
@Min
@Max
```

Example:

```java
public class ReqCourseDTO {

    @NotBlank(message = "Title khong duoc de trong")
    @Size(max = 255, message = "Title toi da 255 ky tu")
    private String title;
}
```

Controller must use:

```java
public ResponseEntity<ResResponse<ResCourseDTO>> create(@Valid @RequestBody ReqCourseDTO request)
```

Use DTOs and params by request type:

- Use request DTOs for `POST`, `PUT`, and `PATCH` request bodies.
- Use `@RequestParam Map<String, String> params` for list, search, filter, sort, and pagination query strings.
- Use `@PathVariable` for resource id values in detail, update, and delete endpoints.
- Do not pass many individual body fields as separate service parameters when a request DTO can represent the input.

Example:

```java
Course createCourse(ReqCourseDTO request);
Course updateCourse(int id, ReqCourseDTO request);
List<Course> getCourses(Map<String, String> params);
Course getCourseById(int id);
void deleteCourse(int id);
```

## 5. Response Rules

All successful API responses should use `ResResponse<T>`.

Format:

```json
{
  "statusCode": 200,
  "error": null,
  "message": "Success message",
  "data": {}
}
```

Create response DTOs for API output. Do not return Hibernate entities directly unless the endpoint is internal and safe.

Use `DTOMapper` for common mapping logic.

## 6. Exception Rules

Throw custom exceptions from service/controller when business rules fail.

Existing custom exceptions:

```java
IdInvalidException
PermissionException
StorageException
```

Use them like this:

```java
throw new IdInvalidException("Course khong ton tai");
throw new PermissionException("Ban khong co quyen thuc hien thao tac nay");
throw new StorageException("Upload file that bai");
```

`GlobalException` currently handles:

```text
DTO validation errors                 400
Invalid JSON body                     400
Missing request parameter             400
Wrong request parameter type          400
IdInvalidException                    400
IllegalArgumentException              400
BadCredentialsException               401
AuthenticationException               401
PermissionException                   403
AccessDeniedException                 403
NoResourceFoundException              404
NoResultException                     404
DataIntegrityViolationException       409
ResponseStatusException               custom status
Exception fallback                    500
```

If a new feature introduces a new business error, either reuse an existing custom exception or add a new exception class under `com.paq.utils.error`, then add an `@ExceptionHandler` in `GlobalException`.

## 7. Service Rules

Services contain business logic.

Service interfaces go in:

```text
src/main/java/com/paq/service
```

Implementations go in:

```text
src/main/java/com/paq/service/impl
```

Use `@Service` on implementation classes.

Throw exceptions from service when business rules fail.

Example:

```java
@Service
public class CourseServiceImpl implements CourseService {

    @Override
    public ResCourseDTO createCourse(ReqCourseDTO request) {
        if (this.courseRepo.existsByTitle(request.getTitle())) {
            throw new IllegalArgumentException("Course title da ton tai");
        }

        // create and save entity
    }
}
```

## 8. Repository Rules

Repositories are responsible only for database access.

Repository interfaces go in:

```text
com.paq.repository
```

Implementations go in:

```text
com.paq.repository.impl
```

Use:

```java
@Repository
@Transactional
```

Use Hibernate `Session` from `LocalSessionFactoryBean`.

Catch `NoResultException` and return `null` when lookup does not find data.

Do not put business workflows, permission checks, or controller response building in repositories.

Repositories may return either entities or read-only response DTO projections. Choose intentionally:

- Return entities when the service needs persistence behavior, update/delete logic, permission traversal, or domain rules.
- Return DTO projections for read-only APIs that only need flat response data.
- Do not call `DTOMapper` inside repositories. If a repository returns DTOs, build them directly in the query with `CriteriaBuilder.construct(...)` or a JPQL constructor expression.
- Keep `DTOMapper` in the service/mapper layer when repositories return entities.

Current recommended flow:

```text
Read-only flat response      Repository DTO Projection -> Service returns DTO
Read/detail with relations   Repository Entity + JOIN FETCH -> Service DTOMapper
Create/update/delete         Repository Entity + session.get/persist/merge
Dashboard/statistics         Aggregate query -> DTO/service result, no fetch
```

### 8.1 Repository Implementation Pattern

Repository implementations should follow this general pattern:

- Annotate implementation classes with `@Repository` and `@Transactional`.
- Inject `LocalSessionFactoryBean` and get the current Hibernate session with:

```java
Session session = this.factory.getObject().getCurrentSession();
```

- If the repository needs configurable pagination size, add:

```java
@PropertySource("classpath:configs.properties")
```

and inject:

```java
@Autowired
private Environment env;
```

- Use Criteria API for list/search endpoints that support dynamic filters.
- Use HQL/JPQL for fixed queries when the query shape is stable.
- Use `JOIN FETCH` only when the repository returns an entity and the caller needs related data.
- Use DTO projection when the repository returns read-only, flat DTO data.
- Use `session.get(Entity.class, id)` for simple primary-key lookup when no relation is needed.
- Use `session.persist(entity)` for create.
- Use `session.merge(entity)` for update.
- Use `session.remove(entity)` for delete.

Example list method with filters and pagination:

```java
@Override
public List<Product> getProducts(Map<String, String> params) {
    Session session = this.factory.getObject().getCurrentSession();
    CriteriaBuilder b = session.getCriteriaBuilder();
    CriteriaQuery<Product> q = b.createQuery(Product.class);
    Root<Product> root = q.from(Product.class);
    q.select(root);

    if (params != null) {
        List<Predicate> predicates = new ArrayList<>();

        String kw = params.get("kw");
        if (kw != null && !kw.isEmpty()) {
            predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
        }

        String fromPrice = params.get("fromPrice");
        if (fromPrice != null && !fromPrice.isEmpty()) {
            predicates.add(b.greaterThanOrEqualTo(root.get("price"), fromPrice));
        }

        String toPrice = params.get("toPrice");
        if (toPrice != null && !toPrice.isEmpty()) {
            predicates.add(b.lessThanOrEqualTo(root.get("price"), toPrice));
        }

        String cateId = params.get("cateId");
        if (cateId != null && !cateId.isEmpty()) {
            predicates.add(b.equal(root.get("categoryId").as(Integer.class), cateId));
        }

        q.where(predicates.toArray(Predicate[]::new));
    }

    q.orderBy(b.desc(root.get("id")));

    Query<Product> query = session.createQuery(q);

    if (params != null) {
        int pageSize = this.env.getProperty("products.page_size", Integer.class);
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int start = (page - 1) * pageSize;

        query.setMaxResults(pageSize);
        query.setFirstResult(start);
    }

    return query.getResultList();
}
```

For pagination:

- Page numbers are 1-based.
- Default page should be `1` when the request does not provide `page`.
- Start offset is:

```java
int start = (page - 1) * pageSize;
```

- Apply pagination with:

```java
query.setMaxResults(pageSize);
query.setFirstResult(start);
```

For create or update:

```java
@Override
public void addOrUpdateProduct(Product p) {
    Session session = this.factory.getObject().getCurrentSession();
    if (p.getId() != null) {
        session.merge(p);
    } else {
        session.persist(p);
    }
}
```

For delete:

```java
@Override
public void deleteProduct(int id) {
    Session session = this.factory.getObject().getCurrentSession();
    Product p = this.getProductById(id);
    session.remove(p);
}
```

Before calling `remove`, services should ensure the entity exists and throw a business exception such as `IdInvalidException` when it does not.

### 8.2 Query Strategy Rules

Use this decision table before writing a repository query:

```text
Dynamic search/filter/page query        CriteriaBuilder
Fixed read query                        HQL/JPQL
Fixed read query with many relations    HQL/JPQL + JOIN FETCH when returning entity
Read-only flat DTO response             DTO Projection, no JOIN FETCH
Many JOINs with GROUP BY/aggregate       CriteriaBuilder or HQL, no fetch
Simple CRUD                             session.get / persist / merge
Dashboard/statistics                    COUNT/SUM/AVG aggregate query, no fetch
```

#### DTO Projection

Use DTO Projection when the method is read-only and needs joined data for display, but does not need full Hibernate entities. In this case, select only the required fields/columns and construct the response DTO directly. This avoids loading full entities and avoids `DTOMapper` for that method.

Important wording: DTO Projection selects fields/columns, not entities. If the service needs the real entity for update, delete, permission traversal, or domain logic, return an entity instead.

Good candidates:

```text
Answer option list
Category/subject/topic/type/tag list
Dashboard summary values
Flat list/search result DTOs
```

Example:

```java
@Override
public List<ResAnswerOptionDTO> getAnswersByQuestionId(int questionId) {
    Session session = this.factory.getObject().getCurrentSession();
    CriteriaBuilder b = session.getCriteriaBuilder();
    CriteriaQuery<ResAnswerOptionDTO> q = b.createQuery(ResAnswerOptionDTO.class);
    Root<AnswerOption> root = q.from(AnswerOption.class);
    Join<AnswerOption, Question> question = root.join("questionId", JoinType.INNER);
    Join<Question, Quiz> quiz = question.join("quizId", JoinType.INNER);

    q.select(b.construct(ResAnswerOptionDTO.class,
            root.get("id"),
            root.get("content"),
            root.get("isCorrect")))
            .where(b.and(
                    b.equal(question.get("id"), questionId),
                    b.or(b.isFalse(root.get("isDeleted")), b.isNull(root.get("isDeleted"))),
                    b.or(b.isFalse(question.get("isDeleted")), b.isNull(question.get("isDeleted"))),
                    b.or(b.isFalse(quiz.get("isDeleted")), b.isNull(quiz.get("isDeleted")))))
            .orderBy(b.asc(root.get("id")));

    return session.createQuery(q).getResultList();
}
```

Rules for DTO Projection:

- Add a matching constructor to the DTO.
- Constructor argument order and Java types must match the query selection.
- Use normal `JOIN`, not `JOIN FETCH`; fetch is only for entity loading.
- Keep DTO Projection mostly for flat DTOs.
- Use DTO Projection for `read-only + only required fields`.
- Do not use DTO Projection when the service needs the managed entity for update/delete/business logic.
- Be careful with DTOs that contain lists, such as `CourseDTO.subjects` or `QuizDTO.questions`. A join over a collection creates multiple rows. For nested collections, either keep entity `JOIN FETCH + DTOMapper`, or query flat rows and group them manually.

#### JOIN FETCH

Use `JOIN FETCH` when the repository returns an entity and the service/mapper needs related data immediately.

Good candidates:

```text
getAnswerById for update/delete/check permission
getCourseById when DTOMapper needs createdBy, lecturer.user, subjects
getResourceById when DTOMapper needs uploadBy, subjects, topics, tags, types
getQuizById when DTOMapper needs course, createdBy, questions, answers
getQuizAttemptById when DTOMapper needs answers, questions, options
```

Example:

```java
@Override
public AnswerOption getAnswerById(int id) {
    try {
        Session session = this.factory.getObject().getCurrentSession();
        Query<AnswerOption> q = session.createQuery(
                "SELECT DISTINCT a FROM AnswerOption a "
                + "JOIN FETCH a.questionId q "
                + "JOIN FETCH q.quizId qz "
                + "LEFT JOIN FETCH q.answerOptionSet "
                + "WHERE a.id = :id "
                + "AND (a.isDeleted = false OR a.isDeleted IS NULL) "
                + "AND (q.isDeleted = false OR q.isDeleted IS NULL) "
                + "AND (qz.isDeleted = false OR qz.isDeleted IS NULL)",
                AnswerOption.class);
        q.setParameter("id", id);
        return q.getSingleResult();
    } catch (NoResultException ex) {
        return null;
    }
}
```

Rules for `JOIN FETCH`:

- Use it only when returning entities.
- Use `DISTINCT` when fetching a collection to avoid duplicate root entities.
- Do not fetch relationships for duplicate/existence checks.
- Do not filter a fetched child collection in `WHERE` when the parent should still be returned. For example, if a `Question` should still be returned even when all answers are deleted, fetch answers and let `DTOMapper` filter deleted answers.
- For permission flows, fetch the relation chain needed by `PermissionService`, such as `payment -> enrollment -> student -> user`.

### 8.3 Repository Lookup and Query Rules

Use these common rules when implementing repository lookup methods:

- Repository methods only access data. Do not build API responses, check permissions, or place business workflows in repositories.
- Always get the current Hibernate session with:

```java
Session session = this.factory.getObject().getCurrentSession();
```

- For primary-key lookup with no relation needs, prefer `session.get(Entity.class, id)`.
- For primary-key lookup that feeds DTO mapping or permission traversal, use fixed HQL with `JOIN FETCH`.
- For fixed-field lookup such as `name`, `username`, or `email`, prefer `createNamedQuery(...)` when the entity already defines a matching named query.
- Use typed queries such as `Query<Course>` instead of raw `Query`.
- Always pass request values with `setParameter(...)`; do not concatenate user input into HQL/SQL.
- If `getSingleResult()` can find no row, catch `NoResultException` and return `null`.
- Repository lookup methods should usually return `null` for not found. Services decide whether to throw `IdInvalidException`, `IllegalArgumentException`, or continue.
- Use Criteria API/`CriteriaBuilder` for list/search endpoints with dynamic filters, optional params, sorting, and pagination.
- Do not use Criteria API for simple fixed lookups unless it makes the code clearer.
- Only use `JOIN FETCH` when the caller really needs related data for DTO mapping or a business rule.
- Do not fetch extra relationships in existence/duplicate checks. For example, `getCourseByName` should only find the course by name, not fetch `createdBy`, `lecturerId`, or subjects.
- For soft-delete entities, treat deleted rows as not found:

```java
if (entity == null || (entity.getIsDeleted() != null && entity.getIsDeleted() == true)) {
    return null;
}
```

Example lookup by id:

```java
@Override
public Course getCourseById(int id) {
    Session session = this.factory.getObject().getCurrentSession();
    Course course = session.get(Course.class, id);

    if (course == null || (course.getIsDeleted() != null && course.getIsDeleted() == true)) {
        return null;
    }

    return course;
}
```

Example lookup by fixed field:

```java
@Override
public Course getCourseByName(String name) {
    try {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Course> q = session.createNamedQuery("Course.findByName", Course.class);
        q.setParameter("name", name);

        Course course = q.getSingleResult();
        if (course.getIsDeleted() != null && course.getIsDeleted() == true) {
            return null;
        }

        return course;
    } catch (NoResultException ex) {
        return null;
    }
}
```

## 9. Entity Rules

Entities go in `com.paq.pojo`.

Every entity must have:

```java
@Entity
@Table(name = "table_name")
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

If an entity is referenced by `@ManyToOne`, `@OneToMany`, `@OneToOne`, or `@ManyToMany`, it must also be annotated with `@Entity`.

Use `jakarta.persistence.*`, not `javax.persistence.*`.

### 9.1 Delete, Status, and Active Rules

Use one consistent state strategy per entity type:

- Main business entities that need history should use soft delete with `isDeleted`.
- Transaction/history entities should keep their business `status` instead of `isDeleted`.
- Users should use `isActive` for account locking/unlocking.

Recommended entity groups:

```text
Soft delete with isDeleted:
Course, Resource, Quiz, Question, AnswerOption,
ForumThread, ForumPost, ChatMessage, Interaction, InteractionReply,
LearningPath, LearningPathItem, Notification,
Subject, Topic, ResourceType, ResourceTag, ForumCategory

Business status, no isDeleted:
Enrollment, Payment, QuizAttempt, StudentAnswer,
LearningLog, LearningAnalysis

Active/disabled:
User
```

For soft-delete entities:

- Map the database column with:

```java
@Column(name = "is_deleted")
private Boolean isDeleted;
```

- `DELETE` APIs must not call `session.remove(entity)`.
- `DELETE` APIs should set `isDeleted = true`.
- Create APIs should set `isDeleted = false`.
- List APIs must filter out deleted rows by default:

```java
isDeleted = false OR isDeleted IS NULL
```

- Detail, update, and business actions should treat deleted rows as not found.
- Only add an explicit admin restore/include-deleted API when the feature requires it.

For users:

- Keep account locking separate from deletion:

```java
@Column(name = "is_active")
private Boolean isActive;
```

- Locking an account should set `isActive = false`.
- Public/user lookup flows should ignore inactive accounts unless an admin workflow explicitly needs them.

## 10. Security Rules

Public endpoints must be added to `SpringSecurityConfigs`:

```java
.requestMatchers("/api/login", "/api/users").permitAll()
```

Protected endpoints should follow:

```text
/api/secure/**
```

JWT-protected endpoints should rely on `JwtFilter` and `SecurityContextHolder`.

Do not manually parse JWT in every controller unless there is a specific reason.

### 10.1 Permission Service Rules

Authorization business rules must be centralized in `PermissionService`.

Controllers must not check roles, owners, enrollments, payments, or quiz ownership directly. Controllers should only call the feature service and return `ResResponse<T>`.

Feature services should call `PermissionService` before changing protected data or reading private data. Use `require...()` methods that throw `PermissionException` when the current user is not allowed.

Current permission methods:

```java
requireAdmin();
requireLecturerOrAdmin();
requireStudent();
requireCurrentUserOrAdmin(Integer userId);
requireResourceOwnerOrAdmin(Integer resourceId);
requireCourseLecturerOrAdmin(Integer courseId);
requireEnrollmentOrAdmin(Integer courseId);
requirePaymentOwnerOrAdmin(Integer paymentId);
requireQuizOwnerOrAdmin(Integer quizId);
```

Use these rules from `pa.md`:

- Admin can manage the whole system.
- Student can access personal data, enrolled courses, payments, and student-only learning actions.
- Lecturer can upload resources, manage own resources, and manage course/quiz workflows assigned to lecturers.
- Lecturer can only update or delete a `Resource` they uploaded, unless the user is admin.
- Guest can only access public endpoints and public resources.

Example:

```java
@Override
public ResResourceDTO updateResource(int id, ReqResourceDTO request) {
    this.permissionService.requireResourceOwnerOrAdmin(id);
    // update resource
}
```

If the entity needed for a permission check does not exist, `PermissionService` should throw `IdInvalidException`. If the entity exists but the user cannot access it, throw `PermissionException`.

## 11. Swagger Rules

After adding a new API, check Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

For Swagger to show request fields clearly:

- Prefer request DTOs over `Map<String, String>` for request bodies.
- Use `@RequestBody` for JSON APIs.
- Use `@RequestParam Map<String, String> params` for list, search, filter, sort, and pagination query strings such as `kw`, `page`, or `sort`.
- Use `@ModelAttribute` only for multipart form APIs.
- Add validation annotations to DTO fields.

## 12. File Upload Rules

Use `MultipartFile` only when the endpoint really uploads files.

If upload fails, throw:

```java
throw new StorageException("Upload file that bai", ex);
```

Do not swallow upload exceptions silently.

## 13. Encoding Rules

Prefer ASCII messages in Java code unless the file encoding is confirmed clean UTF-8.

Good:

```java
"Dang nhap thanh cong"
```

Avoid mojibake-prone strings if the file has encoding issues.

## 14. Testing Checklist

Before considering a feature done:

1. Build or compile changed files.
2. Redeploy Tomcat if running as WAR.
3. Test success case in Postman or Swagger.
4. Test validation error case.
5. Test unauthorized/forbidden case if endpoint is protected.
6. Confirm response uses `ResResponse<T>`.
7. Confirm no stacktrace is returned to the client.

## 15. Commit Checklist

Before commit:

```bash
git status
```

Commit message format:

```text
feat: add course management APIs
fix: handle invalid JWT profile requests
refactor: centralize API error handling
```

If the feature changes API behavior, mention:

- endpoint path
- DTO added
- security rule changed
- exception behavior changed
