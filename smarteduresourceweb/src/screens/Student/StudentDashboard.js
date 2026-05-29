import { useContext, useEffect, useState } from "react";
import { Badge, Col, Container, ListGroup, ProgressBar, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import MySpinner from "../../components/common/MySpinner";

const StudentDashboard = () => {
    const [user] = useContext(MyUserContext);
    const [loading, setLoading] = useState(true);
    const nav = useNavigate();

    useEffect(() => {
        if (!user) { nav('/login'); return; }
        const t = setTimeout(() => setLoading(false), 400);
        return () => clearTimeout(t);
    }, [user, nav]);

    if (loading) return <MySpinner />;

    const stats = [
        { val: "42", lbl: "Tài liệu đã xem" },
        { val: "68h", lbl: "Giờ học" },
        { val: "3", lbl: "Khóa học" },
        { val: "65%", lbl: "Hoàn thành" },
    ];

    const recentResources = [
        { id: 1, title: "Giáo trình Lập trình Java cơ bản", format: "PDF", date: "28/05/2026" },
        { id: 9, title: "Bài tập thực hành Spring Boot", format: "PDF", date: "27/05/2026" },
        { id: 3, title: "Video hướng dẫn React.js", format: "MP4", date: "26/05/2026" },
        { id: 2, title: "Slide Cấu trúc dữ liệu và Giải thuật", format: "PPTX", date: "25/05/2026" },
    ];

    const myCourses = [
        { id: 1, name: "Lập trình Java Spring Boot", progress: 75 },
        { id: 4, name: "Cơ sở dữ liệu quan hệ", progress: 45 },
        { id: 6, name: "Nhập môn Python", progress: 90 },
    ];

    const quizzes = [
        { id: 1, name: "Kiểm tra Java OOP", course: "Lập trình Java Spring Boot", due: "01/06/2026" },
        { id: 2, name: "Quiz SQL cơ bản", course: "Cơ sở dữ liệu quan hệ", due: "05/06/2026" },
    ];

    return (
        <Container className="py-4">
            <div className="welcome-banner">
                <h3>Xin chào, {user ? user.fullName : "Sinh viên"}</h3>
                <p>Tiếp tục hành trình học tập của bạn</p>
            </div>

            <Row className="g-3 mb-4">
                {stats.map((s, i) => (
                    <Col key={i} xs={6} md={3}>
                        <div className="dash-stat">
                            <div className="val">{s.val}</div>
                            <div className="lbl">{s.lbl}</div>
                        </div>
                    </Col>
                ))}
            </Row>

            <Row className="g-4">
                <Col lg={8}>
                    <div className="panel-card mb-4">
                        <div className="panel-head">Tài liệu xem gần đây</div>
                        <ListGroup variant="flush">
                            {recentResources.map(r => (
                                <ListGroup.Item key={r.id} action onClick={() => nav(`/resources/${r.id}`)} className="d-flex justify-content-between" style={{ fontSize: '0.88rem' }}>
                                    <span><Badge bg="light" text="dark" className="me-2">{r.format}</Badge>{r.title}</span>
                                    <small className="text-muted">{r.date}</small>
                                </ListGroup.Item>
                            ))}
                        </ListGroup>
                    </div>
                    <div className="panel-card">
                        <div className="panel-head">Tiến độ khóa học</div>
                        <ListGroup variant="flush">
                            {myCourses.map(c => (
                                <ListGroup.Item key={c.id} action onClick={() => nav(`/courses/${c.id}`)} style={{ fontSize: '0.88rem' }}>
                                    <div className="d-flex justify-content-between mb-1">
                                        <span>{c.name}</span>
                                        <span className="text-muted">{c.progress}%</span>
                                    </div>
                                    <ProgressBar now={c.progress} variant={c.progress >= 80 ? "success" : c.progress >= 50 ? "info" : "warning"} style={{ height: '6px' }} />
                                </ListGroup.Item>
                            ))}
                        </ListGroup>
                    </div>
                </Col>
                <Col lg={4}>
                    <div className="panel-card mb-4">
                        <div className="panel-head">Bài kiểm tra sắp tới</div>
                        <ListGroup variant="flush">
                            {quizzes.map(q => (
                                <ListGroup.Item key={q.id} action onClick={() => nav('/quizzes')} style={{ fontSize: '0.85rem' }}>
                                    <strong>{q.name}</strong><br />
                                    <small className="text-muted">{q.course}</small><br />
                                    <Badge bg="light" text="dark" className="mt-1">{q.due}</Badge>
                                </ListGroup.Item>
                            ))}
                        </ListGroup>
                    </div>
                    <div className="panel-card">
                        <div className="panel-head">Gợi ý tài liệu</div>
                        <ListGroup variant="flush">
                            <ListGroup.Item action onClick={() => nav('/resources/5')} style={{ fontSize: '0.85rem' }}>
                                <strong>Tài liệu Machine Learning</strong><br />
                                <small className="text-muted">Phù hợp với mục tiêu học tập</small>
                            </ListGroup.Item>
                            <ListGroup.Item action onClick={() => nav('/resources/7')} style={{ fontSize: '0.85rem' }}>
                                <strong>Giáo trình Hệ điều hành Linux</strong><br />
                                <small className="text-muted">Bổ sung kiến thức nền tảng</small>
                            </ListGroup.Item>
                        </ListGroup>
                    </div>
                </Col>
            </Row>
        </Container>
    );
}
export default StudentDashboard;
