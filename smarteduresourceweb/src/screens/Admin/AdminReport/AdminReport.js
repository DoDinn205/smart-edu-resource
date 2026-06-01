import { useContext, useEffect, useState } from "react";
import { Alert, Badge, Button, Col, Form, Row, Table } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend,
    PieChart, Pie, Cell
} from "recharts";
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Admin.css";

const COLORS = ["#6366f1", "#22c55e", "#f59e0b", "#ef4444", "#06b6d4", "#8b5cf6", "#ec4899"];

const AdminReport = () => {
    const [user] = useContext(MyUserContext);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [stats, setStats] = useState(null);
    const [fromDate, setFromDate] = useState("");
    const [toDate, setToDate] = useState("");
    const nav = useNavigate();

    useEffect(() => {
        if (!user || user.role !== "ADMIN") { nav('/login'); return; }
        loadStats();
    }, [user, nav]);

    const loadStats = async () => {
        try {
            setLoading(true);
            setErr("");
            let params = {};
            if (fromDate) params.fromDate = fromDate;
            if (toDate) params.toDate = toDate;
            let res = await authApis().get(endpoints['admin-payment-stats'], { params });
            setStats(res.data.data);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải dữ liệu báo cáo.");
        } finally {
            setLoading(false);
        }
    };

    const handleFilter = (e) => {
        e.preventDefault();
        loadStats();
    };

    const monthNames = ["", "Th1", "Th2", "Th3", "Th4", "Th5", "Th6", "Th7", "Th8", "Th9", "Th10", "Th11", "Th12"];

    const revenueChartData = (stats?.revenueByMonth || []).map(item => ({
        name: `${monthNames[item.month]}/${item.year}`,
        revenue: item.revenue,
        transactions: item.transactions,
    }));

    const methodChartData = stats?.methodCounts
        ? Object.entries(stats.methodCounts).map(([key, value]) => ({ name: key, value }))
        : [];

    const userRoleData = stats?.userRoleCounts
        ? Object.entries(stats.userRoleCounts).map(([key, value]) => ({ name: key, value }))
        : [];

    const handleExportExcel = () => {
        if (!stats) return;

        const wb = XLSX.utils.book_new();

        const summaryData = [
            ["Tiêu chí", "Giá trị"],
            ["Tổng doanh thu (VNĐ)", stats.totalRevenue || 0],
            ["Tổng giao dịch", stats.totalTransactions || 0],
            ["Giao dịch thành công", stats.successfulTransactions || 0],
            ["Giao dịch chờ xử lý", stats.pendingTransactions || 0],
            ["Giao dịch hoàn tiền", stats.refundedTransactions || 0],
            ["Giao dịch đã hủy", stats.cancelledTransactions || 0],
        ];
        const ws1 = XLSX.utils.aoa_to_sheet(summaryData);
        XLSX.utils.book_append_sheet(wb, ws1, "Tổng quan");

        if (stats.revenueByMonth && stats.revenueByMonth.length > 0) {
            const monthlyData = [["Tháng/Năm", "Doanh thu (VNĐ)", "Số giao dịch"]];
            stats.revenueByMonth.forEach(item => {
                monthlyData.push([`${item.month}/${item.year}`, item.revenue, item.transactions]);
            });
            const ws2 = XLSX.utils.aoa_to_sheet(monthlyData);
            XLSX.utils.book_append_sheet(wb, ws2, "Doanh thu theo tháng");
        }

        if (stats.methodCounts) {
            const methodData = [["Phương thức thanh toán", "Số lượng"]];
            Object.entries(stats.methodCounts).forEach(([k, v]) => methodData.push([k, v]));
            const ws3 = XLSX.utils.aoa_to_sheet(methodData);
            XLSX.utils.book_append_sheet(wb, ws3, "Phương thức thanh toán");
        }

        if (stats.userRoleCounts) {
            const roleData = [["Nhóm người dùng", "Số giao dịch"]];
            Object.entries(stats.userRoleCounts).forEach(([k, v]) => roleData.push([k, v]));
            const ws4 = XLSX.utils.aoa_to_sheet(roleData);
            XLSX.utils.book_append_sheet(wb, ws4, "Phân loại người dùng");
        }

        const wbout = XLSX.write(wb, { bookType: "xlsx", type: "array" });
        const fileName = `BaoCao_${fromDate || "all"}_${toDate || "all"}.xlsx`;
        saveAs(new Blob([wbout], { type: "application/octet-stream" }), fileName);
    };

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mt-4 mb-3">
                <h4 className="mb-0">Báo cáo & Thống kê</h4>
                <Button variant="success" size="sm" onClick={handleExportExcel} disabled={!stats}>
                    <i className="bi bi-file-earmark-excel me-1"></i> Xuất Excel
                </Button>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            {loading && <MySpinner />}

            {!loading && <div className="admin-panel" style={{ padding: '1rem 1.3rem' }}>
                <Form onSubmit={handleFilter}>
                    <Row className="align-items-end g-3">
                        <Col md={4}>
                            <Form.Group>
                                <Form.Label style={{ fontSize: '0.82rem', fontWeight: 600, color: '#64748b' }}>Từ ngày</Form.Label>
                                <Form.Control type="date" value={fromDate} onChange={e => setFromDate(e.target.value)} />
                            </Form.Group>
                        </Col>
                        <Col md={4}>
                            <Form.Group>
                                <Form.Label style={{ fontSize: '0.82rem', fontWeight: 600, color: '#64748b' }}>Đến ngày</Form.Label>
                                <Form.Control type="date" value={toDate} onChange={e => setToDate(e.target.value)} />
                            </Form.Group>
                        </Col>
                        <Col md={4}>
                            <Button type="submit" variant="primary" className="w-100">
                                <i className="bi bi-funnel me-1"></i> Lọc báo cáo
                            </Button>
                        </Col>
                    </Row>
                </Form>
            </div>}

            {!loading && stats && (
                <>
                    <div className="admin-dashboard-stats">
                        <div className="stat-card">
                            <div className="stat-icon"><i className="bi bi-currency-dollar"></i></div>
                            <div className="stat-value">{(stats.totalRevenue || 0).toLocaleString('vi-VN')} đ</div>
                            <div className="stat-label">Tổng doanh thu</div>
                        </div>
                        <div className="stat-card">
                            <div className="stat-icon"><i className="bi bi-receipt"></i></div>
                            <div className="stat-value">{stats.totalTransactions || 0}</div>
                            <div className="stat-label">Tổng giao dịch</div>
                        </div>
                        <div className="stat-card">
                            <div className="stat-icon"><i className="bi bi-check-circle"></i></div>
                            <div className="stat-value">{stats.successfulTransactions || 0}</div>
                            <div className="stat-label">Thành công</div>
                        </div>
                        <div className="stat-card">
                            <div className="stat-icon"><i className="bi bi-hourglass-split"></i></div>
                            <div className="stat-value">{stats.pendingTransactions || 0}</div>
                            <div className="stat-label">Chờ xử lý</div>
                        </div>
                    </div>

                    <Row className="g-4 mb-4">
                        <Col lg={8}>
                            <div className="admin-panel">
                                <div className="panel-header">Doanh thu theo thời gian</div>
                                <div style={{ padding: '1rem' }}>
                                    {revenueChartData.length > 0 ? (
                                        <ResponsiveContainer width="100%" height={300}>
                                            <BarChart data={revenueChartData}>
                                                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                                                <XAxis dataKey="name" tick={{ fontSize: 12 }} />
                                                <YAxis tick={{ fontSize: 12 }}
                                                    tickFormatter={v => v >= 1000000 ? `${(v / 1000000).toFixed(1)}M` : v >= 1000 ? `${(v / 1000).toFixed(0)}K` : v} />
                                                <Tooltip formatter={(v) => `${v.toLocaleString('vi-VN')} đ`} />
                                                <Legend />
                                                <Bar dataKey="revenue" name="Doanh thu" fill="#6366f1" radius={[4, 4, 0, 0]} />
                                            </BarChart>
                                        </ResponsiveContainer>
                                    ) : (
                                        <p className="text-center text-muted py-4">Chưa có dữ liệu doanh thu</p>
                                    )}
                                </div>
                            </div>
                        </Col>
                        <Col lg={4}>
                            <div className="admin-panel">
                                <div className="panel-header">Phương thức thanh toán</div>
                                <div style={{ padding: '1rem' }}>
                                    {methodChartData.length > 0 ? (
                                        <ResponsiveContainer width="100%" height={300}>
                                            <PieChart>
                                                <Pie data={methodChartData} cx="50%" cy="50%" outerRadius={100}
                                                    dataKey="value" label={({ name, percent }) => `${name} (${(percent * 100).toFixed(0)}%)`}>
                                                    {methodChartData.map((_, idx) => (
                                                        <Cell key={idx} fill={COLORS[idx % COLORS.length]} />
                                                    ))}
                                                </Pie>
                                                <Tooltip />
                                            </PieChart>
                                        </ResponsiveContainer>
                                    ) : (
                                        <p className="text-center text-muted py-4">Chưa có dữ liệu</p>
                                    )}
                                </div>
                            </div>
                        </Col>
                    </Row>

                    <Row className="g-4">
                        <Col lg={6}>
                            <div className="admin-panel">
                                <div className="panel-header">Phân loại theo nhóm người dùng</div>
                                <Table hover className="mb-0">
                                    <thead>
                                        <tr>
                                            <th>Nhóm</th>
                                            <th>Số giao dịch</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {userRoleData.map((item, idx) => (
                                            <tr key={idx}>
                                                <td>
                                                    <Badge bg={item.name === "STUDENT" ? "primary" : item.name === "LECTURER" ? "info" : "secondary"}>
                                                        {item.name}
                                                    </Badge>
                                                </td>
                                                <td>{item.value}</td>
                                            </tr>
                                        ))}
                                        {userRoleData.length === 0 && (
                                            <tr><td colSpan="2" className="text-center text-muted">Chưa có dữ liệu</td></tr>
                                        )}
                                    </tbody>
                                </Table>
                            </div>
                        </Col>
                        <Col lg={6}>
                            <div className="admin-panel">
                                <div className="panel-header">Chi tiết trạng thái giao dịch</div>
                                <Table hover className="mb-0">
                                    <thead>
                                        <tr>
                                            <th>Trạng thái</th>
                                            <th>Số lượng</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td><Badge bg="success">Thành công</Badge></td>
                                            <td>{stats.successfulTransactions || 0}</td>
                                        </tr>
                                        <tr>
                                            <td><Badge bg="warning" text="dark">Chờ xử lý</Badge></td>
                                            <td>{stats.pendingTransactions || 0}</td>
                                        </tr>
                                        <tr>
                                            <td><Badge bg="info">Hoàn tiền</Badge></td>
                                            <td>{stats.refundedTransactions || 0}</td>
                                        </tr>
                                        <tr>
                                            <td><Badge bg="danger">Đã hủy</Badge></td>
                                            <td>{stats.cancelledTransactions || 0}</td>
                                        </tr>
                                    </tbody>
                                </Table>
                            </div>
                        </Col>
                    </Row>
                </>
            )}
        </>
    );
}

export default AdminReport;
