import React, { useState, useEffect } from 'react';
import { Row, Col, Table, Form, Button, Modal, Badge, ProgressBar, Accordion } from 'react-bootstrap';
import { authApis, endpoints } from '../../../configs/Apis';
import MySpinner from '../../../components/common/MySpinner';
import '../Lecturer.css';

const LecturerResult = () => {
    const [courses, setCourses] = useState([]);
    const [selectedCourseId, setSelectedCourseId] = useState('');
    const [progressList, setProgressList] = useState([]);
    const [loading, setLoading] = useState(false);

    // Modal state
    const [showModal, setShowModal] = useState(false);
    const [selectedStudent, setSelectedStudent] = useState(null);
    const [feedback, setFeedback] = useState('');
    const [savingFeedback, setSavingFeedback] = useState(false);

    useEffect(() => {
        loadCourses();
    }, []);

    useEffect(() => {
        if (selectedCourseId) {
            loadProgress(selectedCourseId);
        } else {
            setProgressList([]);
        }
    }, [selectedCourseId]);

    const loadCourses = async () => {
        try {
            const res = await authApis().get(endpoints['lecturer-courses']);
            setCourses(res.data.data);
            if (res.data.data.length > 0) {
                setSelectedCourseId(res.data.data[0].id);
            }
        } catch (ex) {
            console.error(ex);
            alert('Lỗi khi tải danh sách khóa học');
        }
    };

    const loadProgress = async (courseId) => {
        setLoading(true);
        try {
            const res = await authApis().get(endpoints['lecturer-course-progress'](courseId));
            setProgressList(res.data.data);
        } catch (ex) {
            console.error(ex);
            alert('Lỗi khi tải tiến độ học tập');
        } finally {
            setLoading(false);
        }
    };

    const handleOpenModal = (student) => {
        setSelectedStudent(student);
        setFeedback(student.lecturerFeedback || '');
        setShowModal(true);
    };

    const handleSaveFeedback = async () => {
        if (!selectedStudent) return;
        setSavingFeedback(true);
        try {
            const res = await authApis().post(endpoints['lecturer-progress-feedback'](selectedStudent.enrollmentId), {
                feedback: feedback
            });
            alert('Đã lưu nhận xét');
            // Cập nhật lại trong danh sách
            setProgressList(progressList.map(p => 
                p.enrollmentId === selectedStudent.enrollmentId ? { ...p, lecturerFeedback: feedback } : p
            ));
            setShowModal(false);
        } catch (ex) {
            console.error(ex);
            alert('Lỗi khi lưu nhận xét');
        } finally {
            setSavingFeedback(false);
        }
    };

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4>Kết quả học tập</h4>
            </div>

            <div className="lecturer-panel">
                <div className="panel-header d-flex justify-content-between align-items-center">
                    <span>Tiến độ học viên</span>
                    <Form.Select 
                        style={{ width: '300px' }} 
                        value={selectedCourseId} 
                        onChange={(e) => setSelectedCourseId(e.target.value)}
                    >
                        <option value="">-- Chọn khóa học --</option>
                        {courses.map(c => (
                            <option key={c.id} value={c.id}>{c.name}</option>
                        ))}
                    </Form.Select>
                </div>
                
                <div className="p-3">
                    {loading ? <MySpinner /> : (
                        <Table responsive hover className="table align-middle">
                            <thead>
                                <tr>
                                    <th>Mã HV</th>
                                    <th>Họ tên</th>
                                    <th>Tiến độ</th>
                                    <th>Thời gian học (phút)</th>
                                    <th>Bài kiểm tra</th>
                                    <th>Nhận xét</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                {progressList.length === 0 ? (
                                    <tr>
                                        <td colSpan="7" className="text-center py-4 text-muted">Không có dữ liệu</td>
                                    </tr>
                                ) : progressList.map(p => (
                                    <tr key={p.enrollmentId}>
                                        <td><Badge bg="secondary">{p.studentCode}</Badge></td>
                                        <td>
                                            <div className="d-flex align-items-center">
                                                <img src={p.studentUser?.avatar || 'https://via.placeholder.com/40'} 
                                                    alt="avatar" 
                                                    className="rounded-circle me-2" 
                                                    style={{ width: '32px', height: '32px', objectFit: 'cover' }} 
                                                />
                                                {p.studentUser?.fullName}
                                            </div>
                                        </td>
                                        <td style={{ minWidth: '150px' }}>
                                            <div className="d-flex align-items-center">
                                                <ProgressBar 
                                                    now={p.overallProgress} 
                                                    variant="success" 
                                                    style={{ height: '8px', flex: 1 }} 
                                                    className="me-2"
                                                />
                                                <span style={{ fontSize: '0.8rem' }}>{Math.round(p.overallProgress)}%</span>
                                            </div>
                                        </td>
                                        <td>{p.totalStudyTime || 0}</td>
                                        <td>{p.quizAttempts?.length || 0} lượt</td>
                                        <td>
                                            {p.lecturerFeedback ? (
                                                <Badge bg="info">Đã nhận xét</Badge>
                                            ) : (
                                                <Badge bg="light" text="dark" className="border">Chưa có</Badge>
                                            )}
                                        </td>
                                        <td>
                                            <Button variant="outline-primary" size="sm" onClick={() => handleOpenModal(p)}>
                                                <i className="bi bi-eye"></i> Chi tiết
                                            </Button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    )}
                </div>
            </div>

            {/* Modal Chi tiết */}
            <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>Chi tiết học tập - {selectedStudent?.studentUser?.fullName}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Row className="mb-4">
                        <Col md={6}>
                            <h6>Tiến độ chung</h6>
                            <ProgressBar now={selectedStudent?.overallProgress} variant="success" label={`${Math.round(selectedStudent?.overallProgress || 0)}%`} style={{ height: '20px' }} />
                        </Col>
                        <Col md={6}>
                            <h6>Tổng thời gian</h6>
                            <p className="mb-0 text-primary fw-bold fs-5">{selectedStudent?.totalStudyTime || 0} phút</p>
                        </Col>
                    </Row>

                    <h6 className="mb-3">Kết quả bài kiểm tra ({selectedStudent?.quizAttempts?.length || 0})</h6>
                    {selectedStudent?.quizAttempts && selectedStudent.quizAttempts.length > 0 ? (
                        <Accordion className="mb-4">
                            {selectedStudent.quizAttempts.map((attempt, index) => (
                                <Accordion.Item eventKey={index.toString()} key={attempt.id}>
                                    <Accordion.Header>
                                        <div className="d-flex justify-content-between w-100 pe-3">
                                            <span>{attempt.quizTitle}</span>
                                            <span>
                                                Điểm: <Badge bg={attempt.score >= (attempt.totalScore / 2) ? 'success' : 'danger'}>{attempt.score} / {attempt.totalScore}</Badge>
                                            </span>
                                        </div>
                                    </Accordion.Header>
                                    <Accordion.Body>
                                        <p className="text-muted small mb-2">
                                            Nộp lúc: {new Date(attempt.submittedAt).toLocaleString('vi-VN')} | 
                                            Trạng thái: {attempt.status}
                                        </p>
                                        <div className="d-flex align-items-center">
                                            <Button variant="outline-info" size="sm" onClick={() => window.open(`/lecturer/quiz-attempts/${attempt.id}`, '_blank')}>
                                                Xem bài làm chi tiết
                                            </Button>
                                        </div>
                                    </Accordion.Body>
                                </Accordion.Item>
                            ))}
                        </Accordion>
                    ) : (
                        <div className="alert alert-light text-center py-2 mb-4">Học viên chưa làm bài kiểm tra nào</div>
                    )}

                    <h6>Nhận xét & Gợi ý cải thiện</h6>
                    <Form.Group>
                        <Form.Control 
                            as="textarea" 
                            rows={4} 
                            placeholder="Nhập nhận xét của giảng viên..." 
                            value={feedback}
                            onChange={(e) => setFeedback(e.target.value)}
                        />
                        <Form.Text className="text-muted">
                            Nhận xét này sẽ được hiển thị cho học viên.
                        </Form.Text>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>Đóng</Button>
                    <Button variant="primary" onClick={handleSaveFeedback} disabled={savingFeedback}>
                        {savingFeedback ? <MySpinner /> : 'Lưu nhận xét'}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default LecturerResult;
