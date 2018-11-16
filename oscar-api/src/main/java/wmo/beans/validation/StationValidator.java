package wmo.beans.validation;

import java.util.HashMap;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import wmo.beans.Observation;
import wmo.beans.Station;
import wmo.beans.WigosID;

/**
 * @author Timo
 *
 */
public class StationValidator implements ConstraintValidator<CheckStation, Station> {


	public void initialize(CheckStation constraintAnnotation) {
	}

	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 check that local block of the WIGOS ID is not in the name
	 */
	public boolean isValid(Station station, 
			ConstraintValidatorContext context) {

		String name = station.getName();
		List<WigosID> wigosIDlist =  station.getWigosIDs();

		boolean isValid = false;
		String problemWid = null;
		for ( WigosID wid : wigosIDlist ) {
			problemWid=wid.toString();
			String[] arrOfStr = wid.toString().split("-"); 

			if (arrOfStr.length != 4) { 
				isValid = false;
				
				break;
			}
			isValid = !name.contains(  arrOfStr[3] ) ;
		}


		if ( !isValid ) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("WIGOS ID "+problemWid+" cannot be in name").addConstraintViolation();
		}

		
		HashMap<Integer, Boolean> map = new HashMap<Integer,Boolean>();
		for (Observation o : station.getObservations()) {
			if (map.containsKey(o.getVariable())) {
				
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("observation for variable " + o.getVariable() + " double" ).addConstraintViolation();
				return false;
				
			}
			map.put(o.getVariable(), true);
		}


		return isValid;
	}
}

