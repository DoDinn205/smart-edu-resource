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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "learning_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LearningLog.findAll", query = "SELECT l FROM LearningLog l"),
    @NamedQuery(name = "LearningLog.findById", query = "SELECT l FROM LearningLog l WHERE l.id = :id"),
    @NamedQuery(name = "LearningLog.findByStartTime", query = "SELECT l FROM LearningLog l WHERE l.startTime = :startTime"),
    @NamedQuery(name = "LearningLog.findByEndTime", query = "SELECT l FROM LearningLog l WHERE l.endTime = :endTime"),
    @NamedQuery(name = "LearningLog.findByCompletionStatus", query = "SELECT l FROM LearningLog l WHERE l.completionStatus = :completionStatus")})
public class LearningLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "completion_status")
    private Integer completionStatus;
    @JoinColumn(name = "enrollment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Enrollment enrollmentId;
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Resource resourceId;

    public LearningLog() {
    }

    public LearningLog(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(Integer completionStatus) {
        this.completionStatus = completionStatus;
    }

    public Enrollment getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Enrollment enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Resource getResourceId() {
        return resourceId;
    }

    public void setResourceId(Resource resourceId) {
        this.resourceId = resourceId;
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
        if (!(object instanceof LearningLog)) {
            return false;
        }
        LearningLog other = (LearningLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.LearningLog[ id=" + id + " ]";
    }
    
}
