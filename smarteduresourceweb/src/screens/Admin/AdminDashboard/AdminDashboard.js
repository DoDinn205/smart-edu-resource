import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import AdminReport from "../AdminReport/AdminReport";
import "../Admin.css";

const AdminDashboard = () => {
    const [user] = useContext(MyUserContext);
    const [loading, setLoading] = useState(true);
    const [stats, setStats] = useState(null);
    const nav = useNavigate();

    useEffect(() => {
        if (!user || user.role !== "ADMIN") { nav('/login'); return; }

        const loadDashboard = async () => {
            try {
                setLoading(true);
                let res = await authApis().get(endpoints['admin-dashboard']);
                setStats(res.data.data);
            } catch (ex) {
                console.error(ex);
                setStats({
                    totalStudents: 0, totalLecturers: 0,
                    totalCourses: 0, totalResources: 0,
                    pendingLecturers: 0
                });
            } finally {
                setLoading(false);
            }
        };
        loadDashboard();
    }, [user, nav]);

    if (loading) return <MySpinner />;

    const cards = [
        {
            icon: "bi-people",
            value: stats?.totalStudents || 0,
            label: "Sinh viên"
        }, {
            icon: "bi-person-badge",
            value: stats?.totalLecturers || 0,
            label: "Giảng viên"
        }, {
            icon: "bi-journal-bookmark",
            value: stats?.totalCourses || 0,
            label: "Khóa học"
        }, {
            icon: "bi-file-earmark-text",
            value: stats?.totalResources || 0,
            label: "Học liệu"
        }, {
            icon: "bi-hourglass-split",
            value: stats?.pendingLecturers || 0,
            label: "Chờ duyệt"
        },
    ];

    return (
        <>
            <h4 className="mb-4">Dashboard Quản trị</h4>
            <div className="admin-dashboard-stats">
                {cards.map((c, i) => (
                    <div key={i} className="stat-card">
                        <div className="stat-icon"><i className={`bi ${c.icon}`}></i></div>
                        <div className="stat-value">{c.value}</div>
                        <div className="stat-label">{c.label}</div>
                    </div>
                ))}
            </div>

            <AdminReport />
        </>
    );
}

export default AdminDashboard;
