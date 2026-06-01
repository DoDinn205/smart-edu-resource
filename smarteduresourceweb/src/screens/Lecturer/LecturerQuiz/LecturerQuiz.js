import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Form, Modal, Table, InputGroup, Pagination } from "react-bootstrap";
import { useNavigate, useSearchParams } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import useSubmissionGuard from "../../../hooks/useSubmissionGuard";
import "../Lecturer.css";

const LecturerQuiz = () => {
    const [user] = useContext(MyUserContext);
    const [quizzes, setQuizzes] = useState([]);
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [editingQuiz, setEditingQuiz] = useState(null);
    const [formData, setFormData] = useState({});
    const [showQuestions, setShowQuestions] = useState(false);
    const [selectedQuiz, setSelectedQuiz] = useState(null);
    const [questions, setQuestions] = useState([]);
    const [showQModal, setShowQModal] = useState(false);
    const [editingQ, setEditingQ] = useState(null);
    const [qFormData, setQFormData] = useState({});
    const { isSubmitting, runSubmission } = useSubmissionGuard();
    const { isSubmitting: isSubmittingQuestion, runSubmission: runQuestionSubmission } = useSubmissionGuard();
    const nav = useNavigate();
    const [q] = useSearchParams();
    const kwParam = q.get("kw") || "";
    const [searchKw, setSearchKw] = useState(kwParam);
    const pageParam = Number.parseInt(q.get("page"), 10);
    const currentPage = Number.isInteger(pageParam) && pageParam > 0 ? pageParam : 1;
    const [totalPages, setTotalPages] = useState(1);

    useEffect(() => {
        if (!user || (user.role !== "LECTURER" && user.role !== "ADMIN")) {
            nav('/login'); return;
        }
        loadQuizzes();
        loadCourses();
    }, [user, nav, kwParam, currentPage]);

    useEffect(() => {
        setSearchKw(kwParam);
    }, [kwParam]);

    const loadQuizzes = async () => {
        try {
            setLoading(true);
            setErr("");
            let url = endpoints['lecturer-quizzes'] + `?page=${currentPage}`;
            if (kwParam) url += `&keyword=${kwParam}`;
            let res = await authApis().get(url);
            const pageData = res.data.data;
            setQuizzes(pageData?.items || []);
            setTotalPages(pageData?.totalPages || 1);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách quiz.");
        } finally {
            setLoading(false);
        }
    };

    const loadCourses = async () => {
        try {
            let res = await authApis().get(endpoints['lecturer-courses']);
            setCourses(res.data.data?.items || []);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách khóa học.");
        }
    };

    const handleOpenCreate = () => {
        setEditingQuiz(null);
        setFormData({ durationMinutes: 30, courseId: "" });
        setShowModal(true);
    };

    const handleOpenEdit = (q) => {
        setEditingQuiz(q);
        setFormData({
            title: q.title || "",
            description: q.description || "",
            durationMinutes: q.durationMinutes || 30,
            courseId: q.courseId || "",
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await runSubmission(async () => {
            try {
                setErr("");
                if (editingQuiz) {
                    await authApis().put(endpoints['lecturer-quiz-detail'](editingQuiz.id), formData);
                } else {
                    await authApis().post(endpoints['lecturer-quizzes'], formData);
                }
                setShowModal(false);
                loadQuizzes();
            } catch (ex) {
                console.error(ex);
                setErr("Có lỗi xảy ra khi lưu quiz.");
            }
        });
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa quiz này?")) return;
        try {
            await authApis().delete(endpoints['lecturer-quiz-detail'](id));
            loadQuizzes();
        } catch (ex) {
            console.error(ex);
            setErr("Không thể xóa quiz.");
        }
    };

    const handleViewQuestions = async (quiz) => {
        try {
            setSelectedQuiz(quiz);
            let res = await authApis().get(endpoints['lecturer-quiz-questions'](quiz.id));
            setQuestions(res.data.data || []);
            setShowQuestions(true);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải câu hỏi.");
        }
    };

    const getInitialOptions = (type, existingAnswers = []) => {
        if (existingAnswers && existingAnswers.length > 0) {
            return existingAnswers.map(ans => ({ ...ans }));
        }
        if (type === "SINGLE_CHOICE" || type === "MULTIPLE_CHOICE") {
            return [
                { content: "", isCorrect: false },
                { content: "", isCorrect: false },
                { content: "", isCorrect: false },
                { content: "", isCorrect: false }
            ];
        } else if (type === "TRUE_FALSE") {
            return [
                { content: "Đúng", isCorrect: true },
                { content: "Sai", isCorrect: false }
            ];
        } else if (type === "SHORT_ANSWER") {
            return [{ content: "", isCorrect: true }];
        }
        return [];
    };

    const handleCreateQuestion = () => {
        setEditingQ(null);
        setQFormData({ score: 1, type: "SINGLE_CHOICE", options: getInitialOptions("SINGLE_CHOICE") });
        setShowQModal(true);
    };

    const handleEditQuestion = (q) => {
        setEditingQ(q);
        setQFormData({
            content: q.content || "",
            score: q.score || 1,
            explanation: q.explanation || "",
            type: q.type || "SINGLE_CHOICE",
            options: getInitialOptions(q.type || "SINGLE_CHOICE", q.answers || q.options),
        });
        setShowQModal(true);
    };

    const handleTypeChange = (e) => {
        const newType = e.target.value;
        setQFormData({
            ...qFormData,
            type: newType,
            options: getInitialOptions(newType)
        });
    };

    const handleSubmitQuestion = async (e) => {
        e.preventDefault();
        await runQuestionSubmission(async () => {
            try {
                if (editingQ) {
                    await authApis().put(endpoints['lecturer-question-detail'](editingQ.id), qFormData);
                } else {
                    await authApis().post(endpoints['lecturer-quiz-questions'](selectedQuiz.id), qFormData);
                }
                setShowQModal(false);
                handleViewQuestions(selectedQuiz);
            } catch (ex) {
                console.error(ex);
                setErr("Có lỗi xảy ra khi lưu câu hỏi.");
            }
        });
    };

    const handleDeleteQuestion = async (qId) => {
        if (!window.confirm("Xóa câu hỏi này?")) return;
        try {
            await authApis().delete(endpoints['lecturer-question-detail'](qId));
            handleViewQuestions(selectedQuiz);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể xóa câu hỏi.");
        }
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

    if (loading) return <MySpinner />;

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="mb-0">Ngân hàng Quiz</h4>
                <div className="d-flex align-items-center w-50">
                    <Form onSubmit={handleSearch} className="w-100 me-3">
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
                    <Button style={{ backgroundColor: "#6366f1", borderColor: "#6366f1", whiteSpace: "nowrap" }} variant="primary" size="sm" onClick={handleOpenCreate}>
                        <i className="bi bi-plus-lg me-1"></i> Tạo quiz
                    </Button>
                </div>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="lecturer-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên quiz</th>
                            <th>Thời gian</th>
                            <th>Số câu hỏi</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {quizzes.map(q => (
                            <tr key={q.id}>
                                <td>{q.id}</td>
                                <td>{q.title}</td>
                                <td>{q.durationMinutes} phút</td>
                                <td><Badge bg="info">{q.questionCount || 0}</Badge></td>
                                <td>
                                    <Button variant="outline-info" size="sm" className="me-1"
                                        onClick={() => handleViewQuestions(q)}>
                                        <i className="bi bi-list-ul"></i> Câu hỏi
                                    </Button>
                                    <Button variant="outline-primary" size="sm" className="me-1"
                                        onClick={() => handleOpenEdit(q)}>
                                        <i className="bi bi-pencil"></i>
                                    </Button>
                                    <Button variant="outline-danger" size="sm"
                                        onClick={() => handleDelete(q.id)}>
                                        <i className="bi bi-trash"></i>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {quizzes.length === 0 && (
                            <tr><td colSpan="5" className="text-center text-muted py-3">Chưa có quiz</td></tr>
                        )}
                    </tbody>
                </Table>

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

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{editingQuiz ? "Sửa quiz" : "Tạo quiz"}</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên quiz</Form.Label>
                            <Form.Control type="text" value={formData.title || ''}
                                onChange={e => setFormData({ ...formData, title: e.target.value })} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Khóa học</Form.Label>
                            <Form.Select value={formData.courseId || ''}
                                onChange={e => setFormData({ ...formData, courseId: parseInt(e.target.value) })}
                                required>
                                <option value="">-- Chọn khóa học --</option>
                                {courses.map(course => (
                                    <option key={course.id} value={course.id}>{course.name}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Thời gian (phút)</Form.Label>
                            <Form.Control type="number" min={1} value={formData.durationMinutes || 30}
                                onChange={e => setFormData({ ...formData, durationMinutes: parseInt(e.target.value) })} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Mô tả</Form.Label>
                            <Form.Control as="textarea" rows={3} value={formData.description || ''}
                                onChange={e => setFormData({ ...formData, description: e.target.value })} />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModal(false)} disabled={isSubmitting}>Hủy</Button>
                        <Button variant="primary" type="submit" disabled={isSubmitting}>
                            {isSubmitting ? "Đang lưu..." : "Lưu"}
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            <Modal show={showQuestions && !showQModal} onHide={() => setShowQuestions(false)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>Câu hỏi - {selectedQuiz?.title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="d-flex justify-content-end mb-3">
                        <Button variant="primary" size="sm" onClick={handleCreateQuestion}>
                            <i className="bi bi-plus-lg me-1"></i> Thêm câu hỏi
                        </Button>
                    </div>
                    <Table hover responsive>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Nội dung câu hỏi</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            {questions.map((q, idx) => (
                                <tr key={q.id}>
                                    <td>{idx + 1}</td>
                                    <td style={{ fontSize: '0.88rem' }}>{q.content}</td>
                                    <td>
                                        <Button variant="outline-primary" size="sm" className="me-1"
                                            onClick={() => handleEditQuestion(q)}>
                                            <i className="bi bi-pencil"></i>
                                        </Button>
                                        <Button variant="outline-danger" size="sm"
                                            onClick={() => handleDeleteQuestion(q.id)}>
                                            <i className="bi bi-trash"></i>
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                            {questions.length === 0 && (
                                <tr><td colSpan="3" className="text-center text-muted">Chưa có câu hỏi</td></tr>
                            )}
                        </tbody>
                    </Table>

                </Modal.Body>
            </Modal>

            <Modal show={showQModal} onHide={() => setShowQModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{editingQ ? "Sửa câu hỏi" : "Thêm câu hỏi"}</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmitQuestion}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Nội dung câu hỏi</Form.Label>
                            <Form.Control as="textarea" rows={3} value={qFormData.content || ''}
                                onChange={e => setQFormData({ ...qFormData, content: e.target.value })} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Điểm</Form.Label>
                            <Form.Control type="number" min={0.1} step={0.1} value={qFormData.score || 1}
                                onChange={e => setQFormData({ ...qFormData, score: parseFloat(e.target.value) })} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Loại câu hỏi</Form.Label>
                            <Form.Select value={qFormData.type || 'SINGLE_CHOICE'}
                                onChange={handleTypeChange} required>
                                <option value="SINGLE_CHOICE">Một đáp án</option>
                                <option value="MULTIPLE_CHOICE">Nhiều đáp án</option>
                                <option value="TRUE_FALSE">Đúng / sai</option>
                                <option value="SHORT_ANSWER">Trả lời ngắn</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Giải thích</Form.Label>
                            <Form.Control as="textarea" rows={2} value={qFormData.explanation || ''}
                                onChange={e => setQFormData({ ...qFormData, explanation: e.target.value })} />
                        </Form.Group>
                        {qFormData.options && qFormData.options.length > 0 && (
                            <Form.Group className="mb-3">
                                <Form.Label>Đáp án</Form.Label>
                                {qFormData.options.map((opt, idx) => (
                                    <div key={idx} className="d-flex align-items-center mb-2">
                                        <div className="me-2" style={{ width: '30px', fontWeight: 'bold' }}>{String.fromCharCode(65 + idx)}.</div>
                                        <Form.Control
                                            type="text"
                                            placeholder={`Nhập đáp án ${String.fromCharCode(65 + idx)}`}
                                            value={opt.content || ''}
                                            onChange={(e) => {
                                                const newOptions = [...qFormData.options];
                                                newOptions[idx].content = e.target.value;
                                                setQFormData({ ...qFormData, options: newOptions });
                                            }}
                                            className="me-2"
                                            required
                                            disabled={qFormData.type === 'TRUE_FALSE'}
                                        />
                                        <Form.Check
                                            type={qFormData.type === 'MULTIPLE_CHOICE' ? 'checkbox' : 'radio'}
                                            name="correctOption"
                                            id={`correct-opt-${idx}`}
                                            checked={opt.isCorrect || false}
                                            onChange={(e) => {
                                                const newOptions = [...qFormData.options];
                                                if (qFormData.type === 'SINGLE_CHOICE' || qFormData.type === 'TRUE_FALSE') {
                                                    newOptions.forEach(o => o.isCorrect = false);
                                                    newOptions[idx].isCorrect = e.target.checked;
                                                } else {
                                                    newOptions[idx].isCorrect = e.target.checked;
                                                }
                                                setQFormData({ ...qFormData, options: newOptions });
                                            }}
                                            style={{ whiteSpace: 'nowrap' }}
                                        />
                                    </div>
                                ))}
                            </Form.Group>
                        )}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowQModal(false)} disabled={isSubmittingQuestion}>Hủy</Button>
                        <Button variant="primary" type="submit" disabled={isSubmittingQuestion}>
                            {isSubmittingQuestion ? "Đang lưu..." : "Lưu"}
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </>
    );
}

export default LecturerQuiz;
