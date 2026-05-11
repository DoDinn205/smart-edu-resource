/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo;

import jakarta.persistence.Basic;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "student_answer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentAnswer.findAll", query = "SELECT s FROM StudentAnswer s"),
    @NamedQuery(name = "StudentAnswer.findById", query = "SELECT s FROM StudentAnswer s WHERE s.id = :id"),
    @NamedQuery(name = "StudentAnswer.findByIsCorrect", query = "SELECT s FROM StudentAnswer s WHERE s.isCorrect = :isCorrect"),
    @NamedQuery(name = "StudentAnswer.findByScore", query = "SELECT s FROM StudentAnswer s WHERE s.score = :score")})
public class StudentAnswer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "answer_text")
    private String answerText;
    @Column(name = "is_correct")
    private Boolean isCorrect;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "score")
    private Double score;
    @JoinColumn(name = "option_id", referencedColumnName = "id")
    @ManyToOne
    private AnswerOption optionId;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Question questionId;
    @JoinColumn(name = "attempt_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private QuizAttempt attemptId;

    public StudentAnswer() {
    }

    public StudentAnswer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public AnswerOption getOptionId() {
        return optionId;
    }

    public void setOptionId(AnswerOption optionId) {
        this.optionId = optionId;
    }

    public Question getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Question questionId) {
        this.questionId = questionId;
    }

    public QuizAttempt getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(QuizAttempt attemptId) {
        this.attemptId = attemptId;
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
        if (!(object instanceof StudentAnswer)) {
            return false;
        }
        StudentAnswer other = (StudentAnswer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.StudentAnswer[ id=" + id + " ]";
    }
    
}
