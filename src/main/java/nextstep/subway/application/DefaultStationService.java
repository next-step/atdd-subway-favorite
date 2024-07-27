package nextstep.subway.application;

import static nextstep.subway.application.DefaultLineCommandService.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.application.dto.StationRequest;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.model.Station;
import nextstep.subway.domain.repository.StationRepository;
import nextstep.subway.domain.service.StationService;

@Service
@Transactional(readOnly = true)
public class DefaultStationService implements StationService {
    private final StationRepository stationRepository;

    public DefaultStationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Station findStationById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE));
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
