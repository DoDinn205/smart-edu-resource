import { useContext, useEffect, useRef, useState } from "react";
import { Alert, Button, Form, Modal, Table , InputGroup, Pagination} from "react-bootstrap";
import { useNavigate , useSearchParams} from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import Apis, { authApis, endpoints } from "../../../configs/Apis";

import MySpinner from "../../../components/common/MySpinner";
import "../Lecturer.css";

const LecturerResource = () => {
    const [user] = useContext(MyUserContext);
    const [resources, setResources] = useState([]);
    const [subjects, setSubjects] = useState([]);
    const [resourceTypes, setResourceTypes] = useState([]);
    const [loading, setLoading] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [err, setErr] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [editingResource, setEditingResource] = useState(null);
    const [formData, setFormData] = useState({});
    const fileRef = useRef();
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
        loadResources();
        loadSubjects();
        loadResourceTypes();
    }, [user, nav, kwParam, currentPage]);

    useEffect(() => {
        setSearchKw(kwParam);
    }, [kwParam]);

    const loadResources = async () => {
        try {
            setLoading(true);
            setErr("");
            let url = endpoints['lecturer-resources'] + `?page=${currentPage}`;
            if (kwParam) {
                url += `&keyword=${kwParam}`;
            }
            let res = await authApis().get(url);
            const pageData = res.data.data;
            setResources(pageData?.items || []);
            setTotalPages(pageData?.totalPages || 1);
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

    const loadResourceTypes = async () => {
        try {
            let res = await Apis.get(endpoints['resource-types']);
            setResourceTypes(res.data.data || []);
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
            fileUrl: r.fileUrl || "",
            thumbnailUrl: r.thumbnailUrl || "",
            format: r.format || "",
            fileSize: r.fileSize,
            level: r.level || "",
            pageCount: r.pageCount,
            subjectIds: r.subjects?.map(s => s.id) || [],
            topicIds: r.topics?.map(t => t.id) || [],
            tagIds: r.tags?.map(t => t.id) || [],
            typeIds: r.types?.map(t => t.id) || [],
            relatedResourceIds: r.relatedResources?.map(resource => resource.id) || [],
        });
        setShowModal(true);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (isSubmitting) return;

        try {
            setIsSubmitting(true);
            setErr("");
            let data = new FormData();
            Object.entries(formData).forEach(([key, value]) => {
                if (value !== undefined && value !== null && value !== "") {
                    if (Array.isArray(value)) {
                        value.forEach(item => data.append(key, item));
                    } else {
                        data.append(key, value);
                    }
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
        } finally {
            setIsSubmitting(false);
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
                <h4 className="mb-0">Quản lý Học liệu</h4>
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
                    <i className="bi bi-plus-lg me-1"></i> Upload học liệu
                </Button>
                </div>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="lecturer-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tiêu đề</th>
                            <th>Môn học</th>
                            <th>Độ khó</th>
                            <th>Loại</th>
                            <th>Tài liệu</th>
                            <th>Thumbnail</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {resources.map(r => (
                            <tr key={r.id}>
                                <td>{r.id}</td>
                                <td>{r.title}</td>
                                <td>{r.subjects.map(s => s.name).join(", ") || "—"}</td>
                                <td>
                                    {r.level === "BEGINNER" ? "Cơ bản" :
                                     r.level === "INTERMEDIATE" ? "Trung bình" :
                                     r.level === "ADVANCED" ? "Nâng cao" : "—"}
                                </td>
                                <td>{r.types.map(t => t.name).join(", ") || "—"}</td>
                                <td>
                                    {r.fileUrl ? (
                                        <a href={r.fileUrl} target="_blank" rel="noreferrer" className="text-decoration-none">
                                            <i className="bi bi-box-arrow-up-right me-1"></i> Xem file
                                        </a>
                                    ) : "—"}
                                </td>
                                <td>
                                    {r.thumbnailUrl ? (
                                        <a href={r.thumbnailUrl} target="_blank" rel="noreferrer" className="text-decoration-none">
                                            <i className="bi bi-box-arrow-up-right me-1"></i> Xem ảnh
                                        </a>
                                    ) : "—"}
                                </td>
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
                            <Form.Select value={formData.subjectIds?.[0] || ''}
                                onChange={e => setFormData({
                                    ...formData,
                                    subjectIds: e.target.value ? [parseInt(e.target.value)] : [],
                                })}>

                                <option value="">-- Chọn môn học --</option>
                                {subjects.map(s => (
                                    <option key={s.id} value={s.id}>{s.name}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Loại tài liệu</Form.Label>
                            <Form.Select value={formData.typeIds?.[0] || ''}
                                onChange={e => setFormData({
                                    ...formData,
                                    typeIds: e.target.value ? [parseInt(e.target.value)] : [],
                                })}>
                                <option value="">-- Chọn loại tài liệu --</option>
                                {resourceTypes.map(t => (
                                    <option key={t.id} value={t.id}>{t.name}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Độ khó (Level)</Form.Label>
                            <Form.Select value={formData.level || ''}
                                onChange={e => setFormData({ ...formData, level: e.target.value })}>
                                <option value="">-- Chọn độ khó --</option>
                                <option value="BEGINNER">Cơ bản (Beginner)</option>
                                <option value="INTERMEDIATE">Trung bình (Intermediate)</option>
                                <option value="ADVANCED">Nâng cao (Advanced)</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Mô tả</Form.Label>
                            <Form.Control as="textarea" rows={3} value={formData.description || ''}
                                onChange={e => setFormData({ ...formData, description: e.target.value })} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Thumbnail (Tùy chọn)</Form.Label>
                            <Form.Control type="file" accept="image/*" onChange={e => setFormData({ ...formData, thumbnailFile: e.target.files[0] })} />
                            {formData.thumbnailUrl && <div className="mt-2 text-muted" style={{fontSize: '0.85rem'}}>Đã có thumbnail hiện tại (chọn file mới để thay đổi)</div>}
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>File tài liệu</Form.Label>
                            <Form.Control type="file" ref={fileRef} />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModal(false)} disabled={isSubmitting}>Hủy</Button>
                        <Button variant="primary" type="submit" disabled={isSubmitting}>
                            {isSubmitting ? <><span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Đang lưu...</> : "Lưu"}
                        </Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </>
    );
}

export default LecturerResource;

