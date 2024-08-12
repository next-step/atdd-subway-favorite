package nextstep.station.infrastructure;

import java.util.List;
import nextstep.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findStationsByIdIn(List<Long> ids);

    List<Station> findByNameIn(List<String> names);
}