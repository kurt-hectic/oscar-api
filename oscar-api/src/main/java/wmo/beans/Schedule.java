package wmo.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private int monthfrom;
	private int monthto;
	private int dayfrom;
	private int dayto;
	private int hourfrom;
	private int hourto;
	private int minutefrom;
	private int minuteto;
	private int interval;
	private boolean international; 
	

	public Schedule() {
	}
	
	
	public int getMonthfrom() {
		return monthfrom;
	}

	public void setMonthfrom(int monthfrom) {
		this.monthfrom = monthfrom;
	}

	public int getMonthto() {
		return monthto;
	}

	public void setMonthto(int monthto) {
		this.monthto = monthto;
	}

	public int getDayfrom() {
		return dayfrom;
	}

	public void setDayfrom(int dayfrom) {
		this.dayfrom = dayfrom;
	}

	public int getDayto() {
		return dayto;
	}

	public void setDayto(int dayto) {
		this.dayto = dayto;
	}

	public int getHourfrom() {
		return hourfrom;
	}

	public void setHourfrom(int hourfrom) {
		this.hourfrom = hourfrom;
	}

	public int getHourto() {
		return hourto;
	}

	public void setHourto(int hourto) {
		this.hourto = hourto;
	}

	public int getMinutefrom() {
		return minutefrom;
	}

	public void setMinutefrom(int minutefrom) {
		this.minutefrom = minutefrom;
	}

	public int getMinuteto() {
		return minuteto;
	}

	public void setMinuteto(int minuteto) {
		this.minuteto = minuteto;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public boolean isInternational() {
		return international;
	}

	public void setInternational(boolean international) {
		this.international = international;
	}




	public static final int int247 = 1;
	public static final int nat247 = 2;
	public static final int only247 = 3;
	

	public static Schedule getSchedule(int type)  {
		
		Schedule ret = new Schedule();
		
		switch (type) {
		case only247:
			ret.setDayfrom(1);
			ret.setDayto(7);
			ret.setHourfrom(0);
			ret.setHourto(23);
			ret.setMonthfrom(1);
			ret.setMonthfrom(12);
			ret.setMinutefrom(0);
			ret.setMinuteto(59);
			ret.setInterval(60);
			break;
		
		case int247:
			ret=getSchedule(only247);
			ret.setInternational(true);
			break;
			
		case nat247:
			ret=getSchedule(only247);
			ret.setInternational(false);
			break;
			
			default:
				return null;
		}
		
		return ret;
	}

	
}
