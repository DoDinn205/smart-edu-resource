# CONTEXT.md — Đề tài: Quản lý học liệu số

## 1. Tổng quan hệ thống

Hệ thống Quản lý học liệu số là một nền tảng hỗ trợ quản lý, phân loại, tìm kiếm, truy cập và theo dõi quá trình học tập thông qua các tài liệu học tập số như giáo trình, bài giảng, slide, video, tài nguyên tham khảo, bài tập, bài kiểm tra và khóa học trực tuyến.

Hệ thống phục vụ nhiều nhóm người dùng khác nhau gồm sinh viên, giảng viên và quản trị viên. Mục tiêu chính là giúp giảng viên/quản trị viên quản lý học liệu hiệu quả, giúp sinh viên dễ dàng tìm kiếm tài liệu phù hợp, theo dõi tiến độ học tập và tham gia các hoạt động học tập trực tuyến.

Hệ thống có thể mở rộng thêm các chức năng nâng cao như gợi ý tài liệu liên quan, đề xuất lộ trình học tập bằng AI, thanh toán khóa học trực tuyến, thống kê doanh thu và phân tích kết quả học tập.

---

## 2. Mục tiêu chính của hệ thống

Hệ thống cần đáp ứng các mục tiêu sau:

1. Cho phép giảng viên và quản trị viên tải lên, cập nhật, phân loại, quản lý và gỡ bỏ học liệu.
2. Cho phép sinh viên tìm kiếm, lọc và truy cập học liệu theo từ khóa, môn học, chủ đề, cấp độ hoặc loại tài liệu.
3. Tổ chức học liệu theo cấu trúc rõ ràng gồm môn học, chủ đề, cấp độ học, loại tài liệu và thẻ liên quan.
4. Hỗ trợ lớp học trực tuyến, thảo luận nhóm, bình luận, ghi chú và hỏi đáp trên nội dung học liệu.
5. Cung cấp dashboard cá nhân cho sinh viên để theo dõi tiến độ học tập.
6. Cho phép giảng viên theo dõi tình hình học tập của lớp và từng sinh viên.
7. Hỗ trợ bài kiểm tra trực tuyến, tự động chấm điểm và phân tích kết quả.
8. Hỗ trợ thanh toán trực tuyến cho các lớp học hoặc khóa học có phí.
9. Hỗ trợ quản trị viên thống kê, tra cứu giao dịch và xuất báo cáo.
10. Mở rộng AI để đề xuất học liệu, khóa học và lộ trình học tập cá nhân hóa.

---

## 3. Phạm vi hệ thống

### 3.1. Phạm vi cốt lõi nên làm trước

Các chức năng nên ưu tiên triển khai trong bản chính:

- Đăng nhập, đăng ký, phân quyền người dùng.
- Quản lý người dùng.
- Quản lý môn học.
- Quản lý chủ đề.
- Quản lý cấp độ học.
- Quản lý loại học liệu.
- Quản lý tag/thẻ học liệu.
- Giảng viên/admin upload học liệu.
- Sinh viên tìm kiếm và xem học liệu.
- Bình luận hoặc hỏi đáp cơ bản trên học liệu.
- Theo dõi số lượt xem/tải tài liệu.
- Dashboard cơ bản cho sinh viên.
- Dashboard cơ bản cho admin/giảng viên.

### 3.2. Phạm vi mở rộng

Các chức năng có thể làm sau nếu còn thời gian:

- Lớp học trực tuyến.
- Chat giữa sinh viên và giảng viên.
- Ghi chú trực tiếp trên học liệu.
- Bài kiểm tra online.
- Tự động chấm điểm.
- Phân tích điểm mạnh/yếu của sinh viên.
- Thanh toán khóa học.
- Quản lý giao dịch.
- Xuất báo cáo doanh thu.
- Gợi ý tài liệu liên quan.
- AI đề xuất lộ trình học tập cá nhân hóa.

---

## 4. Nhóm người dùng trong hệ thống

### 4.1. Guest — Khách chưa đăng nhập

Khách chưa đăng nhập có thể:

- Xem trang giới thiệu hệ thống.
- Xem danh sách tài liệu công khai nếu hệ thống cho phép.
- Tìm kiếm tài liệu công khai.
- Xem thông tin khóa học công khai.
- Đăng ký tài khoản.
- Đăng nhập.

