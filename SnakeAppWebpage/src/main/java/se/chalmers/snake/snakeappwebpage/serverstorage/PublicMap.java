package se.chalmers.snake.snakeappwebpage.serverstorage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 */
@Entity
public class PublicMap implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
   @GeneratedValue(strategy = GenerationType.AUTO)
	
	private long id;


	@Temporal(javax.persistence.TemporalType.DATE)
	private Date publicDate;

	@OneToMany
	private List<MapComment> comments;
	
	@ManyToOne
	private UserAccount userAccount;
	
	@OneToOne
	private Map map;
	
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) id;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PublicMap)) {
			return false;
		}
		PublicMap other = (PublicMap) object;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PublicMap{" + "id=" + id + ", publicDate=" + publicDate + ", comments=" + comments + ", userAccount=" + userAccount + '}';
	}
}
