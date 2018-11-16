package wmo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import wmo.beans.Station;

@Repository
public interface StationRepository extends PagingAndSortingRepository<Station, Long> {

	List<Station> findByName(@Param("name") String name);
	List<Station> findAll();
	

}
