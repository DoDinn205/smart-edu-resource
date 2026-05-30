export const SUBJECTS = [
    { id: 1, code: "CS101", name: "Lập trình Java" },
    { id: 2, code: "CS102", name: "Cấu trúc dữ liệu" },
    { id: 3, code: "CS201", name: "Lập trình Web" },
    { id: 4, code: "CS202", name: "Cơ sở dữ liệu" },
    { id: 5, code: "CS301", name: "Trí tuệ nhân tạo" },
    { id: 6, code: "CS103", name: "Mạng máy tính" },
    { id: 7, code: "CS104", name: "Hệ điều hành" },
    { id: 8, code: "CS302", name: "An toàn thông tin" },
];

export const TOPICS = [
    { id: 1, name: "OOP" }, { id: 2, name: "Thuật toán" }, { id: 3, name: "Frontend" },
    { id: 4, name: "SQL" }, { id: 5, name: "Machine Learning" }, { id: 6, name: "Networking" },
    { id: 7, name: "Linux" }, { id: 8, name: "Security" }, { id: 9, name: "Backend" },
];

export const RESOURCE_TYPES = [
    { id: 1, name: "Giáo trình" }, { id: 2, name: "Slide" }, { id: 3, name: "Video" },
    { id: 4, name: "Tài liệu tham khảo" }, { id: 5, name: "Bài tập" },
];

