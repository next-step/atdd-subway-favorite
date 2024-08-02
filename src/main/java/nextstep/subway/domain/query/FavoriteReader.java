package nextstep.subway.domain.query;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.favorite.Favorite;
import nextstep.subway.domain.entity.station.Station;
import nextstep.subway.domain.repository.FavoriteRepository;
import nextstep.subway.domain.repository.StationRepository;
import nextstep.subway.domain.view.FavoriteView;
import nextstep.subway.domain.view.StationView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FavoriteReader {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    @Transactional(readOnly = true)
    public FavoriteView.Main getOneById(Long id) {
        Favorite favorite = favoriteRepository.findByIdOrThrow(id);
        Map<Long, Station> stationMap = getStationMapByIds(List.of(favorite.getSourceStationId(), favorite.getTargetStationId()));
        return joinAndTransform(favorite, stationMap);
    }

    @Transactional(readOnly = true)
    public List<FavoriteView.Main> getFavoritesByMemberId(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        List<Long> stationIds = favorites.stream()
                .flatMap(favorite -> Stream.of(favorite.getSourceStationId(), favorite.getTargetStationId()))
                .collect(Collectors.toList());

        Map<Long, Station> stationMap = getStationMapByIds(stationIds);
        return favorites.stream()
                .map(favorite -> joinAndTransform(favorite, stationMap))
                .collect(Collectors.toList());
    }

    private Map<Long, Station> getStationMapByIds(Iterable<Long> ids) {
        Map<Long, Station> stationMap = new HashMap<>();
        stationRepository.findAllById(ids).forEach((station -> stationMap.putIfAbsent(station.getId(), station)));
        return stationMap;
    }

    private FavoriteView.Main joinAndTransform(Favorite favorite, Map<Long, Station> stationMap) {
        return new FavoriteView.Main(
                favorite.getId(),
                StationView.Main.of(stationMap.get(favorite.getSourceStationId())),
                StationView.Main.of(stationMap.get(favorite.getTargetStationId()))
        );
    }
}
