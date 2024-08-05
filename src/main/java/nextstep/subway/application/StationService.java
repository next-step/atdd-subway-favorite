package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Station;
import nextstep.subway.infrastructure.StationRepository;
import nextstep.subway.presentation.StationRequest;
import nextstep.subway.presentation.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StationService {
    private final StationRepository stationRepository;

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id); // 구간에 포함되어 있는 경우 삭제할 수 없음
    }
}
