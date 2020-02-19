package atdd.path.repository;

import atdd.path.domain.Line;
import org.springframework.data.repository.CrudRepository;

public interface LineRepository extends CrudRepository<Line, Long> {
}
