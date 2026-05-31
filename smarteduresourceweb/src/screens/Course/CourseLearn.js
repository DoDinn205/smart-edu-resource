import { useContext, useEffect, useRef, useState } from "react";
import { Alert, Badge, Button, Col, Container, Form, Row } from "react-bootstrap";
import { Link, useNavigate, useParams } from "react-router-dom";

import { MyUserContext } from "../../configs/Context";
import MySpinner from "../../components/common/MySpinner";
import { authApis, endpoints } from "../../configs/Apis";

// Mock group chat messages for the course group
const MOCK_GROUP_MSGS = [
    { id: 1, sender: "Giảng viên", isInstructor: true, isMine: false, content: "Chào cả lớp! Hãy hoàn thành bài tập và đặt câu hỏi tại đây nhé.", time: "09:00" },
    { id: 2, sender: "Nguyễn Văn B", isInstructor: false, isMine: false, content: "Thầy ơi em không hiểu phần dependency injection ạ.", time: "09:30" },
    { id: 3, sender: "Bạn", isInstructor: false, isMine: true, content: "Em cũng không hiểu phần này, thầy giải thích thêm được không ạ?", time: "09:32" },
];

const CourseLearn = () => {
    const { id } = useParams();
    const [user] = useContext(MyUserContext);
    const nav = useNavigate();

    const [learnData, setLearnData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState("");

    // Selected lesson state
    const [activeLesson, setActiveLesson] = useState(null);
    const [expandedChapters, setExpandedChapters] = useState({});

    // Tabs: 'notes' | 'group-chat' | 'dm'
    const [activeTab, setActiveTab] = useState("group-chat");

    // Group chat
    const [groupMsgs, setGroupMsgs] = useState(MOCK_GROUP_MSGS);
    const [chatInput, setChatInput] = useState("");
    const chatEndRef = useRef(null);

    useEffect(() => {
        if (!user) {
            nav(`/login?next=/courses/${id}/learn`);
            return;
        }
        loadLearnPage();
    }, [id, user]);

    const loadLearnPage = async () => {
        setLoading(true);
        setErr("");

        try {
            const response = await authApis().get(endpoints['course-learn'](id));
            const data = response.data.data;
            setLearnData(data);

            // Auto-expand first chapter and select first lesson
            if (data.chapters && data.chapters.length > 0) {
                const firstChapter = data.chapters[0];
                setExpandedChapters({ [firstChapter.chapterNum]: true });
                if (firstChapter.lessons && firstChapter.lessons.length > 0) {
                    setActiveLesson(firstChapter.lessons[0]);
                }
            }
        } catch (ex) {
            setErr(ex.response?.data?.message || "Không tìm thấy nội dung khóa học. Vui lòng thử lại.");
        } finally {
            setLoading(false);
        }
    };

    const toggleChapter = (chapterNum) => {
        setExpandedChapters(prev => ({ ...prev, [chapterNum]: !prev[chapterNum] }));
    };

    const sendGroupMsg = (e) => {
        e.preventDefault();
        if (!chatInput.trim()) return;
        setGroupMsgs(prev => [...prev, {
            id: Date.now(),
            sender: "Bạn",
            isInstructor: false,
            isMine: true,
            content: chatInput,
            time: new Date().toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
        }]);
        setChatInput("");
        setTimeout(() => chatEndRef.current?.scrollIntoView({ behavior: 'smooth' }), 50);
    };

    const getLessonIcon = (lesson) => {
        if (isLessonLocked(lesson)) return <i className="bi bi-lock-fill" />;
        if (lesson.itemType === "VIDEO") return <i className="bi bi-play-circle-fill" />;
        if (lesson.itemType === "QUIZ") return <i className="bi bi-pencil-square" />;
        return <i className="bi bi-file-earmark-text-fill" />;
    };

    const isLessonLocked = (lesson) => !lesson.isFree && !learnData?.hasAccess;

    const getLessonTypeBadge = (lesson) => {
        if (lesson.itemType === "VIDEO") return <Badge bg="danger" className="cl-type-badge">Video</Badge>;
        if (lesson.itemType === "QUIZ") return <Badge bg="warning" text="dark" className="cl-type-badge">Quiz</Badge>;
        return <Badge bg="secondary" className="cl-type-badge">Tài liệu</Badge>;
    };

    const renderViewer = () => {
        if (!activeLesson) {
            return (
                <div className="cl-viewer-empty">
                    <div className="cl-viewer-empty-icon">▶</div>
                    <div>Chọn một bài học để bắt đầu</div>
                </div>
            );
        }

        if (isLessonLocked(activeLesson)) {
            return (
                <div className="cl-viewer-empty">
                    <div className="cl-viewer-empty-icon"><i className="bi bi-lock-fill" /></div>
                    <div>Nội dung này yêu cầu đăng ký và thanh toán thành công.</div>
                </div>
            );
        }

        if (activeLesson.itemType === "VIDEO") {
            return (
                <div className="cl-video-wrapper">
                    {activeLesson.fileUrl ? (
                        <video controls className="cl-video" src={activeLesson.fileUrl}>
                            Trình duyệt không hỗ trợ phát video.
                        </video>
                    ) : (
                        <div className="cl-viewer-empty">
                            <span>▶ {activeLesson.title}</span>
                            <p className="mt-2">Video chưa có sẵn</p>
                        </div>
                    )}
                </div>
            );
        }

        if (activeLesson.itemType === "QUIZ") {
            return (
                <div className="cl-quiz-card">
                    <div className="cl-quiz-icon">✎</div>
                    <h4>{activeLesson.quizTitle || activeLesson.title}</h4>
                    {activeLesson.durationMinutes && (
                        <p className="cl-quiz-meta">{activeLesson.durationMinutes} phút · {activeLesson.questionCount || "?"} câu hỏi</p>
                    )}
                    <Button
                        className="cl-quiz-start-btn"
                        onClick={() => nav(`/quizzes/${activeLesson.quizId}/take`)}
                    >
                        Bắt đầu làm bài
                    </Button>
                </div>
            );
        }

        // DOCUMENT
        return (
            <div className="cl-doc-viewer">
                {activeLesson.fileUrl ? (
                    <iframe
                        src={activeLesson.fileUrl}
                        className="cl-doc-iframe"
                        title={activeLesson.title}
                    />
                ) : (
                    <div className="cl-viewer-empty">
                        <span>📄 {activeLesson.title}</span>
                        <p className="mt-2">Tài liệu chưa có sẵn</p>
                    </div>
                )}
            </div>
        );
    };

    if (loading) return <MySpinner />;

    if (err) {
        return (
            <Container className="py-5">
                <Alert variant="danger">{err}</Alert>
                <Link to={`/courses/${id}`} className="btn btn-outline-primary">← Quay lại khóa học</Link>
            </Container>
        );
    }

    if (!learnData) return null;

    return (
        <div className="cl-page">
            {/* Mini Header */}
            <div className="cl-topbar">
                <div className="cl-topbar-left">
                    <Link to="/courses" className="cl-topbar-logo">SmartEdu</Link>
                    <span className="cl-topbar-sep">›</span>
                    <span className="cl-topbar-course">{learnData.courseName}</span>
                </div>
            </div>

            <div className="cl-layout">
                {/* LEFT: Curriculum Sidebar */}
                <div className="cl-sidebar">
                    <div className="cl-sidebar-header">
                        <div className="cl-sidebar-title">Nội dung khóa học</div>
                        <div className="cl-sidebar-meta">
                            {learnData.totalChapters} chương · {learnData.totalLessons} bài học
                        </div>
                    </div>

                    <div className="cl-curriculum">
                        {(learnData.chapters || []).map(chapter => (
                            <div key={chapter.chapterNum} className="cl-chapter">
                                <button
                                    className="cl-chapter-header"
                                    onClick={() => toggleChapter(chapter.chapterNum)}
                                >
                                    <span>{chapter.chapterTitle}</span>
                                    <span className="cl-chapter-toggle">
                                        {expandedChapters[chapter.chapterNum] ? '▲' : '▼'}
                                    </span>
                                </button>

                                {expandedChapters[chapter.chapterNum] && (
                                    <div className="cl-lessons">
                                        {(chapter.lessons || []).map(lesson => (
                                            <button
                                                key={lesson.id}
                                                className={`cl-lesson-btn ${activeLesson?.id === lesson.id ? 'active' : ''}`}
                                                onClick={() => setActiveLesson(lesson)}
                                            >
                                                <span className="cl-lesson-icon">{getLessonIcon(lesson)}</span>
                                                <span className="cl-lesson-name">{lesson.title}</span>
                                                {isLessonLocked(lesson) && (
                                                    <span className="badge bg-danger">Đã khóa</span>
                                                )}
                                                {lesson.isFree && (
                                                    <span className="cl-free-tag">Miễn phí</span>
                                                )}
                                            </button>
                                        ))}
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                </div>

                {/* RIGHT: Main content */}
                <div className="cl-main">
                    {/* Lesson title bar */}
                    {activeLesson && (
                        <div className="cl-lesson-bar">
                            <div className="cl-lesson-bar-left">
                                {getLessonTypeBadge(activeLesson)}
                                <span className="cl-lesson-bar-title">
                                    Chương {activeLesson.chapterNum} · Bài {activeLesson.lessonNum}: {activeLesson.title}
                                </span>
                            </div>
                        </div>
                    )}

                    {/* Viewer area */}
                    <div className="cl-viewer">
                        {renderViewer()}
                    </div>

                    {/* Bottom tabs: Thảo luận nhóm | Nhắn tin GV | Ghi chú */}
                    <div className="cl-bottom">
                        <div className="cl-tab-bar">
                            {[
                                ["group-chat", `Thảo luận nhóm (${learnData.courseName?.split(' ').slice(0, 3).join(' ')})`],
                                ["dm", `Nhắn tin với ${learnData.lecturerName || 'Giảng viên'}`],
                            ].map(([key, label]) => (
                                <button
                                    key={key}
                                    className={`cl-tab-btn ${activeTab === key ? 'active' : ''}`}
                                    onClick={() => setActiveTab(key)}
                                >
                                    {label}
                                </button>
                            ))}
                        </div>

                        {/* Tab: Group Chat */}
                        {activeTab === "group-chat" && (
                            <div className="cl-chat-panel">
                                <div className="cl-messages">
                                    {groupMsgs.map(m => (
                                        <div key={m.id} className={`cl-msg-row ${m.isMine ? 'mine' : ''}`}>
                                            {!m.isMine && (
                                                <div className={`cl-msg-avatar ${m.isInstructor ? 'instructor' : ''}`}>
                                                    {m.sender.charAt(0)}
                                                </div>
                                            )}
                                            <div className="cl-msg-body">
                                                {!m.isMine && (
                                                    <span className={`cl-msg-sender ${m.isInstructor ? 'instructor' : ''}`}>
                                                        {m.sender}{m.isInstructor && ' · Giảng viên'}
                                                    </span>
                                                )}
                                                <div className={`cl-msg-bubble ${m.isMine ? 'mine' : ''}`}>
                                                    {m.content}
                                                    <span className="cl-msg-time">{m.time}</span>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                    <div ref={chatEndRef} />
                                </div>
                                <Form className="cl-chat-input" onSubmit={sendGroupMsg}>
                                    <Form.Control
                                        type="text"
                                        placeholder="Gửi tin nhắn đến cả lớp..."
                                        value={chatInput}
                                        onChange={e => setChatInput(e.target.value)}
                                    />
                                    <Button type="submit" className="cl-chat-send">Gửi</Button>
                                </Form>
                            </div>
                        )}

                        {/* Tab: DM Instructor */}
                        {activeTab === "dm" && (
                            <div className="cl-dm-panel">
                                <div className="cl-dm-info">
                                    <div className="cl-dm-avatar">{(learnData.lecturerName || "G").charAt(0)}</div>
                                    <div>
                                        <div className="cl-dm-name">{learnData.lecturerName || "Giảng viên"}</div>
                                        {learnData.lecturerTitle && (
                                            <div className="cl-dm-title">{learnData.lecturerTitle}</div>
                                        )}
                                    </div>
                                </div>
                                <p className="cl-dm-hint">
                                    Tin nhắn riêng với giảng viên sẽ được xử lý tại trang Chat.
                                </p>
                                <Button
                                    className="cl-dm-open-btn"
                                    onClick={() => nav('/chat')}
                                >
                                    Mở trang nhắn tin
                                </Button>
                            </div>
                        )}

                        {/* Tab: Notes */}
                        {activeTab === "notes" && (
                            <div className="cl-notes-panel">
                                <Form.Control
                                    as="textarea"
                                    rows={6}
                                    placeholder="Viết ghi chú cá nhân cho bài học này..."
                                    className="cl-notes-area"
                                />
                                <Button className="cl-notes-save mt-2">Lưu ghi chú</Button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CourseLearn;
