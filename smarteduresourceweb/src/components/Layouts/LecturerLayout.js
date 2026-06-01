import DashboardLayout from "../DashboardLayout/DashboardLayout";

const LecturerLayout = ({ children }) => {
    const lecturerMenus = [
        { title: "Dashboard", path: "/lecturer/dashboard", icon: "bi-graph-up" },
        { title: "Khóa học", path: "/lecturer/courses", icon: "bi-journal-bookmark" },
        { title: "Học liệu", path: "/lecturer/resources", icon: "bi-file-earmark-text" },
        { title: "Ngân hàng Quiz", path: "/lecturer/quizzes", icon: "bi-question-circle" },
        { title: "Kết quả học tập", path: "/lecturer/results", icon: "bi-bar-chart-line" },
        { title: "Phòng Chat", path: "/lecturer/chat", icon: "bi-chat-dots" },
    ];

    return (
        <DashboardLayout menus={lecturerMenus} brandTitle="Lecturer Portal" brandIcon="🎓">
            <div className="lecturer-theme">
                {children}
            </div>
        </DashboardLayout>
    );
};

export default LecturerLayout;
