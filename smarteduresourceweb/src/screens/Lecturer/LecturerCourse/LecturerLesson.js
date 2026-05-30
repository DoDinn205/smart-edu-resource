import { useContext, useEffect, useMemo, useState } from "react";
import { Accordion, Alert, Badge, Button, Col, Form, Modal, Row, Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Lecturer.css";

const LecturerLesson = () => {
    const { id } = useParams();
    const [user] = useContext(MyUserContext);
    const [lessons, setLessons] = useState([]);
    const [resources, setResources] = useState([]);
    const [quizzes, setQuizzes] = useState([]);
    const [courseName, setCourseName] = useState("");
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [editingLesson, setEditingLesson] = useState(null);
    const [formData, setFormData] = useState({});
    const nav = useNavigate();

    useEffect(() => {
        if (!user || (user.role !== "LECTURER" && user.role !== "ADMIN")) {
            nav('/login'); return;
        }
        loadCourseInfo();
        loadLessons();
        loadResourcesAndQuizzes();
    }, [user, nav, id]);

    const loadResourcesAndQuizzes = async () => {
        try {
            let resResources = await authApis().get(endpoints['lecturer-resources']);
            setResources(resResources.data.data || []);
            console.log(resResources.data.data);

            let resQuizzes = await authApis().get(endpoints['lecturer-quizzes'] + `?courseId=${id}`);
            setQuizzes(resQuizzes.data.data || []);
            console.log(resQuizzes.data.data);
        } catch (ex) {
            console.error("Lỗi tải danh sách resource/quiz:", ex);
        }
    };

    const loadCourseInfo = async () => {
        let res = await authApis().get(endpoints['lecturer-course-detail'](id));
        setCourseName(res.data.data?.name);
    };

    const loadLessons = async () => {
        try {
            setLoading(true);
            setErr("");
            let res = await authApis().get(endpoints['lecturer-course-lessons'](id));
            setLessons(res.data.data || []);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách bài học.");
        } finally {
            setLoading(false);
        }
    };

    const chapters = useMemo(() => {
        const map = {};
        lessons.forEach(l => {
            const ch = l.chapterNum || 1;
            if (!map[ch]) map[ch] = [];
            map[ch].push(l);
        });
        Object.values(map).forEach(arr => arr.sort((a, b) => a.lessonNum - b.lessonNum));
        return Object.entries(map).sort((a, b) => parseInt(a[0]) - parseInt(b[0]));
    }, [lessons]);

    const handleOpenCreateInChapter = (chapterNum) => {
        const chapterLessons = lessons.filter(l => l.chapterNum === chapterNum);
        const nextLessonNum = chapterLessons.length > 0
            ? Math.max(...chapterLessons.map(l => l.lessonNum)) + 1
            : 1;
        setEditingLesson(null);
        setFormData({
            courseId: parseInt(id),
            title: "",
            chapterNum: chapterNum,
            lessonNum: nextLessonNum,
            isFree: false,
            resourceId: "",
            quizId: "",
        });
        setShowModal(true);
    };

    const handleOpenCreateNewChapter = () => {
        const nextChapter = chapters.length > 0
            ? Math.max(...chapters.map(([ch]) => parseInt(ch))) + 1
            : 1;
        setEditingLesson(null);
        setFormData({
            courseId: parseInt(id),
            title: "",
            chapterNum: nextChapter,
            lessonNum: 1,
            isFree: false,
            resourceId: "",
            quizId: "",
        });
        setShowModal(true);
    };

    const handleOpenEdit = (lesson) => {
        setEditingLesson(lesson);
        setFormData({
            courseId: parseInt(id),
            title: lesson.title || "",
            chapterNum: lesson.chapterNum || 1,
            lessonNum: lesson.lessonNum || 1,
            isFree: lesson.isFree || false,
            resourceId: lesson.resourceId || "",
            quizId: lesson.quizId || "",
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setErr("");
            const payload = {
                courseId: formData.courseId,
                title: formData.title,
                chapterNum: parseInt(formData.chapterNum),
                lessonNum: parseInt(formData.lessonNum),
                isFree: formData.isFree,
                resourceId: formData.resourceId ? parseInt(formData.resourceId) : null,
                quizId: formData.quizId ? parseInt(formData.quizId) : null,
            };
            if (editingLesson) {
                await authApis().put(endpoints['lecturer-lesson-detail'](editingLesson.id), payload);
            } else {
                await authApis().post(endpoints['lecturer-lessons'], payload);
            }
            setShowModal(false);
            loadLessons();
        } catch (ex) {
            console.error(ex);
            if (ex.response && ex.response.data && ex.response.data.message) {
                setErr(ex.response.data.message);
            } else {
                setErr("Có lỗi xảy ra khi lưu bài học.");
            }
        }
    };

    const handleDelete = async (lessonId) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa bài học này?")) return;
        try {
            await authApis().delete(endpoints['lecturer-lesson-detail'](lessonId));
            loadLessons();
        } catch (ex) {
            console.error(ex);
            setErr("Không thể xóa bài học.");
        }
    };

    const renderItemBadges = (l) => {
        const badges = [];
        if (l.resourceTitle) {
            if (l.format === "MP4") {
                badges.push(<Badge bg="info" className="me-1" key="video"><i className="bi bi-play-circle me-1"></i>Video</Badge>);
            } else {
                badges.push(<Badge bg="secondary" className="me-1" key="doc"><i className="bi bi-file-earmark-text me-1"></i>Tài liệu</Badge>);
            }
        }
        if (l.quizTitle) {
            badges.push(<Badge bg="warning" text="dark" key="quiz"><i className="bi bi-question-circle me-1"></i>Quiz</Badge>);
        }
        return badges.length > 0 ? badges : <span className="text-muted" style={{ fontSize: '0.8rem' }}>Chưa gắn nội dung</span>;
    };

    if (loading) return <MySpinner />;

    return (
        <>
            {/* Header */}
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div className="d-flex align-items-center">
                    <Button variant="link" className="p-0 text-dark text-decoration-none fs-4 d-flex align-items-center" onClick={() => nav('/lecturer/courses')}>
                        <i className="bi bi-arrow-left-short"></i>
                    </Button>
                    <h4 className="mb-0 ms-2">Bài học — {courseName}</h4>
                </div>
                <Button size="sm" style={{ backgroundColor: '#6366f1', borderColor: '#6366f1' }} onClick={handleOpenCreateNewChapter}>
                    <i className="bi bi-plus-lg me-1"></i> Thêm chương mới
                </Button>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            {/* Accordion Chapters */}
            {chapters.length === 0 ? (
                <div className="text-center py-5 border rounded bg-white">
                    <p className="text-muted mt-2 mb-3">Chưa có bài học nào trong khóa này</p>
                    <Button variant="outline-primary" size="sm" onClick={handleOpenCreateNewChapter}>
                        <i className="bi bi-plus-lg me-1"></i> Tạo bài học đầu tiên
                    </Button>
                </div>
            ) : (
                <Accordion defaultActiveKey={chapters.map(([ch]) => ch)} alwaysOpen className="lecturer-accordion">
                    {chapters.map(([chapterNum, chapterLessons]) => (
                        <Accordion.Item eventKey={chapterNum} key={chapterNum}>
                            <Accordion.Header>
                                <div className="d-flex align-items-center w-100 me-3">
                                    <h6 className="mb-0 fw-bold">Chương {chapterNum}</h6>
                                    <Badge bg="light" text="secondary" className="ms-auto border">{chapterLessons.length} bài</Badge>
                                </div>
                            </Accordion.Header>
                            <Accordion.Body className="p-0">
                                <Table hover responsive className="mb-0">
                                    <tbody>
                                        {chapterLessons.map((l) => (
                                            <tr key={l.id}>
                                                <td className="align-middle fw-bold text-center" style={{ width: '60px' }}>
                                                    {chapterNum}.{l.lessonNum}
                                                </td>
                                                <td className="align-middle">
                                                    <div className="fw-semibold mb-1">
                                                        {l.title}
                                                        {l.isFree && <Badge bg="success" className="ms-2">Miễn phí</Badge>}
                                                    </div>
                                                    <div className="text-muted" style={{ fontSize: '0.85rem' }}>
                                                        {l.resourceTitle && (
                                                            <span className="me-3">
                                                                <i className="bi bi-file-earmark me-1"></i>{l.resourceTitle}
                                                            </span>
                                                        )}
                                                        {l.quizTitle && (
                                                            <span className="me-3">
                                                                <i className="bi bi-pencil-square me-1"></i>{l.quizTitle}
                                                                {l.questionCount ? ` (${l.questionCount} câu)` : ""}
                                                            </span>
                                                        )}
                                                        {!l.resourceTitle && !l.quizTitle && (
                                                            <span className="text-muted">Chưa gắn nội dung</span>
                                                        )}
                                                    </div>
                                                </td>
                                                <td className="align-middle text-end" style={{ width: '120px' }}>
                                                    <Button variant="outline-primary" size="sm" className="me-1"
                                                        onClick={() => handleOpenEdit(l)} title="Sửa">
                                                        <i className="bi bi-pencil"></i>
                                                    </Button>
                                                    <Button variant="outline-danger" size="sm"
                                                        onClick={() => handleDelete(l.id)} title="Xóa">
                                                        <i className="bi bi-trash"></i>
                                                    </Button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </Table>
                                <div className="text-center p-3 border-top bg-light" style={{ cursor: 'pointer' }} onClick={() => handleOpenCreateInChapter(parseInt(chapterNum))}>
                                    <span className="text-primary fw-medium">
                                        <i className="bi bi-plus-circle me-2"></i> Thêm bài học vào chương {chapterNum}
                                    </span>
                                </div>
                            </Accordion.Body>
                        </Accordion.Item>
                    ))}
                </Accordion>
            )}

            <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>{editingLesson ? "Sửa bài học" : "Thêm bài học"}</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Tiêu đề <span className="text-danger">*</span></Form.Label>
                            <Form.Control type="text" value={formData.title || ''}
                                onChange={e => setFormData({ ...formData, title: e.target.value })} required />
                        </Form.Group>
                        <Row>
                            <Col md={4}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Chương <span className="text-danger">*</span></Form.Label>
                                    <Form.Control type="number" min={1} value={formData.chapterNum || 1}
                                        onChange={e => setFormData({ ...formData, chapterNum: parseInt(e.target.value) })} required />
                                </Form.Group>
                            </Col>
                            <Col md={4}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Bài số <span className="text-danger">*</span></Form.Label>
                                    <Form.Control type="number" min={1} value={formData.lessonNum || 1}
                                        onChange={e => setFormData({ ...formData, lessonNum: parseInt(e.target.value) })} required />
                                </Form.Group>
                            </Col>
                            <Col md={4}>
                                <Form.Group className="mb-3 d-flex align-items-end h-100">
                                    <Form.Check
                                        type="switch"
                                        id="isFreeSwitch"
                                        label="Miễn phí"
                                        checked={formData.isFree || false}
                                        onChange={e => setFormData({ ...formData, isFree: e.target.checked })}
                                    />
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Tài liệu (Resource)</Form.Label>
                                    <Form.Select value={formData.resourceId || ''}
                                        onChange={e => setFormData({ ...formData, resourceId: e.target.value })}>
                                        <option value="">-- Không gắn tài liệu --</option>
                                        {resources.map(r => (
                                            <option key={r.id} value={r.id}>{r.title} ({r.format})</option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                            <Col md={6}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Bài kiểm tra (Quiz)</Form.Label>
                                    <Form.Select value={formData.quizId || ''}
                                        onChange={e => setFormData({ ...formData, quizId: e.target.value })}>
                                        <option value="">-- Không gắn quiz --</option>
                                        {quizzes.map(q => (
                                            <option key={q.id} value={q.id}>{q.title}</option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>
                            </Col>
                        </Row>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModal(false)}>Hủy</Button>
                        <Button variant="primary" type="submit">Lưu</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </>
    );
}

export default LecturerLesson;
