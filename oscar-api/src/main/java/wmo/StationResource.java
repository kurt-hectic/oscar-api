package wmo;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import wmo.beans.Station;
import wmo.beans.validation.ValidationSequence;

@RestController
public class StationResource {

	
	@Autowired
	private StationRepository stationRepository;

	@GetMapping("/stations")
	public List<Station> retrieveAllStations() {
		return stationRepository.findAll();
	}
	
	


	@RequestMapping(
			  method = RequestMethod.GET, 
			  value = "/stations/{id}", 
			  produces = "application/wmdr"
			)
	@ResponseBody
	public Station retrieveStationWMO(@PathVariable long id) throws StationNotFoundException, JsonProcessingException, TransformerException {
		Optional<Station> station = stationRepository.findById(id);

		if (!station.isPresent())
			throw new StationNotFoundException("id-" + id);
		
		return station.get();
	}	
	
	@GetMapping("/stations/{id}")
	public Station retrieveStation(@PathVariable long id) throws StationNotFoundException {
		Optional<Station> station = stationRepository.findById(id);

		if (!station.isPresent())
			throw new StationNotFoundException("id-" + id);

		return station.get();
	}
	
	@GetMapping("/stations/bywigosid/{wid}")
	public Station retrieveStationByWigosID(@PathVariable String wid) throws StationNotFoundException {
		List<Station> stations = stationRepository.findByWigosIDs_WigosID(wid);

		if (stations.isEmpty() )
			throw new StationNotFoundException("wigosID-" + wid);

		return stations.get(0);
	}
	
	@RequestMapping(
			  method = RequestMethod.GET, 
			  value = "/stations/bywigosid/{wid}", 
			  produces = "application/wmdr"
			)
	@ResponseBody
	public Station retrieveStationByWigosIDWMDR(@PathVariable String wid) throws StationNotFoundException, JsonProcessingException, TransformerException {
		List<Station> stations = stationRepository.findByWigosIDs_WigosID(wid);

		if (stations.isEmpty() )
			throw new StationNotFoundException("wigosID-" + wid);

		return stations.get(0);
	}	


	

	@DeleteMapping("/stations/{id}")
	public void deleteStation(@PathVariable long id) {
		stationRepository.deleteById(id);
	}
	
	
	
	@RequestMapping(
			  method = RequestMethod.POST, 
			  value = "/stations", 
			  consumes = "application/wmds"
			)
	public ResponseEntity<Object> createStationWMO(@Validated(ValidationSequence.class) @RequestBody Station station) throws TransformerException, JsonParseException, JsonMappingException, IOException {
		
		Station savedStation = stationRepository.save(station);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedStation.getId()).toUri();

		return ResponseEntity.created(location).build(); 
	}


	@PostMapping("/stations")
	public ResponseEntity<Object> createStation(@Validated(ValidationSequence.class) @RequestBody Station station) {
		Station savedStation = stationRepository.save(station);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(
						savedStation.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}
	
		
	@PutMapping("/stations/{id}")
	public ResponseEntity<Object> updateStation(@RequestBody Station station, @PathVariable long id) {

		Optional<Station> stationOptional = stationRepository.findById(id);

		if (!stationOptional.isPresent())
			return ResponseEntity.notFound().build();

		station.setId(id);
		
		stationRepository.save(station);

		return ResponseEntity.noContent().build();
	}
}