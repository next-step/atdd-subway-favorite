package nextstep.station.infrastructure;

import java.util.List;
import nextstep.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationJpaRepository extends JpaRepository<Station, Long>, StationRepository {

}
