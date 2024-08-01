package nextstep.subway.domain.repository;

import lombok.NonNull;
import nextstep.subway.domain.exception.NotFoundStationException;
import org.springframework.data.jpa.repository.JpaRepository;
import nextstep.subway.domain.entity.station.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
    @NonNull
    default Station findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundStationException(id));
    }
}