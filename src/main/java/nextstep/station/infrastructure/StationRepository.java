package nextstep.station.infrastructure;

import java.util.List;
import java.util.Optional;
import nextstep.station.domain.Station;

public interface StationRepository {

    List<Station> findByNameIn(List<String> names);
    Station save(Station station);
    List<Station> findAll();
    Optional<Station> findById(Long id);
    void deleteById(Long id);
    List<Station> findStationsByIdIn(List<Long> stationIds);
}
