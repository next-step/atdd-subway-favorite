package nextstep.station.service;

import nextstep.line.service.LineService;
import nextstep.station.dto.StationRequest;
import nextstep.station.dto.StationResponse;
import nextstep.station.entity.Station;
import nextstep.station.exception.StationNotFoundException;
import nextstep.station.repository.StationRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.common.constant.ErrorCode.STATION_NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class StationService {

    private StationRepository stationRepository;
    private LineService lineService;

    public StationService(StationRepository stationRepository, @Lazy LineService lineService) {
        this.stationRepository = stationRepository;
        this.lineService = lineService;
    }

    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        Station station = stationRepository.save(Station.of(stationRequest.getName()));
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(final Station station) {
        return StationResponse.of(
                station.getId(),
                station.getName()
        );
    }

    public Station getStationByIdOrThrow(final Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(String.valueOf(STATION_NOT_FOUND)));
    }

}

