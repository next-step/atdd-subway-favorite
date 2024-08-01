package nextstep.subway.domain.query;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Service;
import nextstep.subway.domain.entity.station.Station;
import nextstep.subway.domain.view.StationView;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationReader {

    private final StationRepository stationRepository;

    public StationView.Main getOneById(Long id) {
        return transform(stationRepository.findByIdOrThrow(id));
    }

    public List<StationView.Main> getAll() {
        return stationRepository.findAll().stream()
                .map(this::transform)
                .collect(Collectors.toList());
    }

    private StationView.Main transform(Station station) {
        return new StationView.Main(
                station.getId(),
                station.getName()
        );
    }
}
