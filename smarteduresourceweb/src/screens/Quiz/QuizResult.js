import { useEffect, useState } from "react";
import { Badge, Button, Col, Container, ListGroup, Row } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";

import MySpinner from "../../components/common/MySpinner";
import { QUIZZES, QUIZ_QUESTIONS } from "../../configs/MockData";

const QuizResult = () => {
    const { id } = useParams();
    const [loading, setLoading] = useState(true);
    const nav = useNavigate();

    useEffect(() => {
        const t = setTimeout(() => setLoading(false), 400);
        return () => clearTimeout(t);
    }, []);

    if (loading) return <MySpinner />;

    const quiz = QUIZZES.find(q => q.id === parseInt(id)) || QUIZZES[0];
    const questions = QUIZ_QUESTIONS;
    const score = 2;
    const total = questions.length;
    const percentage = Math.round((score / total) * 100);

    return (
        <Container className="py-4">
            <a href="#!" className="detail-back" onClick={e => { e.preventDefault(); nav('/quizzes'); }}>← Quay lại danh sách</a>

            <div className="panel-card mb-4" style={{ padding: '32px', textAlign: 'center' }}>
                <h4 style={{ fontWeight: 700, marginBottom: '16px' }}>Kết quả: {quiz.title}</h4>
                <div className="score-circle">
                    <span className="score-val">{score}/{total}</span>
                </div>
                <p style={{ fontSize: '1.1rem', fontWeight: 600, color: percentage >= 70 ? '#059669' : '#DC2626' }}>
                    {percentage >= 70 ? "Đạt" : "Chưa đạt"} — {percentage}%
                </p>
                <div className="d-flex justify-content-center gap-3 mt-3">
                    <Button variant="outline-primary" onClick={() => nav(`/quizzes/${id}/take`)}>Làm lại</Button>
                    <Button variant="primary" onClick={() => nav('/quizzes')}>Về danh sách</Button>
                </div>
            </div>

            <div className="panel-card">
                <div className="panel-head">Chi tiết kết quả</div>
                <ListGroup variant="flush">
                    {questions.map((q, idx) => {
                        const correctOpt = q.options.find(o => o.isCorrect);
                        const userAnswer = q.options[1]; // mock: user chọn option thứ 2
                        const isCorrect = userAnswer && userAnswer.isCorrect;
                        return (
                            <ListGroup.Item key={q.id} style={{ fontSize: '0.88rem' }}>
                                <Row>
                                    <Col xs={1}>
                                        <Badge bg={isCorrect ? "success" : "danger"} style={{ width: '24px' }}>
                                            {isCorrect ? "✓" : "✗"}
                                        </Badge>
                                    </Col>
                                    <Col>
                                        <strong>Câu {idx + 1}:</strong> {q.content}
                                        <br />
                                        <small className="text-muted">Bạn chọn: {userAnswer?.content || "—"}</small>
                                        {!isCorrect && <><br /><small style={{ color: '#059669' }}>Đáp án đúng: {correctOpt?.content}</small></>}
                                    </Col>
                                </Row>
                            </ListGroup.Item>
                        );
                    })}
                </ListGroup>
            </div>
        </Container>
    );
}
export default QuizResult;
