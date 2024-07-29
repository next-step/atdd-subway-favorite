package nextstep.station.repository;

import nextstep.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findByIdIn(Collection<Long> stationIds);
}