Khách chưa đăng nhập không được:

- Tải tài liệu riêng tư.
- Bình luận.
- Ghi chú.
- Tham gia lớp học.
- Làm bài kiểm tra.
- Thanh toán.
- Theo dõi tiến độ học tập.

---

### 4.2. Student — Sinh viên

Sinh viên có thể:

- Đăng nhập hệ thống.
- Tìm kiếm học liệu.
- Lọc học liệu theo môn học, chủ đề, cấp độ, loại tài liệu, tag.
- Xem chi tiết học liệu.
- Tải tài liệu nếu được phép.
- Xem video/bài giảng nếu có quyền truy cập.
- Tham gia lớp học trực tuyến.
- Bình luận trên học liệu.
- Đặt câu hỏi trên học liệu.
- Trả lời trong thảo luận nếu được phép.
- Ghi chú cá nhân.
- Theo dõi tiến độ học tập.
- Làm bài kiểm tra online.
- Xem điểm và phân tích kết quả.
- Thanh toán khóa học có phí.
- Xem lịch sử thanh toán.
- Nhận gợi ý tài liệu hoặc lộ trình học tập.

Sinh viên không được:

- Tải học liệu lên nếu hệ thống chỉ cho giảng viên/admin upload.
- Xóa học liệu của người khác.
- Chỉnh sửa học liệu của người khác.
- Duyệt học liệu.
- Quản lý người dùng.
- Xem giao dịch của người khác.
- Xem báo cáo doanh thu toàn hệ thống.

---

### 4.3. Lecturer — Giảng viên

Giảng viên có thể:

- Đăng nhập hệ thống.
- Upload học liệu.
- Cập nhật học liệu do mình tạo.
- Xóa hoặc gỡ học liệu do mình tạo nếu chưa bị khóa.
- Phân loại học liệu theo môn học, chủ đề, cấp độ, loại tài liệu, tag.
- Quản lý lớp học do mình phụ trách.
- Xem danh sách sinh viên trong lớp.
- Theo dõi tiến độ học tập của sinh viên.
- Tạo bài kiểm tra.
- Chấm hoặc xem kết quả bài kiểm tra.
- Phản hồi câu hỏi của sinh viên.
- Tham gia thảo luận.
- Gợi ý cải thiện cho sinh viên.
- Xem thống kê học liệu của mình.

Giảng viên không được:

- Quản lý toàn bộ người dùng nếu không có quyền admin.
- Xem giao dịch thanh toán toàn hệ thống.
- Xóa học liệu của giảng viên khác nếu không được cấp quyền.
- Thay đổi cấu hình hệ thống.
- Phân quyền người dùng.

---

### 4.4. Admin — Quản trị viên

Admin có toàn quyền quản lý hệ thống:

- Quản lý tài khoản người dùng.
- Khóa/mở khóa tài khoản.
- Gán vai trò cho người dùng.
- Quản lý môn học.
- Quản lý chủ đề.
- Quản lý cấp độ học.
- Quản lý loại học liệu.
- Quản lý tag.
- Quản lý toàn bộ học liệu.
- Duyệt học liệu nếu hệ thống có cơ chế kiểm duyệt.
- Gỡ học liệu vi phạm.
- Quản lý lớp học.
- Quản lý bài kiểm tra.
- Quản lý giao dịch thanh toán.
- Tra cứu giao dịch theo ngày, tháng, người dùng, trạng thái.
- Xem thống kê doanh thu.
- Xuất báo cáo.
- Xem nhật ký hoạt động.
- Cấu hình hệ thống.
- Quản lý nội dung bị báo cáo.

---

## 5. Phân quyền tổng quát

