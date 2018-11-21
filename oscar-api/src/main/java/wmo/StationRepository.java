package wmo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import wmo.beans.Station;

@Repository
public interface StationRepository extends PagingAndSortingRepository<Station, Long> {

	List<Station> findByName(@Param("name") String name);
	List<Station> findAll();
	
	List<Station> findByWigosIDs_WigosID(String wigosID);
		
	<S extends Station>Station save(Station station);
	
	/*
	@Query(value = "SELECT * from stations s join wigosids w on (s.wigosid_id = w.id ) where w.wigosid  ")
	List<Station> findByWigosID(String WigosID);
	*/

}
