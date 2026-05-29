import { useEffect, useState } from "react";
import { Button, Container, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import MySpinner from "../../components/common/MySpinner";

const MOCK_POSTS = [
    { id: 1, author: { fullName: "Nguyễn Minh Tuấn" }, content: "Mình đang gặp vấn đề khi truy vấn bảng orders có hơn 5 triệu dòng, query mất hơn 10 giây. Đã thử thêm index nhưng không cải thiện nhiều. Ai có kinh nghiệm giúp mình với.", createdAt: "2026-05-20 10:30" },
    { id: 2, author: { fullName: "Trần Thu Hà" }, content: "Bạn thử dùng EXPLAIN ANALYZE để xem execution plan. Có thể index chưa đúng cột. Ngoài ra kiểm tra xem query có sử dụng subquery không, chuyển sang JOIN sẽ nhanh hơn.", createdAt: "2026-05-20 11:15" },
    { id: 3, author: { fullName: "Phạm Đức Anh" }, content: "Thêm: nếu bảng quá lớn thì nên cân nhắc partition table theo thời gian. MySQL 8 hỗ trợ tốt tính năng này.", createdAt: "2026-05-20 14:02" },
];

const ForumThread = () => {
    const [loading, setLoading] = useState(true);
    const [posts, setPosts] = useState(MOCK_POSTS);
    const [replyText, setReplyText] = useState("");
    const nav = useNavigate();

    useEffect(() => {
        const t = setTimeout(() => setLoading(false), 400);
        return () => clearTimeout(t);
    }, []);

    const handleReply = (e) => {
        e.preventDefault();
        if (!replyText.trim()) return;
        setPosts([...posts, { id: Date.now(), author: { fullName: "Bạn" }, content: replyText, createdAt: new Date().toLocaleString('vi-VN') }]);
        setReplyText("");
    };

    if (loading) return <MySpinner />;

    return (
        <Container className="py-4">
            <a href="#!" className="detail-back" onClick={e => { e.preventDefault(); nav(-1); }}>← Quay lại</a>

            <h4 style={{ fontWeight: 700, marginBottom: '20px' }}>Cách tối ưu truy vấn SQL khi bảng có hàng triệu dòng?</h4>

            <div className="panel-card mb-4">
                <div style={{ padding: '16px' }}>
                    {posts.map(p => (
                        <div key={p.id} className="post-item">
                            <div className="d-flex justify-content-between align-items-center mb-1">
                                <div className="d-flex align-items-center gap-2">
                                    <span className="user-avatar-circle" style={{ width: '28px', height: '28px', fontSize: '0.7rem' }}>
                                        {p.author.fullName.charAt(0)}
                                    </span>
                                    <strong style={{ fontSize: '0.88rem' }}>{p.author.fullName}</strong>
                                </div>
                                <small className="text-muted">{p.createdAt}</small>
                            </div>
                            <p style={{ fontSize: '0.9rem', lineHeight: 1.6, margin: '8px 0 0' }}>{p.content}</p>
                        </div>
                    ))}
                </div>
            </div>

            <div className="panel-card" style={{ padding: '16px' }}>
                <h6 style={{ fontSize: '0.88rem', fontWeight: 600, marginBottom: '12px' }}>Trả lời</h6>
                <Form onSubmit={handleReply}>
                    <Form.Control as="textarea" rows={3} placeholder="Viết câu trả lời..." value={replyText} onChange={e => setReplyText(e.target.value)} className="mb-2" />
                    <Button type="submit" variant="primary" size="sm">Gửi</Button>
                </Form>
            </div>
        </Container>
    );
}
export default ForumThread;
