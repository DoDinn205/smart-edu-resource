import { useContext, useEffect, useState } from "react";
import { Col, ListGroup, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import { authApis, endpoints } from "../../configs/Apis";
import MySpinner from "../../components/common/MySpinner";
import "./Lecturer.css";

const LecturerDashboard = () => {
    const [user] = useContext(MyUserContext);
    const [loading, setLoading] = useState(true);
    const [stats, setStats] = useState(null);
    const nav = useNavigate();

    useEffect(() => {
        if (!user || (user.role !== "LECTURER" && user.role !== "ADMIN")) {
            nav('/login'); return;
        }

        const loadDashboard = async () => {
            try {
                setLoading(true);
                let res = await authApis().get(endpoints['lecturer-dashboard']);
                console.log(res.data.data)
                setStats(res.data.data);
            } catch (ex) {
                console.error(ex);
                setStats({ totalCourses: 0, totalResources: 0, totalQuizzes: 0, totalStudents: 0 });
            } finally {
                setLoading(false);
            }
        };
        loadDashboard();
    }, [user, nav]);

    if (loading) return <MySpinner />;

    const cards = [
        { icon: "bi-journal-bookmark", value: stats?.totalCourses || 0, label: "Khóa học" },
        { icon: "bi-file-earmark-text", value: stats?.totalResources || 0, label: "Học liệu" },
        { icon: "bi-pencil-square", value: stats?.totalQuizzes || 0, label: "Bài quiz" },
        { icon: "bi-people", value: stats?.totalStudents || 0, label: "Học viên" },
    ];

    return (
        <>
            <h4 className="mb-4">Dashboard Giảng viên</h4>
            <div className="lecturer-stats">
                {cards.map((c, i) => (
                    <div key={i} className="lecturer-stat-card">
                        <div className="stat-icon"><i className={`bi ${c.icon}`}></i></div>
                        <div className="stat-value">{c.value}</div>
                        <div className="stat-label">{c.label}</div>
                    </div>
                ))}
            </div>

            <Row className="g-4">
                <Col lg={6}>
                    <div className="lecturer-panel">
                        <div className="panel-header">Truy cập nhanh</div>
                        <ListGroup variant="flush">
                            <ListGroup.Item action onClick={() => nav('/lecturer/courses')}>
                                <i className="bi bi-journal-bookmark me-2"></i> Quản lý Khóa học
                            </ListGroup.Item>
                            <ListGroup.Item action onClick={() => nav('/lecturer/resources')}>
                                <i className="bi bi-file-earmark-text me-2"></i> Quản lý Học liệu
                            </ListGroup.Item>
                            <ListGroup.Item action onClick={() => nav('/lecturer/quizzes')}>
                                <i className="bi bi-question-circle me-2"></i> Ngân hàng Quiz
                            </ListGroup.Item>
                            <ListGroup.Item action onClick={() => nav('/lecturer/chat')}>
                                <i className="bi bi-chat-dots me-2"></i> Phòng Chat
                            </ListGroup.Item>
                        </ListGroup>
                    </div>
                </Col>
            </Row>
        </>
    );
}

export default LecturerDashboard;
