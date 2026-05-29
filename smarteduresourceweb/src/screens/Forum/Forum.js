import { useEffect, useState } from "react";
import { Badge, Button, Container } from "react-bootstrap";
import { useNavigate, useSearchParams } from "react-router-dom";

import MySpinner from "../../components/common/MySpinner";
import { FORUM_CATEGORIES, FORUM_THREADS } from "../../configs/MockData";

const Forum = () => {
    const [loading, setLoading] = useState(true);
    const [searchParams] = useSearchParams();
    const categoryId = searchParams.get("categoryId");
    const nav = useNavigate();

    useEffect(() => {
        const t = setTimeout(() => setLoading(false), 400);
        return () => clearTimeout(t);
    }, []);

    if (loading) return <MySpinner />;

    if (!categoryId) {
        return (
            <Container className="py-4">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <h2 style={{ fontSize: '1.35rem', fontWeight: 700, margin: 0 }}>Diễn đàn</h2>
                </div>
                <div className="panel-card">
                    {FORUM_CATEGORIES.map(cat => (
                        <div key={cat.id} className="forum-cat-item" onClick={() => nav(`/forum?categoryId=${cat.id}`)}>
                            <div>
                                <h6 style={{ fontWeight: 600, marginBottom: '2px' }}>{cat.name}</h6>
                                <small className="text-muted">{cat.description}</small>
                            </div>
                            <Badge bg="secondary">{cat.threadCount} chủ đề</Badge>
                        </div>
                    ))}
                </div>
            </Container>
        );
    }

    const category = FORUM_CATEGORIES.find(c => c.id === parseInt(categoryId));
    const threads = FORUM_THREADS.filter(t => t.categoryId === parseInt(categoryId));

    return (
        <Container className="py-4">
            <a href="#!" className="detail-back" onClick={e => { e.preventDefault(); nav('/forum'); }}>← Danh mục</a>
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2 style={{ fontSize: '1.35rem', fontWeight: 700, margin: 0 }}>{category?.name || "Chủ đề"}</h2>
                <Button variant="primary" size="sm" onClick={() => nav('/forum/new-thread')}>Tạo chủ đề</Button>
            </div>
            <div className="panel-card">
                {threads.length === 0 ? (
                    <div className="empty-state"><h5>Chưa có chủ đề nào</h5></div>
                ) : threads.map(t => (
                    <div key={t.id} className="thread-row" onClick={() => nav(`/forum/threads/${t.id}`)}>
                        <div className="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 style={{ fontWeight: 600, marginBottom: '4px', fontSize: '0.92rem' }}>
                                    {t.isPinned && <Badge bg="danger" className="me-2" style={{ fontSize: '0.68rem' }}>Ghim</Badge>}
                                    {t.title}
                                </h6>
                                <small className="text-muted">{t.author.fullName} · {t.createdAt}</small>
                            </div>
                            <Badge bg="light" text="dark">{t.replyCount} trả lời</Badge>
                        </div>
                    </div>
                ))}
            </div>
        </Container>
    );
}
export default Forum;
