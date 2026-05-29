import { useContext, useState } from "react";
import { Alert, Button, Container, Form } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import cookies from "react-cookies";

import { MyUserContext } from "../../configs/Context";
import MySpinner from "../../components/common/MySpinner";

const Login = () => {
    const [formData, setFormData] = useState({});
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const [, dispatch] = useContext(MyUserContext);
    const nav = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErr("");
        setLoading(true);
        try {
            const mockUsers = [
                { accessToken: "mock-token-student", userId: 1, username: "student1", role: "STUDENT", fullName: "Nguyễn Văn Minh", email: "student@test.com", avatar: null },
                { accessToken: "mock-token-lecturer", userId: 2, username: "lecturer1", role: "LECTURER", fullName: "TS. Nguyễn Văn An", email: "lecturer@test.com", avatar: null },
                { accessToken: "mock-token-admin", userId: 3, username: "admin1", role: "ADMIN", fullName: "Quản trị viên", email: "admin@test.com", avatar: null },
            ];
            await new Promise(r => setTimeout(r, 600));
            const found = mockUsers.find(u => u.username === formData.username);
            if (!found || formData.password !== "123456") {
                setErr("Tên đăng nhập hoặc mật khẩu không chính xác.");
                return;
            }
            cookies.save('token', found.accessToken);
            const userData = { id: found.userId, username: found.username, fullName: found.fullName, email: found.email, role: found.role, avatar: found.avatar };
            cookies.save('user', userData);
            dispatch({ "type": "LOGIN", "payload": userData });
            nav('/');
        } catch (ex) {
            console.error(ex);
            setErr("Có lỗi xảy ra, vui lòng thử lại.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container>
            <div className="auth-wrap">
                <div className="auth-card">
                    <h2>Đăng nhập</h2>
                    <p className="sub">Chào mừng bạn quay trở lại</p>
                    {err && <Alert variant="danger">{err}</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên đăng nhập</Form.Label>
                            <Form.Control type="text" placeholder="Nhập tên đăng nhập" value={formData.username || ''} onChange={e => setFormData({ ...formData, username: e.target.value })} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Mật khẩu</Form.Label>
                            <Form.Control type="password" placeholder="Nhập mật khẩu" value={formData.password || ''} onChange={e => setFormData({ ...formData, password: e.target.value })} required />
                        </Form.Group>
                        <Form.Check type="checkbox" label="Ghi nhớ đăng nhập" className="mb-3" id="remember" />
                        {loading ? <MySpinner /> : <Button type="submit" className="btn-submit">Đăng nhập</Button>}
                    </Form>
                    <div className="text-center mt-3">
                        <p className="mb-1"><Link to="/forgot-password" className="auth-link">Quên mật khẩu?</Link></p>
                        <p className="mb-0">Chưa có tài khoản? <Link to="/register/student" className="auth-link">Đăng ký</Link></p>
                    </div>
                    <div className="mock-hint">
                        <strong>Tài khoản demo:</strong> student1 / lecturer1 / admin1 &mdash; mật khẩu: 123456
                    </div>
                </div>
            </div>
        </Container>
    );
}

export default Login;
