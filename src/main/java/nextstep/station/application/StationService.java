package nextstep.station.application;

import nextstep.station.application.dto.StationRequest;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationInspector;
import nextstep.station.domain.StationRepository;
import nextstep.station.domain.exception.CantDeleteStationException;
import nextstep.station.domain.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationInspector stationInspector;
    private final StationRepository stationRepository;

    public StationService(StationInspector stationInspector, StationRepository stationRepository) {
        this.stationInspector = stationInspector;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(stationRequest.toEntity());
        return StationResponse.of(station);
    }

    public Station findById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(StationNotFoundException::new);
    }

    public List<Station> findByIds(List<Long> stationIds) {
        return stationRepository.findByIdIn(stationIds);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        Station station = findById(id);
        if (stationInspector.belongsToLine(station)) {
            throw new CantDeleteStationException("노선에 포함된 지하철역을 삭제할 수 없습니다.");
        }
        stationRepository.delete(station);
    }
}
