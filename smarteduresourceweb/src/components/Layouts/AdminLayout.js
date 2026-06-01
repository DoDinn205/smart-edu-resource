import DashboardLayout from "../DashboardLayout/DashboardLayout";

const AdminLayout = ({ children }) => {
    const adminMenus = [
        { title: "Dashboard", path: "/admin/dashboard", icon: "bi-speedometer2" },
        { title: "Sinh viên", path: "/admin/students", icon: "bi-people" },
        { title: "Giảng viên", path: "/admin/lecturers", icon: "bi-person-badge" },
        { title: "Danh mục", path: "/admin/categories", icon: "bi-tags" },
        { title: "Giao dịch", path: "/admin/payments", icon: "bi-wallet2" },
        { title: "Diễn đàn", path: "/admin/forum", icon: "bi-chat-square-text" },
    ];

    return (
        <DashboardLayout menus={adminMenus} brandTitle="Admin Panel" brandIcon="🛡️">
            {children}
        </DashboardLayout>
    );
};

export default AdminLayout;
