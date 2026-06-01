import { useContext, useEffect, useRef, useState } from "react";
import { Alert, Badge, Button, Form, InputGroup, Pagination, Table, Tabs, Tab } from "react-bootstrap";
import { useNavigate, useParams, Link } from "react-router-dom";

import { MyUserContext } from "../../../configs/Context";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../../components/common/MySpinner";
import "../Lecturer.css";

const LecturerChatParticipants = () => {
    const [user] = useContext(MyUserContext);
    const { id: roomId } = useParams();
    const nav = useNavigate();
    const [room, setRoom] = useState(null);
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState("");

    const [tab, setTab] = useState("current");
    
    const [participants, setParticipants] = useState([]);
    const [pPage, setPPage] = useState(1);
    const [pSearchKw, setPSearchKw] = useState("");
    const [pKw, setPKw] = useState("");
    const [pTotalPages, setPTotalPages] = useState(1);
    const [pLoading, setPLoading] = useState(false);

    const [available, setAvailable] = useState([]);
    const [aPage, setAPage] = useState(1);
    const [aSearchKw, setASearchKw] = useState("");
    const [aKw, setAKw] = useState("");
    const [aTotalPages, setATotalPages] = useState(1);
    const [aLoading, setALoading] = useState(false);

    const [inviting, setInviting] = useState(false);
    const invitingRef = useRef(false);

    useEffect(() => {
        if (!user || (user.role !== "LECTURER" && user.role !== "ADMIN")) {
            nav('/login'); return;
        }
        loadRoom();
    }, [user, nav, roomId]);

    useEffect(() => {
        if (room) {
            if (tab === "current") loadParticipants();
            else loadAvailable();
        }
    }, [room, tab, pPage, pKw, aPage, aKw]);

    const loadRoom = async () => {
        try {
            setLoading(true);
            const res = await authApis().get(endpoints['lecturer-chat-room-detail'](roomId));
            setRoom(res.data.data);
        } catch (ex) {
            console.error(ex);
            setErr("Không thể tải thông tin phòng chat.");
        } finally {
            setLoading(false);
        }
    };

    const loadParticipants = async () => {
        if (!room) return;
        try {
            setPLoading(true);
            let url = `${endpoints['lecturer-chat-participants'](room.id)}?page=${pPage}`;
            if (pKw) url += `&kw=${pKw}`;
            const res = await authApis().get(url);
            const data = res.data.data;
            setParticipants(data?.items || []);
            setPTotalPages(data?.totalPages || 1);
        } catch (ex) {
            console.error(ex);
        } finally {
            setPLoading(false);
        }
    };

    const loadAvailable = async () => {
        if (!room) return;
        try {
            setALoading(true);
            let url = `${endpoints['lecturer-course-enrollments'](room.courseId)}?excludeRoomId=${room.id}&page=${aPage}`;
            if (aKw) url += `&kw=${aKw}`;
            const res = await authApis().get(url);
            const data = res.data.data;
            setAvailable(data?.items || []);
            setATotalPages(data?.totalPages || 1);
        } catch (ex) {
            console.error(ex);
        } finally {
            setALoading(false);
        }
    };

    const handlePSearch = (e) => {
        e.preventDefault();
        setPKw(pSearchKw);
        setPPage(1);
    };

    const handleASearch = (e) => {
        e.preventDefault();
        setAKw(aSearchKw);
        setAPage(1);
    };

    const handleAddStudent = async (studentUser) => {
        if (!studentUser || invitingRef.current) return;
        invitingRef.current = true;
        setInviting(true);
        try {
            await authApis().post(endpoints['lecturer-chat-participants'](room.id), {
                userId: studentUser.id
            });
            await loadAvailable();
            if (tab === "current") await loadParticipants();
        } catch (ex) {
            console.error(ex);
            alert("Không thể thêm sinh viên vào phòng.");
        } finally {
            invitingRef.current = false;
            setInviting(false);
        }
    };

    const handleRemoveStudent = async (participant) => {
        if (invitingRef.current || !window.confirm("Xóa sinh viên này khỏi phòng chat?")) return;
        invitingRef.current = true;
        setInviting(true);
        try {
            await authApis().delete(endpoints['lecturer-chat-participant-detail'](participant.id));
            await loadParticipants();
            if (tab === "available") await loadAvailable();
        } catch (ex) {
            console.error(ex);
            alert("Không thể xóa sinh viên khỏi phòng.");
        } finally {
            invitingRef.current = false;
            setInviting(false);
        }
    };

    if (loading) return <MySpinner />;

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div className="d-flex align-items-center">
                    <Button variant="link" className="p-0 text-dark text-decoration-none fs-4 d-flex align-items-center" onClick={() => nav('/lecturer/chat')}>
                        <i className="bi bi-arrow-left-short"></i>
                    </Button>
                    <h4 className="mb-0 ms-2">
                        Quản lý thành viên — <span className="text-primary">{room?.name}</span>
                    </h4>
                </div>
                <div className="d-flex align-items-center w-50">
                    <Form onSubmit={tab === "current" ? handlePSearch : handleASearch} className="w-100">
                        <InputGroup>
                            <Form.Control
                                type="text"
                                placeholder={tab === "current" ? "Tìm kiếm thành viên..." : "Tìm kiếm sinh viên..."}
                                value={tab === "current" ? pSearchKw : aSearchKw}
                                onChange={(e) => tab === "current" ? setPSearchKw(e.target.value) : setASearchKw(e.target.value)}
                            />
                            <Button variant="outline-secondary" type="submit">
                                <i className="bi bi-search"></i>
                            </Button>
                        </InputGroup>
                    </Form>
                </div>
            </div>

            {err && <Alert variant="danger">{err}</Alert>}

            <div className="lecturer-panel">
                <Tabs activeKey={tab} onSelect={(k) => setTab(k)} className="mb-4">
                    <Tab eventKey="current" title={<span><i className="bi bi-people-fill me-1"></i>Thành viên hiện tại</span>}>
                        {pLoading ? <MySpinner /> : (
                            <>
                                <Table hover responsive className="mb-0">
                                    <thead>
                                        <tr>
                                            <th>HỌ TÊN</th>
                                            <th>USERNAME</th>
                                            <th>VAI TRÒ</th>
                                            <th>NGÀY THAM GIA</th>
                                            <th>HÀNH ĐỘNG</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {participants.length === 0 ? (
                                            <tr><td colSpan="5" className="text-center text-muted py-4">Không tìm thấy thành viên nào.</td></tr>
                                        ) : (
                                            participants.map(p => (
                                                <tr key={p.id || p.userId}>
                                                    <td>{p.user?.fullName || p.fullName}</td>
                                                    <td>{p.user?.username || `User ID: ${p.userId}`}</td>
                                                    <td>
                                                        <Badge bg={p.user?.role === 'LECTURER' ? 'primary' : 'secondary'}>
                                                            {p.user?.role || 'STUDENT'}
                                                        </Badge>
                                                    </td>
                                                    <td>{p.joinedAt ? new Date(p.joinedAt).toLocaleDateString('vi-VN') : '—'}</td>
                                                    <td>
                                                        <Button size="sm" variant="outline-danger" className="me-1"
                                                            onClick={() => handleRemoveStudent(p)}
                                                            disabled={inviting || p.user?.role === 'LECTURER'}>
                                                            <i className="bi bi-person-dash"></i>
                                                        </Button>
                                                    </td>
                                                </tr>
                                            ))
                                        )}
                                    </tbody>
                                </Table>

                                {pTotalPages > 1 && (
                                    <div className="d-flex justify-content-center mt-4">
                                        <Pagination>
                                            {Array.from({ length: pTotalPages }, (_, i) => i + 1).map(num => (
                                                <Pagination.Item key={num} active={num === pPage} onClick={() => setPPage(num)}>
                                                    {num}
                                                </Pagination.Item>
                                            ))}
                                        </Pagination>
                                    </div>
                                )}
                            </>
                        )}
                    </Tab>
                    
                    <Tab eventKey="available" title={<span><i className="bi bi-person-plus-fill me-1"></i>Thêm sinh viên</span>}>
                        {!room?.courseId ? (
                            <Alert variant="warning" className="mt-3">
                                <i className="bi bi-exclamation-triangle me-2"></i>
                                Phòng chat này không được gắn với khóa học nào nên không thể lấy danh sách sinh viên khả dụng.
                            </Alert>
                        ) : (
                            <>
                                {aLoading ? <MySpinner /> : (
                                    <>
                                        <Table hover responsive className="mb-0">
                                            <thead>
                                                <tr>
                                                    <th>HỌ TÊN</th>
                                                    <th>MÃ SV</th>
                                                    <th>USERNAME</th>
                                                    <th>HÀNH ĐỘNG</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {available.length === 0 ? (
                                                    <tr><td colSpan="4" className="text-center text-muted py-4">Không có sinh viên nào có thể thêm.</td></tr>
                                                ) : (
                                                    available.map(e => (
                                                        <tr key={e.id}>
                                                            <td>{e.user?.fullName}</td>
                                                            <td>{e.studentCode || '—'}</td>
                                                            <td>{e.user?.username}</td>
                                                            <td>
                                                                <Button size="sm" variant="outline-primary" className="me-1"
                                                                    onClick={() => handleAddStudent(e.user)}
                                                                    disabled={inviting}>
                                                                    <i className="bi bi-person-plus"></i>
                                                                </Button>
                                                            </td>
                                                        </tr>
                                                    ))
                                                )}
                                            </tbody>
                                        </Table>

                                        {aTotalPages > 1 && (
                                            <div className="d-flex justify-content-center mt-4">
                                                <Pagination>
                                                    {Array.from({ length: aTotalPages }, (_, i) => i + 1).map(num => (
                                                        <Pagination.Item key={num} active={num === aPage} onClick={() => setAPage(num)}>
                                                            {num}
                                                        </Pagination.Item>
                                                    ))}
                                                </Pagination>
                                            </div>
                                        )}
                                    </>
                                )}
                            </>
                        )}
                    </Tab>
                </Tabs>
            </div>
        </>
    );
};

export default LecturerChatParticipants;
