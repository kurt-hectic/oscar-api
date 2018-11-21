package wmo.beans;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;

@Entity
public class WigosID {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Pattern(regexp = "^[a-zA-Z0-9]+-[a-zA-Z0-9]+-[a-zA-Z0-9]+-[a-zA-Z0-9]+$")
	@Column(unique=true)
	private String wigosID;
	
	private boolean isPrimary = false;
	
	public WigosID() {
		this.wigosID=null;
	}

	public WigosID(String wigosID) {
		this.wigosID = wigosID;
	}
	
	public WigosID(String wigosID, boolean isPrimary) {
		this.wigosID = wigosID;
		this.isPrimary = isPrimary;
	}
	
	public boolean isPrimary() {
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getWigosID() {
		return wigosID;
	}

	public void setWigosID(String wigosID) {
		this.wigosID = wigosID;
	}

	public String toString() {
		return wigosID;
	}
	
}