export const RESOURCES = [
    { id: 1, title: "Giáo trình Lập trình Java cơ bản", description: "Tài liệu nền tảng về ngôn ngữ lập trình Java, bao gồm OOP, Collections, Exception Handling.", fileUrl: "#", thumbnailUrl: "/thumbnails/thumb_java.png", format: "PDF", fileSize: 15200, level: "BEGINNER", pageCount: 320, viewCount: 2400, createdAt: "2025-09-15", subjects: [{ id: 1, name: "Lập trình Java" }], topics: [{ id: 1, name: "OOP" }], types: [{ id: 1, name: "Giáo trình" }], tags: [{ id: 1, name: "Java" }, { id: 2, name: "Cơ bản" }], uploadBy: { id: 2, fullName: "TS. Nguyễn Văn An", role: "LECTURER" }, relatedResources: [{ id: 9, title: "Bài tập thực hành Spring Boot", format: "PDF", level: "INTERMEDIATE" }] },
    { id: 2, title: "Slide Cấu trúc dữ liệu và Giải thuật nâng cao", description: "Slide bài giảng về cây đỏ đen, đồ thị và các thuật toán tìm kiếm tối ưu cho sinh viên.", fileUrl: "#", thumbnailUrl: "/thumbnails/thumb_dsa.png", format: "PPTX", fileSize: 8500, level: "INTERMEDIATE", pageCount: 150, viewCount: 3200, createdAt: "2025-10-20", subjects: [{ id: 2, name: "Cấu trúc dữ liệu" }], topics: [{ id: 2, name: "Thuật toán" }], types: [{ id: 2, name: "Slide" }], tags: [{ id: 3, name: "DSA" }], uploadBy: { id: 3, fullName: "PGS. Trần Thị Bình", role: "LECTURER" }, relatedResources: [] },
    { id: 3, title: "Cơ học Lượng tử cơ bản – Bài 1", description: "Giới thiệu về phương trình Schrödinger và các khái niệm cơ bản trong cơ học lượng tử.", fileUrl: "#", thumbnailUrl: "/thumbnails/thumb_physics.png", format: "MP4", fileSize: 256000, level: "INTERMEDIATE", pageCount: null, viewCount: 1800, createdAt: "2025-11-05", subjects: [{ id: 3, name: "Vật lý" }], topics: [{ id: 3, name: "Frontend" }], types: [{ id: 3, name: "Video" }], tags: [{ id: 4, name: "Physics" }, { id: 5, name: "Quantum" }], uploadBy: { id: 4, fullName: "ThS. Lê Văn Cường", role: "LECTURER" }, relatedResources: [] },
    { id: 4, title: "Bài giảng Cơ sở dữ liệu nâng cao", description: "Tối ưu truy vấn, indexing, stored procedures, transaction management.", fileUrl: "#", thumbnailUrl: "/thumbnails/thumb_database.png", format: "PDF", fileSize: 12800, level: "ADVANCED", pageCount: 280, viewCount: 1560, createdAt: "2025-08-10", subjects: [{ id: 4, name: "Cơ sở dữ liệu" }], topics: [{ id: 4, name: "SQL" }], types: [{ id: 1, name: "Giáo trình" }], tags: [{ id: 6, name: "SQL" }, { id: 7, name: "Database" }], uploadBy: { id: 5, fullName: "TS. Phạm Văn Dũng", role: "LECTURER" }, relatedResources: [] },
    { id: 5, title: "Cơ sở Toán học cho Trí tuệ Nhân tạo", description: "Tài liệu tổng hợp các kiến thức nền tảng xác suất thống kê và đại số tuyến tính.", fileUrl: "#", thumbnailUrl: "/thumbnails/thumb_math.png", format: "PDF", fileSize: 18500, level: "ADVANCED", pageCount: 420, viewCount: 4100, createdAt: "2025-12-05", subjects: [{ id: 5, name: "Toán học" }], topics: [{ id: 5, name: "Machine Learning" }], types: [{ id: 4, name: "Tài liệu tham khảo" }], tags: [{ id: 8, name: "ML" }, { id: 9, name: "Python" }], uploadBy: { id: 6, fullName: "GS. Hoàng Thị Mai", role: "LECTURER" }, relatedResources: [] },
    { id: 6, title: "Slide Mạng máy tính", description: "Mô hình OSI, TCP/IP, giao thức DNS, HTTP, DHCP.", fileUrl: "#", thumbnailUrl: "/thumbnails/thumb_network.png", format: "PPTX", fileSize: 6200, level: "BEGINNER", pageCount: 98, viewCount: 980, createdAt: "2025-07-22", subjects: [{ id: 6, name: "KH Máy tính" }], topics: [{ id: 6, name: "Networking" }], types: [{ id: 2, name: "Slide" }], tags: [{ id: 10, name: "Network" }], uploadBy: { id: 7, fullName: "TS. Đỗ Văn Phúc", role: "LECTURER" }, relatedResources: [] },
    { id: 7, title: "Giáo trình Hệ điều hành Linux", description: "Quản trị hệ điều hành Linux, shell scripting, file system.", fileUrl: "#", thumbnailUrl: "/thumbnails/thumb_linux.png", format: "PDF", fileSize: 14000, level: "INTERMEDIATE", pageCount: 350, viewCount: 2150, createdAt: "2025-06-15", subjects: [{ id: 7, name: "Hệ điều hành" }], topics: [{ id: 7, name: "Linux" }], types: [{ id: 1, name: "Giáo trình" }], tags: [{ id: 11, name: "Linux" }], uploadBy: { id: 2, fullName: "TS. Nguyễn Văn An", role: "LECTURER" }, relatedResources: [] },
    { id: 8, title: "Video An toàn thông tin", description: "Bảo mật mạng, mã hóa, phòng chống tấn công.", fileUrl: "#", thumbnailUrl: "/thumbnails/thumb_security.png", format: "MP4", fileSize: 198000, level: "BEGINNER", pageCount: null, viewCount: 760, createdAt: "2025-11-30", subjects: [{ id: 8, name: "An toàn thông tin" }], topics: [{ id: 8, name: "Security" }], types: [{ id: 3, name: "Video" }], tags: [{ id: 12, name: "Security" }], uploadBy: { id: 4, fullName: "ThS. Lê Văn Cường", role: "LECTURER" }, relatedResources: [] },
    { id: 9, title: "Đề thi Cuối kỳ Giải tích 1 – K65", description: "Bao gồm 5 câu tự luận có kèm đáp án chi tiết và thang điểm chấm của giảng viên.", fileUrl: "#", thumbnailUrl: "/thumbnails/thumb_exam.png", format: "PDF", fileSize: 5400, level: "INTERMEDIATE", pageCount: 85, viewCount: 5100, createdAt: "2026-01-15", subjects: [{ id: 1, name: "Toán học" }], topics: [{ id: 9, name: "Backend" }], types: [{ id: 5, name: "Đề thi" }], tags: [{ id: 1, name: "Toán" }, { id: 13, name: "Đề thi" }], uploadBy: { id: 2, fullName: "TS. Nguyễn Văn An", role: "LECTURER" }, relatedResources: [] },
];

