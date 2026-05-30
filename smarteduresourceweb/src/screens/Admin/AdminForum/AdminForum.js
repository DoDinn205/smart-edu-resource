import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Form, Modal, Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Admin.css";

const AdminForum = () => {
    const [user] = useContext(MyUserContext);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [editingItem, setEditingItem] = useState(null);
    const [formData, setFormData] = useState({});
    const nav = useNavigate();

    useEffect(() => {
        if (!user || user.role !== "ADMIN") { nav('/login'); return; }
        loadCategories();
    }, [user, nav]);

    const loadCategories = async () => {
        try {
            setLoading(true);
            setErr("");
            let res = await authApis().get(endpoints['admin-forum-categories']);
            setCategories(res.data.data || []);
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

    if (loading) return <MySpinner />;

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="mb-0">Quản lý Diễn đàn</h4>
                <Button variant="primary" size="sm" onClick={handleOpenCreate}>
                    <i className="bi bi-plus-lg me-1"></i> Thêm danh mục
                </Button>
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
                        <Button variant="secondary" onClick={() => setShowModal(false)}>Hủy</Button>
                        <Button variant="primary" type="submit">Lưu</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </>
    );
}

export default AdminForum;
