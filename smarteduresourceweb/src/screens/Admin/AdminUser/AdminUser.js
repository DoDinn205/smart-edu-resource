import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Form, Modal, Table , InputGroup, Pagination} from "react-bootstrap";
import { useNavigate , useSearchParams} from "react-router-dom";

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
    const [q] = useSearchParams();
    const kwParam = q.get("kw") || "";
    const [searchKw, setSearchKw] = useState(kwParam);
    const pageParam = Number.parseInt(q.get("page"), 10);
    const currentPage = Number.isInteger(pageParam) && pageParam > 0 ? pageParam : 1;
    const [totalPages, setTotalPages] = useState(1);

    useEffect(() => {
        if (!user || user.role !== "ADMIN") { nav('/login'); return; }
        loadStudents();
    }, [user, nav, kwParam, currentPage]);

    useEffect(() => {
        setSearchKw(kwParam);
    }, [kwParam]);

    const loadStudents = async () => {
        try {
            setLoading(true);
            setErr("");
            let url = endpoints['admin-students'] + `?page=${currentPage}`;
            if (kwParam) url += `&kw=${kwParam}`;
            let res = await authApis().get(url);
            const pageData = res.data.data;
            setStudents(pageData?.items || []);
            setTotalPages(pageData?.totalPages || 1);
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
            fullName: s.user?.fullName || "",
            email: s.user?.email || "",
            studentCode: s.studentCode || "",
            username: s.user?.username || "",
            isActive: s.user?.isActive !== false,
            userId: s.user?.id
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setErr("");
            if (editingStudent) {
                await authApis().put(endpoints['admin-student-detail'](editingStudent.id), formData);
                if (formData.userId) {
                    await authApis().put(endpoints['admin-user-status'](formData.userId), {
                        isActive: formData.isActive
                    });
                }
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
        { field: "fullName", label: "Họ tên", type: "text" },
        { field: "email", label: "Email", type: "email" },
        { field: "studentCode", label: "Mã sinh viên", type: "text" },
    ];

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

    console.log(students);

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="mb-0">Quản lý Sinh viên</h4>
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
                    <i className="bi bi-plus-lg me-1"></i> Thêm sinh viên
                </Button>
                </div>
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
                                <td>{s.user?.fullName}</td>
                                <td>{s.user?.email}</td>
                                <td>{s.studentCode}</td>
                                <td>
                                    <Badge bg={s.user?.isActive ? "success" : "secondary"}>
                                        {s.user?.isActive ? "Hoạt động" : "Khóa"}
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
                        {editingStudent && (
                            <Form.Group className="mb-3">
                                <Form.Label>Trạng thái tài khoản (Hệ thống)</Form.Label>
                                <Form.Check
                                    type="switch"
                                    id="status-switch"
                                    label={formData.isActive ? "Đang hoạt động" : "Khóa tài khoản"}
                                    checked={formData.isActive}
                                    onChange={e => setFormData({ ...formData, isActive: e.target.checked })}
                                />
                            </Form.Group>
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