export const COURSES = [
    {
        id: 1, name: "Lập trình Java Spring Boot", description: "Xây dựng REST API, Spring Security, JPA/Hibernate từ cơ bản đến nâng cao.",
        longDescription: "Khóa học được thiết kế để đưa bạn từ một lập trình viên Java có bản trở thành một Senior Backend Architect thực thụ. Chúng tôi không chỉ dạy cú pháp; chúng tôi dạy cách tư duy hệ thống, cách xử lý hàng triệu request đồng thời và cách xây dựng hạ tầng bền vững.",
        isPaid: false, price: 0, originalPrice: 0, targetLevel: "INTERMEDIATE",
        startDate: "2026-01-15", endDate: "2026-06-15", lastUpdated: "01/2026", language: "Tiếng Việt",
        lecturerUser: { id: 2, fullName: "TS. Nguyễn Văn An", title: "Senior Backend Architect", experience: "15 năm kinh nghiệm" },
        subject: { id: 1, name: "Lập trình Java" }, enrollmentCount: 245,
        thumbnailUrl: "/thumbnails/thumb_course_java.png",
        highlights: ["Xây dựng REST API chuẩn doanh nghiệp", "Bảo mật với Spring Security & JWT", "Tích hợp JPA/Hibernate & PostgreSQL", "Deploy lên Docker & AWS"],
        totalLessons: 145, totalChapters: 12, totalHours: 45,
        sections: [
            { id: 1, title: "Tổng quan về Spring Boot 3.x & Hệ sinh thái Java hiện đại", lessons: 8, duration: "1h 45p", expanded: true, items: [
                { id: 1, title: "Cài đặt môi trường phát triển (IntelliJ, Docker)", type: "video", duration: "15:20", isFree: true },
                { id: 2, title: "Spring Core & Dependency Injection chuyên sâu", type: "doc", duration: "", isFree: false },
            ]},
            { id: 2, title: "Thiết kế RESTful APIs theo chuẩn doanh nghiệp", lessons: 15, duration: "4h 20p", expanded: false, items: [
                { id: 3, title: "HTTP Methods & Status Codes best practices", type: "video", duration: "22:10", isFree: false },
                { id: 4, title: "Request Validation & Error Handling", type: "video", duration: "18:45", isFree: false },
            ]},
            { id: 3, title: "Data Access với Spring Data JPA & PostgreSQL", lessons: 12, duration: "3h 15p", expanded: false, items: [] },
        ],
    },
    {
        id: 2, name: "Phát triển Web với React.js", description: "Ứng dụng web hiện đại với React.js, hooks, context, routing.",
        longDescription: "Khóa học toàn diện về React.js từ cơ bản đến nâng cao. Bạn sẽ học cách xây dựng Single Page Application hiệu suất cao với các kỹ thuật tối ưu hóa và state management hiện đại.",
        isPaid: true, price: 790000, originalPrice: 1200000, targetLevel: "INTERMEDIATE",
        startDate: "2026-02-01", endDate: "2026-07-01", lastUpdated: "02/2026", language: "Tiếng Việt",
        lecturerUser: { id: 4, fullName: "ThS. Lê Văn Cường", title: "Frontend Tech Lead", experience: "8 năm kinh nghiệm" },
        subject: { id: 3, name: "Lập trình Web" }, enrollmentCount: 189,
        thumbnailUrl: "/thumbnails/thumb_course_react.png",
        highlights: ["Nắm vững React Hooks (useState, useEffect, useContext)", "State management với Redux Toolkit", "Xây dựng UI với TailwindCSS", "Tích hợp REST API & React Query"],
        totalLessons: 98, totalChapters: 8, totalHours: 32,
        sections: [
            { id: 1, title: "Nền tảng React & JSX", lessons: 10, duration: "2h 30p", expanded: true, items: [
                { id: 1, title: "Giới thiệu React và Virtual DOM", type: "video", duration: "12:00", isFree: true },
                { id: 2, title: "JSX syntax và component lifecycle", type: "video", duration: "20:30", isFree: false },
            ]},
            { id: 2, title: "React Hooks nâng cao", lessons: 14, duration: "3h 45p", expanded: false, items: [] },
            { id: 3, title: "State Management với Redux Toolkit", lessons: 12, duration: "3h 00p", expanded: false, items: [] },
        ],
    },
    {
        id: 3, name: "Machine Learning cơ bản", description: "Python, Scikit-learn, TensorFlow — từ lý thuyết đến ứng dụng.",
        longDescription: "Khóa học Machine Learning thực chiến với Python. Từ nền tảng toán học đến triển khai mô hình production, bạn sẽ được hướng dẫn bởi chuyên gia AI hàng đầu.",
        isPaid: true, price: 1200000, originalPrice: 1800000, targetLevel: "ADVANCED",
        startDate: "2026-03-01", endDate: "2026-08-01", lastUpdated: "03/2026", language: "Tiếng Việt",
        lecturerUser: { id: 6, fullName: "GS. Hoàng Thị Mai", title: "AI Research Scientist", experience: "20 năm kinh nghiệm" },
        subject: { id: 5, name: "Trí tuệ nhân tạo" }, enrollmentCount: 312,
        thumbnailUrl: "/thumbnails/thumb_course_ml.png",
        highlights: ["Nắm vững các thuật toán ML cổ điển", "Xây dựng mô hình Deep Learning với TensorFlow", "Xử lý dữ liệu thực tế với Pandas & NumPy", "Deploy AI model lên cloud"],
        totalLessons: 120, totalChapters: 10, totalHours: 50,
        sections: [
            { id: 1, title: "Nền tảng toán học cho ML", lessons: 14, duration: "4h 00p", expanded: true, items: [
                { id: 1, title: "Linear Algebra & Matrix Operations", type: "video", duration: "25:00", isFree: true },
                { id: 2, title: "Probability & Statistics cơ bản", type: "video", duration: "30:00", isFree: false },
            ]},
            { id: 2, title: "Supervised Learning", lessons: 20, duration: "6h 30p", expanded: false, items: [] },
            { id: 3, title: "Neural Networks & Deep Learning", lessons: 18, duration: "7h 00p", expanded: false, items: [] },
        ],
    },
    {
        id: 4, name: "Cơ sở dữ liệu quan hệ", description: "MySQL, PostgreSQL, thiết kế schema, SQL optimization.",
        longDescription: "Khóa học toàn diện về Database từ cơ bản đến nâng cao. Học cách thiết kế schema chuẩn, tối ưu hóa truy vấn và quản lý dữ liệu lớn.",
        isPaid: false, price: 0, originalPrice: 0, targetLevel: "BEGINNER",
        startDate: "2026-01-20", endDate: "2026-05-20", lastUpdated: "01/2026", language: "Tiếng Việt",
        lecturerUser: { id: 5, fullName: "TS. Phạm Văn Dũng", title: "Database Architect", experience: "12 năm kinh nghiệm" },
        subject: { id: 4, name: "Cơ sở dữ liệu" }, enrollmentCount: 156,
        thumbnailUrl: "/thumbnails/thumb_course_db.png",
        highlights: ["Thiết kế ERD và chuẩn hóa dữ liệu", "SQL từ cơ bản đến nâng cao", "Tối ưu hóa query với Index", "Stored Procedures & Triggers"],
        totalLessons: 80, totalChapters: 7, totalHours: 28,
        sections: [
            { id: 1, title: "Giới thiệu CSDL & Mô hình quan hệ", lessons: 8, duration: "2h 00p", expanded: true, items: [
                { id: 1, title: "Khái niệm CSDL và DBMS", type: "video", duration: "18:00", isFree: true },
                { id: 2, title: "Thiết kế ERD thực hành", type: "video", duration: "25:00", isFree: true },
            ]},
            { id: 2, title: "SQL Cơ bản & Nâng cao", lessons: 20, duration: "5h 00p", expanded: false, items: [] },
            { id: 3, title: "Tối ưu hiệu suất Database", lessons: 12, duration: "3h 30p", expanded: false, items: [] },
        ],
    },
    {
        id: 5, name: "An toàn thông tin mạng", description: "Penetration testing, bảo mật ứng dụng web, mã hóa dữ liệu.",
        longDescription: "Khóa học Cybersecurity thực chiến, từ ethical hacking đến bảo mật ứng dụng web. Học cách tư duy như hacker để bảo vệ hệ thống tốt hơn.",
        isPaid: true, price: 950000, originalPrice: 1500000, targetLevel: "ADVANCED",
        startDate: "2026-04-01", endDate: "2026-09-01", lastUpdated: "04/2026", language: "Tiếng Việt",
        lecturerUser: { id: 4, fullName: "ThS. Lê Văn Cường", title: "Cybersecurity Expert", experience: "10 năm kinh nghiệm" },
        subject: { id: 8, name: "An toàn thông tin" }, enrollmentCount: 98,
        thumbnailUrl: "/thumbnails/thumb_course_security.png",
        highlights: ["Penetration Testing với Kali Linux", "Bảo mật ứng dụng web (OWASP Top 10)", "Mã hóa dữ liệu & PKI", "Incident Response & Forensics"],
        totalLessons: 90, totalChapters: 8, totalHours: 38,
        sections: [
            { id: 1, title: "Nền tảng Networking & Hacking mindset", lessons: 10, duration: "3h 00p", expanded: true, items: [
                { id: 1, title: "TCP/IP & Protocol Analysis", type: "video", duration: "22:00", isFree: true },
                { id: 2, title: "Kali Linux setup & basic tools", type: "video", duration: "18:30", isFree: false },
            ]},
            { id: 2, title: "Web Application Security", lessons: 15, duration: "4h 30p", expanded: false, items: [] },
            { id: 3, title: "Network Penetration Testing", lessons: 14, duration: "4h 00p", expanded: false, items: [] },
        ],
    },
    {
        id: 6, name: "Nhập môn Python", description: "Cú pháp, hàm, OOP, file handling với ngôn ngữ Python.",
        longDescription: "Khóa học Python toàn diện cho người mới bắt đầu. Từ cú pháp cơ bản đến lập trình hướng đối tượng và các ứng dụng thực tế.",
        isPaid: false, price: 0, originalPrice: 0, targetLevel: "BEGINNER",
        startDate: "2026-02-15", endDate: "2026-06-15", lastUpdated: "02/2026", language: "Tiếng Việt",
        lecturerUser: { id: 3, fullName: "PGS. Trần Thị Bình", title: "Python Developer & Educator", experience: "11 năm kinh nghiệm" },
        subject: { id: 5, name: "Trí tuệ nhân tạo" }, enrollmentCount: 420,
        thumbnailUrl: "/thumbnails/thumb_course_python.png",
        highlights: ["Nắm vững cú pháp Python", "Lập trình hướng đối tượng (OOP)", "Xử lý file & dữ liệu với Python", "Thư viện phổ biến: NumPy, Pandas"],
        totalLessons: 75, totalChapters: 6, totalHours: 22,
        sections: [
            { id: 1, title: "Cú pháp Python cơ bản", lessons: 12, duration: "3h 00p", expanded: true, items: [
                { id: 1, title: "Cài đặt Python & IDE", type: "video", duration: "10:00", isFree: true },
                { id: 2, title: "Biến, kiểu dữ liệu, toán tử", type: "video", duration: "20:00", isFree: true },
            ]},
            { id: 2, title: "Cấu trúc điều khiển & Hàm", lessons: 14, duration: "3h 30p", expanded: false, items: [] },
            { id: 3, title: "Lập trình hướng đối tượng", lessons: 16, duration: "4h 00p", expanded: false, items: [] },
        ],
    },
];

