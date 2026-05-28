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

import com.paq.utils.constant.TypeInteractionEnum;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "interaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Interaction.findAll", query = "SELECT i FROM Interaction i"),
    @NamedQuery(name = "Interaction.findById", query = "SELECT i FROM Interaction i WHERE i.id = :id"),
    @NamedQuery(name = "Interaction.findByPageNumber", query = "SELECT i FROM Interaction i WHERE i.pageNumber = :pageNumber"),
    @NamedQuery(name = "Interaction.findByTimeOffsetSeconds", query = "SELECT i FROM Interaction i WHERE i.timeOffsetSeconds = :timeOffsetSeconds"),
    @NamedQuery(name = "Interaction.findByCreatedAt", query = "SELECT i FROM Interaction i WHERE i.createdAt = :createdAt"),
    @NamedQuery(name = "Interaction.findByUpdatedAt", query = "SELECT i FROM Interaction i WHERE i.updatedAt = :updatedAt"),
    @NamedQuery(name = "Interaction.findByPositionX", query = "SELECT i FROM Interaction i WHERE i.positionX = :positionX"),
    @NamedQuery(name = "Interaction.findByType", query = "SELECT i FROM Interaction i WHERE i.type = :type")})
public class Interaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "page_number")
    private Integer pageNumber;
    @Column(name = "time_offset_seconds")
    private Integer timeOffsetSeconds;
    @Lob
    @Size(max = 65535)
    @Column(name = "selected_text")
    private String selectedText;
    @Lob
    @Size(max = 65535)
    @Column(name = "note")
    private String note;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "position_x")
    private Integer positionX;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeInteractionEnum type;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "interactionId")
    @JsonIgnore
    private Set<InteractionReply> interactionReplySet;
    @JoinColumn(name = "resource_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Resource resourceId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public Interaction() {
    }

    public Interaction(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getTimeOffsetSeconds() {
        return timeOffsetSeconds;
    }

    public void setTimeOffsetSeconds(Integer timeOffsetSeconds) {
        this.timeOffsetSeconds = timeOffsetSeconds;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public TypeInteractionEnum getType() {
        return type;
    }

    public void setType(TypeInteractionEnum type) {
        this.type = type;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @XmlTransient
    public Set<InteractionReply> getInteractionReplySet() {
        return interactionReplySet;
    }

    public void setInteractionReplySet(Set<InteractionReply> interactionReplySet) {
        this.interactionReplySet = interactionReplySet;
    }

    public Resource getResourceId() {
        return resourceId;
    }

    public void setResourceId(Resource resourceId) {
        this.resourceId = resourceId;
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
        if (!(object instanceof Interaction)) {
            return false;
        }
        Interaction other = (Interaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.Interaction[ id=" + id + " ]";
    }
    
}
