package nextstep.station.domain;

import nextstep.station.domain.exception.StationNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
    default Station findByIdOrFail(Long id) {
        return findById(id).orElseThrow(StationNotFoundException::new);
    }
}
