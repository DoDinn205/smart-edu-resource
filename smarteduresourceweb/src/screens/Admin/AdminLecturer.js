import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Form, Modal, Table, InputGroup, Pagination } from "react-bootstrap";
import { useNavigate, useSearchParams } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import { authApis, endpoints } from "../../configs/Apis";
import MySpinner from "../../components/common/MySpinner";
import useSubmissionGuard from "../../hooks/useSubmissionGuard";
import "./Admin.css";

const AdminLecturer = () => {
    const [user] = useContext(MyUserContext);
    const [lecturers, setLecturers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [editingLecturer, setEditingLecturer] = useState(null);
    const [formData, setFormData] = useState({});
    const [certificateFile, setCertificateFile] = useState(null);
    const { isSubmitting, runSubmission } = useSubmissionGuard();
    const { isSubmitting: isUpdatingApproval, runSubmission: runApprovalUpdate } = useSubmissionGuard();
    const nav = useNavigate();
    const [q] = useSearchParams();
    const kwParam = q.get("kw") || "";
    const [searchKw, setSearchKw] = useState(kwParam);
    const pageParam = Number.parseInt(q.get("page"), 10);
    const currentPage = Number.isInteger(pageParam) && pageParam > 0 ? pageParam : 1;
    const [totalPages, setTotalPages] = useState(1);

    useEffect(() => {
        if (!user || user.role !== "ADMIN") { nav('/login'); return; }
        loadLecturers();
    }, [user, nav, kwParam, currentPage]);

    useEffect(() => {
        setSearchKw(kwParam);
    }, [kwParam]);

    const loadLecturers = async () => {
        try {
            setLoading(true);
            setErr("");
            let url = endpoints['admin-lecturers'] + `?page=${currentPage}`;
            if (kwParam) url += `&kw=${kwParam}`;
            let res = await authApis().get(url);
            const pageData = res.data.data;
            setLecturers(pageData?.items || []);
            setTotalPages(pageData?.totalPages || 1);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách giảng viên.");
        } finally {
            setLoading(false);
        }
    };

    const handleApprove = async (id, approved) => {
        await runApprovalUpdate(async () => {
            try {
                setErr("");
                await authApis().put(endpoints['admin-lecturer-approval'](id), { isApprove: approved });
                await loadLecturers();
            } catch (ex) {
                console.error(ex);
                setErr("Lỗi khi cập nhật trạng thái duyệt.");
            }
        });
    };

    const handleOpenCreate = () => {
        setEditingLecturer(null);
        setFormData({});
        setCertificateFile(null);
        setShowModal(true);
    };

    const handleOpenEdit = (lec) => {
        setEditingLecturer(lec);
        setCertificateFile(null);
        setFormData({
            fullName: lec.user?.fullName || "",
            email: lec.user?.email || "",
            username: lec.user?.username || "",
            specialization: lec.specialization || "",
            degree: lec.degree || "",
            certificateUrl: lec.certificateUrl || "",
            isActive: lec.user?.isActive !== false,
            userId: lec.user?.id
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await runSubmission(async () => {
            try {
                setErr("");
                const payload = new FormData();
                ["fullName", "email", "username", "password", "phone", "degree", "specialization", "bio"]
                    .forEach(field => {
                        if (formData[field] !== undefined && formData[field] !== "") {
                            payload.append(field, formData[field]);
                        }
                    });
                if (certificateFile) {
                    payload.append("certificate", certificateFile);
                }
                if (editingLecturer) {
                    await authApis().put(endpoints['admin-lecturer-detail'](editingLecturer.id), payload);
                    if (formData.userId) {
                        await authApis().put(endpoints['admin-user-status'](formData.userId), {
                            isActive: formData.isActive
                        });
                    }
                } else {
                    await authApis().post(endpoints['admin-lecturers'], payload);
                }
                setShowModal(false);
                loadLecturers();
            } catch (ex) {
                console.error(ex);
                setErr("Có lỗi xảy ra khi lưu giảng viên.");
            }
        });
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa giảng viên này?")) return;
        try {
            await authApis().delete(endpoints['admin-lecturer-detail'](id));
            loadLecturers();
        } catch (ex) {
            console.error(ex);
            setErr("Không thể xóa giảng viên.");
        }
    };

    const fields = [
        { field: "fullName", label: "Họ tên", type: "text" },
        { field: "email", label: "Email", type: "email" },
        { field: "specialization", label: "Chuyên môn", type: "text" },
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

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="mb-0">Quản lý Giảng viên</h4>
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
                        <i className="bi bi-plus-lg me-1"></i> Thêm giảng viên
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
                            <th>Trình độ</th>
                            <th>Chuyên môn</th>
                            <th>Portfolio</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {lecturers.map(lec => (
                            <tr key={lec.id}>
                                <td>{lec.id}</td>
                                <td>{lec.user?.fullName}</td>
                                <td>{lec.user?.email}</td>
                                <td>{lec.degree || <span className="text-muted fst-italic" style={{ fontSize: '0.85rem' }}>Chưa có</span>}</td>
                                <td>{lec.specialization || <span className="text-muted fst-italic" style={{ fontSize: '0.85rem' }}>Chưa có</span>}</td>
                                <td>
                                    {lec.certificateUrl ? (
                                        <a href={lec.certificateUrl} target="_blank" rel="noreferrer" className="text-decoration-none">
                                            <i className="bi bi-link-45deg"></i> Xem link
                                        </a>
                                    ) : (
                                        <span className="text-muted fst-italic" style={{ fontSize: '0.85rem' }}>Không có</span>
                                    )}
                                </td>
                                <td>
                                    {lec.isApprove ?
                                        <Badge bg="success">Đã duyệt</Badge> :
                                        <Badge bg="warning" text="dark">Chờ duyệt</Badge>
                                    }
                                </td>
                                <td>
                                    {!lec.isApprove && (
                                        <Button
                                            variant="success"
                                            size="sm"
                                            className="me-1"
                                            onClick={() => handleApprove(lec.id, true)}
                                            disabled={isUpdatingApproval}
                                        >
                                            <i className="bi bi-check-lg"></i> Duyệt
                                        </Button>
                                    )}
                                    {lec.isApprove && (
                                        <Button
                                            variant="warning"
                                            size="sm"
                                            className="me-1"
                                            onClick={() => handleApprove(lec.id, false)}
                                            disabled={isUpdatingApproval}
                                        >
                                            <i className="bi bi-x-circle"></i> Hủy duyệt
                                        </Button>
                                    )}
                                    <Button variant="outline-primary" size="sm" className="me-1"
                                        onClick={() => handleOpenEdit(lec)}>
                                        <i className="bi bi-pencil"></i>
                                    </Button>
                                    <Button variant="outline-danger" size="sm"
                                        onClick={() => handleDelete(lec.id)}>
                                        <i className="bi bi-trash"></i>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {lecturers.length === 0 && (
                            <tr><td colSpan="6" className="text-center text-muted py-3">Chưa có giảng viên nào</td></tr>
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

            <Modal className="admin-theme" show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{editingLecturer ? "Sửa giảng viên" : "Thêm giảng viên"}</Modal.Title>
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
                        <Form.Group className="mb-3">
                            <Form.Label>Trình độ học vấn</Form.Label>
                            <Form.Select
                                value={formData.degree || ""}
                                onChange={e => setFormData({ ...formData, degree: e.target.value })}
                                required
                            >
                                <option value="">Chọn trình độ</option>
                                <option value="MASTER">Thạc sĩ</option>
                                <option value="PHD">Tiến sĩ</option>
                                <option value="ASSOCPROF">Phó giáo sư</option>
                                <option value="PROFESSOR">Giáo sư</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Chứng chỉ (PDF)</Form.Label>
                            <Form.Control
                                type="file"
                                accept="application/pdf,.pdf"
                                onChange={e => setCertificateFile(e.target.files?.[0] || null)}
                                required={!editingLecturer}
                            />
                            {editingLecturer && formData.certificateUrl && (
                                <Form.Text>
                                    <a href={formData.certificateUrl} target="_blank" rel="noreferrer">
                                        Xem chứng chỉ hiện tại
                                    </a>
                                </Form.Text>
                            )}
                        </Form.Group>
                        {!editingLecturer && (
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
                        {editingLecturer && (
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
                        <Button variant="secondary" onClick={() => setShowModal(false)} disabled={isSubmitting}>Hủy</Button>
                        <Button variant="primary" type="submit" disabled={isSubmitting}>
                            {isSubmitting ? "Đang lưu..." : "Lưu"}
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </>
    );
}

export default AdminLecturer;
