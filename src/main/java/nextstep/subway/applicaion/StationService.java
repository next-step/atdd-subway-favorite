package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.request.StationRequest;
import nextstep.subway.applicaion.dto.response.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NullPointerSectionsException;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

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

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }


    public Station getStations(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new NullPointerSectionsException("역을 찾을 수 없습니다.  id : "+stationId));
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
