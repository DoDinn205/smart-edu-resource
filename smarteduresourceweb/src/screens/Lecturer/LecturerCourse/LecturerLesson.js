import { useContext, useEffect, useState } from "react";
import { Alert, Button, Form, Modal, Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Lecturer.css";

const LecturerLesson = () => {
    const { id } = useParams();
    const [user] = useContext(MyUserContext);
    const [lessons, setLessons] = useState([]);
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
        loadLessons();
    }, [user, nav, id]);

    const loadLessons = async () => {
        try {
            setLoading(true);
            setErr("");
            let res = await authApis().get(endpoints['course-lessons'](id));
            setLessons(res.data.data || []);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách bài học.");
        } finally {
            setLoading(false);
        }
    };

    const handleOpenCreate = () => {
        setEditingLesson(null);
        setFormData({ courseId: parseInt(id) });
        setShowModal(true);
    };

    const handleOpenEdit = (lesson) => {
        setEditingLesson(lesson);
        setFormData({
            title: lesson.title || "",
            content: lesson.content || "",
            videoUrl: lesson.videoUrl || "",
            orderIndex: lesson.orderIndex || 0,
            courseId: parseInt(id),
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setErr("");
            if (editingLesson) {
                await authApis().put(endpoints['lecturer-lesson-detail'](editingLesson.id), formData);
            } else {
                await authApis().post(endpoints['lecturer-lessons'], formData);
            }
            setShowModal(false);
            loadLessons();
        } catch (ex) {
            console.error(ex);
            setErr("Có lỗi xảy ra khi lưu bài học.");
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

    if (loading) return <MySpinner />;

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <Button variant="link" className="p-0 me-2" onClick={() => nav('/lecturer/courses')}>
                        <i className="bi bi-arrow-left"></i> Quay lại
                    </Button>
                    <span className="fw-bold fs-5">Bài học - Khóa #{id}</span>
                </div>
                <Button variant="primary" size="sm" onClick={handleOpenCreate}>
                    <i className="bi bi-plus-lg me-1"></i> Thêm bài học
                </Button>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="lecturer-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>Thứ tự</th>
                            <th>Tiêu đề</th>
                            <th>Video</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {lessons.map(l => (
                            <tr key={l.id}>
                                <td>{l.orderIndex}</td>
                                <td>{l.title}</td>
                                <td style={{ fontSize: '0.82rem' }}>{l.videoUrl ? "Có" : "Chưa có"}</td>
                                <td>
                                    <Button variant="outline-primary" size="sm" className="me-1"
                                        onClick={() => handleOpenEdit(l)}>
                                        <i className="bi bi-pencil"></i>
                                    </Button>
                                    <Button variant="outline-danger" size="sm"
                                        onClick={() => handleDelete(l.id)}>
                                        <i className="bi bi-trash"></i>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {lessons.length === 0 && (
                            <tr><td colSpan="4" className="text-center text-muted py-3">Chưa có bài học</td></tr>
                        )}
                    </tbody>
                </Table>
            </div>

            <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>{editingLesson ? "Sửa bài học" : "Thêm bài học"}</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Tiêu đề</Form.Label>
                            <Form.Control type="text" value={formData.title || ''}
                                onChange={e => setFormData({ ...formData, title: e.target.value })} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Thứ tự hiển thị</Form.Label>
                            <Form.Control type="number" value={formData.orderIndex || 0}
                                onChange={e => setFormData({ ...formData, orderIndex: parseInt(e.target.value) })} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>URL Video</Form.Label>
                            <Form.Control type="url" value={formData.videoUrl || ''}
                                onChange={e => setFormData({ ...formData, videoUrl: e.target.value })} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Nội dung</Form.Label>
                            <Form.Control as="textarea" rows={5} value={formData.content || ''}
                                onChange={e => setFormData({ ...formData, content: e.target.value })} />
                        </Form.Group>
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
