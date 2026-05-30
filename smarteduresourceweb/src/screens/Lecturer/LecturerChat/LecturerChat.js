import { useContext, useEffect, useState } from "react";
import { Alert, Button, Form, Modal, Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Lecturer.css";

const LecturerChat = () => {
    const [user] = useContext(MyUserContext);
    const [chatRooms, setChatRooms] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({});
    const nav = useNavigate();

    useEffect(() => {
        if (!user || (user.role !== "LECTURER" && user.role !== "ADMIN")) {
            nav('/login'); return;
        }
        loadChatRooms();
    }, [user, nav]);

    const loadChatRooms = async () => {
        try {
            setLoading(true);
            setErr("");
            let res = await authApis().get(endpoints['lecturer-chat-rooms']);
            setChatRooms(res.data.data || []);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách phòng chat.");
        } finally {
            setLoading(false);
        }
    };

    const handleOpenCreate = () => {
        setFormData({});
        setShowModal(true);
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

    if (loading) return <MySpinner />;

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="mb-0">Quản lý Phòng Chat</h4>
                <Button variant="primary" size="sm" onClick={handleOpenCreate}>
                    <i className="bi bi-plus-lg me-1"></i> Tạo phòng chat
                </Button>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="lecturer-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên phòng</th>
                            <th>Loại</th>
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
                                <td>{room.participantCount || 0}</td>
                                <td>
                                    <Button variant="outline-danger" size="sm"
                                        onClick={() => handleDelete(room.id)}>
                                        <i className="bi bi-trash"></i>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {chatRooms.length === 0 && (
                            <tr><td colSpan="5" className="text-center text-muted py-3">Chưa có phòng chat</td></tr>
                        )}
                    </tbody>
                </Table>
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
        </>
    );
}

export default LecturerChat;
