import { useEffect, useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

import MySpinner from "../../components/common/MySpinner";
import CourseCard from "../../components/common/CourseCard";
import { COURSES, SUBJECTS } from "../../configs/MockData";

const CourseBrowse = () => {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [filters, setFilters] = useState({ kw: "", subjectId: "", priceType: "" });
    const nav = useNavigate();

    useEffect(() => {
        const load = async () => {
            try {
                setLoading(true);
                await new Promise(r => setTimeout(r, 400));
                let list = [...COURSES];
                if (filters.kw) { const kw = filters.kw.toLowerCase(); list = list.filter(c => c.name.toLowerCase().includes(kw) || c.description.toLowerCase().includes(kw)); }
                if (filters.subjectId) list = list.filter(c => c.subject?.id === parseInt(filters.subjectId));
                if (filters.priceType === "free") list = list.filter(c => !c.isPaid);
                else if (filters.priceType === "paid") list = list.filter(c => c.isPaid);
                setCourses(list);
            } catch (ex) { console.error(ex); } finally { setLoading(false); }
        };
        load();
    }, [filters]);

    return (
        <Container className="py-4">
            <h2 style={{ fontSize: '1.35rem', fontWeight: 700, marginBottom: '20px' }}>Khóa học</h2>
            <Row className="mb-4 g-2">
                <Col md={4}>
                    <Form.Control type="text" placeholder="Tìm kiếm khóa học..." value={filters.kw} onChange={e => setFilters({ ...filters, kw: e.target.value })} style={{ fontSize: '0.88rem' }} />
                </Col>
                <Col md={3}>
                    <Form.Select value={filters.subjectId} onChange={e => setFilters({ ...filters, subjectId: e.target.value })} style={{ fontSize: '0.88rem' }}>
                        <option value="">Tất cả môn học</option>
                        {SUBJECTS.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
                    </Form.Select>
                </Col>
                <Col md={3}>
                    <Form.Select value={filters.priceType} onChange={e => setFilters({ ...filters, priceType: e.target.value })} style={{ fontSize: '0.88rem' }}>
                        <option value="">Tất cả</option>
                        <option value="free">Miễn phí</option>
                        <option value="paid">Có phí</option>
                    </Form.Select>
                </Col>
                <Col md={2}><Button variant="outline-secondary" className="w-100" size="sm" onClick={() => setFilters({ kw: "", subjectId: "", priceType: "" })}>Xóa lọc</Button></Col>
            </Row>
            {loading ? <MySpinner /> : courses.length === 0 ? (
                <div className="empty-state"><h5>Không tìm thấy khóa học</h5></div>
            ) : (
                <Row className="g-3">
                    {courses.map(c => (
                        <Col key={c.id} xs={12} sm={6} lg={4}>
                            <CourseCard course={c} />
                        </Col>
                    ))}
                </Row>
            )}
        </Container>
    );
}
export default CourseBrowse;
