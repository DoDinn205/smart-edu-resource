/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.pojo;

import jakarta.persistence.Basic;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

import com.paq.utils.constant.DegreeEnum;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "lecturer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lecturer.findAll", query = "SELECT l FROM Lecturer l"),
    @NamedQuery(name = "Lecturer.findById", query = "SELECT l FROM Lecturer l WHERE l.id = :id"),
    @NamedQuery(name = "Lecturer.findByDegree", query = "SELECT l FROM Lecturer l WHERE l.degree = :degree"),
    @NamedQuery(name = "Lecturer.findByCertificateUrl", query = "SELECT l FROM Lecturer l WHERE l.certificateUrl = :certificateUrl"),
    @NamedQuery(name = "Lecturer.findBySpecialization", query = "SELECT l FROM Lecturer l WHERE l.specialization = :specialization"),
    @NamedQuery(name = "Lecturer.findByIsApprove", query = "SELECT l FROM Lecturer l WHERE l.isApprove = :isApprove"),
    @NamedQuery(name = "Lecturer.findByApproveAt", query = "SELECT l FROM Lecturer l WHERE l.approveAt = :approveAt")})
public class Lecturer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(name = "degree")
    private DegreeEnum degree;
    @Size(max = 255)
    @Column(name = "certificate_url")
    private String certificateUrl;
    @Size(max = 255)
    @Column(name = "specialization")
    private String specialization;
    @Lob
    @Size(max = 65535)
    @Column(name = "bio")
    private String bio;
    @Column(name = "is_approve")
    private Boolean isApprove;
    @Column(name = "approve_at")
    @Temporal(TemporalType.DATE)
    private Date approveAt;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private User userId;

    public Lecturer() {
    }

    public Lecturer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DegreeEnum getDegree() {
        return degree;
    }

    public void setDegree(DegreeEnum degree) {
        this.degree = degree;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Boolean getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(Boolean isApprove) {
        this.isApprove = isApprove;
    }

    public Date getApproveAt() {
        return approveAt;
    }

    public void setApproveAt(Date approveAt) {
        this.approveAt = approveAt;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof Lecturer)) {
            return false;
        }
        Lecturer other = (Lecturer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.Lecturer[ id=" + id + " ]";
    }
    
}
