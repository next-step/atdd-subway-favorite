package nextstep.subway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nextstep.subway.domain.Station;
@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findStationByName(String name);
}