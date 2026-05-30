import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

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

    useEffect(() => {
        if (!user || user.role !== "ADMIN") { nav('/login'); return; }
        loadPayments();
    }, [user, nav]);

    const loadPayments = async () => {
        try {
            setLoading(true);
            setErr("");
            let res = await authApis().get(endpoints['admin-payments']);
            setPayments(res.data.data || []);
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

    if (loading) return <MySpinner />;

    return (
        <>
            <h4 className="mb-4">Quản lý Giao dịch</h4>

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
            </div>
        </>
    );
}

export default AdminPayment;
