package wmo.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	public Location() {
		this.longitude=0;
		this.latitude=0;
		this.elevation=0;
		this.datefrom=null;
		this.dateto=null;
	}
	
	public Location(float longitude,float latitude, float elevation, Date from) {
		this.longitude=longitude;
		this.latitude=latitude;
		this.elevation=elevation;
		this.datefrom=from;
	}

	private float longitude;
	private float latitude;	
	private float elevation;
	
	private Date datefrom;
	private Date dateto=null;
	
	public Date getDatefrom() {
		return datefrom;
	}

	public void setDatefrom(Date datefrom) {
		this.datefrom = datefrom;
	}

	public Date getDateto() {
		return dateto;
	}

	public void setDateto(Date dateto) {
		this.dateto = dateto;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getElevation() {
		return elevation;
	}

	public void setElevation(float elevation) {
		this.elevation = elevation;
	}


	
}
