package subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByUpStationOrDownStation(Station upStation, Station downStation);
}
