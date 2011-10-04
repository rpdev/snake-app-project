/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

@Entity
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String commentText;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date commentMade;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date commentLatestEdit = null;
    @ManyToOne
    private SnakeMap map;
    @ManyToOne
    private UserAcc userCommentViolate;

    public Comment() {
    }

    public Comment(String commentText, SnakeMap map, UserAcc user) {
	if (commentText.length() < 1) {
	    throw new IllegalArgumentException("Comment have to contian charachters");
	}
	this.commentText = commentText;
	commentMade = new Date();
	this.map = map;
	this.userCommentViolate = user;
	map.addComment(this);
	user.addComment(this);
    }

    /**
     * Used before a comment is removed from the database, will
     * remove the comment from both the user and the map that
     * is associated with this comment. Note that this function
     * is called by the class {@link Database}
     */
    void cleanUp() {
	map.removeComment(this);
	userCommentViolate.removeComment(this);
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
     * @return the commentText
     */
    public String getCommentText() {
	return commentText;
    }

    /**
     * @param commentText the commentText to set
     */
    public void setCommentText(String commentText) {
	this.commentText = commentText;
    }

    /**
     * @return the commentMade
     */
    public Date getCommentMade() {
	return commentMade;
    }

    /**
     * @param commentMade the commentMade to set
     */
    public void setCommentMade(Date commentMade) {
	this.commentMade = commentMade;
    }

    /**
     * @return the commentLatestEdit
     */
    public Date getCommentLatestEdit() {
	return commentLatestEdit;
    }

    /**
     * @param commentLatestEdit the commentLatestEdit to set
     */
    public void setCommentLatestEdit(Date commentLatestEdit) {
	this.commentLatestEdit = commentLatestEdit;
    }

    /**
     * @return the map
     */
    public SnakeMap getMap() {
	return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(SnakeMap map) {
	this.map = map;
    }

    /**
     * @return the user
     */
    public UserAcc getUserCommentViolate() {
	return userCommentViolate;
    }

    /**
     * @param user the user to set
     */
    public void setUserCommentViolate(UserAcc userCommentViolate) {
	this.userCommentViolate = userCommentViolate;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (getId() != null ? getId().hashCode() : 0);
	return hash;}
    
    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof Comment)) {
	    return false;
	}
	Comment other = (Comment) object;
	if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "se.chalmers.snake.snakeappwebpage.serverstorage.Comment[ id=" + getId() + " ]";
    }
}
