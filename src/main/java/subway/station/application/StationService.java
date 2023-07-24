package subway.station.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.constant.SubwayMessage;
import subway.exception.SubwayNotFoundException;
import subway.station.application.dto.StationRequest;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = Station.builder()
                .name(stationRequest.getName())
                .build();
        return StationResponse.from(stationRepository.save(station));
    }

    public List<StationResponse> findAll() {
        return stationRepository.findAll().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.STATION_NOT_FOUND));
    }
}
