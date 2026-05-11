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
@Table(name = "enrollment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Enrollment.findAll", query = "SELECT e FROM Enrollment e"),
    @NamedQuery(name = "Enrollment.findById", query = "SELECT e FROM Enrollment e WHERE e.id = :id"),
    @NamedQuery(name = "Enrollment.findByEnrollDate", query = "SELECT e FROM Enrollment e WHERE e.enrollDate = :enrollDate"),
    @NamedQuery(name = "Enrollment.findByOverallProgress", query = "SELECT e FROM Enrollment e WHERE e.overallProgress = :overallProgress"),
    @NamedQuery(name = "Enrollment.findByStatus", query = "SELECT e FROM Enrollment e WHERE e.status = :status"),
    @NamedQuery(name = "Enrollment.findByTotalStudyTime", query = "SELECT e FROM Enrollment e WHERE e.totalStudyTime = :totalStudyTime")})
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "enroll_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enrollDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "overall_progress")
    private Double overallProgress;
    @Size(max = 50)
    @Column(name = "status")
    private String status;
    @Column(name = "total_study_time")
    private Integer totalStudyTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enrollmentId")
    private Set<LearningLog> learningLogSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enrollmentId")
    private Set<Payment> paymentSet;
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Course courseId;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Student studentId;

    public Enrollment() {
    }

    public Enrollment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate) {
        this.enrollDate = enrollDate;
    }

    public Double getOverallProgress() {
        return overallProgress;
    }

    public void setOverallProgress(Double overallProgress) {
        this.overallProgress = overallProgress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalStudyTime() {
        return totalStudyTime;
    }

    public void setTotalStudyTime(Integer totalStudyTime) {
        this.totalStudyTime = totalStudyTime;
    }

    @XmlTransient
    public Set<LearningLog> getLearningLogSet() {
        return learningLogSet;
    }

    public void setLearningLogSet(Set<LearningLog> learningLogSet) {
        this.learningLogSet = learningLogSet;
    }

    @XmlTransient
    public Set<Payment> getPaymentSet() {
        return paymentSet;
    }

    public void setPaymentSet(Set<Payment> paymentSet) {
        this.paymentSet = paymentSet;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
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
        if (!(object instanceof Enrollment)) {
            return false;
        }
        Enrollment other = (Enrollment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.Enrollment[ id=" + id + " ]";
    }
    
}
