import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Form, Modal, Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Admin.css";

const AdminUser = () => {
    const [user] = useContext(MyUserContext);
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [editingStudent, setEditingStudent] = useState(null);
    const [formData, setFormData] = useState({});
    const nav = useNavigate();

    useEffect(() => {
        if (!user || user.role !== "ADMIN") { nav('/login'); return; }
        loadStudents();
    }, [user, nav]);

    const loadStudents = async () => {
        try {
            setLoading(true);
            setErr("");
            let res = await authApis().get(endpoints['admin-students']);
            setStudents(res.data.data || []);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách sinh viên.");
        } finally {
            setLoading(false);
        }
    };

    const handleOpenCreate = () => {
        setEditingStudent(null);
        setFormData({});
        setShowModal(true);
    };

    const handleOpenEdit = (s) => {
        setEditingStudent(s);
        setFormData({
            firstName: s.userId?.firstName || "",
            lastName: s.userId?.lastName || "",
            email: s.userId?.email || "",
            studentCode: s.studentCode || "",
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setErr("");
            if (editingStudent) {
                await authApis().put(endpoints['admin-student-detail'](editingStudent.id), formData);
            } else {
                await authApis().post(endpoints['admin-students'], formData);
            }
            setShowModal(false);
            loadStudents();
        } catch (ex) {
            console.error(ex);
            setErr("Có lỗi xảy ra khi lưu sinh viên.");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa sinh viên này?")) return;
        try {
            await authApis().delete(endpoints['admin-student-detail'](id));
            loadStudents();
        } catch (ex) {
            console.error(ex);
            setErr("Không thể xóa sinh viên.");
        }
    };

    const fields = [
        { field: "firstName", label: "Họ", type: "text" },
        { field: "lastName", label: "Tên", type: "text" },
        { field: "email", label: "Email", type: "email" },
        { field: "studentCode", label: "Mã sinh viên", type: "text" },
    ];

    if (loading) return <MySpinner />;

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="mb-0">Quản lý Sinh viên</h4>
                <Button variant="primary" size="sm" onClick={handleOpenCreate}>
                    <i className="bi bi-plus-lg me-1"></i> Thêm sinh viên
                </Button>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="admin-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Họ tên</th>
                            <th>Email</th>
                            <th>Mã SV</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {students.map(s => (
                            <tr key={s.id}>
                                <td>{s.id}</td>
                                <td>{s.userId?.firstName} {s.userId?.lastName}</td>
                                <td>{s.userId?.email}</td>
                                <td>{s.studentCode}</td>
                                <td>
                                    <Badge bg={s.userId?.isActive ? "success" : "secondary"}>
                                        {s.userId?.isActive ? "Hoạt động" : "Khóa"}
                                    </Badge>
                                </td>
                                <td>
                                    <Button variant="outline-primary" size="sm" className="me-1"
                                        onClick={() => handleOpenEdit(s)}>
                                        <i className="bi bi-pencil"></i>
                                    </Button>
                                    <Button variant="outline-danger" size="sm"
                                        onClick={() => handleDelete(s.id)}>
                                        <i className="bi bi-trash"></i>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {students.length === 0 && (
                            <tr><td colSpan="6" className="text-center text-muted py-3">Chưa có sinh viên nào</td></tr>
                        )}
                    </tbody>
                </Table>
            </div>

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{editingStudent ? "Sửa sinh viên" : "Thêm sinh viên"}</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        {fields.map(f => (
                            <Form.Group key={f.field} className="mb-3">
                                <Form.Label>{f.label}</Form.Label>
                                <Form.Control
                                    type={f.type}
                                    value={formData[f.field] || ''}
                                    onChange={e => setFormData({ ...formData, [f.field]: e.target.value })}
                                    required
                                />
                            </Form.Group>
                        ))}
                        {!editingStudent && (
                            <>
                                <Form.Group className="mb-3">
                                    <Form.Label>Tên đăng nhập</Form.Label>
                                    <Form.Control type="text" value={formData.username || ''}
                                        onChange={e => setFormData({ ...formData, username: e.target.value })} required />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Mật khẩu</Form.Label>
                                    <Form.Control type="password" value={formData.password || ''}
                                        onChange={e => setFormData({ ...formData, password: e.target.value })} required />
                                </Form.Group>
                            </>
                        )}
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

export default AdminUser;
