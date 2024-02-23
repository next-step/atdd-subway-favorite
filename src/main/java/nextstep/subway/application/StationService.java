package nextstep.subway.application;

import nextstep.exception.NotFoundLineException;
import nextstep.subway.application.dto.ShowStationDto;
import nextstep.subway.application.request.CreateStationRequest;
import nextstep.subway.application.response.CreateStationResponse;
import nextstep.subway.application.response.ShowAllStationsResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {

    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station findById(Long StationId) {
        return stationRepository.findById(StationId)
                .orElseThrow(NotFoundLineException::new);
    }

    @Transactional
    public CreateStationResponse saveStation(CreateStationRequest createStationRequest) {
        Station station = stationRepository.save(Station.from(createStationRequest.getName()));
        return CreateStationResponse.from(station);
    }

    public ShowAllStationsResponse findAllStations() {
        return ShowAllStationsResponse.from(stationRepository.findAll().stream()
                .map(ShowStationDto::from)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void deleteStationById(Long stationId) {
        stationRepository.deleteById(stationId);
    }

}
