package wmo;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import wmo.beans.Deployment;
import wmo.beans.Location;
import wmo.beans.Observation;
import wmo.beans.Schedule;
import wmo.beans.Station;

@Component
public class DemoData {

	@Autowired
	private final StationRepository repo = null;

	@EventListener
	public void appReady(ApplicationReadyEvent event) throws ParseException {


		Date established = new GregorianCalendar(2017, 1, 25).getTime() ;
		
		Station s = new Station( "Timo's test station", "0-20000-1-12334" , new Location(10.10f,20.20f,123.456f, established )  , established, "DEU", "Europe" );
		Observation o = new Observation( 224 ,  new Deployment( established, Schedule.getSchedule(Schedule.int247) ) );
		s.getObservations().add(o);

		repo.save(   s    );
		
	}
	
	
	private void createStation() {
		
	}
	
}