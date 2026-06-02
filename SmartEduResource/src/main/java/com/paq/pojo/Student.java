/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.paq.utils.constant.EduLevelEnum;
import com.paq.utils.constant.ExpLevelEnum;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "student")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
    @NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.id = :id"),
    @NamedQuery(name = "Student.findByStudentCode", query = "SELECT s FROM Student s WHERE s.studentCode = :studentCode"),
    @NamedQuery(name = "Student.findByDob", query = "SELECT s FROM Student s WHERE s.dob = :dob"),
    @NamedQuery(name = "Student.findByGender", query = "SELECT s FROM Student s WHERE s.gender = :gender"),
    @NamedQuery(name = "Student.findByExperienceLevel", query = "SELECT s FROM Student s WHERE s.experienceLevel = :experienceLevel"),
    @NamedQuery(name = "Student.findByEducationLevel", query = "SELECT s FROM Student s WHERE s.educationLevel = :educationLevel")})
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "student_code", unique = true)
    private String studentCode;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "gender")
    private Integer gender;
    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level")
    private ExpLevelEnum experienceLevel;
    @Enumerated(EnumType.STRING)
    @Column(name = "education_level")
    private EduLevelEnum educationLevel;
    @Lob
    @Size(max = 65535)
    @Column(name = "learning_goal")
    private String learningGoal;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private User userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentId")
    @JsonIgnore
    private Set<QuizAttempt> quizAttemptSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentId")
    @JsonIgnore
    private Set<Enrollment> enrollmentSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentId")
    @JsonIgnore
    private Set<LearningAnalysis> learningAnalysisSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentId")
    @JsonIgnore
    private Set<LearningPath> learningPathSet;

    public Student() {
    }

    public Student(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public ExpLevelEnum getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExpLevelEnum experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public EduLevelEnum getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(EduLevelEnum educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getLearningGoal() {
        return learningGoal;
    }

    public void setLearningGoal(String learningGoal) {
        this.learningGoal = learningGoal;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Set<QuizAttempt> getQuizAttemptSet() {
        return quizAttemptSet;
    }

    public void setQuizAttemptSet(Set<QuizAttempt> quizAttemptSet) {
        this.quizAttemptSet = quizAttemptSet;
    }

    @XmlTransient
    public Set<Enrollment> getEnrollmentSet() {
        return enrollmentSet;
    }

    public void setEnrollmentSet(Set<Enrollment> enrollmentSet) {
        this.enrollmentSet = enrollmentSet;
    }

    @XmlTransient
    public Set<LearningAnalysis> getLearningAnalysisSet() {
        return learningAnalysisSet;
    }

    public void setLearningAnalysisSet(Set<LearningAnalysis> learningAnalysisSet) {
        this.learningAnalysisSet = learningAnalysisSet;
    }

    @XmlTransient
    public Set<LearningPath> getLearningPathSet() {
        return learningPathSet;
    }

    public void setLearningPathSet(Set<LearningPath> learningPathSet) {
        this.learningPathSet = learningPathSet;
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
        if (!(object instanceof Student)) {
            return false;
        }
        Student other = (Student) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.Student[ id=" + id + " ]";
    }
    
}
