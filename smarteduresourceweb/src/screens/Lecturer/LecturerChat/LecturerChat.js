import { useContext, useEffect, useState } from "react";
import { Alert, Button, Form, InputGroup, Modal, Pagination, Table } from "react-bootstrap";
import { useNavigate, useSearchParams } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Lecturer.css";

const LecturerChat = () => {
    const [user] = useContext(MyUserContext);
    const [chatRooms, setChatRooms] = useState([]);
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({});

    // Invite Modal State
    const [showInvite, setShowInvite] = useState(false);
    const [inviteRoom, setInviteRoom] = useState(null);
    const [availableStudents, setAvailableStudents] = useState([]);
    const [currentParticipants, setCurrentParticipants] = useState([]);
    const [inviting, setInviting] = useState(false);

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

    const handleOpenInvite = async (room) => {
        setInviteRoom(room);
        setShowInvite(true);
        setAvailableStudents([]);
        setCurrentParticipants([]);

        try {
            const enrollRes = await authApis().get(endpoints['lecturer-course-enrollments'](room.courseId));
            const enrollments = enrollRes.data.data?.items || [];

            const partRes = await authApis().get(endpoints['lecturer-chat-participants'](room.id));
            const participants = partRes.data.data || [];
            setCurrentParticipants(participants.filter(p => p.user?.role === "STUDENT"));

            const participantIds = participants.map(p => p.user?.id);

            const available = enrollments.filter(e => e.user && !participantIds.includes(e.user.id));
            setAvailableStudents(available);
        } catch (ex) {
            console.error(ex);
            alert("Lỗi tải danh sách sinh viên. Vui lòng thử lại.");
        }
    };

    const handleAddStudent = async (studentUser) => {
        if (!studentUser) return;
        setInviting(true);
        try {
            await authApis().post(endpoints['lecturer-chat-participants'](inviteRoom.id), {
                userId: studentUser.id
            });
            await handleOpenInvite(inviteRoom);
            await loadChatRooms();
        } catch (ex) {
            console.error(ex);
            alert("Không thể thêm sinh viên vào phòng.");
        } finally {
            setInviting(false);
        }
    };

    const handleRemoveStudent = async (participant) => {
        if (!window.confirm("Xóa sinh viên này khỏi phòng chat?")) return;
        setInviting(true);
        try {
            await authApis().delete(endpoints['lecturer-chat-participant-detail'](participant.id));
            await handleOpenInvite(inviteRoom);
            await loadChatRooms();
        } catch (ex) {
            console.error(ex);
            alert("Không thể xóa sinh viên khỏi phòng.");
        } finally {
            setInviting(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setErr("");
            await authApis().post(endpoints['lecturer-chat-rooms'], formData);
            setShowModal(false);
            loadChatRooms();
        } catch (ex) {
            console.error(ex);
            setErr("Có lỗi xảy ra khi tạo phòng chat.");
        }
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
                    <Button variant="primary" size="sm" onClick={handleOpenCreate} style={{ whiteSpace: "nowrap" }}>
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
                            <th>Tên phòng</th>
                            <th>Loại</th>
                            <th>Khóa học</th>
                            <th>Thành viên</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {chatRooms.map(room => (
                            <tr key={room.id}>
                                <td>{room.id}</td>
                                <td>{room.name}</td>
                                <td>{room.type || "GROUP"}</td>
                                <td>{room.courseName || "Chưa gắn"}</td>
                                <td>{room.participantCount || 0}</td>
                                <td>
                                    <Button variant="outline-primary" size="sm" className="me-2"
                                        onClick={() => handleOpenInvite(room)}
                                        disabled={!room.courseId}
                                        title="Quản lý thành viên">
                                        <i className="bi bi-person-plus"></i>
                                    </Button>
                                    <Button variant="outline-success" size="sm" className="me-2"
                                        onClick={() => nav(`/chat?room=${room.id}`)} title="Vào phòng">
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

            <Modal show={showModal} onHide={() => setShowModal(false)}>
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
                        <Button variant="secondary" onClick={() => setShowModal(false)}>Hủy</Button>
                        <Button variant="primary" type="submit">Tạo</Button>
                    </Modal.Footer>
                </Form>
            </Modal>

            <Modal show={showInvite} onHide={() => setShowInvite(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Quản lý thành viên: {inviteRoom?.name}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <h6 className="fw-bold mb-3">Thành viên hiện tại</h6>
                    {currentParticipants.length === 0 ? (
                        <p className="text-muted small">Phòng chưa có thành viên nào.</p>
                    ) : (
                        <Table size="sm" hover className="mb-4">
                            <tbody>
                                {currentParticipants.map(p => (
                                    <tr key={p.id || p.userId}>
                                        <td className="align-middle">
                                            {p.user?.fullName || p.fullName || `User ID: ${p.userId}`}
                                        </td>
                                        <td className="align-middle text-end">
                                            <Button size="sm" variant="outline-danger"
                                                onClick={() => handleRemoveStudent(p)}
                                                disabled={inviting}>
                                                Xóa
                                            </Button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    )}

                    <h6 className="fw-bold mb-3 border-top pt-3">Thêm sinh viên mới</h6>
                    {availableStudents.length === 0 ? (
                        <p className="text-muted small">Không có sinh viên nào có thể thêm.</p>
                    ) : (
                        <Table size="sm" hover>
                            <tbody>
                                {availableStudents.map(e => (
                                    <tr key={e.id}>
                                        <td className="align-middle">
                                            {e.user?.fullName} <br />
                                            <small className="text-muted">{e.studentCode}</small>
                                        </td>
                                        <td className="align-middle text-end">
                                            <Button size="sm" variant="outline-primary"
                                                onClick={() => handleAddStudent(e.user)}
                                                disabled={inviting}>
                                                Thêm
                                            </Button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowInvite(false)}>Đóng</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default LecturerChat;
