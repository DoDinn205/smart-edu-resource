import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Col, Form, Modal, Row, Table, Pagination, InputGroup } from "react-bootstrap";
import { useNavigate, useSearchParams } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import Apis, { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Lecturer.css";

const LecturerCourse = () => {
    const [user] = useContext(MyUserContext);
    const [courses, setCourses] = useState([]);
    const [subjects, setSubjects] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [editingCourse, setEditingCourse] = useState(null);
    const [formData, setFormData] = useState({});
    const [showEnrollments, setShowEnrollments] = useState(false);
    const [enrollments, setEnrollments] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
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
        loadCourses();
        loadSubjects();
    }, [user, nav, kwParam, currentPage]);

    useEffect(() => {
        setSearchKw(kwParam);
    }, [kwParam]);

    const loadCourses = async () => {
        try {
            setLoading(true);
            setErr("");
            let url = endpoints['lecturer-courses'] + `?page=${currentPage}`;
            if (kwParam) {
                url += `&keyword=${kwParam}`;
            }
            let res = await authApis().get(url);
            setCourses(res.data.data?.items || []);
            setTotalPages(res.data.data?.totalPages || 1);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách khóa học.");
        } finally {
            setLoading(false);
        }
    };

    const loadSubjects = async () => {
        try {
            let res = await Apis.get(endpoints['subjects']);
            setSubjects(res.data.data || []);
        } catch (ex) {
            console.error(ex);
        }
    };

    const handleOpenCreate = () => {
        setEditingCourse(null);
        setFormData({});
        setShowModal(true);
    };

    const handleOpenEdit = (course) => {
        setEditingCourse(course);
        setFormData({
            name: course.name || "",
            description: course.description || "",
            subjectId: course.subjectId || "",
            price: course.price || 0,
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setErr("");
            if (editingCourse) {
                await authApis().put(endpoints['lecturer-course-detail'](editingCourse.id), formData);
            } else {
                await authApis().post(endpoints['lecturer-courses'], formData);
            }
            setShowModal(false);
            loadCourses();
        } catch (ex) {
            console.error(ex);
            setErr("Có lỗi xảy ra khi lưu khóa học.");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa khóa học này?")) return;
        try {
            await authApis().delete(endpoints['lecturer-course-detail'](id));
            loadCourses();
        } catch (ex) {
            console.error(ex);
            setErr("Không thể xóa khóa học.");
        }
    };

    const handleViewEnrollments = async (course) => {
        try {
            setSelectedCourse(course);
            let res = await authApis().get(endpoints['lecturer-course-enrollments'](course.id));
            setEnrollments(res.data.data || []);
            setShowEnrollments(true);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách đăng ký.");
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

    console.log(subjects);

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="mb-0">Quản lý Khóa học</h4>
                <div className="d-flex align-items-center w-50">
                    <Form onSubmit={handleSearch} className="w-100 me-3">
                        <InputGroup>
                            <Form.Control
                                type="text"
                                placeholder="Tìm kiếm khóa học..."
                                value={searchKw}
                                onChange={(e) => setSearchKw(e.target.value)}
                            />
                            <Button variant="outline-secondary" type="submit">
                                <i className="bi bi-search"></i>
                            </Button>
                        </InputGroup>
                    </Form>
                    <Button variant="primary" size="sm" onClick={handleOpenCreate} style={{ backgroundColor: '#6366f1', borderColor: '#6366f1', whiteSpace: 'nowrap' }}>
                        <i className="bi bi-plus-lg me-1"></i> Tạo khóa học
                    </Button>
                </div>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="lecturer-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên khóa học</th>
                            <th>Môn học</th>
                            <th>Giá</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {courses.map(c => (
                            <tr key={c.id}>
                                <td>{c.id}</td>
                                <td>{c.name}</td>
                                <td>{c.subject?.name || "—"}</td>
                                <td>
                                    {c.isPaid ? (
                                        <span className="text-secondary fw-bold">
                                            {c.price ? `${c.price.toLocaleString('vi-VN')} đ` : "Có phí"}
                                        </span>
                                    ) : (
                                        <span className="text-secondary fw-bold">Miễn phí</span>
                                    )}
                                </td>
                                <td>
                                    <Button variant="outline-info" size="sm" className="me-1"
                                        onClick={() => nav(`/lecturer/courses/${c.id}/lessons`)}>
                                        <i className="bi bi-list-ul"></i>
                                    </Button>
                                    <Button variant="outline-success" size="sm" className="me-1"
                                        onClick={() => handleViewEnrollments(c)}>
                                        <i className="bi bi-people"></i>
                                    </Button>
                                    <Button variant="outline-primary" size="sm" className="me-1"
                                        onClick={() => handleOpenEdit(c)}>
                                        <i className="bi bi-pencil"></i>
                                    </Button>
                                    <Button variant="outline-danger" size="sm"
                                        onClick={() => handleDelete(c.id)}>
                                        <i className="bi bi-trash"></i>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {courses.length === 0 && (
                            <tr><td colSpan="6" className="text-center text-muted py-3">Chưa có khóa học</td></tr>
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

            <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>{editingCourse ? "Sửa khóa học" : "Tạo khóa học"}</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Row>
                            <Col md={8}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Tên khóa học</Form.Label>
                                    <Form.Control type="text" value={formData.name || ''}
                                        onChange={e => setFormData({ ...formData, name: e.target.value })} required />
                                </Form.Group>
                            </Col>
                            <Col md={4}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Giá (VNĐ)</Form.Label>
                                    <Form.Control type="number" value={formData.price || 0}
                                        onChange={e => setFormData({ ...formData, price: parseInt(e.target.value) })} />
                                </Form.Group>
                            </Col>
                        </Row>
                        <Form.Group className="mb-3">
                            <Form.Label>Môn học</Form.Label>
                            <Form.Select value={formData.subjectId || ''}
                                onChange={e => setFormData({ ...formData, subjectId: parseInt(e.target.value) })}>
                                <option value="">-- Chọn môn học --</option>
                                {subjects.map(s => (
                                    <option key={s.id} value={s.id}>{s.name}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Mô tả</Form.Label>
                            <Form.Control as="textarea" rows={4} value={formData.description || ''}
                                onChange={e => setFormData({ ...formData, description: e.target.value })} />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModal(false)}>Hủy</Button>
                        <Button variant="primary" type="submit">Lưu</Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            <Modal show={showEnrollments} onHide={() => setShowEnrollments(false)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>Học viên - {selectedCourse?.name}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Table hover responsive>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tên học viên</th>
                                <th>Ngày đăng ký</th>
                                <th>Trạng thái</th>
                            </tr>
                        </thead>
                        <tbody>
                            {enrollments.map(e => (
                                <tr key={e.id}>
                                    <td>{e.id}</td>
                                    <td>{e.studentName || "—"}</td>
                                    <td>{e.enrollDate || "—"}</td>
                                    <td><Badge bg="success">{e.status || "ACTIVE"}</Badge></td>
                                </tr>
                            ))}
                            {enrollments.length === 0 && (
                                <tr><td colSpan="4" className="text-center text-muted">Chưa có học viên</td></tr>
                            )}
                        </tbody>
                    </Table>
                </Modal.Body>
            </Modal>
        </>
    );
}

export default LecturerCourse;