export const FORUM_CATEGORIES = [
    { id: 1, name: "Thảo luận chung", description: "Trao đổi các vấn đề liên quan đến học tập", threadCount: 24 },
    { id: 2, name: "Hỏi đáp lập trình", description: "Đặt câu hỏi về lập trình, debug, thuật toán", threadCount: 56 },
    { id: 3, name: "Chia sẻ tài liệu", description: "Chia sẻ tài liệu, link hữu ích", threadCount: 18 },
    { id: 4, name: "Tuyển dụng & Thực tập", description: "Thông tin tuyển dụng, cơ hội thực tập", threadCount: 9 },
];

export const FORUM_THREADS = [
    { id: 1, title: "Cách tối ưu truy vấn SQL khi bảng có hàng triệu dòng?", categoryId: 2, author: { fullName: "Nguyễn Minh Tuấn" }, createdAt: "2026-05-20", replyCount: 12, isPinned: true },
    { id: 2, title: "So sánh React vs Vue cho dự án mới", categoryId: 2, author: { fullName: "Trần Thu Hà" }, createdAt: "2026-05-22", replyCount: 8, isPinned: false },
    { id: 3, title: "Lộ trình học Java Backend 2026", categoryId: 1, author: { fullName: "Phạm Đức Anh" }, createdAt: "2026-05-25", replyCount: 15, isPinned: false },
    { id: 4, title: "Tổng hợp tài liệu Machine Learning hay nhất", categoryId: 3, author: { fullName: "Lê Thị Hương" }, createdAt: "2026-05-18", replyCount: 6, isPinned: false },
];

