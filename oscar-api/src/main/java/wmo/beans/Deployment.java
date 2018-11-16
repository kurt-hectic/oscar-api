package wmo.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Deployment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	
	
	@NotNull
	private Date beginning;
	private Date end;
	
	private String observationsource;
	
	@Valid
	@OneToMany(
			cascade = CascadeType.ALL, 
			orphanRemoval = true
	)
	private List<Schedule> schedules = new ArrayList<>();

	
	public Deployment() {
		this.beginning = null;
		this.end = null;
		this.schedules = new ArrayList<Schedule>();
	}
	
	public Deployment(Date beginning, Schedule schedule) {
		this.beginning = beginning;
		this.end = null;
		this.schedules = new ArrayList<Schedule>();
		this.schedules.add(schedule);
		this.observationsource="automaticReading";
	}
	
	public Date getBeginning() {
		return beginning;
	}
	public void setBeginning(Date beginning) {
		this.beginning = beginning;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getObservationsource() {
		return observationsource;
	}
	public void setObservationsource(String observationsource) {
		this.observationsource = observationsource;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}
	
	
	
}
