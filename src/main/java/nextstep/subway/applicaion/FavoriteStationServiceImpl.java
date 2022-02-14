package nextstep.subway.applicaion;

import nextstep.favorite.application.manager.FavoriteStationService;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteStationServiceImpl implements FavoriteStationService {
    private StationRepository stationRepository;

    public FavoriteStationServiceImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Station> loadFindStationsIds(Set<Long> stationIds) {
        return stationRepository.findAllById(stationIds).stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }
}
