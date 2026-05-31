import { useContext, useEffect, useState } from "react";
import { Badge, Container, ProgressBar } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import MySpinner from "../../components/common/MySpinner";

const LearningPath = () => {
    const [user] = useContext(MyUserContext);
    const [loading, setLoading] = useState(true);
    const nav = useNavigate();

    useEffect(() => {
        if (!user) { nav('/login'); return; }
        const t = setTimeout(() => setLoading(false), 400);
        return () => clearTimeout(t);
    }, [user, nav]);

    if (loading) return <MySpinner />;

    const steps = [
        { id: 1, title: "Nền tảng lập trình", desc: "Nắm vững cú pháp Java, OOP, Collections", status: "done", progress: 100, resources: 8, completed: 8 },
        { id: 2, title: "Cấu trúc dữ liệu & Giải thuật", desc: "Array, LinkedList, Tree, Graph, thuật toán sắp xếp", status: "done", progress: 100, resources: 6, completed: 6 },
        { id: 3, title: "Cơ sở dữ liệu", desc: "SQL, thiết kế schema, indexing, transaction", status: "active", progress: 65, resources: 5, completed: 3 },
        { id: 4, title: "Lập trình Web Frontend", desc: "HTML/CSS, JavaScript, React.js", status: "active", progress: 40, resources: 7, completed: 3 },
        { id: 5, title: "Lập trình Web Backend", desc: "Spring Boot, REST API, Security, JPA", status: "pending", progress: 0, resources: 8, completed: 0 },
        { id: 6, title: "Triển khai & DevOps", desc: "Docker, CI/CD, Cloud deployment", status: "pending", progress: 0, resources: 4, completed: 0 },
    ];

    const overallProgress = Math.round(steps.reduce((sum, s) => sum + s.progress, 0) / steps.length);

    return (
        <Container className="py-4">
            <h2 style={{ fontSize: '1.35rem', fontWeight: 700, marginBottom: '8px' }}>Lộ trình học tập</h2>
            <p className="text-muted" style={{ fontSize: '0.88rem', marginBottom: '24px' }}>
                Tiến độ tổng thể: <strong>{overallProgress}%</strong>
            </p>
            <ProgressBar now={overallProgress} style={{ height: '8px', marginBottom: '32px' }} />

            {steps.map(step => (
                <div key={step.id} className="path-step">
                    <div className={`step-marker ${step.status}`}>{step.status === "done" ? "✓" : step.id}</div>
                    <div style={{ flex: 1 }}>
                        <div className="d-flex justify-content-between align-items-start">
                            <div>
                                <h6 style={{ fontWeight: 600, marginBottom: '4px' }}>{step.title}</h6>
                                <p style={{ fontSize: '0.85rem', color: '#64748B', margin: 0 }}>{step.desc}</p>
                            </div>
                            <Badge bg={step.status === "done" ? "success" : step.status === "active" ? "primary" : "secondary"}>
                                {step.status === "done" ? "Hoàn thành" : step.status === "active" ? "Đang học" : "Chưa bắt đầu"}
                            </Badge>
                        </div>
                        {step.status !== "pending" && (
                            <div className="mt-2">
                                <div className="d-flex justify-content-between" style={{ fontSize: '0.78rem', color: '#94A3B8' }}>
                                    <span>{step.completed}/{step.resources} tài liệu</span>
                                    <span>{step.progress}%</span>
                                </div>
                                <ProgressBar now={step.progress} style={{ height: '4px', marginTop: '4px' }} variant={step.progress === 100 ? "success" : "info"} />
                            </div>
                        )}
                    </div>
                </div>
            ))}
        </Container>
    );
}
export default LearningPath;
