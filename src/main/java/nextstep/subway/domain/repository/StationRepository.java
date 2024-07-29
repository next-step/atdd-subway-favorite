package nextstep.subway.domain.repository;

import java.util.List;
import java.util.Optional;

import nextstep.subway.domain.model.Station;

public interface StationRepository {
    Station save(Station station);

    List<Station> findAll();

    void deleteById(Long id);

    Optional<Station> findById(Long stationId);
}