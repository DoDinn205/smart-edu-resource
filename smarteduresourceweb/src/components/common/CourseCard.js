import { Badge, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { formatLevel, levelVariant, formatPrice } from "../../configs/MockData";

const CourseCard = ({ course }) => {
    const nav = useNavigate();
    const c = course;

    return (
        <div className="crs-card" onClick={() => nav(`/courses/${c.id}`)}>
            <div className="crs-card-header">
                {c.thumbnailUrl ? (
                    <img src={c.thumbnailUrl} alt={c.name} className="crs-thumbnail" />
                ) : (
                    <span className="crs-initial">{c.name.charAt(0)}</span>
                )}
                <Badge
                    className="crs-price-badge"
                    bg={c.isPaid ? "warning" : "success"}
                >
                    {c.isPaid ? "Có phí" : "Miễn phí"}
                </Badge>
            </div>
            <div className="crs-card-body">
                <div className="crs-title-row">
                    <div className="title">{c.name}</div>
                    <Badge bg={levelVariant(c.targetLevel)} className="crs-level-badge">
                        {formatLevel(c.targetLevel)}
                    </Badge>
                </div>
                <div className="desc">{c.description}</div>
                {c.subjects[0] && (
                    <div className="crs-subject-tag">{c.subjects[0].name}</div>
                )}
                <div className="crs-meta">
                    <span className="crs-meta-item">
                        {c.lecturerUser.fullName}
                    </span>
                    <span className="crs-meta-item">
                        {c.enrollmentCount} học viên
                    </span>
                </div>
                <div className="crs-footer">
                    <div className="crs-price-info">
                        {c.isPaid ? (
                            <>
                                <span className="crs-price">{formatPrice(c.price)}</span>
                                {c.originalPrice > c.price && (
                                    <span className="crs-original-price">{formatPrice(c.originalPrice)}</span>
                                )}
                            </>
                        ) : (
                            <span className="crs-price free">Miễn phí</span>
                        )}
                    </div>
                    <Button
                        variant="outline-primary"
                        size="sm"
                        className="crs-detail-btn"
                        onClick={(e) => { e.stopPropagation(); nav(`/courses/${c.id}`); }}
                    >
                        Chi tiết
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default CourseCard;
