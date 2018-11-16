package wmo.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import wmo.beans.validation.CheckStation;
import wmo.beans.validation.SecondaryValidation;

@Entity
@JsonRootName("station")
@CheckStation(groups = SecondaryValidation.class)
public class Station {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Size(min=4,max=20)
	private String name;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date established;
	
	@Valid
	@OneToMany(
			cascade = CascadeType.ALL, 
			orphanRemoval = true
			)
	@JsonIgnore
	private List<WigosID> wigosIDs = new ArrayList<>();
	
	@Valid
	@OneToMany(
			cascade = CascadeType.ALL,
			orphanRemoval = true
			)
	private List<Observation> observations = new ArrayList<>();
	
	@Valid
	@OneToMany(
			cascade = CascadeType.ALL,
			orphanRemoval = true
			)
	private List<Location> locations = new ArrayList<>();
	
	private String countryCode;
	private String region;
	
	@JsonGetter("wigosID")
	public WigosID getWigosID() {
		for (WigosID wid : this.wigosIDs) {
			if (wid.isPrimary()) {
				return wid;
			}
		}
		return null;
	}
	@JsonSetter("wigosID")
	public void setWigosID(WigosID wid) {
		this.wigosIDs.add(wid);
	}
	
	
	
	public Station() {
		this.name=null;
		this.wigosIDs=new ArrayList<>();
		this.observations=new ArrayList<Observation>();
		this.locations=new ArrayList<Location>();
		
		this.established= new GregorianCalendar(2016, 3, 1).getTime();
		this.countryCode=null;
		this.region=null;
	}


	public Station(String name, String wigosID, Location loc, Date established, String countryCode, String region) {
		this.name=name;
		this.wigosIDs.add( new WigosID(wigosID,true) );
		this.observations = new ArrayList<Observation>();
		this.locations = new ArrayList<Location>();
		this.locations.add(loc);
		this.established=established;
		this.countryCode=countryCode;
		this.region=region;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<WigosID> getWigosIDs() {
		return wigosIDs;
	}

	public void setWigosIDs(List<WigosID> wigosIDs) {
		this.wigosIDs = wigosIDs;
	}

	public List<Observation> getObservations() {
		return observations;
	}

	public void setObservations(List<Observation> observations) {
		this.observations = observations;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public long getId() {
		return this.id ;
	}

	public Date getEstablished() {
		return established;
	}

	public void setEstablished(Date established) {
		this.established = established;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	
	
}