export const CHAT_ROOMS = [
    { id: 1, name: "Lớp Java Spring Boot", type: "GROUP", lastMessage: "Bài tập tuần này nộp trước thứ 6 nhé", lastMessageAt: "15:30" },
    { id: 2, name: "TS. Nguyễn Văn An", type: "PRIVATE", lastMessage: "Em cảm ơn thầy ạ", lastMessageAt: "10:20" },
    { id: 3, name: "Nhóm đồ án", type: "GROUP", lastMessage: "Mình đã push code lên repo rồi", lastMessageAt: "Hôm qua" },
];

export const PAYMENTS = [
    { id: 1, courseName: "Phát triển Web với React.js", amount: 590000, method: "MOMO", status: "SUCCESS", createdAt: "2026-03-10" },
    { id: 2, courseName: "Machine Learning cơ bản", amount: 790000, method: "VNPAY", status: "SUCCESS", createdAt: "2026-04-02" },
    { id: 3, courseName: "An toàn thông tin mạng", amount: 690000, method: "BANKING", status: "PENDING", createdAt: "2026-05-28" },
];

export const QUIZZES = [
    { id: 1, title: "Kiểm tra Java OOP", courseId: 1, courseName: "Lập trình Java Spring Boot", duration: 30, questionCount: 15, status: "OPEN", bestScore: null },
    { id: 2, title: "Quiz SQL cơ bản", courseId: 4, courseName: "Cơ sở dữ liệu quan hệ", duration: 20, questionCount: 10, status: "COMPLETED", bestScore: 8 },
    { id: 3, title: "Bài test React Hooks", courseId: 2, courseName: "Phát triển Web với React.js", duration: 25, questionCount: 12, status: "OPEN", bestScore: null },
];

