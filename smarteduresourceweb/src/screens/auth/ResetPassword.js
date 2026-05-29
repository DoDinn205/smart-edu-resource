import { useState } from "react";
import { Alert, Button, Container, Form } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import MySpinner from "../../components/common/MySpinner";

const ResetPassword = () => {
    const [formData, setFormData] = useState({});
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [success, setSuccess] = useState("");
    const nav = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErr(""); setSuccess("");
        if (formData.password !== formData.confirmPassword) { setErr("Mật khẩu xác nhận không khớp."); return; }
        setLoading(true);
        try {
            await new Promise(r => setTimeout(r, 800));
            setSuccess("Đặt lại mật khẩu thành công!");
            setTimeout(() => nav('/login'), 2000);
        } catch (ex) { console.error(ex); setErr("Có lỗi xảy ra."); } finally { setLoading(false); }
    };

    return (
        <Container>
            <div className="auth-wrap">
                <div className="auth-card">
                    <h2>Đặt lại mật khẩu</h2>
                    <p className="sub">Nhập mật khẩu mới cho tài khoản của bạn</p>
                    {err && <Alert variant="danger">{err}</Alert>}
                    {success && <Alert variant="success">{success}</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Mật khẩu mới</Form.Label>
                            <Form.Control type="password" placeholder="Mật khẩu mới" value={formData.password || ''} onChange={e => setFormData({ ...formData, password: e.target.value })} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Xác nhận mật khẩu</Form.Label>
                            <Form.Control type="password" placeholder="Nhập lại mật khẩu" value={formData.confirmPassword || ''} onChange={e => setFormData({ ...formData, confirmPassword: e.target.value })} required />
                        </Form.Group>
                        {loading ? <MySpinner /> : <Button type="submit" className="btn-submit">Xác nhận</Button>}
                    </Form>
                    <div className="text-center mt-3">
                        <Link to="/login" className="auth-link">Quay lại đăng nhập</Link>
                    </div>
                </div>
            </div>
        </Container>
    );
}
export default ResetPassword;
