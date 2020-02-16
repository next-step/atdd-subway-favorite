package atdd.path.repository;

import atdd.path.domain.Station;
import org.springframework.data.repository.CrudRepository;

public interface StationRepository extends CrudRepository<Station, Long> {
}
