import { useContext, useEffect, useRef, useState } from "react";
import { Alert, Button, Form, Modal, Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import Apis, { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Lecturer.css";

const LecturerResource = () => {
    const [user] = useContext(MyUserContext);
    const [resources, setResources] = useState([]);
    const [subjects, setSubjects] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [editingResource, setEditingResource] = useState(null);
    const [formData, setFormData] = useState({});
    const fileRef = useRef();
    const nav = useNavigate();

    useEffect(() => {
        if (!user || (user.role !== "LECTURER" && user.role !== "ADMIN")) {
            nav('/login'); return;
        }
        loadResources();
        loadSubjects();
    }, [user, nav]);

    const loadResources = async () => {
        try {
            setLoading(true);
            setErr("");
            let res = await authApis().get(endpoints['lecturer-resources']);
            setResources(res.data.data || []);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách học liệu.");
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
        setEditingResource(null);
        setFormData({});
        setShowModal(true);
    };

    const handleOpenEdit = (r) => {
        setEditingResource(r);
        setFormData({
            title: r.title || "",
            description: r.description || "",
            subjectId: r.subjectId || "",
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setErr("");
            let data = new FormData();
            Object.keys(formData).forEach(key => {
                if (formData[key] !== undefined && formData[key] !== "") {
                    data.append(key, formData[key]);
                }
            });
            if (fileRef.current && fileRef.current.files[0]) {
                data.append("file", fileRef.current.files[0]);
            }

            if (editingResource) {
                await authApis().put(endpoints['lecturer-resource-detail'](editingResource.id), data, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
            } else {
                await authApis().post(endpoints['lecturer-resources'], data, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
            }
            setShowModal(false);
            loadResources();
        } catch (ex) {
            console.error(ex);
            setErr("Có lỗi xảy ra khi lưu học liệu.");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bạn có chắc chắn muốn xóa học liệu này?")) return;
        try {
            await authApis().delete(endpoints['lecturer-resource-detail'](id));
            loadResources();
        } catch (ex) {
            console.error(ex);
            setErr("Không thể xóa học liệu.");
        }
    };

    if (loading) return <MySpinner />;

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h4 className="mb-0">Quản lý Học liệu</h4>
                <Button variant="primary" size="sm" onClick={handleOpenCreate}>
                    <i className="bi bi-plus-lg me-1"></i> Upload học liệu
                </Button>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="lecturer-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tiêu đề</th>
                            <th>Môn học</th>
                            <th>Loại</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {resources.map(r => (
                            <tr key={r.id}>
                                <td>{r.id}</td>
                                <td>{r.title}</td>
                                <td>{r.subjectName || "—"}</td>
                                <td>{r.resourceTypeName || "—"}</td>
                                <td>
                                    <Button variant="outline-primary" size="sm" className="me-1"
                                        onClick={() => handleOpenEdit(r)}>
                                        <i className="bi bi-pencil"></i>
                                    </Button>
                                    <Button variant="outline-danger" size="sm"
                                        onClick={() => handleDelete(r.id)}>
                                        <i className="bi bi-trash"></i>
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        {resources.length === 0 && (
                            <tr><td colSpan="5" className="text-center text-muted py-3">Chưa có học liệu</td></tr>
                        )}
                    </tbody>
                </Table>
            </div>

            <Modal show={showModal} onHide={() => setShowModal(false)} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>{editingResource ? "Sửa học liệu" : "Upload học liệu"}</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Tiêu đề</Form.Label>
                            <Form.Control type="text" value={formData.title || ''}
                                onChange={e => setFormData({ ...formData, title: e.target.value })} required />
                        </Form.Group>
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
                            <Form.Control as="textarea" rows={3} value={formData.description || ''}
                                onChange={e => setFormData({ ...formData, description: e.target.value })} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>File tài liệu</Form.Label>
                            <Form.Control type="file" ref={fileRef} />
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

export default LecturerResource;
