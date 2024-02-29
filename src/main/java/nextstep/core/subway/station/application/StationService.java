package nextstep.core.subway.station.application;

import nextstep.core.subway.station.application.converter.StationConverter;
import nextstep.core.subway.station.application.dto.StationRequest;
import nextstep.core.subway.station.application.dto.StationResponse;
import nextstep.core.subway.station.domain.Station;
import nextstep.core.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse createStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationConverter.convertToResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationConverter::convertToResponse)
                .collect(Collectors.toList());
    }

    public Station findStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("역 번호에 해당하는 역이 없습니다."));
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
