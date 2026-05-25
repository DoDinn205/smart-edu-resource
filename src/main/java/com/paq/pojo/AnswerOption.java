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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "answer_option")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnswerOption.findAll", query = "SELECT a FROM AnswerOption a"),
    @NamedQuery(name = "AnswerOption.findById", query = "SELECT a FROM AnswerOption a WHERE a.id = :id"),
    @NamedQuery(name = "AnswerOption.findByIsCorrect", query = "SELECT a FROM AnswerOption a WHERE a.isCorrect = :isCorrect")})
public class AnswerOption implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content")
    private String content;
    @Column(name = "is_correct")
    private Boolean isCorrect;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Question questionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "optionId")
    private Set<StudentAnswer> studentAnswerSet;

    public AnswerOption() {
    }

    public AnswerOption(Integer id) {
        this.id = id;
    }

    public AnswerOption(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Question getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Question questionId) {
        this.questionId = questionId;
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
        if (!(object instanceof AnswerOption)) {
            return false;
        }
        AnswerOption other = (AnswerOption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.AnswerOption[ id=" + id + " ]";
    }
    
}
