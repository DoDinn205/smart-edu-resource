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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

import com.paq.utils.constant.PathItemTypeEnum;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "learning_path_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LearningPathItem.findAll", query = "SELECT l FROM LearningPathItem l"),
    @NamedQuery(name = "LearningPathItem.findById", query = "SELECT l FROM LearningPathItem l WHERE l.id = :id"),
    @NamedQuery(name = "LearningPathItem.findByItemType", query = "SELECT l FROM LearningPathItem l WHERE l.itemType = :itemType"),
    @NamedQuery(name = "LearningPathItem.findByReferenceId", query = "SELECT l FROM LearningPathItem l WHERE l.referenceId = :referenceId"),
    @NamedQuery(name = "LearningPathItem.findByOrderNumber", query = "SELECT l FROM LearningPathItem l WHERE l.orderNumber = :orderNumber"),
    @NamedQuery(name = "LearningPathItem.findByIsRequired", query = "SELECT l FROM LearningPathItem l WHERE l.isRequired = :isRequired")})
public class LearningPathItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type")
    private PathItemTypeEnum itemType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reference_id")
    private int referenceId;
    @Column(name = "order_number")
    private Integer orderNumber;
    @Column(name = "is_required")
    private Boolean isRequired;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @JoinColumn(name = "path_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LearningPath pathId;

    public LearningPathItem() {
    }

    public LearningPathItem(Integer id) {
        this.id = id;
    }

    public LearningPathItem(Integer id, int referenceId) {
        this.id = id;
        this.referenceId = referenceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PathItemTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(PathItemTypeEnum itemType) {
        this.itemType = itemType;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LearningPath getPathId() {
        return pathId;
    }

    public void setPathId(LearningPath pathId) {
        this.pathId = pathId;
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
        if (!(object instanceof LearningPathItem)) {
            return false;
        }
        LearningPathItem other = (LearningPathItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.LearningPathItem[ id=" + id + " ]";
    }
    
}
