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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "resource_relation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResourceRelation.findAll", query = "SELECT r FROM ResourceRelation r"),
    @NamedQuery(name = "ResourceRelation.findById", query = "SELECT r FROM ResourceRelation r WHERE r.id = :id"),
    @NamedQuery(name = "ResourceRelation.findByRelatedType", query = "SELECT r FROM ResourceRelation r WHERE r.relatedType = :relatedType")})
public class ResourceRelation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "related_type")
    private Integer relatedType;
    @JoinColumn(name = "related_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Resource relatedId;
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Resource sourceId;

    public ResourceRelation() {
    }

    public ResourceRelation(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(Integer relatedType) {
        this.relatedType = relatedType;
    }

    public Resource getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Resource relatedId) {
        this.relatedId = relatedId;
    }

    public Resource getSourceId() {
        return sourceId;
    }

    public void setSourceId(Resource sourceId) {
        this.sourceId = sourceId;
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
        if (!(object instanceof ResourceRelation)) {
            return false;
        }
        ResourceRelation other = (ResourceRelation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.ResourceRelation[ id=" + id + " ]";
    }
    
}