export const QUIZ_QUESTIONS = [
    { id: 1, content: "Trong Java, từ khóa nào dùng để kế thừa một class?", type: "SINGLE", options: [
        { id: 1, content: "implements", isCorrect: false },
        { id: 2, content: "extends", isCorrect: true },
        { id: 3, content: "inherits", isCorrect: false },
        { id: 4, content: "super", isCorrect: false },
    ]},
    { id: 2, content: "Phương thức nào trong interface List trả về số phần tử?", type: "SINGLE", options: [
        { id: 5, content: "length()", isCorrect: false },
        { id: 6, content: "count()", isCorrect: false },
        { id: 7, content: "size()", isCorrect: true },
        { id: 8, content: "total()", isCorrect: false },
    ]},
    { id: 3, content: "Đâu là access modifier mặc định trong Java?", type: "SINGLE", options: [
        { id: 9, content: "public", isCorrect: false },
        { id: 10, content: "private", isCorrect: false },
        { id: 11, content: "protected", isCorrect: false },
        { id: 12, content: "package-private (default)", isCorrect: true },
    ]},
];

export const formatLevel = (level) => {
    switch (level) {
        case "BEGINNER": return "Cơ bản";
        case "INTERMEDIATE": return "Trung bình";
        case "ADVANCED": return "Nâng cao";
        default: return level || "";
    }
};

