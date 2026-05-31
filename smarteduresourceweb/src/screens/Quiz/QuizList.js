import { useContext, useEffect, useState } from "react";
import { Badge, Button, Col, Container, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import MySpinner from "../../components/common/MySpinner";
import { QUIZZES } from "../../configs/MockData";

const QuizList = () => {
    const [user] = useContext(MyUserContext);
    const [loading, setLoading] = useState(true);
    const nav = useNavigate();

    useEffect(() => {
        if (!user) { nav('/login'); return; }
        const t = setTimeout(() => setLoading(false), 400);
        return () => clearTimeout(t);
    }, [user, nav]);

    if (loading) return <MySpinner />;

    return (
        <Container className="py-4">
            <h2 style={{ fontSize: '1.35rem', fontWeight: 700, marginBottom: '20px' }}>Bài kiểm tra</h2>
            <Row className="g-3">
                {QUIZZES.map(q => (
                    <Col key={q.id} xs={12} md={6} lg={4}>
                        <div className="panel-card" style={{ padding: '20px' }}>
                            <div className="d-flex justify-content-between align-items-start mb-2">
                                <h6 style={{ fontWeight: 600, marginBottom: 0 }}>{q.title}</h6>
                                <Badge bg={q.status === "COMPLETED" ? "success" : "primary"}>
                                    {q.status === "COMPLETED" ? "Đã làm" : "Chưa làm"}
                                </Badge>
                            </div>
                            <p style={{ fontSize: '0.82rem', color: '#64748B', marginBottom: '12px' }}>{q.courseName}</p>
                            <div className="d-flex gap-2 mb-3" style={{ fontSize: '0.78rem' }}>
                                <Badge bg="light" text="dark">{q.duration} phút</Badge>
                                <Badge bg="light" text="dark">{q.questionCount} câu</Badge>
                                {q.bestScore !== null && <Badge bg="light" text="dark">Điểm cao nhất: {q.bestScore}/{q.questionCount}</Badge>}
                            </div>
                            <Button
                                variant={q.status === "COMPLETED" ? "outline-primary" : "primary"}
                                size="sm"
                                className="w-100"
                                onClick={() => nav(q.status === "COMPLETED" ? `/quizzes/${q.id}/result` : `/quizzes/${q.id}/take`)}
                            >
                                {q.status === "COMPLETED" ? "Xem kết quả" : "Bắt đầu làm bài"}
                            </Button>
                        </div>
                    </Col>
                ))}
            </Row>
        </Container>
    );
}
export default QuizList;