| Chức năng | Guest | Student | Lecturer | Admin |
|---|---:|---:|---:|---:|
| Xem trang chủ | Có | Có | Có | Có |
| Đăng ký | Có | Không | Không | Không |
| Đăng nhập | Có | Có | Có | Có |
| Xem học liệu công khai | Có | Có | Có | Có |
| Xem học liệu riêng tư | Không | Có nếu có quyền | Có nếu liên quan | Có |
| Tải học liệu | Không/Tùy cấu hình | Có nếu được phép | Có | Có |
| Upload học liệu | Không | Không | Có | Có |
| Sửa học liệu | Không | Không | Học liệu của mình | Có |
| Xóa học liệu | Không | Không | Học liệu của mình | Có |
| Bình luận | Không | Có | Có | Có |
| Ghi chú cá nhân | Không | Có | Có | Có |
| Làm bài kiểm tra | Không | Có | Không | Có thể test |
| Tạo bài kiểm tra | Không | Không | Có | Có |
| Thanh toán | Không | Có | Không | Có thể kiểm tra |
| Xem lịch sử thanh toán cá nhân | Không | Có | Không | Có |
| Xem toàn bộ giao dịch | Không | Không | Không | Có |
| Quản lý người dùng | Không | Không | Không | Có |
| Xuất báo cáo | Không | Không | Có giới hạn | Có |
---

## 6. Ánh xạ Cấu trúc Dữ liệu (ERD Mapping)

Để triển khai các chức năng trên, hệ thống sử dụng các bảng dữ liệu sau (dựa trên thiết kế ERD):

- **Người dùng:** Bảng `User` (lưu chung), `Student` (thông tin sinh viên), `Lecturer` (thông tin giảng viên). Phân quyền qua `RoleEnum`.
- **Học liệu & Phân loại:**
  - `Subject`, `Topic`, `ResourceType`, `ResourceTag`.
  - `Resource`: Lưu thông tin file học liệu, liên kết với các bảng phân loại trên.
  - `ResourceRelation`: Lưu quan hệ giữa các học liệu (phục vụ tính năng gợi ý).
- **Tương tác & Lớp học:**
  - `Course`: Khóa học/lớp học. Sinh viên tham gia qua bảng `Enrollment`.
  - `ForumCategory`, `ForumThread`, `ForumPost`: Diễn đàn thảo luận.
  - `ChatRoom`, `ChatMessage`, `ChatParticipant`: Chat trực tuyến.
  - `Interaction`, `InteractionReply`: Ghi chú, bình luận trực tiếp trên tọa độ tài liệu.
- **Tiến độ & Đánh giá:**
  - `LearningLog`: Lưu thời gian bắt đầu/kết thúc xem tài liệu.
  - `Quiz`, `Question`, `AnswerOption`: Đề test và câu hỏi.
  - `QuizAttempt`, `StudentAnswer`: Lịch sử làm bài.
  - `LearningAnalysis`: Kết quả phân tích điểm mạnh/yếu bằng AI.
- **Thanh toán:** `Payment` lưu các giao dịch mua khóa học.
- **AI Lộ trình:** `LearningPath`, `LearningPathItem`.

## 7. Các trường hợp ngoại lệ cần xử lý (Edge Cases)

1. **Ràng buộc toàn vẹn:** Không cho xóa `Subject`, `Course` nếu đang có sinh viên theo học hoặc có tài nguyên liên kết. Thay vào đó dùng cơ chế Soft Delete hoặc khóa (Deactivate).
2. **Quyền sở hữu:** Giảng viên chỉ được chỉnh sửa/xóa `Resource` do chính mình tạo (check `created_by`).
3. **Bảo mật truy cập:** Các API tải/xem tài liệu cần check quyền `Enrollment` nếu tài liệu đó thuộc `Course` có thu phí.
4. **Đồng bộ thanh toán:** Cần có cơ chế đối soát (cron job) trạng thái `Payment` trong trường hợp Webhook từ cổng thanh toán (MoMo/VNPay) bị lỗi mạng.

## 8. Quy chuẩn Code (Development Rules)

Khi AI sinh code cho bất kỳ chức năng nào, phải tuân thủ nghiêm ngặt `rule.md`:
- **Luồng:** DTO -> Controller -> Service -> Repository.
- **Response:** Mọi API trả về phải bọc trong `ResResponse<T>`.
- **Lỗi:** Bắt buộc ném Custom Exception (`IdInvalidException`, `PermissionException`, v.v.) từ Service lên Controller để `GlobalException` xử lý. Không try-catch trả lỗi tùy tiện ở Controller.
- **Repository:** Dùng Hibernate Session và Criteria API cho các API list có filter/pagination. Mặc định dùng `@Repository` và `@Transactional`.