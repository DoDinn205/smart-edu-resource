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
@Table(name = "chat_room")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatRoom.findAll", query = "SELECT c FROM ChatRoom c"),
    @NamedQuery(name = "ChatRoom.findById", query = "SELECT c FROM ChatRoom c WHERE c.id = :id"),
    @NamedQuery(name = "ChatRoom.findByType", query = "SELECT c FROM ChatRoom c WHERE c.type = :type"),
    @NamedQuery(name = "ChatRoom.findByName", query = "SELECT c FROM ChatRoom c WHERE c.name = :name"),
    @NamedQuery(name = "ChatRoom.findByCreatedAt", query = "SELECT c FROM ChatRoom c WHERE c.createdAt = :createdAt")})
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "type")
    private String type;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId")
    private Set<ChatMessage> chatMessageSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId")
    private Set<ChatParticipant> chatParticipantSet;
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne
    private Course courseId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User createdBy;

    public ChatRoom() {
    }

    public ChatRoom(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @XmlTransient
    public Set<ChatMessage> getChatMessageSet() {
        return chatMessageSet;
    }

    public void setChatMessageSet(Set<ChatMessage> chatMessageSet) {
        this.chatMessageSet = chatMessageSet;
    }

    @XmlTransient
    public Set<ChatParticipant> getChatParticipantSet() {
        return chatParticipantSet;
    }

    public void setChatParticipantSet(Set<ChatParticipant> chatParticipantSet) {
        this.chatParticipantSet = chatParticipantSet;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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
        if (!(object instanceof ChatRoom)) {
            return false;
        }
        ChatRoom other = (ChatRoom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.ChatRoom[ id=" + id + " ]";
    }
    
}
