import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Form, Table , InputGroup, Pagination} from "react-bootstrap";
import { useNavigate , useSearchParams} from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Admin.css";

const AdminPayment = () => {
    const [user] = useContext(MyUserContext);
    const [payments, setPayments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const nav = useNavigate();
    const [q] = useSearchParams();
    const kwParam = q.get("kw") || "";
    const [searchKw, setSearchKw] = useState(kwParam);
    const pageParam = Number.parseInt(q.get("page"), 10);
    const currentPage = Number.isInteger(pageParam) && pageParam > 0 ? pageParam : 1;
    const [totalPages, setTotalPages] = useState(1);

    useEffect(() => {
        if (!user || user.role !== "ADMIN") { nav('/login'); return; }
        loadPayments();
    }, [user, nav, kwParam, currentPage]);

    useEffect(() => {
        setSearchKw(kwParam);
    }, [kwParam]);

    const loadPayments = async () => {
        try {
            setLoading(true);
            setErr("");
            let url = endpoints['admin-payments'] + `?page=${currentPage}`;
            if (kwParam) {
                url += `&keyword=${kwParam}`;
            }
            let res = await authApis().get(url);
            const pageData = res.data.data;
            setPayments(pageData?.items || []);
            setTotalPages(pageData?.totalPages || 1);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải danh sách giao dịch.");
        } finally {
            setLoading(false);
        }
    };

    const handleUpdateStatus = async (id, status) => {
        try {
            setErr("");
            await authApis().put(endpoints['admin-payment-status'](id), { status });
            loadPayments();
        } catch (ex) {
            console.error(ex);
            setErr("Lỗi khi cập nhật trạng thái.");
        }
    };

    const statusVariant = (status) => {
        if (status === "COMPLETED") return "success";
        if (status === "PENDING") return "warning";
        if (status === "CANCELLED") return "danger";
        return "secondary";
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
                <h4 className="mb-0">Quản lý Giao dịch</h4>
                <Form onSubmit={handleSearch} className="w-50">
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
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="admin-panel">
                <Table hover responsive className="mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Người dùng</th>
                            <th>Khóa học</th>
                            <th>Số tiền</th>
                            <th>Trạng thái</th>
                            <th>Ngày tạo</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {payments.map(p => (
                            <tr key={p.id}>
                                <td>{p.id}</td>
                                <td>{p.userName || "—"}</td>
                                <td>{p.courseName || "—"}</td>
                                <td>{p.amount?.toLocaleString('vi-VN')} đ</td>
                                <td>
                                    <Badge bg={statusVariant(p.status)}>{p.status}</Badge>
                                </td>
                                <td style={{ fontSize: '0.85rem' }}>{p.createdDate || "—"}</td>
                                <td>
                                    {p.status === "PENDING" && (
                                        <>
                                            <Button variant="success" size="sm" className="me-1"
                                                onClick={() => handleUpdateStatus(p.id, "COMPLETED")}>
                                                <i className="bi bi-check-lg"></i>
                                            </Button>
                                            <Button variant="danger" size="sm"
                                                onClick={() => handleUpdateStatus(p.id, "CANCELLED")}>
                                                <i className="bi bi-x-lg"></i>
                                            </Button>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))}
                        {payments.length === 0 && (
                            <tr><td colSpan="7" className="text-center text-muted py-3">Chưa có giao dịch</td></tr>
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
        </>
    );
}

export default AdminPayment;
