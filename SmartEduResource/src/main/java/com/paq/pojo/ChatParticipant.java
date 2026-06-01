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
import jakarta.persistence.UniqueConstraint;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author paqvi
 */
@Entity
@Table(name = "chat_participant", uniqueConstraints = {
    @UniqueConstraint(name = "uk_chat_participant_room_user", columnNames = {"room_id", "user_id"})
})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChatParticipant.findAll", query = "SELECT c FROM ChatParticipant c"),
    @NamedQuery(name = "ChatParticipant.findById", query = "SELECT c FROM ChatParticipant c WHERE c.id = :id"),
    @NamedQuery(name = "ChatParticipant.findByJoinedAt", query = "SELECT c FROM ChatParticipant c WHERE c.joinedAt = :joinedAt"),
    @NamedQuery(name = "ChatParticipant.findByIsMuted", query = "SELECT c FROM ChatParticipant c WHERE c.isMuted = :isMuted")})
public class ChatParticipant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "joined_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedAt;
    @Column(name = "is_muted")
    private Boolean isMuted;
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ChatRoom roomId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public ChatParticipant() {
    }

    public ChatParticipant(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Boolean getIsMuted() {
        return isMuted;
    }

    public void setIsMuted(Boolean isMuted) {
        this.isMuted = isMuted;
    }

    public ChatRoom getRoomId() {
        return roomId;
    }

    public void setRoomId(ChatRoom roomId) {
        this.roomId = roomId;
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
        if (!(object instanceof ChatParticipant)) {
            return false;
        }
        ChatParticipant other = (ChatParticipant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.paq.pojo.ChatParticipant[ id=" + id + " ]";
    }
    
}
