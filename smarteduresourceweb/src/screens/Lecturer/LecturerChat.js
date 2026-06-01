import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Form, InputGroup, Modal, Pagination, Table } from "react-bootstrap";
import { useNavigate, useSearchParams } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import { authApis, endpoints } from "../../configs/Apis";
import MySpinner from "../../components/common/MySpinner";
import useSubmissionGuard from "../../hooks/useSubmissionGuard";
import "./Lecturer.css";

const LecturerChat = () => {
    const [user] = useContext(MyUserContext);
    const [chatRooms, setChatRooms] = useState([]);
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({});
    const { isSubmitting, runSubmission } = useSubmissionGuard();

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
        loadChatRooms();
        loadCourses();
    }, [user, nav, kwParam, currentPage]);

    useEffect(() => {
        setSearchKw(kwParam);
    }, [kwParam]);

    const loadCourses = async () => {
        try {
            const res = await authApis().get(endpoints['lecturer-courses']);
            setCourses(res.data.data?.items || []);
        } catch (ex) {
            console.error("Failed to load courses:", ex);
        }
    };

    const loadChatRooms = async () => {
        try {
            setLoading(true);
            setErr("");
            let url = endpoints['lecturer-chat-rooms'] + `?page=${currentPage}`;
            if (kwParam) url += `&keyword=${kwParam}`;
            let res = await authApis().get(url);
            const pageData = res.data.data;
            setChatRooms(pageData?.items || []);
            setTotalPages(pageData?.totalPages || 1);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách phòng chat.");
        } finally {
            setLoading(false);
        }
    };

    const handleOpenCreate = () => {
        setFormData({ type: "GROUP" });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await runSubmission(async () => {
            try {
                setErr("");
                await authApis().post(endpoints['lecturer-chat-rooms'], formData);
                setShowModal(false);
                loadChatRooms();
            } catch (ex) {
                console.error(ex);
                setErr("Có lỗi xảy ra khi tạo phòng chat.");
            }
        });
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa phòng chat này?")) return;
        try {
            await authApis().delete(endpoints['lecturer-chat-room-detail'](id));
            loadChatRooms();
        } catch (ex) {
            console.error(ex);
            setErr("Không thể xóa phòng chat.");
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
                <h4 className="mb-0">Quản lý Phòng Chat</h4>
                <div className="d-flex align-items-center w-50">
                    <Form onSubmit={handleSearch} className="w-100 me-3">
                        <InputGroup>
                            <Form.Control
                                type="text"
                                placeholder="Tìm kiếm phòng chat..."
                                value={searchKw}
                                onChange={(e) => setSearchKw(e.target.value)}
                            />
                            <Button variant="outline-secondary" type="submit">
                                <i className="bi bi-search"></i>
                            </Button>
                        </InputGroup>
                    </Form>
                    <Button variant="primary" size="sm" onClick={handleOpenCreate} style={{ backgroundColor: '#6366f1', borderColor: '#6366f1', whiteSpace: 'nowrap' }}>
                        <i className="bi bi-plus-lg me-1"></i> Tạo phòng chat
                    </Button>
                </div>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="lecturer-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>TÊN PHÒNG</th>
                            <th>LOẠI</th>
                            <th>KHÓA HỌC</th>
                            <th>THÀNH VIÊN</th>
                            <th>HÀNH ĐỘNG</th>
                        </tr>
                    </thead>
                    <tbody>
                        {chatRooms.map(room => (
                            <tr key={room.id}>
                                <td>{room.id}</td>
                                <td>{room.name}</td>
                                <td>
                                    <Badge bg={room.type === 'CLASS' ? 'info' : 'secondary'}>
                                        {room.type || "GROUP"}
                                    </Badge>
                                </td>
                                <td>{room.courseName || "Chưa gắn"}</td>
                                <td>
                                    <Badge bg="primary" pill>
                                        <i className="bi bi-people-fill me-1"></i> {room.participantCount || 0}
                                    </Badge>
                                </td>
                                <td>
                                    <Button variant="outline-primary" size="sm" className="me-1"
                                        onClick={() => nav(`/lecturer/chat/${room.id}/participants`)}
                                        title="Quản lý thành viên">
                                        <i className="bi bi-person-plus"></i>
                                    </Button>
                                    <Button variant="outline-success" size="sm" className="me-1"
                                        onClick={() => nav(`/lecturer/chat/messages?room=${room.id}`)} title="Vào phòng">
                                        <i className="bi bi-chat-dots"></i>
                                    </Button>
                                    <Button variant="outline-danger" size="sm"
                                        onClick={() => handleDelete(room.id)} title="Xóa phòng">
                                        <i className="bi bi-trash"></i>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {chatRooms.length === 0 && (
                            <tr><td colSpan="6" className="text-center text-muted py-3">Chưa có phòng chat</td></tr>
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

            <Modal className="lecturer-theme" show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Tạo phòng chat</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên phòng</Form.Label>
                            <Form.Control type="text" value={formData.name || ''}
                                onChange={e => setFormData({ ...formData, name: e.target.value })} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Khóa học</Form.Label>
                            <Form.Select value={formData.courseId || ''}
                                onChange={e => setFormData({ ...formData, courseId: e.target.value })} required>
                                <option value="">-- Chọn khóa học --</option>
                                {courses.map(c => (
                                    <option key={c.id} value={c.id}>{c.name}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Loại phòng</Form.Label>
                            <Form.Select value={formData.type || 'GROUP'}
                                onChange={e => setFormData({ ...formData, type: e.target.value })}>
                                <option value="GROUP">Nhóm</option>
                                <option value="PRIVATE">Riêng tư</option>
                            </Form.Select>
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModal(false)} disabled={isSubmitting}>Hủy</Button>
                        <Button variant="primary" type="submit" disabled={isSubmitting}>
                            {isSubmitting ? "Đang tạo..." : "Tạo"}
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </>
    );
}

export default LecturerChat;
