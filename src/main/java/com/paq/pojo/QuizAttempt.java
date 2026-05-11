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
@Table(name = "quiz_attempt")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuizAttempt.findAll", query = "SELECT q FROM QuizAttempt q"),
    @NamedQuery(name = "QuizAttempt.findById", query = "SELECT q FROM QuizAttempt q WHERE q.id = :id"),
    @NamedQuery(name = "QuizAttempt.findByStartedAt", query = "SELECT q FROM QuizAttempt q WHERE q.startedAt = :startedAt"),
    @NamedQuery(name = "QuizAttempt.findBySubmittedAt", query = "SELECT q FROM QuizAttempt q WHERE q.submittedAt = :submittedAt"),
    @NamedQuery(name = "QuizAttempt.findByScore", query = "SELECT q FROM QuizAttempt q WHERE q.score = :score"),
    @NamedQuery(name = "QuizAttempt.findByStatus", query = "SELECT q FROM QuizAttempt q WHERE q.status = :status")})
public class QuizAttempt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "started_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedAt;
    @Column(name = "submitted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "score")
    private Double score;
    @Size(max = 50)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Quiz quizId;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Student studentId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "attemptId")
    private Set<StudentAnswer> studentAnswerSet;

    public QuizAttempt() {
    }

    public QuizAttempt(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Quiz getQuizId() {
        return quizId;
    }

    public void setQuizId(Quiz quizId) {
        this.quizId = quizId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    @XmlTransient
    public Set<StudentAnswer> getStudentAnswerSet() {
        return studentAnswerSet;
    }

    public void setStudentAnswerSet(Set<StudentAnswer> studentAnswerSet) {
        this.studentAnswerSet = studentAnswerSet;
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
        if (!(object instanceof QuizAttempt)) {
            return false;
        }
        QuizAttempt other = (QuizAttempt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.QuizAttempt[ id=" + id + " ]";
    }
    
}
