package nextstep.station.payload;

import nextstep.station.domain.Station;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StationMapper {

    private final StationRepository stationRepository;


    public StationMapper(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Map<Long, StationResponse> getStationResponseMap(final Collection<Long> stationsIds) {
        return stationRepository.findByIdIn(stationsIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, (StationResponse::from)));
    }
}
