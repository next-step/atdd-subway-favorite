package atdd.path.repository;

import org.springframework.data.repository.CrudRepository;

import atdd.path.domain.Station;

public interface StationRepository extends CrudRepository<Station, Long> {
}
