/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "learning_path")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LearningPath.findAll", query = "SELECT l FROM LearningPath l"),
    @NamedQuery(name = "LearningPath.findById", query = "SELECT l FROM LearningPath l WHERE l.id = :id"),
    @NamedQuery(name = "LearningPath.findByTitle", query = "SELECT l FROM LearningPath l WHERE l.title = :title"),
    @NamedQuery(name = "LearningPath.findByGeneratedByAi", query = "SELECT l FROM LearningPath l WHERE l.generatedByAi = :generatedByAi"),
    @NamedQuery(name = "LearningPath.findByGoal", query = "SELECT l FROM LearningPath l WHERE l.goal = :goal"),
    @NamedQuery(name = "LearningPath.findByCreatedAt", query = "SELECT l FROM LearningPath l WHERE l.createdAt = :createdAt"),
    @NamedQuery(name = "LearningPath.findByUpdatedAt", query = "SELECT l FROM LearningPath l WHERE l.updatedAt = :updatedAt")})
public class LearningPath implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "title")
    private String title;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Column(name = "generated_by_ai")
    private Boolean generatedByAi;
    @Size(max = 255)
    @Column(name = "goal")
    private String goal;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pathId")
    private Set<LearningPathItem> learningPathItemSet;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Student studentId;
    @JoinColumn(name = "recommended_by", referencedColumnName = "id")
    @ManyToOne
    private User recommendedBy;

    public LearningPath() {
    }

    public LearningPath(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getGeneratedByAi() {
        return generatedByAi;
    }

    public void setGeneratedByAi(Boolean generatedByAi) {
        this.generatedByAi = generatedByAi;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @XmlTransient
    public Set<LearningPathItem> getLearningPathItemSet() {
        return learningPathItemSet;
    }

    public void setLearningPathItemSet(Set<LearningPathItem> learningPathItemSet) {
        this.learningPathItemSet = learningPathItemSet;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public User getRecommendedBy() {
        return recommendedBy;
    }

    public void setRecommendedBy(User recommendedBy) {
        this.recommendedBy = recommendedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LearningPath)) {
            return false;
        }
        LearningPath other = (LearningPath) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.LearningPath[ id=" + id + " ]";
    }
    
}
