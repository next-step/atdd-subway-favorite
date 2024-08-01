package nextstep.subway.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nextstep.subway.domain.entity.station.Station;

@Service
@RequiredArgsConstructor
public class StationCommander {
    private final StationRepository stationRepository;

    @Transactional
    public Long createStation(String name) {
        Station station = stationRepository.save(new Station(name));
        return station.getId();
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
