package nextstep.subway.station.domain;

import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

    default Station findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new StationNotFoundException(id));
    }
}