export const levelVariant = (level) => {
    switch (level) {
        case "BEGINNER": return "success";
        case "INTERMEDIATE": return "warning";
        case "ADVANCED": return "danger";
        default: return "secondary";
    }
};

export const formatFileSize = (kb) => {
    if (!kb) return "";
    if (kb >= 1024) return `${(kb / 1024).toFixed(1)} MB`;
    return `${kb} KB`;
};

export const formatPrice = (price) => {
    if (!price || price === 0) return "Miễn phí";
    return price.toLocaleString('vi-VN') + 'đ';
};

export const formatViewCount = (count) => {
    if (!count) return "0";
    if (count >= 1000) return `${(count / 1000).toFixed(1)}k`;
    return count.toString();
};

export const formatDate = (dateStr) => {
    if (!dateStr) return "";
    const d = new Date(dateStr);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    return `${day}/${month}/${year}`;
};


export const getFormatLabel = (format) => {
    switch (format) {
        case "PDF": return "PDF";
        case "PPTX": return "Slide";
        case "MP4": return "Video";
        case "DOCX": return "Doc";
        default: return format;
    }
};

export const getTypeLabel = (types) => {
    if (!types || types.length === 0) return null;
    return types[0].name;
};

// Mock data theo đúng cấu trúc ResCourseLearnDTO trả về từ GET /api/secure/courses/:id/learn
export const COURSE_LEARN_DATA = {
    1: {
        courseId: 1,
        courseName: "Lập trình Java Spring Boot",
        description: "Xây dựng REST API, Spring Security, JPA/Hibernate từ cơ bản đến nâng cao.",
        targetLevel: "INTERMEDIATE",
        isPaid: false,
        hasAccess: true,
        enrollmentStatus: "ACTIVE",
        lecturerName: "TS. Nguyễn Văn An",
        lecturerTitle: "Senior Backend Architect",
        totalChapters: 3,
        totalLessons: 8,
        chapters: [
            {
                chapterNum: 1,
                chapterTitle: "Chương 1: Tổng quan Spring Boot",
                lessons: [
                    { id: 1, title: "Cài đặt môi trường (IntelliJ, Docker)", chapterNum: 1, lessonNum: 1, isFree: true, itemType: "VIDEO", fileUrl: null, resourceTitle: "Bài 1 - Video giới thiệu", format: "MP4" },
                    { id: 2, title: "Spring Core & Dependency Injection", chapterNum: 1, lessonNum: 2, isFree: false, itemType: "DOCUMENT", fileUrl: null, resourceTitle: "Slide DI", format: "PDF" },
                    { id: 3, title: "Kiểm tra chương 1", chapterNum: 1, lessonNum: 3, isFree: false, itemType: "QUIZ", quizId: 1, quizTitle: "Quiz Chương 1", durationMinutes: 15, questionCount: 10 },
                ]
            },
            {
                chapterNum: 2,
                chapterTitle: "Chương 2: Xây dựng REST API",
                lessons: [
                    { id: 4, title: "HTTP Methods & Status Codes", chapterNum: 2, lessonNum: 1, isFree: false, itemType: "VIDEO", fileUrl: null, resourceTitle: "Video REST", format: "MP4" },
                    { id: 5, title: "Request Validation & Error Handling", chapterNum: 2, lessonNum: 2, isFree: false, itemType: "VIDEO", fileUrl: null, resourceTitle: "Video Validation", format: "MP4" },
                    { id: 6, title: "Tài liệu tham khảo REST API", chapterNum: 2, lessonNum: 3, isFree: false, itemType: "DOCUMENT", fileUrl: null, resourceTitle: "REST Cheatsheet", format: "PDF" },
                ]
            },
            {
                chapterNum: 3,
                chapterTitle: "Chương 3: Spring Security & JWT",
                lessons: [
                    { id: 7, title: "Cấu hình Spring Security", chapterNum: 3, lessonNum: 1, isFree: false, itemType: "VIDEO", fileUrl: null, resourceTitle: "Video Security", format: "MP4" },
                    { id: 8, title: "Quiz tổng kết", chapterNum: 3, lessonNum: 2, isFree: false, itemType: "QUIZ", quizId: 2, quizTitle: "Quiz Tổng kết", durationMinutes: 30, questionCount: 20 },
                ]
            },
        ]
    },
    2: {
        courseId: 2,
        courseName: "Phát triển Web với React.js",
        description: "Ứng dụng web hiện đại với React.js, hooks, context, routing.",
        targetLevel: "INTERMEDIATE",
        isPaid: false,
        hasAccess: true,
        enrollmentStatus: "COMPLETED",
        lecturerName: "PGS. Trần Thị Bình",
        lecturerTitle: "Frontend Engineer",
        totalChapters: 2,
        totalLessons: 4,
        chapters: [
            {
                chapterNum: 1,
                chapterTitle: "Chương 1: React Fundamentals",
                lessons: [
                    { id: 11, title: "JSX & Components", chapterNum: 1, lessonNum: 1, isFree: true, itemType: "VIDEO", fileUrl: null, format: "MP4" },
                    { id: 12, title: "State & Props", chapterNum: 1, lessonNum: 2, isFree: false, itemType: "DOCUMENT", fileUrl: null, format: "PDF" },
                ]
            },
            {
                chapterNum: 2,
                chapterTitle: "Chương 2: Hooks & Context",
                lessons: [
                    { id: 13, title: "useState & useEffect", chapterNum: 2, lessonNum: 1, isFree: false, itemType: "VIDEO", fileUrl: null, format: "MP4" },
                    { id: 14, title: "Quiz Hooks", chapterNum: 2, lessonNum: 2, isFree: false, itemType: "QUIZ", quizId: 3, quizTitle: "Quiz Hooks", durationMinutes: 10, questionCount: 5 },
                ]
            },
        ]
    },
};
export const MY_ENROLLMENTS = [
    {
        id: 101,
        courseId: 1,
        courseName: "Lập trình Java Spring Boot",
        status: "ACTIVE",
        overallProgress: 75,
        totalStudyTime: 2700,
        enrollDate: "2026-01-15",
        studentId: 1,
        studentCode: "SV001",
        user: { id: 10, fullName: "Nguyễn Văn Học Viên", username: "student1" }
    },
    {
        id: 102,
        courseId: 4,
        courseName: "Trí tuệ nhân tạo & Machine Learning",
        status: "ACTIVE",
        overallProgress: 45,
        totalStudyTime: 1260,
        enrollDate: "2026-02-20",
        studentId: 1,
        studentCode: "SV001",
        user: { id: 10, fullName: "Nguyễn Văn Học Viên", username: "student1" }
    },
    {
        id: 103,
        courseId: 6,
        courseName: "An toàn thông tin & Ethical Hacking",
        status: "ACTIVE",
        overallProgress: 90,
        totalStudyTime: 3600,
        enrollDate: "2025-12-01",
        studentId: 1,
        studentCode: "SV001",
        user: { id: 10, fullName: "Nguyễn Văn Học Viên", username: "student1" }
    },
    {
        id: 104,
        courseId: 2,
        courseName: "Phát triển Web với React.js",
        status: "COMPLETED",
        overallProgress: 100,
        totalStudyTime: 5400,
        enrollDate: "2025-09-01",
        studentId: 1,
        studentCode: "SV001",
        user: { id: 10, fullName: "Nguyễn Văn Học Viên", username: "student1" }
    },
];
