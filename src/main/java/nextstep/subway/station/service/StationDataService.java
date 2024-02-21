package nextstep.subway.station.service;

import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.station.exception.StationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class StationDataService {

    private final StationRepository stationRepository;

    public StationDataService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationException("존재하지 않는 역입니다."));
    }
}
