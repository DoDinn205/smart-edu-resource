import { useContext } from "react";
import { Container, Nav, Navbar, NavDropdown } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";

const Header = () => {
    const [user, dispatch] = useContext(MyUserContext);
    const nav = useNavigate();

    const handleLogout = () => {
        dispatch({ "type": "LOGOUT" });
        nav('/login');
    };

    return (
        <Navbar expand="lg" className="site-header">
            <Container>
                <Navbar.Brand as={Link} to="/">SmartEdu</Navbar.Brand>
                <Navbar.Toggle aria-controls="main-nav" />
                <Navbar.Collapse id="main-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/">Trang chủ</Nav.Link>
                        <Nav.Link as={Link} to="/resources">Tài liệu</Nav.Link>
                        <Nav.Link as={Link} to="/courses">Khóa học</Nav.Link>
                        {user && <Nav.Link as={Link} to="/forum">Diễn đàn</Nav.Link>}
                    </Nav>
                    <Nav className="align-items-center gap-2">
                        {user === null ? (
                            <>
                                <Nav.Link as={Link} to="/login" className="btn-outline-auth">Đăng nhập</Nav.Link>
                                <Nav.Link as={Link} to="/register/student" className="btn-primary-auth">Đăng ký</Nav.Link>
                            </>
                        ) : (
                            <NavDropdown
                                title={
                                    <span className="d-inline-flex align-items-center gap-2">
                                        <span className="user-avatar-circle">
                                            {user.fullName ? user.fullName.charAt(0) : "U"}
                                        </span>
                                        <span style={{ fontSize: '0.9rem', fontWeight: 500 }}>{user.fullName}</span>
                                    </span>
                                }
                                id="user-dropdown"
                                align="end"
                            >
                                <NavDropdown.Item as={Link} to="/profile">Hồ sơ cá nhân</NavDropdown.Item>
                                {user.role === "STUDENT" && (
                                    <>
                                        <NavDropdown.Item as={Link} to="/student/dashboard">Dashboard</NavDropdown.Item>
                                        <NavDropdown.Item as={Link} to="/my-courses">Khóa học của tôi</NavDropdown.Item>
                                        <NavDropdown.Item as={Link} to="/learning-path">Lộ trình học tập</NavDropdown.Item>
                                        <NavDropdown.Item as={Link} to="/chat">Tin nhắn</NavDropdown.Item>
                                        <NavDropdown.Item as={Link} to="/payments">Lịch sử thanh toán</NavDropdown.Item>
                                    </>
                                )}
                                {user.role === "LECTURER" && (
                                    <NavDropdown.Item as={Link} to="/lecturer/dashboard">Quản lý Giảng viên</NavDropdown.Item>
                                )}
                                {user.role === "ADMIN" && (
                                    <NavDropdown.Item as={Link} to="/admin/dashboard">Trang quản trị</NavDropdown.Item>
                                )}
                                <NavDropdown.Divider />
                                <NavDropdown.Item onClick={handleLogout}>Đăng xuất</NavDropdown.Item>
                            </NavDropdown>
                        )}
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
}

export default Header;
