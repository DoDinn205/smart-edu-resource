import { useContext, useEffect, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import { useNavigate, useSearchParams } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import { authApis, endpoints } from "../../configs/Apis";
import MySpinner from "../../components/common/MySpinner";

const MOCK_MESSAGES = [
    { id: 1, sender: "TS. Nguyễn Văn An", content: "Tuần này các em hoàn thành bài tập chương 3 nhé", isMine: false, time: "09:00" },
    { id: 2, sender: "Bạn", content: "Dạ vâng thầy. Em có thắc mắc về phần JPA ạ", isMine: true, time: "09:15" },
    { id: 3, sender: "TS. Nguyễn Văn An", content: "Em cứ hỏi, thầy sẽ giải đáp", isMine: false, time: "09:20" },
    { id: 4, sender: "Bạn", content: "Em cảm ơn thầy ạ", isMine: true, time: "10:20" },
];

const Chat = () => {
    const [user] = useContext(MyUserContext);
    const [loading, setLoading] = useState(true);
    const [rooms, setRooms] = useState([]);
    const [activeRoom, setActiveRoom] = useState(null);
    const [messages, setMessages] = useState(MOCK_MESSAGES);
    const [msgText, setMsgText] = useState("");
    const nav = useNavigate();
    const [q] = useSearchParams();
    const roomIdParam = q.get("room");

    useEffect(() => {
        if (!user) { nav('/login'); return; }
        loadRooms();
    }, [user, nav]);

    const loadRooms = async () => {
        setLoading(true);
        try {
            let url = (user.role === 'LECTURER' || user.role === 'ADMIN')
                ? `${endpoints['lecturer-chat-rooms']}?lecturerId=${user.id}`
                : endpoints['chat-rooms'];

            const res = await authApis().get(url);
            const data = res.data.data;
            const loadedRooms = data?.items || data || [];
            setRooms(loadedRooms);

            if (roomIdParam) {
                const targetRoom = loadedRooms.find(r => r.id.toString() === roomIdParam);
                if (targetRoom) setActiveRoom(targetRoom);
                else if (loadedRooms.length > 0) setActiveRoom(loadedRooms[0]);
            } else if (loadedRooms.length > 0) {
                setActiveRoom(loadedRooms[0]);
            }
        } catch (ex) {
            console.error("Failed to load chat rooms", ex);
        } finally {
            setLoading(false);
        }
    };

    const handleSend = (e) => {
        e.preventDefault();
        if (!msgText.trim()) return;
        setMessages([...messages, { id: Date.now(), sender: "Bạn", content: msgText, isMine: true, time: new Date().toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' }) }]);
        setMsgText("");
    };

    if (loading) return <MySpinner />;

    return (
        <Container className="py-4">
            <h2 style={{ fontSize: '1.35rem', fontWeight: 700, marginBottom: '16px' }}>Tin nhắn</h2>
            <div className="chat-layout">
                <div className="chat-sidebar">
                    {rooms.map(room => (
                        <div key={room.id} className={`room-item ${activeRoom?.id === room.id ? 'active' : ''}`} onClick={() => setActiveRoom(room)}>
                            <div className="room-name">{room.name}</div>
                            <div className="room-last">{room.courseName || 'Phòng chung'}</div>
                        </div>
                    ))}
                    {rooms.length === 0 && (
                        <div className="text-muted text-center mt-4">Không có phòng chat nào</div>
                    )}
                </div>
                <div className="chat-main">
                    {activeRoom ? (
                        <>
                            <div style={{ padding: '12px 16px', borderBottom: '1px solid #E2E8F0', fontWeight: 600, fontSize: '0.95rem' }}>
                                {activeRoom.name}
                            </div>
                            <div className="chat-messages">
                                {messages.map(m => (
                                    <div key={m.id}>
                                        {!m.isMine && <div style={{ fontSize: '0.72rem', color: '#94A3B8', marginBottom: '2px' }}>{m.sender}</div>}
                                        <div className={`msg-bubble ${m.isMine ? 'mine' : 'other'}`}>
                                            {m.content}
                                            <div style={{ fontSize: '0.68rem', opacity: 0.7, textAlign: 'right', marginTop: '2px' }}>{m.time}</div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                            <Form onSubmit={handleSend} className="chat-input-bar">
                                <Form.Control type="text" placeholder="Nhập tin nhắn..." value={msgText} onChange={e => setMsgText(e.target.value)} />
                                <Button type="submit" variant="primary" size="sm">Gửi</Button>
                            </Form>
                        </>
                    ) : (
                        <div className="d-flex align-items-center justify-content-center flex-grow-1 text-muted">
                            Chọn cuộc hội thoại
                        </div>
                    )}
                </div>
            </div>
        </Container>
    );
}
export default Chat;
