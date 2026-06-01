import { useContext, useEffect, useState } from "react";
import { Alert, Button, Form, Modal, Table , InputGroup, Pagination} from "react-bootstrap";
import { useNavigate , useSearchParams} from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import useSubmissionGuard from "../../../hooks/useSubmissionGuard";
import "../Admin.css";

const AdminForum = () => {
    const [user] = useContext(MyUserContext);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [editingItem, setEditingItem] = useState(null);
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
        if (!user || user.role !== "ADMIN") { nav('/login'); return; }
        loadCategories();
    }, [user, nav, kwParam, currentPage]);

    useEffect(() => {
        setSearchKw(kwParam);
    }, [kwParam]);

    const loadCategories = async () => {
        try {
            setLoading(true);
            setErr("");
            let url = endpoints['admin-forum-categories'] + `?page=${currentPage}`;
            if (kwParam) {
                url += `&kw=${kwParam}`;
            }
            let res = await authApis().get(url);
            const pageData = res.data.data;
            setCategories(pageData?.items || []);
            setTotalPages(pageData?.totalPages || 1);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh mục diễn đàn.");
        } finally {
            setLoading(false);
        }
    };

    const handleOpenCreate = () => {
        setEditingItem(null);
        setFormData({});
        setShowModal(true);
    };

    const handleOpenEdit = (item) => {
        setEditingItem(item);
        setFormData({ name: item.name || "", description: item.description || "" });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await runSubmission(async () => {
            try {
                setErr("");
                if (editingItem) {
                    await authApis().put(endpoints['admin-forum-category-detail'](editingItem.id), formData);
                } else {
                    await authApis().post(endpoints['admin-forum-categories'], formData);
                }
                setShowModal(false);
                loadCategories();
            } catch (ex) {
                console.error(ex);
                setErr("Có lỗi xảy ra khi lưu danh mục.");
            }
        });
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa danh mục này?")) return;
        try {
            await authApis().delete(endpoints['admin-forum-category-detail'](id));
            loadCategories();
        } catch (ex) {
            console.error(ex);
            setErr("Không thể xóa danh mục.");
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
                <h4 className="mb-0">Quản lý Diễn đàn</h4>
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
                    <i className="bi bi-plus-lg me-1"></i> Thêm danh mục
                </Button>
                </div>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="admin-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên danh mục</th>
                            <th>Mô tả</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {categories.map(cat => (
                            <tr key={cat.id}>
                                <td>{cat.id}</td>
                                <td>{cat.name}</td>
                                <td style={{ fontSize: '0.85rem' }}>{cat.description || "—"}</td>
                                <td>
                                    <Button variant="outline-primary" size="sm" className="me-1"
                                        onClick={() => handleOpenEdit(cat)}>
                                        <i className="bi bi-pencil"></i>
                                    </Button>
                                    <Button variant="outline-danger" size="sm"
                                        onClick={() => handleDelete(cat.id)}>
                                        <i className="bi bi-trash"></i>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {categories.length === 0 && (
                            <tr><td colSpan="4" className="text-center text-muted py-3">Chưa có danh mục</td></tr>
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
                    <Modal.Title>{editingItem ? "Sửa" : "Thêm"} danh mục</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên danh mục</Form.Label>
                            <Form.Control type="text" value={formData.name || ''}
                                onChange={e => setFormData({ ...formData, name: e.target.value })} required />
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
        </>
    );
}

export default AdminForum;
