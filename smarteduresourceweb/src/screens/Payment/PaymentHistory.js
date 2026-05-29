import { useContext, useEffect, useState } from "react";
import { Badge, Container, Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import MySpinner from "../../components/common/MySpinner";
import { PAYMENTS } from "../../configs/MockData";

const PaymentHistory = () => {
    const [user] = useContext(MyUserContext);
    const [loading, setLoading] = useState(true);
    const nav = useNavigate();

    useEffect(() => {
        if (!user) { nav('/login'); return; }
        const t = setTimeout(() => setLoading(false), 400);
        return () => clearTimeout(t);
    }, [user, nav]);

    if (loading) return <MySpinner />;

    const statusBadge = (status) => {
        switch (status) {
            case "SUCCESS": return <Badge bg="success">Thành công</Badge>;
            case "PENDING": return <Badge bg="warning" text="dark">Chờ xử lý</Badge>;
            case "FAILED": return <Badge bg="danger">Thất bại</Badge>;
            default: return <Badge bg="secondary">{status}</Badge>;
        }
    };

    return (
        <Container className="py-4">
            <h2 style={{ fontSize: '1.35rem', fontWeight: 700, marginBottom: '20px' }}>Lịch sử thanh toán</h2>
            <div className="panel-card">
                <Table responsive className="payment-table mb-0">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Khóa học</th>
                            <th>Số tiền</th>
                            <th>Phương thức</th>
                            <th>Trạng thái</th>
                            <th>Ngày</th>
                        </tr>
                    </thead>
                    <tbody>
                        {PAYMENTS.map((p, idx) => (
                            <tr key={p.id}>
                                <td>{idx + 1}</td>
                                <td>{p.courseName}</td>
                                <td>{p.amount.toLocaleString('vi-VN')}đ</td>
                                <td><Badge bg="light" text="dark">{p.method}</Badge></td>
                                <td>{statusBadge(p.status)}</td>
                                <td>{p.createdAt}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </div>
        </Container>
    );
}
export default PaymentHistory;
