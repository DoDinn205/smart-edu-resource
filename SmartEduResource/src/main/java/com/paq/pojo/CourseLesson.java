package com.paq.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "course_lesson")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CourseLesson.findAll", query = "SELECT cl FROM CourseLesson cl"),
    @NamedQuery(name = "CourseLesson.findById", query = "SELECT cl FROM CourseLesson cl WHERE cl.id = :id"),
    @NamedQuery(name = "CourseLesson.findByCourse",
            query = "SELECT cl FROM CourseLesson cl WHERE cl.courseId.id = :courseId "
                  + "AND (cl.isDeleted = false OR cl.isDeleted IS NULL) "
                  + "ORDER BY cl.chapterNum ASC, cl.lessonNum ASC")
})
public class CourseLesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;

    @Basic(optional = false)
    @NotNull
    @Column(name = "chapter_num")
    private Integer chapterNum;

    @Basic(optional = false)
    @NotNull
    @Column(name = "lesson_num")
    private Integer lessonNum;

    @Column(name = "is_free")
    private Boolean isFree = false;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Course courseId;

    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Resource resourceId;

    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Quiz quizId;

    public CourseLesson() {
    }

    public CourseLesson(Integer id) {
        this.id = id;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getChapterNum() { return chapterNum; }
    public void setChapterNum(Integer chapterNum) { this.chapterNum = chapterNum; }

    public Integer getLessonNum() { return lessonNum; }
    public void setLessonNum(Integer lessonNum) { this.lessonNum = lessonNum; }

    public Boolean getIsFree() { return isFree; }
    public void setIsFree(Boolean isFree) { this.isFree = isFree; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public Course getCourseId() { return courseId; }
    public void setCourseId(Course courseId) { this.courseId = courseId; }

    public Resource getResourceId() { return resourceId; }
    public void setResourceId(Resource resourceId) { this.resourceId = resourceId; }

    public Quiz getQuizId() { return quizId; }
    public void setQuizId(Quiz quizId) { this.quizId = quizId; }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CourseLesson)) return false;
        CourseLesson other = (CourseLesson) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.paq.pojo.CourseLesson[ id=" + id + " ]";
    }
}
