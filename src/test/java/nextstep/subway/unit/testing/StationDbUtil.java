package nextstep.subway.unit.testing;

import nextstep.subway.domain.entity.station.Station;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StationDbUtil {
    @Autowired
    private StationRepository stationRepository;

    @Transactional
    public List<Station> insertStations(String... stationNames) {
        List<Station> stations = Arrays.stream(stationNames)
                .map(Station::new)
                .collect(Collectors.toList());
        return stationRepository.saveAll(stations);
    }
}
