import React, { useRef, useState, useEffect } from 'react';
import { Row, Col, Table, Form, Button, Modal, Badge, ProgressBar, Accordion, InputGroup, Pagination } from 'react-bootstrap';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { authApis, endpoints } from '../../../configs/Apis';
import MySpinner from '../../../components/common/MySpinner';
import '../Lecturer.css';

const LecturerResult = () => {
    const [courses, setCourses] = useState([]);
    const [selectedCourseId, setSelectedCourseId] = useState('');
    const [progressList, setProgressList] = useState([]);
    const [loading, setLoading] = useState(false);

    const nav = useNavigate();
    const [q] = useSearchParams();
    const kwParam = q.get("kw") || "";
    const [searchKw, setSearchKw] = useState(kwParam);
    const pageParam = Number.parseInt(q.get("page"), 10);
    const currentPage = Number.isInteger(pageParam) && pageParam > 0 ? pageParam : 1;
    const [totalPages, setTotalPages] = useState(1);

    // Modal state
    const [showModal, setShowModal] = useState(false);
    const [selectedStudent, setSelectedStudent] = useState(null);
    const [feedback, setFeedback] = useState('');
    const [savingFeedback, setSavingFeedback] = useState(false);
    const savingFeedbackRef = useRef(false);

    useEffect(() => {
        setSearchKw(kwParam);
    }, [kwParam]);

    useEffect(() => {
        loadCourses();
    }, []);

    useEffect(() => {
        loadProgress(selectedCourseId);
    }, [selectedCourseId, currentPage, kwParam]);

    const loadCourses = async () => {
        try {
            const res = await authApis().get(endpoints['lecturer-courses']);
            const courseItems = res.data.data?.items || [];
            setCourses(courseItems);
        } catch (ex) {
            console.error(ex);
            alert('Lỗi khi tải danh sách khóa học');
        }
    };

    const loadProgress = async (courseId) => {
        setLoading(true);
        try {
            let url = courseId
                ? endpoints['lecturer-course-progress'](courseId)
                : endpoints['lecturer-progress'];
            url += `?page=${currentPage}`;
            if (kwParam) url += `&keyword=${kwParam}`;

            const res = await authApis().get(url);
            const pageData = res.data.data;
            setProgressList(pageData?.items || []);
            setTotalPages(pageData?.totalPages || 1);
        } catch (ex) {
            console.error(ex);
            alert('Lỗi khi tải tiến độ học tập');
        } finally {
            setLoading(false);
        }
    };

    const handleCourseChange = (e) => {
        setSelectedCourseId(e.target.value);
        nav(`?page=1`);
    };

    const handleSearch = (e) => {
        e.preventDefault();
        const params = new URLSearchParams();
        if (searchKw.trim()) params.set("kw", searchKw.trim());
        nav(`?${params.toString()}`);
    };

    const handlePageChange = (page) => {
        const params = new URLSearchParams();
        if (kwParam) params.set("kw", kwParam);
        if (page > 1) params.set("page", page);
        nav(`?${params.toString()}`);
    };

    const handleOpenModal = (student) => {
        setSelectedStudent(student);
        setFeedback(student.lecturerFeedback || '');
        setShowModal(true);
    };

    const handleSaveFeedback = async () => {
        if (!selectedStudent || savingFeedbackRef.current) return;
        savingFeedbackRef.current = true;
        setSavingFeedback(true);
        try {
            await authApis().post(endpoints['lecturer-progress-feedback'](selectedStudent.enrollmentId), {
                feedback: feedback
            });
            alert('Đã lưu nhận xét');
            setProgressList(progressList.map(p =>
                p.enrollmentId === selectedStudent.enrollmentId ? { ...p, lecturerFeedback: feedback } : p
            ));
            setShowModal(false);
        } catch (ex) {
            console.error(ex);
            alert('Lỗi khi lưu nhận xét');
        } finally {
            savingFeedbackRef.current = false;
            setSavingFeedback(false);
        }
    };

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="mb-0">Kết quả học tập</h4>
                <div className="d-flex align-items-center w-50">
                    <Form onSubmit={handleSearch} className="w-100">
                        <InputGroup>
                            <Form.Control
                                type="text"
                                placeholder="Tìm kiếm..."
                                value={searchKw}
                                onChange={(e) => setSearchKw(e.target.value)}
                            />
                            <Button variant="outline-secondary" type="submit">
                                <i className="bi bi-search"></i>
                            </Button>
                        </InputGroup>
                    </Form>
                </div>
            </div>

            <div className="lecturer-panel">
                <div className="panel-header d-flex justify-content-between align-items-center">
                    <span>Tiến độ học viên</span>
                    <Form.Select
                        style={{ width: '300px' }}
                        value={selectedCourseId}
                        onChange={handleCourseChange}
                    >
                        <option value="">-- Tất cả khóa học --</option>
                        {courses.map(c => (
                            <option key={c.id} value={c.id}>{c.name}</option>
                        ))}
                    </Form.Select>
                </div>

                {loading ? (
                    <div className="p-4"><MySpinner /></div>
                ) : (
                    <Table hover responsive className="mb-0">
                        <thead>
                            <tr>
                                <th>Mã HV</th>
                                <th>Họ tên</th>
                                <th>Tiến độ</th>
                                <th>Thời gian (phút)</th>
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
                                            <span>{p.overallProgress}%</span>
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

                {totalPages > 1 && (
                    <div className="d-flex justify-content-center mt-4">
                        <Pagination>
                            {Array.from({ length: totalPages }, (_, i) => i + 1).map(num => (
                                <Pagination.Item key={num} active={num === currentPage} onClick={() => handlePageChange(num)}>
                                    {num}
                                </Pagination.Item>
                            ))}
                        </Pagination>
                    </div>
                )}
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
                            <ProgressBar now={selectedStudent?.overallProgress} variant="success" label={`${selectedStudent?.overallProgress || 0}%`} style={{ height: '20px' }} />
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
