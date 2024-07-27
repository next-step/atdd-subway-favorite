package nextstep.subway.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.domain.model.Station;
import nextstep.subway.domain.repository.StationRepository;

public interface JpaStationRepository extends StationRepository, JpaRepository<Station, Long> {
}