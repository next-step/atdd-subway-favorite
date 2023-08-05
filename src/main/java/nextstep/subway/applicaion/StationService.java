package nextstep.subway.applicaion;

import java.util.List;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationData;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.StationResponseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;
    private final StationResponseRepository stationResponseRepository;

    public StationService(StationRepository stationRepository, StationResponseRepository stationResponseRepository) {
        this.stationRepository = stationRepository;
        this.stationResponseRepository = stationResponseRepository;
    }

    @Transactional
    public StationData saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationData.of(station);
    }

    @Transactional(readOnly = true)
    public List<StationData> findAllStations() {
        return stationResponseRepository.findAll();
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
