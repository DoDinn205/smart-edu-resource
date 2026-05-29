import { useState } from "react";
import { Alert, Button, Container, Form } from "react-bootstrap";
import { Link } from "react-router-dom";
import MySpinner from "../../components/common/MySpinner";

const ForgotPassword = () => {
    const [email, setEmail] = useState("");
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [success, setSuccess] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErr(""); setSuccess("");
        setLoading(true);
        try {
            await new Promise(r => setTimeout(r, 800));
            setSuccess("Đã gửi link đặt lại mật khẩu đến email của bạn.");
        } catch (ex) { console.error(ex); setErr("Có lỗi xảy ra."); } finally { setLoading(false); }
    };

    return (
        <Container>
            <div className="auth-wrap">
                <div className="auth-card">
                    <h2>Quên mật khẩu</h2>
                    <p className="sub">Nhập email để nhận link đặt lại mật khẩu</p>
                    {err && <Alert variant="danger">{err}</Alert>}
                    {success && <Alert variant="success">{success}</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" placeholder="Nhập email đã đăng ký" value={email} onChange={e => setEmail(e.target.value)} required />
                        </Form.Group>
                        {loading ? <MySpinner /> : <Button type="submit" className="btn-submit">Gửi link</Button>}
                    </Form>
                    <div className="text-center mt-3">
                        <Link to="/login" className="auth-link">Quay lại đăng nhập</Link>
                    </div>
                </div>
            </div>
        </Container>
    );
}
export default ForgotPassword;
