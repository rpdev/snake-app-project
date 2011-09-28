/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import se.chalmers.snake.snakeappwebpage.serverstorage.Database.STATUS;

@Entity
public class SnakeMap implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn(nullable=false)
    @OneToOne
    private UserAcc userName;
    @Enumerated(EnumType.STRING)
    private STATUS status;
    @OneToMany(cascade= CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<Comment>();

    @Column(nullable=false, updatable=false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date creationDate;

    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date publicedDate;

    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date editedDate;

    public SnakeMap(){}

    public SnakeMap(UserAcc user){
	this.userName = user;
	creationDate = new Date();
	status = STATUS.CREATED;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters & setters">
    /**
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @return the userName
     */
    public UserAcc getUserName() {
	return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(UserAcc userName) {
	this.userName = userName;
    }

    /**
     * @return the status
     */
    public STATUS getStatus() {
	return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(STATUS status) {
	this.status = status;
    }

    /**
     * @return the comments
     */
    public List<Comment> getComments() {
	return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(List<Comment> comments) {
	this.comments = comments;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
	return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    /**
     * @return the publicedDate
     */
    public Date getPublicedDate() {
	return publicedDate;
    }

    /**
     * @param publicedDate the publicedDate to set
     */
    public void setPublicedDate(Date publicedDate) {
	this.publicedDate = publicedDate;
    }

    /**
     * @return the editedDate
     */
    public Date getEditedDate() {
	return editedDate;
    }

    /**
     * @param editedDate the editedDate to set
     */
    public void setEditedDate(Date editedDate) {
	this.editedDate = editedDate;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (getId() != null ? getId().hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof SnakeMap)) {
	    return false;
	}
	SnakeMap other = (SnakeMap) object;
	if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "se.chalmers.snake.snakeappwebpage.serverstorage.SnakeMap[ id=" + getId() + " ]";
    }



}
