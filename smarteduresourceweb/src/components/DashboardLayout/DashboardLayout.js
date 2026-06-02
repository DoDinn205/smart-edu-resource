import { useContext, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import "./DashboardLayout.css";

const DashboardLayout = ({ menus, brandTitle, brandIcon, children }) => {
    const [user, dispatch] = useContext(MyUserContext);
    const [collapsed, setCollapsed] = useState(false);
    const location = useLocation();
    const nav = useNavigate();

    const handleLogout = () => {
        dispatch({ "type": "LOGOUT" });
        nav('/login');
    };

    return (
        <div className="dashboard-wrapper">
            <aside className={`dashboard-sidebar ${collapsed ? "collapsed" : ""}`}>
                <Link to="/" className="sidebar-brand">
                    <span className="brand-icon">{brandIcon || "📚"}</span>
                    <span className="brand-text">
                        {brandTitle || "SmartEdu"}
                        <span className="brand-subtitle">Management Portal</span>
                    </span>
                </Link>
                <ul className="sidebar-nav">
                    {menus.map((item, idx) => (
                        <li key={idx}>
                            <Link
                                to={item.path}
                                className={`nav-link ${location.pathname.startsWith(item.path) ? "active" : ""}`}
                            >
                                <i className={`bi ${item.icon} nav-icon`}></i>
                                <span className="nav-label">{item.title}</span>
                            </Link>
                        </li>
                    ))}
                </ul>
                <div className="sidebar-footer">
                    <span className="nav-link" onClick={handleLogout}>
                        <i className="bi bi-box-arrow-left nav-icon"></i>
                        <span className="nav-label">Đăng xuất</span>
                    </span>
                </div>
            </aside>

            <div className={`dashboard-main ${collapsed ? "expanded" : ""}`}>
                <div className="dashboard-topbar">
                    <button className="topbar-toggle" onClick={() => setCollapsed(!collapsed)}>
                        <i className="bi bi-list"></i>
                    </button>
                    <div className="topbar-user">
                        <div className="user-circle">
                            {user && user.fullName ? user.fullName.charAt(0) : "U"}
                        </div>
                        <span>{user ? user.fullName : ""}</span>
                    </div>
                </div>
                <div className="dashboard-content">
                    {children}
                </div>
            </div>
        </div>
    );
}

export default DashboardLayout;
