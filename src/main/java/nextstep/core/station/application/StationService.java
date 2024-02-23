package nextstep.core.station.application;

import nextstep.core.station.application.converter.StationConverter;
import nextstep.core.station.application.dto.StationRequest;
import nextstep.core.station.application.dto.StationResponse;
import nextstep.core.station.domain.Station;
import nextstep.core.station.domain.StationRepository;
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
        return StationConverter.convertToStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationConverter::convertToStationResponse)
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
